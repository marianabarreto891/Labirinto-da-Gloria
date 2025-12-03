package org.estg.ed.Lever;

import java.util.Random;

import org.estg.ed.Content.Content;


public class Lever extends Content {

    private int correctOption;
    private int randomNumero;
    Random r = new Random();

    private static final int MIN_OPTION = 1;
    private static final int MAX_OPTION = 4;

    public Lever(int idContent) {
        super(idContent);
        do {
            randomNumero = r.nextInt(3);
        } while (randomNumero == 0);
        this.correctOption = (int) (randomNumero);
    }

    /*@Override
    public boolean interactType(Player p, String choose) {

        if (isSolved()) {
            System.out.println("The Lever " + getIdContent() + " is already active. The way is open.");
            return true;
        }

        try {

            int playerSelection = Integer.parseInt(choose.trim());

            if (playerSelection == this.correctOption) {

                setSolved(true);
                // aplicarEfeito(); // Desbloqueia as passagens no Grafo

                System.out.println("Good, " + p.getNome()  "! The lever was activated and opened a new path!");

                return true;

            } else {

                System.out.println("Incorrect Number (" + playerSelection + ").");

                return false;
            }

        } catch (Exception e) {

            System.out.println("Invalid entry. Please enter a number.");

            return false;
        }
    }
        */
}
