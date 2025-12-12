package org.estg.ed.Events;

import org.estg.ed.Players.Player;

 //@author Joana Rodrigues 8240334

/**
 * The base class for all random events that can occur inside a Hallway.
 * It ensures that every event knows how to apply its specific effect to a player.
 * The core logic for effects like gaining turns, swapping positions, or moving back
 * will be defined in subclasses.
 */
public abstract class Event implements IEvent {

    /**
     * Applies the specific effect of this event to the player(s).
     * This method must be implemented by all concrete subclasses to define the event's action.
     * * @param player The Player affected by the event.
     */
    @Override
    public abstract void applyEffect(Player player);

    /**
     * Gets a descriptive summary of the event, useful for the game log and end-game reports.
     * * @return A string describing the event's action.
     */
    @Override
    public abstract String getDescription();
    
    /**
     * Provides a string representation of the event, typically used for logging and debugging.
     * It delegates the representation to the getDescription() method.
     * * @return A descriptive string of the event.
     */
    @Override
    public String toString() {
        return getDescription();
    }
}