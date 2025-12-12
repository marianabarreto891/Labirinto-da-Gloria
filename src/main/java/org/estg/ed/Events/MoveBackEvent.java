package org.estg.ed.Events;

import org.estg.ed.Players.Player;

//@author Joana Rodrigues 8240334
/**
 * A specific type of Event that forces the player to move back a certain number of divisions.
 * The movement back is limited by the position held immediately after a position swap, if applicable.
 */
public class MoveBackEvent extends Event {

    private final int numberOfDivisions;

    /**
     * Creates a MoveBackEvent.
     * @param numberOfDivisions The number of divisions to move back.
     */
    public MoveBackEvent(int numberOfDivisions) {
        this.numberOfDivisions = numberOfDivisions;
    }

    /**
     * Applies the effect by moving the player back.
     * (Assumes Jogador and Labirinto classes have the necessary logic to calculate the back move).
     * @param player The Jogador affected.
     */
    @Override
    public void applyEffect(Player player) {
        player.moveBack(numberOfDivisions); 
        System.out.println(" Move Back! " + player.getName() + " was moved back " + numberOfDivisions + " division(s)!");
    }

    /**
     * Returns a string describing the event.
     * @return Description of the event.
     */
    @Override
    public String getDescription() {
        return "Move Back " + numberOfDivisions + " Divisions Event";
    }
}