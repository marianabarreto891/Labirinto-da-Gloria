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
 * Class responsible for the creation of Hallway instances.
 *
 * This class acts as a factory, specifically handling the logic of mapping
 * integer event IDs to concrete Event objects.
 * 
 * ATUALIZADO: Agora recebe GameManager para eventos que precisam de acesso aos jogadores.
 */
public class HallwayManager {

    private static GameManager gameManager;

    /**
     * Define o GameManager para ser usado na criação de eventos.
     * Deve ser chamado durante a inicialização do jogo.
     * 
     * @param manager a instância do GameManager
     */
    public static void setGameManager(GameManager manager) {
        gameManager = manager;
    }

    /**
     * Creates a Hallway with an associated Event based on the eventId.
     * 
     * @param origin divisão de origem
     * @param destination divisão de destino
     * @param eventId identificador do evento (1-8)
     * @param weight peso do corredor (distância)
     * @return Hallway criado com o evento associado
     */
    public static Hallway createHallway(Division origin, Division destination, int eventId, double weight) {
        Event event = createEvent(eventId);
        return new Hallway(origin, destination, event, weight);
    }


    /**
     * Converts an eventId into a concrete Event instance.
     * 
     * MAPEAMENTO DE EVENTOS:
     * 1 → ExtraMoveEvent(1) - Ganhar 1 jogada extra
     * 2 → ExtraMoveEvent(2) - Ganhar 2 jogadas extra
     * 3 → MoveBackEvent(1) - Recuar 1 divisão
     * 4 → MoveBackEvent(2) - Recuar 2 divisões
     * 5 → SkipTurnEvent(1) - Perder 1 turno
     * 6 → SkipTurnEvent(2) - Perder 2 turnos
     * 7 → SwapPositionEvent - Trocar posição com outro jogador
     * 8 → SwapAllPositionsEvent - Trocar posições de TODOS os jogadores
     * 
     * @param eventId o identificador numérico do evento
     * @return Event correspondente ou null se inválido
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
                if (gameManager != null) {
                    return new SwapPositionEvent(gameManager);
                } else {
                    System.err.println("ERROR: GameManager not set! SwapPositionEvent requires GameManager.");
                    return null;
                }

            case 8:
                if (gameManager != null) {
                    return new SwapAllPositionsEvent(gameManager);
                } else {
                    System.err.println("ERROR: GameManager not set! SwapAllPositionsEvent requires GameManager.");
                    return null;
                }

            default:
                System.err.println("WARNING: Invalid eventId " + eventId + ". No event created.");
                return null; 
        }
    }

}