package org.estg.ed.Manager;

import java.util.Iterator;

import org.estg.ed.Content.Content;
import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Hall.Hallway;
import org.estg.ed.Players.Player;

import Graphs.Network;
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
    private Network<Division> mazeNetwork;
    private ArrayUnorderedList<Player> players;
    private Division centralRoom;
    private int currentPlayerIndex;


    /**
     * Constructs a new GameManager.
     *
     * Initializes the game state with the provided maze structure, list of players,
     * and the target central room.
     *
     * @param mazeNetwork The graph representing the labyrinth structure.
     * @param players     The list of players participating in the game.
     * @param centralRoom The target division representing the victory condition.
     */
    public GameManager(Network<Division> mazeNetwork, ArrayUnorderedList<Player> players, Division centralRoom) {
        this.mazeNetwork = mazeNetwork;
        this.players = players;
        this.centralRoom = centralRoom;
        this.currentPlayerIndex = 0;
    }


    /**
     * Starts the main game loop.
     *
     * Cycles through players, allowing them to take turns until the win condition
     * is met. Once the game ends, the winner is announced.
     */
    public void startGame() {
        while (!isGameOver()) {
            Player currentPlayer = findPlayerByIndex(players, currentPlayerIndex);

            takeTurn(currentPlayer);

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
        announceWinner();
    }

    /**
     * Retrieves a specific player from the unordered list based on a numeric index.
     *
     *
     * @param players The list of players.
     * @param index   The zero-based index of the player to retrieve.
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
     * current division and initiates the appropriate
     * interaction or movement logic.
     *
     * @param player The player whose turn it is.
     */
    private void takeTurn(Player player) {
        if (!player.canPlay()) {
            return;
        }
        Division currentDivision = player.getCurrentDivision();

        System.out.println(player.getName() + ", it's your turn. You are in division " + currentDivision.getId());

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
            System.out.println("You solved the enigma! You can continue.");
        } else {
            System.out.println("You failed to solve the enigma. Try again next turn.");
        }
    }

    /**
     * Handles the interaction logic when a player is in a room with a Lever.
     *
     * @param player The player attempting to use the lever.
     * @param lever  The content object representing the lever.
     */
    private void attemptLever(Player player, Content lever) {
        boolean success = lever.interactType(player);
        if (success) {
            System.out.println("You activated the lever! You can continue.");
            movePlayer(player);
        } else {
            System.out.println("You failed to activate the lever. Try again next turn.");
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
     * Uses the maze network graph to find neighboring divisions and constructs
     * Hallway objects representing the edges between them.
     *
     * @param currentDivision The division the player is currently in.
     * @return A doubly linked list of available Hallways.
     */
    private DoublyLinkedUnorderedList<Hallway> getAvailableHallways(Division currentDivision) {
        DoublyLinkedUnorderedList<Hallway> availableHallways = new DoublyLinkedUnorderedList<>();

        Iterator<Division> neighbors = mazeNetwork.iteratorBFS(currentDivision);

        while (neighbors.hasNext()) {
            Division neighbor = neighbors.next();

            double weight = mazeNetwork.shortestPathWeight(currentDivision, neighbor);

            if (weight < Double.POSITIVE_INFINITY) {
                if (neighbor.getDt() != DivisionType.ENTRY) {
                    Hallway hallway = new Hallway(
                            currentDivision,
                            neighbor,
                            0,
                            weight);
                    availableHallways.addToRear(hallway);
                }
            }
        }

        return availableHallways;
    }


    /**
     * Coordinates the process of moving a player to a new division.
     *
     *
     * @param player The player moving.
     */
    private void getNextDivision(Player player) {
        Division currentDivision = player.getCurrentDivision();

        DoublyLinkedUnorderedList<Hallway> availableHallways = getAvailableHallways(currentDivision);

        Hallway chosenHallway = player.chooseNextMove(availableHallways);

        if (chosenHallway != null) {
            player.moveTo(chosenHallway.getDestination(), chosenHallway);
            chosenHallway.activateEvent(player);
        } else {
            System.out.println("No available moves. Skipping turn.");
        }
    }

    /**
     * Checks if the game has ended.
     *
     * The game is over if any player currently occupies the central room.
     *
     * @return true if the win condition is met, false otherwise.
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
                System.out.println(player.getName() + " has reached the central room and won the game!");
                return;
            }
        }
    }
}
