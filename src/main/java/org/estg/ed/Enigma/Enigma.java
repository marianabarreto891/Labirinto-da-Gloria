package org.estg.ed.Enigma;

import org.estg.ed.Content.Content;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Players.Player;

/**
 * Represents a question within the maze.
 *
 * This class extends the generic Content to provide specific logic for
 * question-and-answer
 * interactions. It holds a question, a list of possible options, and the index
 * of the correct answer.
 */
public class Enigma extends Content {

    private final int correctAnswer;

    private final String question;

    private final String options[];

    /**
     * Create a new Enigma.
     *
     * @param question      The text of the riddle or question to be presented to
     *                      the player.
     * @param options       An array of strings representing the possible choices.
     * @param correctAnswer The integer index corresponding to the correct string in
     *                      the options array.
     */
    public Enigma(String question, String options[], int correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.options = options;

    }

    /**
     * Identifies the specific type of this content.
     *
     * @return The enum DivisionType.ENIGMA.
     */
    @Override
    public DivisionType getType() {
        return DivisionType.ENIGMA;
    }

    /**
     * Represents the interaction logic between a player and this enigma.
     *
     * @param p The player attempting to solve the enigma.
     * @return true if the interaction results in a success (solved), false if the
     *         answer was incorrect or invalid.
     */
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

    /**
     * Retrieves the text of the question.
     *
     * @return The question string.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Retrieves the index of the correct answer.
     *
     * @return The integer index of the correct option.
     */
    public int getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Retrieves the list of possible answers.
     *
     * @return An array of strings containing the options.
     */
    public String[] getOptions() {
        return options;
    }

    /**
     * Returns a string representation of the Enigma.
     *
     * @return A formatted string describing the enigma state.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Enigma [question=").append(question)
                .append(", correctAnswer=").append(correctAnswer)
                .append(", options=[");

        for (int i = 0; i < options.length; i++) {
            sb.append(options[i]);
            if (i < options.length - 1) {
                sb.append(", ");
            }
        }

        sb.append("], ").append(super.toString()).append("]");
        return sb.toString();
    }

}
