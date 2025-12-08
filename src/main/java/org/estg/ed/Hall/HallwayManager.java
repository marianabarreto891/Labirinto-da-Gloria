package org.estg.ed.Hall;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Events.*;

public class HallwayManager {

    /**
     * Creates a Hallway with an associated Event based on the eventId.
     */
    public static Hallway createHallway(Division origin, Division destination, int eventId, double weight) {
        Event event = createEvent(eventId);
        return new Hallway(origin, destination, event, weight);
    }


    /**
     * Converts an eventId into a concrete Event instance.
     */
    private static Event createEvent(int eventId) {

        return switch (eventId) {

            case 1 -> new ExtraMoveEvent(1);
            case 2 -> new ExtraMoveEvent(2);

            case 3 -> new MoveBackEvent(1);
            case 4 -> new MoveBackEvent(2);

            case 5 -> new SkipTurnEvent(1);
            case 6 -> new SkipTurnEvent(2);

            case 7 -> new SwapPositionEvent();
            case 8 -> new SwapAllPositionsEvent();

            default -> null;  // eventId = 0 ⇒ sem evento
        };
    }
}
