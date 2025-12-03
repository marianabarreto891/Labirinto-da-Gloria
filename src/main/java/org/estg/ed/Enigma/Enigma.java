package org.estg.ed.Enigma;

import org.estg.ed.Content.Content;


public class Enigma extends Content implements Comparable<Enigma> {

    private int correctAnswer;
    private String question;
    private String options[];

    public Enigma(int idContent, String question, String options[], int correctAnswer) {
        super(idContent);
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.options = options;

    }

    @Override
    public int compareTo(Enigma other) {
        return Integer.compare(this.getIdContent(), other.getIdContent());
    }

    /*@Override
    public boolean interactType(Player p, String input) {

        if (Integer.parseInt(input.trim()) == this.correctAnswer) {

            setSolved(true);

            System.out.println("The player " +  p.getName()  " solved the enigma with success!");

            return true;
        } else {

            System.out.println("Incorrect answer. Try again!");

            return false;
        }
    }
    */

    public String getQuestion() {
        return question;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getOptions() {
        return options;
    }
}
