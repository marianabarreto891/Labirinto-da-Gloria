package org.estg.ed.Events;

import org.estg.ed.Players.Player;


/**
 * A specific type of Event that grants the player one or more extra moves (turns) immediately.
 * This is one of the random effects that can occur when a player traverses a Hallway.
 * 
 * @author Joana Rodrigues 8240334
 */
public class ExtraMoveEvent extends Event {

    private final int extraMoves;

    /**
     * Creates an ExtraMoveEvent, specifying the number of additional moves to be granted.
     * * @param extraMoves The number of extra moves to grant.
     */
    public ExtraMoveEvent(int extraMoves) {
        this.extraMoves = extraMoves;
    }

    /**
     * Applies the effect by calling the necessary method on the Player object to increment their move count.
     * This effect modifies the player's internal state to allow immediate extra turns.
     * * @param player The Player affected by the event.
     */
    @Override
    public void applyEffect(Player player) {
        player.addExtraMoves(extraMoves); 
    }

    /**
     * Returns a string describing the event's action and impact.
     * * @return Description of the event.
     */
    @Override
    public String getDescription() {
        return "Gained " + extraMoves + " extra move(s)!";
    }
}