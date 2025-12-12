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
 * Class responsible for initializing the game, loading or creating the maze and its content,
 * and managing the selection of game modes (Manual or Automatic) and the number of players.
 * * NOTE: This class is NOT the entry point. Use MainMenu.java to run the program.
 */
public class GameInitializer {

    private static final String MAPS_FOLDER = "Files/";
    private static final String ENIGMAS_FOLDER = "files.enigma/";
    private static final String OUTPUT_FOLDER = "Files/";
    private static final String ENIGMAS_FILE = ENIGMAS_FOLDER + "enigma.json";

    /**
     * Initializes a complete game session with a specific map.
     *
     * @param mapPath Path to the map file.
     * @param scanner Scanner for user input.
     * @return {@code true} if the player wants to play again, {@code false} otherwise.
     */
    public boolean startGameSession(String mapPath, Scanner scanner) {
        System.out.println("\n=== Labyrinth of Glory Simulator ===\n");

        MazeManager mazeManager = new MazeManager();
        Maze maze = mazeManager.loadMazeWithContents(mapPath, ENIGMAS_FILE);

        if (maze == null) {
            System.err.println("ERROR: Failed to load the maze.");
            return false;
        }

        System.out.println("Map '" + maze.getName() + "' loaded successfully!");

        ArrayUnorderedList<Player> players = selectModeAndCreatePlayers(scanner);

        if (players.isEmpty()) {
            System.err.println("No players created. Ending session.");
            return false;
        }

        System.out.println("\n--- ENTRY POINTS ---");
        for (Player player : players) {

            System.out.println("\n--- " + player.getName() + ", choose your entry ---");

            Division initialPosition = selectEntryDivisionForPlayer(maze.getD(), scanner, player.getName());

            if (initialPosition == null) {
                System.err.println("ERROR: Maze does not have any entry division (ENTRY). Ending session.");
                return false;
            }

            player.setCurrentDivision(initialPosition);
        }

        GameManager gameManager = new GameManager(maze, players);
        gameManager.startGame();

        return askPlayAgain(scanner);
    }

    /**
     * Creates a new map interactively (WITHOUT starting the game).
     *
     * @param scanner Scanner for user input.
     */
    public void createMapOnly(Scanner scanner) {
        System.out.println("\n--- INTERACTIVE MAP EDITOR MODE ---");
        MazeEditor editor = new MazeEditor();

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

        System.out.print("\nFile name (without extension): ");
        String filename = scanner.nextLine().trim();

        if (!filename.endsWith(".json")) {
            filename += ".json";
        }

        String fullSavePath = OUTPUT_FOLDER + filename;
        editor.saveMaze(fullSavePath);

        System.out.println("Map successfully saved to " + fullSavePath + ".");
    }


    /**
     * Allows the user to select an entry division from a list of options.
     * * NOTE: This method is adapted for EACH player's choice.
     *
     * @param divisions  List of all divisions in the maze.
     * @param scanner    Scanner for user input.
     * @param playerName The name of the player making the choice.
     * @return The chosen entry {@code Division}, or {@code null} if no options exist or an error occurs.
     */
    private Division selectEntryDivisionForPlayer(ArrayUnorderedList<Division> divisions, Scanner scanner, String playerName) {
        ArrayUnorderedList<Division> entryDivisions = new ArrayUnorderedList<>();

        for (Division div : divisions) {
            if (div.getDt() == DivisionType.ENTRY) {
                entryDivisions.addToRear(div);
            }
        }

        if (entryDivisions.isEmpty()) {
            return null;
        }

        if (entryDivisions.size() == 1) {
            Division singleEntry = entryDivisions.first();
            System.out.println(
                    "Only one ENTRY division found (ID: " + singleEntry.getId() + "). Using it for " + playerName + ".");
            return singleEntry;
        }

        System.out.println("Available Entry Divisions:");
        for (Division div : entryDivisions) {
            System.out.println(" [" + div.getId() + "] Division " + div.getId());
        }

        while (true) {
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

    /**
     * Finds the entry division in the maze.
     *
     * @param divisions List of all divisions.
     * @return The first {@code ENTRY} type {@code Division} found, or {@code null} if none exists.
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
     * Asks the player if they want to play again.
     *
     * @param scanner Scanner for user input.
     * @return {@code true} if the player responds 'y' or 'yes', {@code false} otherwise.
     */
    private boolean askPlayAgain(Scanner scanner) {
        System.out.println("\n" + "=".repeat(60));
        System.out.print("Do you want to play again? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y") || response.equals("yes");
    }

    /**
     * Helper method to map an integer to a {@code DivisionType}.
     *
     * @param type The integer representation of the division type (1-5).
     * @return The corresponding {@code DivisionType}, or {@code null} if the input is invalid.
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
     * Creates and returns the list of players based on the mode selection.
     * * NOTE: The Initial Position is NOT set here. It will be set in {@code startGameSession}.
     *
     * @param scanner Scanner for user input.
     * @return An {@code ArrayUnorderedList} of {@code Player} objects.
     */
    private ArrayUnorderedList<Player> selectModeAndCreatePlayers(Scanner scanner) {
        ArrayUnorderedList<Player> players = new ArrayUnorderedList<>();

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
                players.addToRear(new HumanPlayer(name, tempInitialPosition, scanner));
            }

        } else if ("2".equals(modeChoice)) {
            System.out.println("\n--- AUTOMATIC MODE SELECTED ---");
            players.addToRear(new BotPlayer("Bot Alpha", tempInitialPosition, scanner));
            players.addToRear(new BotPlayer("Bot Beta", tempInitialPosition, scanner));

        } else {
            System.out.println("Invalid mode choice. Using Manual mode with 1 default player.");
            players.addToRear(new HumanPlayer("Default Player", tempInitialPosition, scanner));
        }

        return players;
    }


    /**
     * Returns the path to the maps folder (useful for MainMenu).
     *
     * @return The string path to the maps folder.
     */
    public static String getMapsFolder() {
        return MAPS_FOLDER;
    }

    /**
     * Returns the path to the enigmas folder.
     *
     * @return The string path to the enigmas folder.
     */
    public static String getEnigmasFolder() {
        return ENIGMAS_FOLDER;
    }
}