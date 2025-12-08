package org.estg.ed.Enigma;

import java.util.Arrays;

import org.estg.ed.Content.Content;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Players.Player;

public class Enigma extends Content {

    private final int correctAnswer;
    private final String question;
    private final String options[];

    public Enigma(String question, String options[], int correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.options = options;

    }


    @Override
    public DivisionType getType() {
        return DivisionType.ENIGMA;
    }

  
    @Override
    public boolean interactType(Player p) {
        if (isSolved()) {
            return true;
        }

        String answer = p.answerEnigma(question, options);

        try {
            int op = Integer.parseInt(answer.trim());
            if (op == correctAnswer) {
                setSolved(true);
                System.out.println("The player " + p.getName() + " solved the enigma with success!");
                p.incrementEnigmasSolved();
                return true;
            } else {
                System.out.println("Incorrect answer. Try again!");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return false;
        }       
    }

    public String getQuestion() {
        return question;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return "Enigma [correctAnswer=" + correctAnswer + ", question=" + question + ", options="
                + Arrays.toString(options) + ", toString()=" + super.toString() + "]";
    }

    
}
