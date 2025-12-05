package org.estg.ed.Events;

import org.estg.ed.Players.Player;

/**
 * A specific type of Event that grants the player one or more extra moves (turns) immediately.
 * [cite_start]This is one of the random effects that can occur when a player traverses a Hallway[cite: 38].
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
     * [cite_start]This effect modifies the player's internal state to allow immediate extra turns[cite: 38].
     * * @param player The Player affected by the event.
     */
    @Override
    public void applyEffect(Player player) {
        player.addExtraMoves(extraMoves); // This method must exist in the Player class
    }

    /**
     * Returns a string describing the event's action and impact.
     * * @return Description of the event (e.g., "Gained 2 extra move(s)!").
     */
    @Override
    public String getDescription() {
        return "Gained " + extraMoves + " extra move(s)!";
    }
}