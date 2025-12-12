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
 * ATUALIZADO: Inicializa eventos dos corredores que precisam do GameManager
 * 
 * @author Joana Rodrigues 8240334
 */
public class GameManager {
    private final MazeNetwork mazeNetwork;
    private final ArrayUnorderedList<Player> players;
    private final Division centralRoom;
    private int currentPlayerIndex;
    private final String mazeName;

    private static final int MAX_TURNS = 1000;
    private int turnCount = 0;

    /**
     * Constructs a new GameManager, initializing the maze network and hallway events.
     *
     * @param maze    The {@code Maze} object containing divisions and hallways.
     * @param players A list of all {@code Player} objects participating in the game.
     */
    public GameManager(Maze maze, ArrayUnorderedList<Player> players) {
        this.players = players;
        this.centralRoom = maze.getCentralRoom();
        this.mazeName = maze.getName();

        HallwayManager.setGameManager(this);

    
        this.mazeNetwork = new MazeNetwork();

        
        for (Division div : maze.getD()) {
            mazeNetwork.addVertex(div);
        }


        for (Hallway hall : maze.getH()) {
            
            if (!hall.isEventInitialized()) {
                HallwayManager.initializeHallwayEvent(hall);
            }
            mazeNetwork.addHallway(hall);
        }

    
        Division entryDivision = findEntryDivision(maze.getD());
        mazeNetwork.setStartDivision(entryDivision);
        mazeNetwork.setCentralRoom(this.centralRoom);

        this.currentPlayerIndex = 0;
    }

    /**
     * Helper method to find the entry division in the maze.
     *
     * @param divisions The list of all divisions in the maze.
     * @return The {@code ENTRY} type {@code Division} found, or {@code null} if none exists.
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
     *
     * @param players The list of players.
     * @param index   The index of the player to retrieve.
     * @return The {@code Player} object at the given index, or {@code null} if the index is out of bounds.
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
     * Prints the current state of the game, including the position and content of each player.
     */
    private void printGameStatus() {
        System.out.println("\n--- Current Game Status (Turn: " + turnCount + ") ---");

        Iterator<Player> playerIterator = players.iterator();
        while (playerIterator.hasNext()) {
            Player player = playerIterator.next();
            Division currentDivision = player.getCurrentDivision();

            if (currentDivision == null) {
                System.out.println(player.getName() + " is currently unpositioned (ERROR).");
                continue;
            }

            System.out.print(player.getName() + " is in Division " + currentDivision.getId() +
                    " (Type: " + currentDivision.getDt() + ")");

            Content content = currentDivision.getContent();
            if (content != null) {
                System.out.println(" | Content: " + content.getClass().getSimpleName());
            } else {
                System.out.println(" | Content: None");
            }
        }

        System.out.println("--- End of Game Status ---\n");
    }

    /**
     * Starts the main game loop. Continues until a player reaches the central room or the maximum turn limit is reached.
     */
    public void startGame() {
        while (!isGameOver() && turnCount < MAX_TURNS) {
            turnCount++;

            if (turnCount % 100 == 0) {
                System.out.println("\nWarning: Turn " + turnCount + " - Game still in progress...");
            }

            printGameStatus();

            Player currentPlayer = findPlayerByIndex(players, currentPlayerIndex);

            if (currentPlayer != null) {
                if (!currentPlayer.canPlay()) {
                    System.out.println("Turn of " + currentPlayer.getName() + " skipped due to block.");
                } else {
                    boolean playAgain;

                    do {
                        playAgain = false;
                        takeTurn(currentPlayer);

                        if (isGameOver()) {
                            break;
                        }

                        if (currentPlayer.getExtraMoves() > 0) {
                            System.out.println("Extra Move! " + currentPlayer.getName() + " gained the opportunity to play again.");
                            currentPlayer.useExtraMove();
                            playAgain = true;
                            System.out.println("Extra moves remaining: " + currentPlayer.getExtraMoves());
                        }

                    } while (playAgain);
                }
            }

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }

        Player winner = null;
        for (Player player : players) {
            if (player.getCurrentDivision().equals(centralRoom)) {
                winner = player;
                break;
            }
        }

        if (turnCount >= MAX_TURNS) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("GAME OVER: Turn limit reached!");
            System.out.println("Possible infinite loop or impossible map to complete.");
            System.out.println("=".repeat(60));
        } else {
            announceWinner();
        }

