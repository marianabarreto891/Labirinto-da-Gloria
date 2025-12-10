package org.estg.ed.Manager;

import java.util.Iterator;

import org.estg.ed.Content.Content;
import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Graph.MazeNetwork;
import org.estg.ed.Hall.Hallway;
import org.estg.ed.Hall.HallwayManager;
import org.estg.ed.Maze.Maze;
import org.estg.ed.Players.Player;

import Lists.ArrayUnorderedList;
import Lists.DoublyLinkedUnorderedList;


/**
 * Manages the core game logic for the Labyrinth of Glory.
 *
 * This class acts as the central controller, handling the game loop, player turns,
 * movement logic, and interactions with the maze network. It bridges the gap
 * between the data structures and the game rules.
 */
public class GameManager {
    private MazeNetwork mazeNetwork;
    private ArrayUnorderedList<Player> players;
    private Division centralRoom;
    private int currentPlayerIndex;


    /**
     * Constructs a new GameManager.
     *
     * Initializes the game state with the provided maze structure and list of players.
     *
     * @param maze The maze object containing divisions and hallways.
     * @param players The list of players participating in the game.
     */
    public GameManager(Maze maze, ArrayUnorderedList<Player> players) {
        this.players = players;
        this.centralRoom = maze.getCentralRoom();
        
       
        HallwayManager.setGameManager(this);
        
        // Criar e popular o MazeNetwork
        this.mazeNetwork = new MazeNetwork();
        
        // Adicionar todas as divisões como vértices
        for (Division div : maze.getD()) {
            mazeNetwork.addVertex(div);
        }
        
        // Adicionar todos os corredores como arestas
        for (Hallway hall : maze.getH()) {
            mazeNetwork.addHallway(hall);
        }
        
        // Definir o ponto de entrada (primeira divisão de tipo ENTRY encontrada)
        Division entryDivision = findEntryDivision(maze.getD());
        mazeNetwork.setStartDivision(entryDivision);
        mazeNetwork.setCentralRoom(this.centralRoom);
        
        this.currentPlayerIndex = 0;
    }
    
    /**
     * Método auxiliar para encontrar a divisão de entrada.
     * 
     * @param divisions lista de divisões
     * @return a primeira divisão de tipo ENTRY, ou null se não encontrada
     */
    private Division findEntryDivision(ArrayUnorderedList<Division> divisions) {
        for (Division division : divisions) {
            if (division.getDt() == DivisionType.ENTRY) {
                return division;
            }
        }
        return null;
    }


