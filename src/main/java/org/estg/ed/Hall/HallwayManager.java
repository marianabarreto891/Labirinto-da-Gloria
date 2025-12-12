package org.estg.ed.Hall;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Events.Event;
import org.estg.ed.Events.ExtraMoveEvent;
import org.estg.ed.Events.MoveBackEvent;
import org.estg.ed.Events.SkipTurnEvent;
import org.estg.ed.Events.SwapAllPositionsEvent;
import org.estg.ed.Events.SwapPositionEvent;
import org.estg.ed.Manager.GameManager;

/**
 * Manages the creation and initialization of Hallway instances.
 * <p>
 * Supports lazy initialization of events for editor mode and
 * immediate event creation when GameManager is available.
 * </p>
 */
public class HallwayManager {

    private static GameManager gameManager;

    /**
     * Sets the GameManager to be used when creating events.
     * Should be called during game initialization.
     *
     * @param manager the GameManager instance
     */
    public static void setGameManager(GameManager manager) {
        gameManager = manager;
    }

    /**
     * Creates a Hallway with an associated event based on the eventId.
     * <p>
     * Editor mode: if GameManager is null, creates a hallway without initializing the event.
     * Game mode: if GameManager is defined, initializes the event immediately.
     * </p>
     *
     * @param origin the starting division
     * @param destination the ending division
     * @param eventId the ID of the event to associate
     * @param weight the weight of the hallway
     * @return a new Hallway instance
     */
    public static Hallway createHallway(Division origin, Division destination, int eventId, double weight) {
        if (gameManager == null) {
            return new Hallway(origin, destination, eventId, weight);
        }

        Event event = createEvent(eventId);
        return new Hallway(origin, destination, event, weight);
    }

    /**
     * Initializes the event of an existing Hallway.
     * Used when the GameManager becomes available after map loading.
     *
     * @param hallway the Hallway to initialize
     */
    public static void initializeHallwayEvent(Hallway hallway) {
        if (hallway == null || hallway.isEventInitialized() || gameManager == null) {
            return;
        }

        Event event = createEvent(hallway.getEventId());
        hallway.setEvent(event);
    }

    /**
     * Returns a mapping of event IDs to their descriptions.
     *
     * @return a formatted string listing all hallway events
     */
 public static String getEventMapping() {
        return "\n--- Event Mapping (Corridors) ---\n" +
                " [0] No Event\n" +
                " [1] ExtraMoveEvent (Gain 1 extra move)\n" +
                " [2] ExtraMoveEvent (Gain 2 extra moves)\n" +
                " [3] MoveBackEvent (Move back 1 division)\n" +
                " [4] MoveBackEvent (Move back 2 divisions)\n" +
                " [5] SkipTurnEvent (Lose 1 turn)\n" +
                " [6] SkipTurnEvent (Lose 2 turns)\n" +
                " [7] SwapPositionEvent (Swap position with another player)\n" +
                " [8] SwapAllPositionsEvent (Swap ALL players' positions)\n" +
                "----------------------------------------------\n";
    }

    /**
     * Converts an eventId into a concrete Event instance.
     * If GameManager is required and not set, returns null.
     *
     * @param eventId the ID of the event
     * @return the corresponding Event or null if not applicable
     */
    private static Event createEvent(int eventId) {
        switch (eventId) {
            case 0:
                return null;
            case 1:
                return new ExtraMoveEvent(1);
            case 2:
                return new ExtraMoveEvent(2);
            case 3:
                return new MoveBackEvent(1);
            case 4:
                return new MoveBackEvent(2);
            case 5:
                return new SkipTurnEvent(1);
            case 6:
                return new SkipTurnEvent(2);
            case 7:
                if (gameManager == null) return null;
                return new SwapPositionEvent(gameManager);
            case 8:
                if (gameManager == null) return null;
                return new SwapAllPositionsEvent(gameManager);
            default:
                return null;
        }
    }
}
