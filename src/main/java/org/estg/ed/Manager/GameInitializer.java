package org.estg.ed.Manager;

import java.util.Scanner;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Hall.HallwayManager;
import org.estg.ed.Maze.Maze;
import org.estg.ed.Maze.MazeEditor;
import org.estg.ed.Maze.MazeManager;
import org.estg.ed.Players.BotPlayer;
import org.estg.ed.Players.HumanPlayer;
import org.estg.ed.Players.Player;

import Lists.ArrayUnorderedList;

/**
 * Classe responsável por inicializar o jogo, carregar ou criar o labirinto e o
 * conteúdo,
 * e gerir a seleção dos modos de jogo (Manual ou Automático) e o número de
 * jogadores.
 * * NOTA: Esta classe NÃO é o ponto de entrada. Use MainMenu.java para executar o
 * programa.
 */
public class GameInitializer {

    // ⭐ ESTRUTURA DE PASTAS ATUALIZADA
    private static final String MAPS_FOLDER = "Files/";
    private static final String ENIGMAS_FOLDER = "files.enigma/";
    private static final String OUTPUT_FOLDER = "Files/";
    private static final String ENIGMAS_FILE = ENIGMAS_FOLDER + "enigma.json";

    /**
     * Inicia uma sessão completa de jogo com um mapa específico.
     * * @param mapPath Caminho para o ficheiro do mapa
     * @param scanner Scanner para input do utilizador
     * @return true se o jogador quer jogar novamente, false caso contrário
     */
    public boolean startGameSession(String mapPath, Scanner scanner) {
        System.out.println("\n=== Labyrinth of Glory Simulator ===\n");

        // 1. Carregar o mapa selecionado
        MazeManager mazeManager = new MazeManager();
        Maze maze = mazeManager.loadMazeWithContents(mapPath, ENIGMAS_FILE);

        if (maze == null) {
            System.err.println("ERROR: Failed to load the maze.");
            return false;
        }

        System.out.println("Map '" + maze.getName() + "' loaded successfully!");

        // 2. Seleção do modo de jogo e criação dos jogadores (com posição inicial null)
        ArrayUnorderedList<Player> players = selectModeAndCreatePlayers(scanner);

        if (players.isEmpty()) {
            System.err.println("No players created. Ending session.");
            return false;
        }

        // ⭐ PASSO ALTERADO: Cada jogador escolhe (ou é atribuído) um ponto de entrada
        System.out.println("\n--- ENTRY POINTS ---");
        for (Player player : players) {
            
            // ⭐ MENSAGEM ALTERADA
            System.out.println("\n--- " + player.getName() + ", choose your entry ---");
            
            // O selectEntryDivision será chamado para CADA jogador
            Division initialPosition = selectEntryDivisionForPlayer(maze.getD(), scanner, player.getName());
            
            if (initialPosition == null) {
                System.err.println("ERROR: Maze does not have any entry division (ENTRY). Ending session.");
                return false;
            }
            
            player.setCurrentDivision(initialPosition); 
        }

        // 5. Iniciar o jogo
        GameManager gameManager = new GameManager(maze, players);
        gameManager.startGame();

        // 6. Perguntar se quer jogar novamente
        return askPlayAgain(scanner);
    }
    
    // ... (O resto do createMapOnly fica inalterado)

