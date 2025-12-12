package org.estg.ed.Events;

import org.estg.ed.Players.Player;

//@author Joana Rodrigues 8240334
/**
 * A specific type of Event that prevents the player from moving for a specified number of turns.
 * This effect alters the turn sequencing for the player.
 */
public class SkipTurnEvent extends Event {

    private final int turnsToSkip;

    /**
     * Creates a SkipTurnEvent.
     * @param turnsToSkip The number of turns the player is blocked from playing.
     */
    public SkipTurnEvent(int turnsToSkip) {
        this.turnsToSkip = turnsToSkip;
    }

    /**
     * Applies the effect by setting a counter in the Jogador object.
     * (Assumes Jogador has a setTurnsToSkip() method).
     * @param player The Jogador affected.
     */
    @Override
    public void applyEffect(Player player) {
        player.setTurnsToSkip(turnsToSkip); 
        System.out.println(" Skip Turn! " + player.getName() + " is blocked for " + turnsToSkip + " turn(s)!");
    }

    /**
     * Returns a string describing the event.
     * @return Description of the event.
     */
    @Override
    public String getDescription() {
        return "Skip Turn for " + turnsToSkip + " Turns Event";
    }
}