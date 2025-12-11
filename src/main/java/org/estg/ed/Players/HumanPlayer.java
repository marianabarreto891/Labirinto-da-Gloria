package org.estg.ed.Players;

import java.util.Iterator;
import java.util.Scanner;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Hall.Hallway;

import Lists.DoublyLinkedUnorderedList;

/**
 * Represents a human-controlled player (Manual mode) in the Labyrinth of Glory
 * game simulator.
 * This class extends the abstract Player base and implements decision-making
 * logic based on user input. It fulfills the
 * requirement for the Manual game mode.
 */
public class HumanPlayer extends Player {

    /**
     * Constructs a new HumanPlayer instance, setting the initial state inherited
     * from the Player base class.
     * 
     * @param name            The unique identifier of the human player.
     * @param initialPosition The entry Division where the player begins the game.
     */
    public HumanPlayer(String name, Division initialPosition, Scanner sc) {
        super(name, initialPosition, sc);
    }

    /**
     * Represents the interaction for solving an Enigma challenge.
     *
     * @param question The text of the riddle or question to be displayed.
     * @param options  An array of strings representing the possible answers.
     * @return The raw string input provided by the user via the scanner.
     */
    @Override
    public String answerEnigma(String question, String[] options) {
        System.out.println(question);
        for (int i = 0; i < options.length; i++) {
            System.out.println("[" + i + "] " + options[i]);
        }
        return sc.nextLine();
    }

    /**
     * Reprsents the interaction for activating a Lever.
     *
     * @return The string input provided by the user representing their guess.
     */
    @Override
    public String answerLever() {
        System.out.println("Choose a number between 1 and 3 to activate the lever:");
        return sc.nextLine();
    }

    /**
     * Determines the human player's next move by prompting the user for input via
     * the console.
     * This method lists the available Hallways by iterating through the custom
     * Doubly Linked List
     * and uses a temporary array to map the user's selected index to the correct
     * Hallway object.
     * 
     * @param availableHallways The list of valid Hallways the player can choose
     *                          from the current Division.
     * @return The chosen Hallway for the next movement, or null if no moves
     *         are available or if an unrecoverable error occurs.
     */
    @Override
    public Hallway chooseNextMove(DoublyLinkedUnorderedList<Hallway> availableHallways) {
        int size = availableHallways.size();
        if (size == 0) {
            System.out.println(this.getName() + " has no available moves. Skipping turn.");
            return null;
        }

        Hallway[] hallwayOptions = new Hallway[size];
        Iterator<Hallway> iterator = availableHallways.iterator();
        int i = 0;
        System.out.println("\n" + this.getName() + " is in " + this.getCurrentDivision().getId() + ". Choose a path: ");
        while (iterator.hasNext()) {
            Hallway hallway = iterator.next();
            hallwayOptions[i] = hallway;
            System.out.println(
                    "[" + (i + 1) + "] Go to:  " + hallway.getDestination().getId() + " (" + hallway.toString() + ")");
            i++;
        }

        System.out.print("Enter the number of your choice (1 a " + size + "): ");
        try {
            int choiceIndex = Integer.parseInt(sc.nextLine()) - 1;
            if (choiceIndex >= 0 && choiceIndex < hallwayOptions.length) {
                return hallwayOptions[choiceIndex];
            } else {
                System.out.println("Invalid Number");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Invalid entry. Please try again!");
            return null;
        }
    }

    /**
     * Indicates whether the player is a bot.
     * 
     * @return Always false for this class.
     */
    public boolean isBot() {
        return false;
    }
}