    /**
     * Cria um novo mapa interativamente (SEM iniciar o jogo)
     * * @param scanner Scanner para input do utilizador
     */
    public void createMapOnly(Scanner scanner) {
        System.out.println("\n--- INTERACTIVE MAP EDITOR MODE ---");
        MazeEditor editor = new MazeEditor();

        // 1. METADADOS
        System.out.println("\n--- 1. EDITOR ---");
        int id = 1;
        try {
            System.out.print("New Map ID: ");
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID. Using 1.");
            id = 1;
        }

        System.out.print("New Map Name: ");
        String name = scanner.nextLine();
        editor.setMetadata(id, name);

        // 2. ADICIONAR DIVISÕES
        System.out.println("\n--- 2. ADD DIVISIONS ---");
        System.out.println("Types: [1] ENTRY, [2] ENIGMA, [3] LEVER, [4] CENTRAL, [5] NULL");

        String input;
        do {
            System.out.print("Add Division? (y/n, or 'd' for details): ");
            input = scanner.nextLine().trim().toLowerCase();

            if ("y".equals(input)) {
                try {
                    System.out.print("  Division ID: ");
                    int divId = Integer.parseInt(scanner.nextLine());
                    System.out.print("  Type (1-5): ");
                    int typeInt = Integer.parseInt(scanner.nextLine());

                    DivisionType type = getTypeFromInt(typeInt);
                    if (type != null) {
                        editor.addDivision(divId, type);
                    } else {
                        System.err.println("Invalid division type. Try again.");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid input (ID or Type must be a number).");
                }
            } else if ("d".equals(input)) {
                System.out.println("Current Divisions: " + editor.getCurrentMaze().getD());
            }

        } while (!"n".equals(input));

        // 3. CONECTAR DIVISÕES (CORREDORES)
        System.out.println("\n--- 3. CONNECT HALLWAYS ---");
        System.out.println(HallwayManager.getEventMapping());

        do {
            System.out.print("Connect Hallway? (y/n, or 'd' for details): ");
            input = scanner.nextLine().trim().toLowerCase();

            if ("y".equals(input)) {
                try {
                    System.out.print("  Origin Division (ID): ");
                    int id1 = Integer.parseInt(scanner.nextLine());
                    System.out.print("  Destination Division (ID): ");
                    int id2 = Integer.parseInt(scanner.nextLine());
                    System.out.print("  Hallway Weight (Cost): ");
                    double weight = Double.parseDouble(scanner.nextLine());
                    System.out.print("  Event ID (0-8): ");
                    int eventId = Integer.parseInt(scanner.nextLine());

                    editor.connectDivisions(id1, id2, weight, eventId);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid input (IDs, Weight, or Event must be a number).");
                }
            } else if ("d".equals(input)) {
                System.out.println("Current Hallways: " + editor.getCurrentMaze().getH());
            }

        } while (!"n".equals(input));

        // 4. SALVAR O MAPA (NÃO INICIA O JOGO)
        System.out.print("\nFile name (without extension): ");
        String filename = scanner.nextLine().trim();

        if (!filename.endsWith(".json")) {
            filename += ".json";
        }

        // ⭐ GUARDAR NA PASTA MAPS
        String fullSavePath = OUTPUT_FOLDER + filename;
        editor.saveMaze(fullSavePath);

        System.out.println("Map successfully saved to " + fullSavePath + ".");
    }


    /**
     * Permite ao utilizador selecionar uma divisão de entrada de uma lista de
     * opções.
     * * NOTA: Este é o método adaptado para a escolha de CADA jogador.
     * * @param divisions Lista de todas as divisões do labirinto.
     * @param scanner   Scanner para input do utilizador.
     * @param playerName O nome do jogador que está a escolher.
     * @return A Division de entrada escolhida, ou null se não houver opção/erro.
     */
    private Division selectEntryDivisionForPlayer(ArrayUnorderedList<Division> divisions, Scanner scanner, String playerName) {
        ArrayUnorderedList<Division> entryDivisions = new ArrayUnorderedList<>();

        // 1. Coletar todas as entradas disponíveis
        for (Division div : divisions) {
            if (div.getDt() == DivisionType.ENTRY) {
                entryDivisions.addToRear(div);
            }
        }

        if (entryDivisions.isEmpty()) {
            return null; // Não há entradas
        }

        if (entryDivisions.size() == 1) {
            // Se houver apenas uma, escolhe automaticamente
            Division singleEntry = entryDivisions.first();
            System.out.println(
                    "Only one ENTRY division found (ID: " + singleEntry.getId() + "). Using it for " + playerName + ".");
            return singleEntry;
        }

        // 2. Pedir ao utilizador para escolher
        System.out.println("Available Entry Divisions:");
        for (Division div : entryDivisions) {
            System.out.println(" [" + div.getId() + "] Division " + div.getId());
        }

        while (true) {
            // ⭐ MENSAGEM ALTERADA
            System.out.print(playerName + ", choose your entry (Enter the ID): ");
            try {
                int choiceId = Integer.parseInt(scanner.nextLine());
                for (Division div : entryDivisions) {
                    if (div.getId() == choiceId) {
                        System.out.println("Entry point for " + playerName + " set to Division " + choiceId);
                        return div;
                    }
                }
                System.err.println("Invalid ID. Please choose from the list above.");
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    // O método findEntryDivision original não é mais necessário, mas vamos mantê-lo
    // se for usado noutro local:
    /**
     * Encontra a divisão de entrada no labirinto
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
     * Pergunta ao jogador se quer jogar novamente
     */
    private boolean askPlayAgain(Scanner scanner) {
        System.out.println("\n" + "=".repeat(60));
        System.out.print("Do you want to play again? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y") || response.equals("yes");
    }

    /**
     * Auxiliar para mapear int para DivisionType
     */
    private DivisionType getTypeFromInt(int type) {
        switch (type) {
            case 1:
                return DivisionType.ENTRY;
            case 2:
                return DivisionType.ENIGMA;
            case 3:
                return DivisionType.LEVER;
            case 4:
                return DivisionType.CENTRAL;
            case 5:
                return DivisionType.NULL;
            default:
                return null;
        }
    }

    /**
     * Cria e retorna a lista de jogadores baseada na escolha do modo.
     * * NOTA: A Posição Inicial NÃO é definida aqui. Será definida em startGameSession.
     */
    private ArrayUnorderedList<Player> selectModeAndCreatePlayers(Scanner scanner) { // Assinatura alterada!
        ArrayUnorderedList<Player> players = new ArrayUnorderedList<>();
        
        // Passamos null para a posição inicial.
        Division tempInitialPosition = null; 

        System.out.println("\n--- MODE SELECTION ---");
        System.out.println(" [1] Manual (Human Players)");
        System.out.println(" [2] Automatic (Bots)");
        System.out.print("Your choice: ");
        String modeChoice = scanner.nextLine().trim();

        if ("1".equals(modeChoice)) {

            System.out.println("\n--- MANUAL MODE SELECTED ---");
            System.out.print("How many human players? (1-4): ");
            int numHumans = 1;

            try {
                numHumans = Integer.parseInt(scanner.nextLine());
                if (numHumans < 1 || numHumans > 4) {
                    System.out.println("Invalid number of players, using 1 player.");
                    numHumans = 1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, using 1 player.");
                numHumans = 1;
            }

            for (int i = 0; i < numHumans; i++) {
                System.out.print("Player Name " + (i + 1) + ": ");
                String name = scanner.nextLine();
                // Passamos null como initialPosition
                players.addToRear(new HumanPlayer(name, tempInitialPosition, scanner)); 
            }

        } else if ("2".equals(modeChoice)) {
            System.out.println("\n--- AUTOMATIC MODE SELECTED ---");
            // Passamos null como initialPosition
            players.addToRear(new BotPlayer("Bot Alpha", tempInitialPosition, scanner)); 
            players.addToRear(new BotPlayer("Bot Beta", tempInitialPosition, scanner));

        } else {
            System.out.println("Invalid mode choice. Using Manual mode with 1 default player.");
            // Passamos null como initialPosition
            players.addToRear(new HumanPlayer("Default Player", tempInitialPosition, scanner)); 
        }

        return players;
    }


    /**
     * Retorna o caminho da pasta de mapas (útil para o MainMenu)
     */
    public static String getMapsFolder() {
        return MAPS_FOLDER;
    }

    /**
     * Retorna o caminho da pasta de enigmas
     */
    public static String getEnigmasFolder() {
        return ENIGMAS_FOLDER;
    }
}