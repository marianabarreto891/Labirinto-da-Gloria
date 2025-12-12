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
        return "\n--- Mapeamento de Eventos (Corredores) ---\n" +
                " [0] Nenhum Evento\n" +
                " [1] ExtraMoveEvent (Ganhar 1 jogada extra)\n" +
                " [2] ExtraMoveEvent (Ganhar 2 jogadas extra)\n" +
                " [3] MoveBackEvent (Recuar 1 divisão)\n" +
                " [4] MoveBackEvent (Recuar 2 divisões)\n" +
                " [5] SkipTurnEvent (Perder 1 turno)\n" +
                " [6] SkipTurnEvent (Perder 2 turnos)\n" +
                " [7] SwapPositionEvent (Trocar posição com outro jogador)\n" +
                " [8] SwapAllPositionsEvent (Trocar posições de TODOS os jogadores)\n" +
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
