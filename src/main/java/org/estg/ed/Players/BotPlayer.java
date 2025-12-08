package org.estg.ed.Players;

import java.util.Iterator;
import java.util.Scanner;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Hall.Hallway;

import Lists.DoublyLinkedUnorderedList;

/**
 * Represents an automated player (Bot) in the Labyrinth of Glory game
 * simulator.
 * This class extends the abstract Player base and implements automated
 * decision-making
 * logic, fulfilling the requirement for the Automatic game mode.
 */
public class BotPlayer extends Player {

    /**
     * Constructs a new BotPlayer instance, setting the initial state inherited from
     * the Player base class.
     * * @param name The unique identifier of the bot.
     * 
     * @param initialPosition The entry Division where the bot begins the game.
     */
    public BotPlayer(String name, Division initialPosition, Scanner sc) {
        super(name, initialPosition, sc);
    }

    @Override
    public String answerEnigma(String question, String[] options) {
        return String.valueOf((int) (Math.random() * options.length));
    }

    @Override
    public String answerLever() {
        return String.valueOf((int) (Math.random() * 3) + 1);
    }

    /**
     * Determines the Bot's next move using a simple, automated strategy (random
     * selection).
     * This method fulfills the contract of the abstract Player class for the
     * Automatic mode.
     * * The strategy iterates through the list of available hallways until it finds
     * the hallway
     * corresponding to a randomly generated index.
     * * * @param availableHallways The list of valid Hallways the player can choose
     * from the current Division.
     * 
     * @return The randomly chosen Hallway for the next movement, or {@code null} if
     *         no moves are available.
     */
    @Override
    public Hallway chooseNextMove(DoublyLinkedUnorderedList<Hallway> availableHallways) {
        int size = availableHallways.size(); // Gets the number of available moves.

        if (size == 0) {
            return null;
        }

        // 1. Choose a random index (from 0 to size - 1)
        int randomIndex = (int) (Math.random() * size);

        // 2. Iterate until the element is found (O(N) operation, typical for linked
        // lists)
        Hallway chosenHallway = null;
        Iterator<Hallway> iterator = availableHallways.iterator();
        int currentIndex = 0;

        while (iterator.hasNext()) {
            chosenHallway = iterator.next();
            if (currentIndex == randomIndex) {
                return chosenHallway; // Found the randomly selected Hallway
            }
            currentIndex++;
        }

        // Should theoretically not be reached if size > 0, but provides a fallback.
        return chosenHallway;
    }

    public boolean isBot() {
        return true;
    }

}