    /**
     * Starts the main game loop.
     *
     * Cycles through players, allowing them to take turns until the win condition
     * is met. Once the game ends, the winner is announced.
     */
   // Dentro de GameManager.java

public void startGame() {
    while (!isGameOver()) {
        Player currentPlayer = findPlayerByIndex(players, currentPlayerIndex);

        if (currentPlayer != null) {
            
            // 1. Verificar se o jogador pode jogar (lida com SkipTurnEvent)
            if (!currentPlayer.canPlay()) { 
                // Se estiver bloqueado, apenas regista e avança para o próximo jogador
                System.out.println("Turno de " + currentPlayer.getName() + " pulado devido a bloqueio.");
                
            } else {
                
                // 2. Loop para Turno Normal + Turnos Extra
                boolean playAgain;

                do {
                    playAgain = false; // Assume que este é o último movimento

                    // O jogador joga o seu turno (move-se, interage com Enigma/Lever)
                    takeTurn(currentPlayer);
                    
                    // Se o jogo terminou após o movimento/interação, saímos do loop
                    if (isGameOver()) {
                        break;
                    }
                    
                    // 3. Verificação de Movimentos Extra Ganhos
                    if (currentPlayer.getExtraMoves() > 0) {
                        System.out.println(" Jogada Extra! " + currentPlayer.getName() + " ganhou a oportunidade de jogar novamente.");
                        
                        // Consome um movimento extra e prepara para repetir
                        currentPlayer.useExtraMove(); 
                        playAgain = true; 
                        
                        System.out.println("Movimentos extra restantes: " + currentPlayer.getExtraMoves());
                    }
                    
                    // O loop repete APENAS se playAgain for true (i.e., ganhou e consumiu um extra move)

                } while (playAgain);
            }
        }

        // Avança para o próximo jogador (após esgotar todos os movimentos extra)
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
    announceWinner();
}



    /**
     * Retrieves a specific player from the unordered list based on a numeric index.
     *
     * @param players The list of players.
     * @param index The zero-based index of the player to retrieve.
     * @return The Player object at the specified index, or null if not found.
     */
    private Player findPlayerByIndex(ArrayUnorderedList<Player> players, int index) {
        Iterator<Player> iterator = players.iterator();
        int currentIndex = 0;
        while (iterator.hasNext()) {
            Player player = iterator.next();

            if (currentIndex == index) {
                return player;
            }

            currentIndex++;
        }
        return null;
    }


    /**
     * Orchestrates a single turn for the specified player.
     *
     * Checks if the player is blocked. If not, it determines the type of the
     * current division and initiates the appropriate interaction or movement logic.
     *
     * @param player The player whose turn it is.
     */
    private void takeTurn(Player player) {
        if (!player.canPlay()) {
            return;
        }
        Division currentDivision = player.getCurrentDivision();

        System.out.println("\n" + player.getName() + ", é sua vez! Você está na divisão " + currentDivision.getId());

        if (currentDivision.getDt() == DivisionType.ENIGMA) {
            attemptEnigma(player, (Content) currentDivision.getContent());

        } else if (currentDivision.getDt() == DivisionType.LEVER) {
            attemptLever(player, (Content) currentDivision.getContent());

        } else {
            movePlayer(player);
        }
    }

    /**
     * Handles the interaction logic when a player is in a room with an Enigma.
     *
     * @param player The player attempting the enigma.
     * @param enigma The content object representing the enigma.
     */
    private void attemptEnigma(Player player, Content enigma) {
        boolean success = enigma.interactType(player);
        if (success) {
            System.out.println(" Você resolveu o enigma! Pode continuar.");
            movePlayer(player);
        } else {
            System.out.println(" Falhou em resolver o enigma. Tente novamente na próxima vez.");
        }
    }

    /**
     * Handles the interaction logic when a player is in a room with a Lever.
     *
     * @param player The player attempting to use the lever.
     * @param lever The content object representing the lever.
     */
    private void attemptLever(Player player, Content lever) {
        boolean success = lever.interactType(player);
        if (success) {
            System.out.println("Você ativou a alavanca! Pode continuar.");
            movePlayer(player);
        } else {
            System.out.println("Falhou em ativar a alavanca. Tente novamente na próxima vez.");
        }
    }


    /**
     * Initiates the movement phase of a turn.
     *
     * Verifies if the player is allowed to move and then calls the logic
     * to determine the next destination.
     *
     * @param player The player attempting to move.
     */
    private void movePlayer(Player player) {
        if (!player.canPlay()) {
            return;
        }
        getNextDivision(player);
    }


    /**
     * Calculates the valid hallways available from the current division.
     * 
     * Utiliza o método do MazeNetwork que filtra corretamente apenas os vizinhos diretos.
     *
     * @param currentDivision The division the player is currently in.
     * @return A doubly linked list of available Hallways.
     */
    private DoublyLinkedUnorderedList<Hallway> getAvailableHallways(Division currentDivision) {
        return mazeNetwork.getAvailableHallways(currentDivision);
    }


    /**
     * Coordinates the process of moving a player to a new division.
     *
     * @param player The player moving.
     */
    private void getNextDivision(Player player) {
        Division currentDivision = player.getCurrentDivision();

        DoublyLinkedUnorderedList<Hallway> availableHallways = getAvailableHallways(currentDivision);

        if (availableHallways.isEmpty()) {
            System.out.println(" Nenhum caminho disponível. Turno pulado.");
            return;
        }

        Hallway chosenHallway = player.chooseNextMove(availableHallways);

        if (chosenHallway != null) {
            player.moveTo(chosenHallway.getDestination(), chosenHallway);
            chosenHallway.activateEvent(player);
        } else {
            System.out.println(" Movimento cancelado.");
        }
    }

    /**
     * Checks if the game has ended.
     *
     * The game is over if any player currently occupies the central room.
     *
     * @return true if the win condition is met, false otherwise
     */
    private boolean isGameOver() {
        for (Player player : players) {
            if (player.getCurrentDivision().equals(centralRoom)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Announces the winner of the game.
     *
     * Scans the players to find who is in the central room and prints a victory message.
     */
    private void announceWinner() {
        for (Player player : players) {
            if (player.getCurrentDivision().equals(centralRoom)) {
                System.out.println("\n" + "=".repeat(60));
                System.out.println(" VITÓRIA! ");
                System.out.println(player.getName() + " chegou à Sala Central e conquistou o tesouro!");
                System.out.println("Enigmas resolvidos: " + player.generateJSONReport());
                System.out.println("=".repeat(60));
                return;
            }
        }
    }

    /**
     * Getter para o MazeNetwork (útil para debugging e visualização)
     * 
     * @return o MazeNetwork do jogo
     */
    public MazeNetwork getMazeNetwork() {
        return mazeNetwork;
    }

    /**
     * Getter para a lista de jogadores (necessário para SwapPositionEvent e SwapAllPositionsEvent)
     * 
     * @return a lista de todos os jogadores
     */
    public ArrayUnorderedList<Player> getAllPlayers() {
        return players;
    }

    /**
 * Desbloqueia uma Hallway no MazeNetwork (altera o peso para torná-la acessível).
 * Este método é chamado pelo Lever quando ativado com sucesso.
 * @param hallway A Hallway a ser desbloqueada.
 */
public void unlockHallway(Hallway hallway) {
    // Para simplificar, assumimos que o peso é o custo real
    double currentWeight = hallway.getWeight();


}
}