        GameReportManager.saveGameReport(players, winner, mazeName, turnCount);
    }

    /**
     * Executes the actions for a single player's turn: content interaction (if any) and movement.
     *
     * @param player The {@code Player} whose turn it is.
     */
    private void takeTurn(Player player) {
        Division currentDivision = player.getCurrentDivision();
        System.out.println("\n" + player.getName() + ", it's your turn! You are in division " + currentDivision.getId());

        boolean interactionSuccess = true;

        if (currentDivision.getDt() == DivisionType.ENIGMA) {
            interactionSuccess = attemptEnigma(player, (Content) currentDivision.getContent());
        } else if (currentDivision.getDt() == DivisionType.LEVER) {
            interactionSuccess = attemptLever(player, (Content) currentDivision.getContent());
        }

        if (interactionSuccess) {
            movePlayer(player);
        } else {
            System.out.println(player.getName() + " remains in Division " + currentDivision.getId() + " to try again next turn.");
        }
    }

    /**
     * Attempts to interact with an ENIGMA type content.
     *
     * @param player The player attempting to solve the enigma.
     * @param enigma The enigma content.
     * @return {@code true} if the enigma was successfully solved, {@code false} otherwise.
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
     * Attempts to interact with a LEVER type content.
     *
     * @param player The player attempting to activate the lever.
     * @param lever  The lever content.
     * @return {@code true} if the lever was successfully activated, {@code false} otherwise.
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
     * Manages player movement to the next division.
     *
     * @param player The {@code Player} to move.
     */
    private void movePlayer(Player player) {
        if (!player.canPlay()) {
            return;
        }
        getNextDivision(player);
    }

    /**
     * Retrieves the list of hallways available from the current division.
     *
     * @param currentDivision The division from which to check for available hallways.
     * @return A list of available {@code Hallway} objects.
     */
    private DoublyLinkedUnorderedList<Hallway> getAvailableHallways(Division currentDivision) {
        return mazeNetwork.getAvailableHallways(currentDivision);
    }

    /**
     * Prompts the player to choose the next division to move to and executes the movement and associated hallway event.
     *
     * @param player The {@code Player} making the move.
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
     * Checks if the game has ended (i.e., if any player has reached the central room).
     *
     * @return {@code true} if the game is over, {@code false} otherwise.
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
     * Announces the winner and prints the final game statistics.
     */
    private void announceWinner() {
        for (Player player : players) {
            if (player.getCurrentDivision().equals(centralRoom)) {
                System.out.println("\n" + "=".repeat(60));
                System.out.println("VICTORY!");
                System.out.println(player.getName() + " reached the Central Room and conquered the treasure!");
                System.out.println("=".repeat(60));

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
     * Returns the maze network used by the game manager.
     *
     * @return The {@code MazeNetwork} instance.
     */
    public MazeNetwork getMazeNetwork() {
        return mazeNetwork;
    }

    /**
     * Returns the list of all players in the game.
     *
     * @return An {@code ArrayUnorderedList} of all {@code Player} objects.
     */
    public ArrayUnorderedList<Player> getAllPlayers() {
        return players;
    }

    /**
     * Unlocks a specific hallway (implementation detail: currently sets weight to current value,
     * requires actual unlock logic if weight changes).
     *
     * @param hallway The {@code Hallway} to be unlocked.
     */
    public void unlockHallway(Hallway hallway) {
        double currentWeight = hallway.getWeight();
        
    }
}