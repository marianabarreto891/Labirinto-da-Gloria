package org.estg.ed.Lever;

import java.util.Random;

import org.estg.ed.Content.Content;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Players.Player;

public class Lever extends Content {

    private int correctOption;
    private static final int TOTAL_OPTIONS = 3;

    public Lever() {
        Random r = new Random();
        this.correctOption = r.nextInt(TOTAL_OPTIONS) + 1;
    }

    @Override
    public DivisionType getType() {
        return DivisionType.LEVER;
    }

    
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

    @Override
    public String toString() {
        return "Lever [correctOption=" + correctOption + "]";
    }

    

}
