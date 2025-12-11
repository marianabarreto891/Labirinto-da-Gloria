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
 * ATUALIZADO: Agora recebe GameManager para eventos que precisam de acesso aos
 * jogadores.
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
     * @param origin      divisão de origem
     * @param destination divisão de destino
     * @param eventId     identificador do evento (1-8)
     * @param weight      peso do corredor (distância)
     * @return Hallway criado com o evento associado
     */
    public static Hallway createHallway(Division origin, Division destination, int eventId, double weight) {
        Event event = createEvent(eventId);
        return new Hallway(origin, destination, event, weight);
    }

    public static String getEventMapping() {
        // As descrições são baseadas no switch case do método createEvent
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
 * * * @param eventId o identificador numérico do evento
 * @return Event correspondente ou null se inválido
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
            // Passa o gameManager (que é null no modo de edição)
            return new SwapPositionEvent(gameManager); 

        case 8:
            return new SwapAllPositionsEvent(gameManager); 

        default:
            System.err.println("WARNING: Invalid eventId " + eventId + ". No event created.");
            return null;
    }
}
}