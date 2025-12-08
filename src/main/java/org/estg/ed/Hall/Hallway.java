package org.estg.ed.Hall;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Events.Event;
import org.estg.ed.Players.Player;

/**
 * Represents a path (edge) connecting two Divisions (nodes) in the Maze.
 * A Hallway may contain a random Event that is activated when a player crosses
 * it.
 */
public class Hallway {

    /**
     * The Division from which the Hallway starts.
     */
    private final Division origin;

    /**
     * The Division to which the Hallway leads.
     */
    private final Division destination;

    /**
     * The random event that can occur when crossing the hallway.
     * It can be null if no event is associated.
     * These events can include gaining extra moves, swapping positions, moving
     * back, etc..
     */
    private Event event;
    private int eventId;

    /**
     * The weight or cost associated with traversing this Hallway.
     * This can represent distance, difficulty, or any other metric relevant to the
     * game.
     */
    private final double weight;

    /**
     * Constructs a new Hallway connecting an origin Division to a destination
     * Division,
     * and associates an Event with it.
     * * @param origin The starting Division of the path.
     * 
     * @param destination The ending Division of the path.
     * @param event       The random Event associated with this hallway.
     */
    public Hallway(Division origin, Division destination, Event event, double weight) {
        this.origin = origin;
        this.destination = destination;
        this.event = event;
        this.weight = weight;
    }

    public Hallway(Division origin, Division destination, int eventId, double weight) {
        this.origin = origin;
        this.destination = destination;
        this.eventId = eventId;
        this.weight = weight;
    }

    /**
     * Gets the weight (cost/distance) associated with traversing this Hallway.
     * 
     * @return The weight of the Hallway.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Gets the origin Division of this Hallway.
     * * @return The origin Division.
     */
    public Division getOrigin() {
        return origin;
    }

    /**
     * Gets the destination Division of this Hallway.
     * * @return The destination Division.
     */
    public Division getDestination() {
        return destination;
    }

    /**
     * Gets the random Event associated with this Hallway.
     * * @return The Event object, or null if there is none.
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Activates the random event (if it exists) when a Player crosses the Hallway.
     * This method invokes the Event's logic to apply effects, such as gaining extra
     * moves,
     * swapping positions, moving back, or being blocked from playing.
     * * @param player The Player who is crossing the Hallway.
     */
    public void activateEvent(Player player) {
        if (this.event != null) {
            System.out.println(" Random event activated in the hallway! Effect applied to " + player.getName());
            event.applyEffect(player);
        } else {
            System.out.println("Safe path. No random event occurred.");
        }
    }

    /**
     * Checks if this Hallway connects to the specified Division.
     * Useful for traversing the graph's connections.
     * * @param division The Division to be checked (if it's origin or destination).
     * 
     * @return true if the Division is either the origin or destination of this
     *         Hallway, false otherwise.
     */
    public boolean connectsDivision(Division division) {
        return origin.equals(division) || destination.equals(division);
    }

    /**
     * Returns the Division opposite to the one provided.
     * Useful for navigating the maze from a known Division.
     * * @param division The Player's current Division.
     * 
     * @return The Division to which the Hallway leads, or null if the provided
     *         Division
     *         does not belong to this Hallway.
     */
    public Division getOppositeDivision(Division division) {
        if (origin.equals(division)) {
            return destination;
        } else if (destination.equals(division)) {
            return origin;
        } else {
            return null; // The provided Division does not belong to this Hallway
        }
    }

    /**
     * Compares this Hallway to another object for equality.
     * In an undirected maze (graph), two hallways are considered equal if they
     * connect
     * the same two divisions, regardless of which division is designated as
     * 'origin' or 'destination'.
     * * @param obj The object to compare with.
     * 
     * @return true if the objects are functionally equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Hallway other = (Hallway) obj;

        boolean sameForward = this.origin.equals(other.origin) && this.destination.equals(other.destination);
        boolean sameBackward = this.origin.equals(other.destination) && this.destination.equals(other.origin);

        return sameForward || sameBackward;
    }

    @Override
    public String toString() {
        return "Hallway [origin=" + origin +
                ", destination=" + destination +
                ", event=" + (event == null ? "None" : event.getDescription()) +
                ", weight=" + weight + "]";
    }

}