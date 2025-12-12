package org.estg.ed;

import java.io.File;
import java.util.Scanner;

import org.estg.ed.Manager.GameInitializer;

//@author Joana Rodrigues 8240334
/**
 * Main menu for the Labyrinth of Glory game.
 * Handles the overall application flow, including starting game sessions,
 * creating new maps, and listing available maps.
 */
public class MainMenu {

    private static final String FOLDER_NAME = "Files/";
    private final Scanner scanner;

    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
        menu.run();
    }

    public MainMenu() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Main menu loop that displays options and handles user input.
     */
    public void run() {
        boolean exit = false;

        while (!exit) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    playGame();
                    break;
                case "2":
                    createNewMap();
                    break;
                case "3":
                    listAvailableMaps();
                    break;
                case "4":
                    exit = true;
                    System.out.println("\nThank you for playing! See you next time!");
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.\n");
            }
        }

        scanner.close();
    }

    /**
     * Displays the main menu options.
     */
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          LABYRINTH OF GLORY");
        System.out.println("=".repeat(50));
        System.out.println(" [1] Play Game");
        System.out.println(" [2] Create New Map");
        System.out.println(" [3] View Available Maps");
        System.out.println(" [4] Exit");
        System.out.println("=".repeat(50));
        System.out.print("Your choice: ");
    }

    /**
     * Starts a game session by letting the user select a map and launch the game.
     */
    private void playGame() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("              GAME MODE");
        System.out.println("=".repeat(50));

        File folder = new File(FOLDER_NAME);
        File[] mapFiles = folder.listFiles((dir, name) -> name.endsWith(".json") && !name.equals("enigmas.json"));

        if (mapFiles == null || mapFiles.length == 0) {
            System.out.println("\nNo maps found in the 'Files/' folder.");
            System.out.println("Please create a map first using menu option [2].\n");
            waitForEnter();
            return;
        }

        System.out.println("\nAvailable Maps:");
        for (int i = 0; i < mapFiles.length; i++) {
            System.out.println(" [" + (i + 1) + "] " + mapFiles[i].getName());
        }
        System.out.println(" [0] Back to Main Menu");

        System.out.print("\nChoose the map: ");
        String mapChoice = scanner.nextLine().trim();

        try {
            int choice = Integer.parseInt(mapChoice);

            if (choice == 0) return;

            if (choice < 1 || choice > mapFiles.length) {
                System.out.println("\nInvalid choice.");
                waitForEnter();
                return;
            }

            String selectedMap = FOLDER_NAME + mapFiles[choice - 1].getName();
            GameInitializer initializer = new GameInitializer();
            boolean playAgain = initializer.startGameSession(selectedMap, scanner);

            if (playAgain) playGame();

        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input.");
            waitForEnter();
        }
    }

    /**
     * Initiates the interactive map creation process without starting a game.
     */
    private void createNewMap() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          CREATE NEW MAP");
        System.out.println("=".repeat(50));

        GameInitializer initializer = new GameInitializer();
        initializer.createMapOnly(scanner);

        System.out.println("\nMap successfully created!");
        System.out.println("Use option [1] to play this map.\n");
        waitForEnter();
    }

    /**
     * Lists all available maps in the Files folder along with their sizes.
     */
    private void listAvailableMaps() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          AVAILABLE MAPS");
        System.out.println("=".repeat(50));

        File folder = new File(FOLDER_NAME);
        File[] mapFiles = folder.listFiles((dir, name) -> name.endsWith(".json") && !name.equals("enigmas.json"));

        if (mapFiles == null || mapFiles.length == 0) {
            System.out.println("\nNo maps found.");
            System.out.println("Create a map using menu option [2].\n");
        } else {
            System.out.println("\nTotal maps: " + mapFiles.length + "\n");
            for (int i = 0; i < mapFiles.length; i++) {
                File file = mapFiles[i];
                long sizeKB = file.length() / 1024;
                System.out.printf(" [%d] %s (%.1f KB)%n", (i + 1), file.getName(), sizeKB / 1.0);
            }
            System.out.println();
        }

        waitForEnter();
    }

    /**
     * Pauses execution until the user presses ENTER.
     */
    private void waitForEnter() {
        System.out.print("Press ENTER to continue...");
        scanner.nextLine();
    }
}
