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
 * Classe responsável por inicializar o jogo, carregar ou criar o labirinto e o conteúdo,
 * e gerir a seleção dos modos de jogo (Manual ou Automático) e o número de jogadores.
 */
public class GameInitializer {

    private static final String MAZE_FILE_DEFAULT = "files_labirinto/mapa.json";
    private static final String ENIGMAS_FILE = "files_enigmas/enigmas.json";


    public static void main(String[] args) {
        GameInitializer initializer = new GameInitializer();
        initializer.setupAndStartGame();
    }

    /**
     * Coordenador principal para a configuração e início do jogo.
     */
    public void setupAndStartGame() {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Labyrinth of Glory Simulator ===");
        
        // 1. ESCOLHA/CRIAÇÃO DO LABIRINTO (Novo Requisito)
        Maze maze = loadOrCreateMaze(sc);

        if (maze == null) {
            System.err.println("FATAL ERROR: Falha ao carregar/criar o labirinto. A aplicação será encerrada.");
            return;
        }

        // 2. ENCONTRAR PONTO DE ENTRADA (Requisito Existente)
        Division initialPosition = findEntryDivision(maze.getD());
        if (initialPosition == null) {
            System.err.println("FATAL ERROR: Labirinto não tem divisão de entrada (ENTRY).");
            return;
        }

        // 3. SELEÇÃO DO MODO DE JOGO E CRIAÇÃO DE JOGADORES (Requisito com Melhoria)
        ArrayUnorderedList<Player> players = selectModeAndCreatePlayers(initialPosition, sc);

        if (players.isEmpty()) {
            System.err.println("Nenhum jogador criado. Encerrando.");
            return;
        }
        
        // 4. INICIALIZAÇÃO E INÍCIO DO GAME MANAGER
        GameManager gameManager = new GameManager(maze, players);
        
        gameManager.startGame();
        
        sc.close();
    }

    /**
     * Permite ao utilizador escolher entre carregar um mapa ou criar um novo.
     */
    private Maze loadOrCreateMaze(Scanner sc) {
        System.out.println("--- GESTÃO DE MAPAS ---");
        System.out.println(" [1] Carregar mapa existente (" + MAZE_FILE_DEFAULT + ")");
        System.out.println(" [2] Criar novo mapa (Entra no modo Editor Interativo)");
        System.out.print("Sua escolha: ");
        String mapChoice = sc.nextLine().trim();

        if ("1".equals(mapChoice)) {
            // Carregar Labirinto Existente (Inclui Enigmas)
            MazeManager mazeManager = new MazeManager();
            return mazeManager.loadMazeWithContents(MAZE_FILE_DEFAULT, ENIGMAS_FILE);
            
        } else if ("2".equals(mapChoice)) {
            // Criar Novo Mapa de forma interativa
            return interactiveMazeEditor(sc);
            
        } else {
            System.err.println("Escolha inválida. Carregando mapa padrão.");
            MazeManager mazeManager = new MazeManager();
            return mazeManager.loadMazeWithContents(MAZE_FILE_DEFAULT, ENIGMAS_FILE);
        }
    }
    
