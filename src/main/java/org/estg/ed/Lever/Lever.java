package org.estg.ed.Lever;

import java.util.Random;

import org.estg.ed.Content.Content;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Players.Player;

/**
 * Represents a Lever obstacle within a room in the maze.
 *
 * This class extends the generic Content to provide specific logic. A lever requires the player to guess a number to "activate" it, typically
 * opening a blocked path or allowing progress.
 */
public class Lever extends Content {

    private final int correctOption;
    private static final int TOTAL_OPTIONS = 3;


    /**
     * Create a new Lever.
     *
     * Initializes the lever by generating a random integer between 1 and TOTAL_OPTIONS
     * which serves as the "key" to unlock it.
     */
    public Lever() {
        Random r = new Random();
        this.correctOption = r.nextInt(TOTAL_OPTIONS) + 1;
    }


    /**
     * Identifies the specific type of this content.
     *
     * @return The enum constant DivisionType.LEVER.
     */
    @Override
    public DivisionType getType() {
        return DivisionType.LEVER;
    }

    /**
     * Represents the interaction logic between a player and this lever.
     *
     *
     * @param p The player attempting to activate the lever.
     * @return true if the interaction results in success (lever activated), false otherwise.
     */
    @Override
    public boolean interactType(Player p) {
        if (isSolved()) {
            System.out.println("The Lever is already active. The way is open.");
            return true;
        }

        String choice = p.answerLever();
        try {
            int playerSelection = Integer.parseInt(choice.trim());
            if (playerSelection == this.correctOption) {
                setSolved(true);
                System.out.println("Good, " + p.getName() + "! The lever was activated and opened a new path!");
                return true;
            } else {
                System.out.println("Incorrect Number (" + playerSelection + ").");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid entry. Please enter a number between 1 and 3.");
            return false;
        }
    }


    /**
     * Returns a string representation of the Lever.
     *
     * Includes the correct option required to solve it.
     *
     * @return A formatted string describing the lever's state.
     */
    @Override
    public String toString() {
        return "Lever [correctOption=" + correctOption + "]";
    }

    

}
