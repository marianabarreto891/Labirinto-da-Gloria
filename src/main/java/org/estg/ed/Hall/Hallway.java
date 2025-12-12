package org.estg.ed.Hall;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Events.Event;
import org.estg.ed.Players.Player;

/**
 * Represents a path (edge) connecting two Divisions in the Maze.
 * <p>
 * A Hallway may contain a random Event that is activated when a player crosses it.
 * Supports lazy initialization for events requiring the GameManager.
 * </p>
 */
public class Hallway {

    private final Division origin;
    private final Division destination;
    private Event event;
    private final int eventId;
    private final double weight;
    private boolean eventInitialized;

    /**
     * Constructs a Hallway connecting an origin Division to a destination Division,
     * with an associated Event.
     *
     * @param origin the starting division
     * @param destination the ending division
     * @param event the event associated with this hallway
     * @param weight the traversal weight
     */
    public Hallway(Division origin, Division destination, Event event, double weight) {
        this.origin = origin;
        this.destination = destination;
        this.event = event;
        this.eventId = (event != null) ? -1 : 0;
        this.weight = weight;
        this.eventInitialized = true;
    }

    /**
     * Constructs a Hallway connecting two divisions with lazy event initialization.
     *
     * @param origin the starting division
     * @param destination the ending division
     * @param eventId the ID of the event to initialize later
     * @param weight the traversal weight
     */
    public Hallway(Division origin, Division destination, int eventId, double weight) {
        this.origin = origin;
        this.destination = destination;
        this.event = null;
        this.eventId = eventId;
        this.weight = weight;
        this.eventInitialized = false;
    }

    /**
     * Gets the traversal weight of this hallway.
     *
     * @return the weight of the hallway
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Gets the origin division of this hallway.
     *
     * @return the origin division
     */
    public Division getOrigin() {
        return origin;
    }

    /**
     * Gets the destination division of this hallway.
     *
     * @return the destination division
     */
    public Division getDestination() {
        return destination;
    }

    /**
     * Gets the event associated with this hallway.
     * Returns null if the event is not yet initialized.
     *
     * @return the event or null
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Gets the ID of the event associated with this hallway.
     *
     * @return the event ID
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Checks if the event has been initialized.
     *
     * @return true if initialized, false otherwise
     */
    public boolean isEventInitialized() {
        return eventInitialized;
    }

    /**
     * Sets the event for this hallway (used in lazy initialization).
     *
     * @param event the event to set
     */
    public void setEvent(Event event) {
        this.event = event;
        this.eventInitialized = true;
    }

    /**
     * Activates the event when a player crosses the hallway.
     *
     * @param player the player crossing the hallway
     */
    public void activateEvent(Player player) {
        if (!eventInitialized) return;

        if (this.event != null) {
            System.out.println("⚡ Random event activated in the hallway! Effect applied to " + player.getName());
            event.applyEffect(player);
        } else {
            System.out.println("✓ Safe path. No random event occurred.");
        }
    }

    /**
     * Checks if this hallway connects to a given division.
     *
     * @param division the division to check
     * @return true if connected, false otherwise
     */
    public boolean connectsDivision(Division division) {
        return origin.equals(division) || destination.equals(division);
    }

    /**
     * Returns the division opposite to the one provided.
     *
     * @param division the reference division
     * @return the opposite division or null if the reference is not part of this hallway
     */
    public Division getOppositeDivision(Division division) {
        if (origin.equals(division)) return destination;
        if (destination.equals(division)) return origin;
        return null;
    }

    /**
     * Compares this hallway with another object for equality.
     *
     * @param obj the object to compare
     * @return true if the hallways connect the same divisions, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Hallway other = (Hallway) obj;

        boolean sameForward = this.origin.equals(other.origin) && this.destination.equals(other.destination);
        boolean sameBackward = this.origin.equals(other.destination) && this.destination.equals(other.origin);

        return sameForward || sameBackward;
    }

    /**
     * Returns a string representation of the hallway.
     *
     * @return a formatted string with divisions, event, and weight
     */
    @Override
    public String toString() {
        String eventDesc = !eventInitialized ? "Event ID: " + eventId + " (not initialized)" :
                (event == null ? "None" : event.getDescription());

        return "Hallway [origin=" + origin.getId() +
                ", destination=" + destination.getId() +
                ", event=" + eventDesc +
                ", weight=" + weight + "]";
    }
}
