package org.estg.ed.Manager;

import java.util.Scanner;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Maze.Maze;
import org.estg.ed.Maze.MazeManager;
import org.estg.ed.Players.Player;
import org.estg.ed.Players.BotPlayer;
import org.estg.ed.Players.HumanPlayer;

import Lists.ArrayUnorderedList;

/**
 * Classe responsável por inicializar o jogo, carregar o labirinto e o conteúdo,
 * e gerir a seleção dos modos de jogo (Manual ou Automático).
 * Esta classe é a ponte entre a entrada do utilizador e o GameManager.
 */
public class GameInitializer {

    private static final String MAZE_FILE = "mapa.json";
    private static final String ENIGMAS_FILE = "enigmas.json";

    public static void main(String[] args) {
        GameInitializer initializer = new GameInitializer();
        initializer.setupAndStartGame();
    }

    /**
     * Coordenador principal para a configuração e início do jogo.
     */
    public void setupAndStartGame() {
        Scanner sc = new Scanner(System.in);

        // 1. CARREGAR LABIRINTO E CONTEÚDO
        MazeManager mazeManager = new MazeManager();
        Maze maze = mazeManager.loadMazeWithContents(MAZE_FILE, ENIGMAS_FILE);

        if (maze == null) {
            System.err.println("FATAL ERROR: Falha ao carregar o labirinto. A aplicação será encerrada.");
            return;
        }

        // 2. ENCONTRAR PONTO DE ENTRADA
        // Assumimos que a primeira divisão ENTRY na lista de divisões é o ponto de partida
        Division initialPosition = findEntryDivision(maze.getD());
        if (initialPosition == null) {
            System.err.println("FATAL ERROR: Labirinto não tem divisão de entrada (ENTRY).");
            return;
        }

        // 3. SELEÇÃO DO MODO DE JOGO
        System.out.println("=== Labyrinth of Glory Simulator ===");
        System.out.println("Escolha o modo de jogo:");
        System.out.println(" [1] Manual (Jogadores Humanos)");
        System.out.println(" [2] Automático (Bots)");
        System.out.print("Sua escolha: ");
        String modeChoice = sc.nextLine().trim();

        // 4. CRIAÇÃO DE JOGADORES
        ArrayUnorderedList<Player> players = createPlayers(modeChoice, initialPosition, sc);

        if (players.isEmpty()) {
            System.err.println("Nenhum jogador criado. Encerrando.");
            return;
        }
        
        // 5. INICIALIZAÇÃO E INÍCIO DO GAME MANAGER
        // Injetar o GameManager no HallwayManager ANTES da construção (feito no construtor)
        GameManager gameManager = new GameManager(maze, players);
        
        // Iniciar o loop principal do jogo
        gameManager.startGame();
        
        sc.close();
    }

    /**
     * Cria e retorna a lista de jogadores baseada na escolha do modo.
     */
    private ArrayUnorderedList<Player> createPlayers(String choice, Division initialPosition, Scanner sc) {
        ArrayUnorderedList<Player> players = new ArrayUnorderedList<>();

        if ("1".equals(choice)) {
            
            System.out.println("\n--- MODO MANUAL SELECIONADO ---");
            System.out.print("Quantos jogadores humanos? (1-4): ");
            int numHumans;
            try {
                numHumans = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                numHumans = 1;
            }

            for (int i = 0; i < numHumans; i++) {
                System.out.print("Nome do Jogador " + (i + 1) + ": ");
                String name = sc.nextLine();
                players.addToRear(new HumanPlayer(name, initialPosition, sc));
            }

        } else if ("2".equals(choice)) {
            
            System.out.println("\n--- MODO AUTOMÁTICO SELECIONADO ---");
            players.addToRear(new BotPlayer("Bot Alpha", initialPosition, sc));
            players.addToRear(new BotPlayer("Bot Beta", initialPosition, sc));
            // Pode adicionar mais bots
        } else {
            System.out.println("Escolha inválida. Usando modo Manual com 1 jogador padrão.");
            players.addToRear(new HumanPlayer("Default Player", initialPosition, sc));
        }

        return players;
    }
    
    /**
     * Método auxiliar para encontrar a divisão de entrada a partir da lista de divisões.
     */
    private Division findEntryDivision(ArrayUnorderedList<Division> divisions) {
        for (Division division : divisions) {
            if (division.getDt().toString().equals("ENTRY")) { // Assuming DivisionType.ENTRY
                return division;
            }
        }
        return null;
    }
}