    /**
     * Lógica para o modo de edição interativa.
     * Permite ao utilizador adicionar divisões e corredores através da consola.
     */
    private Maze interactiveMazeEditor(Scanner sc) {
        System.out.println("\n--- MODO EDITOR INTERATIVO DE MAPAS ---");
        MazeEditor editor = new MazeEditor();
        
        // 1. METADADOS
        System.out.println("\n--- 1. METADADOS ---");
        int id = -1;
        try {
            System.out.print("ID do Novo Mapa: ");
            id = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("ID inválido. Usando 1.");
            id = 1;
        }
        System.out.print("Nome do Novo Mapa: ");
        String name = sc.nextLine();
        editor.setMetadata(id, name);
        
        // 2. ADICIONAR DIVISÕES
        System.out.println("\n--- 2. ADICIONAR DIVISÕES ---");
        System.out.println("Tipos: [1] ENTRY, [2] ENIGMA, [3] LEVER, [4] CENTRAL, [5] NULL");
        
        String input;
        do {
            System.out.print("Adicionar Divisão? (s/n, ou 'd' para detalhes): ");
            input = sc.nextLine().trim().toLowerCase();
            
            if ("s".equals(input)) {
                try {
                    System.out.print("  ID da Divisão: ");
                    int divId = Integer.parseInt(sc.nextLine());
                    System.out.print("  Tipo (1-5): ");
                    int typeInt = Integer.parseInt(sc.nextLine());
                    
                    DivisionType type = getTypeFromInt(typeInt);
                    if (type != null) {
                        editor.addDivision(divId, type);
                    } else {
                        System.err.println("Tipo de divisão inválido. Tente novamente.");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Entrada inválida (ID ou Tipo deve ser um número).");
                }
            } else if ("d".equals(input)) {
                 System.out.println("Divisões Atuais: " + editor.getCurrentMaze().getD());
            }

        } while (!"n".equals(input));


        // 3. CONECTAR DIVISÕES (CORREDORES)
        System.out.println("\n--- 3. CONECTAR CORREDORES ---");
        
        // **********************************************
        // IMPRIME O MAPAMENTO DOS EVENTOS AQUI!
        // **********************************************
        System.out.println(HallwayManager.getEventMapping());

        do {
            System.out.print("Conectar Corredor? (s/n, ou 'd' para detalhes): ");
            input = sc.nextLine().trim().toLowerCase();

            if ("s".equals(input)) {
                try {
                    System.out.print("  Divisão Origem (ID): ");
                    int id1 = Integer.parseInt(sc.nextLine());
                    System.out.print("  Divisão Destino (ID): ");
                    int id2 = Integer.parseInt(sc.nextLine());
                    System.out.print("  Peso do Corredor (Custo): ");
                    double weight = Double.parseDouble(sc.nextLine());
                    System.out.print("  ID do Evento (0-8): ");
                    int eventId = Integer.parseInt(sc.nextLine());
                    
                    editor.connectDivisions(id1, id2, weight, eventId);
                } catch (NumberFormatException e) {
                    System.err.println("Entrada inválida (IDs, Peso ou Evento deve ser um número).");
                }
            } else if ("d".equals(input)) {
                System.out.println("Corredores Atuais: " + editor.getCurrentMaze().getH());
            }

        } while (!"n".equals(input));


        // 4. SALVAR E PREPARAR PARA O JOGO
        System.out.print("\nNome do ficheiro para Salvar (ex: meu_mapa.json): ");
        String filename = sc.nextLine();
        editor.saveMaze(filename);
        
        // Recarregar o mapa recém-criado, mas agora com o conteúdo (Enigmas/Levers)
        MazeManager mazeManager = new MazeManager();
        return mazeManager.loadMazeWithContents(filename, ENIGMAS_FILE);
    }
    
    /**
     * Auxiliar para mapear int para DivisionType (simetria com MazeLoader/Saver).
     */
    private DivisionType getTypeFromInt(int type) {
        switch (type) {
            case 1: return DivisionType.ENTRY;
            case 2: return DivisionType.ENIGMA;
            case 3: return DivisionType.LEVER;
            case 4: return DivisionType.CENTRAL;
            case 5: return DivisionType.NULL;
            default: return null;
        }
    }
    
    /**
     * Cria e retorna a lista de jogadores baseada na escolha do modo e número de jogadores.
     */
    private ArrayUnorderedList<Player> selectModeAndCreatePlayers(Division initialPosition, Scanner sc) {
        ArrayUnorderedList<Player> players = new ArrayUnorderedList<>();

        System.out.println("\n--- SELEÇÃO DE MODO ---");
        System.out.println(" [1] Manual (Jogadores Humanos)");
        System.out.println(" [2] Automático (Bots)");
        System.out.print("Sua escolha: ");
        String modeChoice = sc.nextLine().trim();

        if ("1".equals(modeChoice)) {
            
            System.out.println("\n--- MODO MANUAL SELECIONADO ---");
            System.out.print("Quantos jogadores humanos? (1-4): ");
            int numHumans = 1;
            try {
                numHumans = Integer.parseInt(sc.nextLine());
                if (numHumans < 1 || numHumans > 4) {
                    System.out.println("Número de jogadores inválido, usando 1 jogador.");
                    numHumans = 1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida, usando 1 jogador.");
                numHumans = 1;
            }

            for (int i = 0; i < numHumans; i++) {
                System.out.print("Nome do Jogador " + (i + 1) + ": ");
                String name = sc.nextLine();
                players.addToRear(new HumanPlayer(name, initialPosition, sc));
            }

        } else if ("2".equals(modeChoice)) {
            
            System.out.println("\n--- MODO AUTOMÁTICO SELECIONADO ---");
            players.addToRear(new BotPlayer("Bot Alpha", initialPosition, sc));
            players.addToRear(new BotPlayer("Bot Beta", initialPosition, sc));
            
        } else {
            System.out.println("Escolha de modo inválida. Usando modo Manual com 1 jogador padrão.");
            players.addToRear(new HumanPlayer("Default Player", initialPosition, sc));
        }

        return players;
    }
    
    private Division findEntryDivision(ArrayUnorderedList<Division> divisions) {
        for (Division division : divisions) {
            if (division.getDt().toString().equals("ENTRY")) {
                return division;
            }
        }
        return null;
    }
}