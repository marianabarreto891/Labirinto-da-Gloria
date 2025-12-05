package org.estg.ed.Hall;
import org.estg.ed.Events.Event;
import org.estg.ed.Players.Player;
import org.estg.ed.teste1.Divisao;

/**
 * Represents a path (edge) connecting two Divisions (nodes) in the Maze.
 * A Hallway may contain a random Event that is activated when a player crosses it.
 */
public class Hallway {

  
    /**
     * The Division from which the Hallway starts.
     */
    private final Divisao origin;

    /**
     * The Division to which the Hallway leads.
     */
    private final Divisao destination;

    /**
     * The random event that can occur when crossing the hallway.
     * It can be null if no event is associated.
     * These events can include gaining extra moves, swapping positions, moving back, etc..
     */
    private final Event event;

    /**
     * The weight or cost associated with traversing this Hallway.
     * This can represent distance, difficulty, or any other metric relevant to the game.
     */
    private final double weight;

    /**
     * Constructs a new Hallway connecting an origin Division to a destination Division,
     * and associates an Event with it.
     * * @param origin The starting Division of the path.
     * @param destination The ending Division of the path.
     * @param event The random Event associated with this hallway.
     */
    public Hallway(Divisao origin, Divisao destination, Event event, double weight) {
        this.origin = origin;
        this.destination = destination;
        this.event = event;
        this.weight = weight;
    }

    /**
     * Gets the weight (cost/distance) associated with traversing this Hallway.
     * @return The weight of the Hallway.
     */
    public double getWeight() {
        return weight;
    }
    
    /**
     * Gets the origin Division of this Hallway.
     * * @return The origin Division.
     */
    public Divisao getOrigin() {
        return origin;
    }

    /**
     * Gets the destination Division of this Hallway.
     * * @return The destination Division.
     */
    public Divisao getDestination() {
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
     * This method invokes the Event's logic to apply effects, such as gaining extra moves,
     * swapping positions, moving back, or being blocked from playing.
     * * @param player The Player who is crossing the Hallway.
     */
    protected void activateEvent(Player player) {
        if (this.event != null) {
            // We assume the Event class has an 'applyEffect' method.
            // You will need to define what this action entails in the Event class.
            this.event.applyEffect(player);
            System.out.println(" Random event activated in the hallway! Effect applied to " + player.getName());
        } else {
            System.out.println("Safe path. No random event occurred.");
        }
    }

    /**
     * Checks if this Hallway connects to the specified Division.
     * Useful for traversing the graph's connections.
     * * @param division The Division to be checked (if it's origin or destination).
     * @return true if the Division is either the origin or destination of this Hallway, false otherwise.
     */
    protected boolean connectsDivision(Divisao division) {
        return origin.equals(division) || destination.equals(division);
    }

    /**
     * Returns the Division opposite to the one provided.
     * Useful for navigating the maze from a known Division.
     * * @param division The Player's current Division.
     * @return The Division to which the Hallway leads, or null if the provided Division
     * does not belong to this Hallway.
     */
    protected Divisao getOppositeDivision(Divisao division) {
        if (origin.equals(division)) {
            return destination;
        } else if (destination.equals(division)) {
            return origin;
        } else {
            return null; // The provided Division does not belong to this Hallway
        }
    }

    // --- Other methods (like toString, equals, if necessary) ---

    /**
     * Generates a string representation of the Hallway.
     * This is useful for debugging and generating reports.
     * * @return A string detailing the connection and the associated event.
     */
    @Override
    public String toString() {
        String eventDescription = (event != null) ? event.toString() : "None";
        return String.format("Hallway: %s <-> %s | Event: %s", 
                             origin.getName(), // Assumes Divisao has a getName() method
                             destination.getName(), 
                             eventDescription);
    }


    /**
     * Compares this Hallway to another object for equality.
     * In an undirected maze (graph), two hallways are considered equal if they connect
     * the same two divisions, regardless of which division is designated as 'origin' or 'destination'.
     * * @param obj The object to compare with.
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

        // Two Hallways are considered equal if they connect the same two divisions.
        // Since the maze seems undirected (you can move back and forth), we check both permutations.
        boolean sameForward = this.origin.equals(other.origin) && this.destination.equals(other.destination);
        boolean sameBackward = this.origin.equals(other.destination) && this.destination.equals(other.origin);

        
        return sameForward || sameBackward;
    }
}