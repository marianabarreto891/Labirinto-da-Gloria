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
    private final MazeNetwork mazeNetwork;
    private final ArrayUnorderedList<Player> players;
    private final Division centralRoom;
    private int currentPlayerIndex;
    private final String mazeName;
    
    // Proteção contra loops infinitos
    private static final int MAX_TURNS = 1000;
    private int turnCount = 0;

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
        this.mazeName = maze.getName();
        
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
        
        // Definir o ponto de entrada
        Division entryDivision = findEntryDivision(maze.getD());
        mazeNetwork.setStartDivision(entryDivision);
        mazeNetwork.setCentralRoom(this.centralRoom);
        
        this.currentPlayerIndex = 0;
    }
    
    /**
     * Método auxiliar para encontrar a divisão de entrada.
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
 * Retrieves a specific player from the list based on index.
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



    public void startGame() {
    while (!isGameOver() && turnCount < MAX_TURNS) {
        turnCount++;
        
        // Aviso a cada 100 turnos (para debug)
        if (turnCount % 100 == 0) {
            System.out.println("\nWarning: Turn " + turnCount + " - Game still in progress...");
        }
        
        // USAR O MÉTODO QUE JÁ EXISTE NO GameManager
        Player currentPlayer = findPlayerByIndex(players, currentPlayerIndex);

        if (currentPlayer != null) {
            // Verificar se o jogador pode jogar
            if (!currentPlayer.canPlay()) {
                System.out.println("Turn of " + currentPlayer.getName() + " skipped due to block.");
            } else {
                // Loop para turno normal + turnos extra
                boolean playAgain;

                do {
                    playAgain = false;

                    // O jogador joga o seu turno
                    takeTurn(currentPlayer);
                    
                    // Se o jogo terminou, sair do loop
                    if (isGameOver()) {
                        break;
                    }
                    
                    // Verificação de movimentos extra
                    if (currentPlayer.getExtraMoves() > 0) {
                        System.out.println("Extra Move! " + currentPlayer.getName() + " gained the opportunity to play again.");
                        currentPlayer.useExtraMove();
                        playAgain = true;
                        System.out.println("Extra moves remaining: " + currentPlayer.getExtraMoves());
                    }

                } while (playAgain);
            }
        }

        // Avança para o próximo jogador
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
    
    // Determinar o vencedor
    Player winner = null;
    for (Player player : players) {
        if (player.getCurrentDivision().equals(centralRoom)) {
            winner = player;
            break;
        }
    }
    
    // Verificar se atingiu o limite de turnos
    if (turnCount >= MAX_TURNS) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("GAME OVER: Turn limit reached!");
        System.out.println("Possible infinite loop or impossible map to complete.");
        System.out.println("=".repeat(60));
    } else {
        announceWinner();
    }
    
    //  GUARDAR O RELATÓRIO DO JOGO (NOVA FUNCIONALIDADE)
    GameReportManager.saveGameReport(players, winner, mazeName, turnCount);
}

    /**
     * Orchestrates a single turn for the specified player.
     * * CORREÇÃO: Agora permite movimento MESMO quando falharem enigmas/alavancas
     */
    private void takeTurn(Player player) {
        if (!player.canPlay()) {
            return;
        }
        
        Division currentDivision = player.getCurrentDivision();
        System.out.println("\n" + player.getName() + ", it's your turn! You are in division " + currentDivision.getId());

        boolean canMove = true; // Por padrão, pode mover

        if (currentDivision.getDt() == DivisionType.ENIGMA) {
            boolean solved = attemptEnigma(player, (Content) currentDivision.getContent());
            
            // If the room has content, the player must interact with it, but the movement 
            // is decoupled from success (new logic).
            canMove = true;

        } else if (currentDivision.getDt() == DivisionType.LEVER) {
            boolean activated = attemptLever(player, (Content) currentDivision.getContent());
            
            // Movement is decoupled from activation success.
            canMove = true;
        }

        // Always attempts to move (unless the player cannot play)
        if (canMove) {
            movePlayer(player);
        }
    }

    /**
     * Handles enigma interaction.
     * Retorna true se resolveu, false se falhou.
     */
    private boolean attemptEnigma(Player player, Content enigma) {
        boolean success = enigma.interactType(player);
        
        if (success) {
            System.out.println("You solved the enigma!");
        } else {
            System.out.println("Failed to solve the enigma. You can try again or move forward.");
        }
        
        return success;
    }

    /**
     * Handles lever interaction.
     * Retorna true se ativou, false se falhou.
     */
    private boolean attemptLever(Player player, Content lever) {
        boolean success = lever.interactType(player);
        
        if (success) {
            System.out.println("You activated the lever!");
        } else {
            System.out.println("Failed to activate the lever. You can try again or move forward.");
        }
        
        return success;
    }

    /**
     * Initiates the movement phase of a turn.
     */
    private void movePlayer(Player player) {
        if (!player.canPlay()) {
            return;
        }
        getNextDivision(player);
    }

    /**
     * Calculates the valid hallways available from the current division.
     */
    private DoublyLinkedUnorderedList<Hallway> getAvailableHallways(Division currentDivision) {
        return mazeNetwork.getAvailableHallways(currentDivision);
    }

    /**
     * Coordinates the process of moving a player to a new division.
     */
    private void getNextDivision(Player player) {
        Division currentDivision = player.getCurrentDivision();

        DoublyLinkedUnorderedList<Hallway> availableHallways = getAvailableHallways(currentDivision);

        if (availableHallways.isEmpty()) {
            System.out.println("No path available. Turn skipped.");
            return;
        }

        Hallway chosenHallway = player.chooseNextMove(availableHallways);

        if (chosenHallway != null) {
            player.moveTo(chosenHallway.getDestination(), chosenHallway);
            chosenHallway.activateEvent(player);
        } else {
            System.out.println("Movement canceled.");
        }
    }

    /**
     * Checks if the game has ended.
     */
    private boolean isGameOver() {
        for (Player player : players) {
            if (player.getCurrentDivision().equals(centralRoom)) {
                return true;
            }
        }
        return false;
    }

   private void announceWinner() {
    for (Player player : players) {
        if (player.getCurrentDivision().equals(centralRoom)) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("VICTORY!");
            System.out.println(player.getName() + " reached the Central Room and conquered the treasure!");
            System.out.println("=".repeat(60));
            
            // Mostrar estatísticas na consola
            System.out.println("\n--- GAME STATISTICS ---");
            System.out.println("Total turns: " + turnCount);
            System.out.println("\n--- PLAYER REPORTS ---");
            
            for (Player p : players) {
                System.out.println("\nPlayer: " + p.getName());
                System.out.println("Current Position: " + p.getCurrentDivision().getId());
                System.out.println("Path History Size: " + p.getPathHistorySize());
            }
            
            return;
        }
    }
    }

    /**
     * Getter para o MazeNetwork
     */
    public MazeNetwork getMazeNetwork() {
        return mazeNetwork;
    }

    /**
     * Getter para a lista de jogadores
     */
    public ArrayUnorderedList<Player> getAllPlayers() {
        return players;
    }

    /**
     * Desbloqueia uma Hallway no MazeNetwork
     */
    public void unlockHallway(Hallway hallway) {
        double currentWeight = hallway.getWeight();
        // Implementar lógica de desbloqueio se necessário
    }
}