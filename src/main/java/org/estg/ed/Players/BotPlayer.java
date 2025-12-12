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
 * 
 * @author Joana Rodrigues 8240334 and Mariana Barreto 8240344
 */
public class BotPlayer extends Player {

    /**
     * Constructs a new BotPlayer instance, setting the initial state inherited from
     * the Player base class.
     * 
     * @param name The unique identifier of the bot.
     * @param initialPosition The entry Division where the bot begins the game.
     */
    public BotPlayer(String name, Division initialPosition, Scanner sc) {
        super(name, initialPosition, sc);
    }

    
    /**
     * Simulates the bot attempting to solve an enigma.
     *
     *
     * @param question The question.
     * @param options  The array of possible answer choices.
     * @return A string representing the index of the randomly selected option.
     */
    @Override
    public String answerEnigma(String question, String[] options) {
        return String.valueOf((int) (Math.random() * options.length));
    }


    /**
     * Simulates the bot attempting to activate a lever.
     *
     * @return A string containing a random number from "1" to "3".
     */
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
     * 
     * @param availableHallways The list of valid Hallways the player can choose
     * from the current Division. 
     * @return The randomly chosen Hallway for the next movement, or null if
     *         no moves are available.
     */
    @Override
    public Hallway chooseNextMove(DoublyLinkedUnorderedList<Hallway> availableHallways) {
        int size = availableHallways.size(); // Gets the number of available moves.

        if (size == 0) {
            return null;
        }

        int randomIndex = (int) (Math.random() * size);

        Hallway chosenHallway = null;
        Iterator<Hallway> iterator = availableHallways.iterator();
        int currentIndex = 0;

        while (iterator.hasNext()) {
            chosenHallway = iterator.next();
            if (currentIndex == randomIndex) {
                return chosenHallway; 
            }
            currentIndex++;
        }

        return chosenHallway;
    }


    /**
     * Indicates whether the player is a bot.
     *
     * @return Always true for this class.
     */
    public boolean isBot() {
        return true;
    }

}