package org.estg.ed.Events;

import org.estg.ed.Players.Player;

//@author Joana Rodrigues 8240334
/**
 * Interface that defines the contract for all random events that can occur in a Hallway.
 * Any class implementing this interface guarantees the definition of an effect
 * and a description for game logging and reports.
 */
public interface IEvent {
    
    /**
     * Applies the specific effect of this event to the player(s) and potentially the game state.
     * This method contains the core logic for the event, such as giving extra moves, 
     * swapping positions, or blocking turns.
     * * @param player The Jogador (Player) affected by the event (usually the one who triggered it).
     */
    public void applyEffect(Player player);

    /**
     * Gets a descriptive string of the event.
     * This is crucial for generating end-game reports in JSON format.
     * * @return A string describing the event's action and impact.
     */
    public String getDescription();
}