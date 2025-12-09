package org.estg.ed.Hall;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Events.Event;
import org.estg.ed.Events.ExtraMoveEvent;
import org.estg.ed.Events.MoveBackEvent;
import org.estg.ed.Events.SkipTurnEvent;
import org.estg.ed.Events.SwapAllPositionsEvent;
import org.estg.ed.Events.SwapPositionEvent;


/**
 * Class responsible for the creation of Hallway instances.
 *
 * This class acts as a factory, specifically handling the logic of mapping
 * integer event IDs to concrete Event objects.
 * 
 */
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

        switch (eventId) {
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
                return new SwapPositionEvent();

            case 8:
                return new SwapAllPositionsEvent();

            default:
            return null; 
        }
    }

}
