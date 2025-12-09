package org.estg.ed.Events;

import java.util.Iterator;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Manager.GameManager;
import org.estg.ed.Players.Player;

import Lists.ArrayUnorderedList;

/**
 * Evento que troca as posições de TODOS os jogadores de forma aleatória.
 * Este evento embaralha as posições de todos os jogadores no labirinto,
 * criando caos estratégico e imprevisibilidade no jogo.
 * 
 * REGRA IMPORTANTE: Jogadores que já estão na sala central (tesouro) 
 * NÃO são afetados pela troca para preservar a condição de vitória.
 */
public class SwapAllPositionsEvent extends Event {

    private final GameManager gameManager;

    /**
     * Construtor que recebe referência ao GameManager.
     * O GameManager é necessário para aceder à lista completa de jogadores.
     *
     * @param gameManager instância do gestor de jogo
     */
    public SwapAllPositionsEvent(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Aplica o efeito de trocar aleatoriamente as posições de todos os jogadores.
     * 
     * Algoritmo:
     * 1. Filtra jogadores que NÃO estão na sala central
     * 2. Coleta as posições desses jogadores elegíveis
     * 3. Embaralha as posições aleatoriamente (algoritmo Fisher-Yates)
     * 4. Redistribui as posições embaralhadas
     * 5. Regista o evento no histórico de cada jogador afetado
     *
     * @param player O Player que ativou o evento ao atravessar o corredor.
     */
    @Override
    public void applyEffect(Player player) {
        if (gameManager == null) {
            System.err.println("Error: GameManager not set in SwapAllPositionsEvent");
            return;
        }

        ArrayUnorderedList<Player> allPlayers = gameManager.getAllPlayers();

        if (allPlayers == null || allPlayers.isEmpty()) {
            System.out.println(" Swap All failed: No players found.");
            return;
        }

        // Obter a sala central para verificação
        Division centralRoom = gameManager.getMazeNetwork().getCentralRoom();

        // 1. Filtrar jogadores elegíveis (NÃO estão na sala central)
        ArrayUnorderedList<Player> eligiblePlayers = new ArrayUnorderedList<>();
        for (Player p : allPlayers) {
            // Só adiciona se NÃO estiver na sala central
            if (!p.getCurrentDivision().equals(centralRoom)) {
                eligiblePlayers.addToRear(p);
            }
        }

        // Se não há jogadores elegíveis para trocar
        if (eligiblePlayers.isEmpty()) {
            System.out.println("Swap All skipped: All players are in the central room.");
            return;
        }

        // Se há apenas 1 jogador elegível, troca consigo mesmo (sem efeito real)
        if (eligiblePlayers.size() == 1) {
            System.out.println("Swap All triggered, but only one eligible player. No position change.");
            return;
        }

        // 2. Coletar todas as posições atuais dos jogadores elegíveis
        ArrayUnorderedList<Division> currentPositions = new ArrayUnorderedList<>();
        for (Player p : eligiblePlayers) {
            currentPositions.addToRear(p.getCurrentDivision());
        }

        // 3. Embaralhar as posições usando algoritmo Fisher-Yates
        shufflePositions(currentPositions);

        // 4. Atribuir as novas posições a cada jogador elegível
        Iterator<Player> swapIterator = eligiblePlayers.iterator();
        Iterator<Division> positionIterator = currentPositions.iterator();

        while (swapIterator.hasNext() && positionIterator.hasNext()) {
            Player p = swapIterator.next();
            Division oldPos = p.getCurrentDivision();
            Division newPos = positionIterator.next();

            // Atribui a nova posição (usa setCurrentDivision para preservar previousRecoilPosition)
            p.setCurrentDivision(newPos);

            // 5. Registar o evento no histórico do jogador para relatórios
            p.registerEventPositionChange(this, oldPos);

            // Log individual da troca
            if (!oldPos.equals(newPos)) {
                System.out.println(p.getName() + ": " + oldPos.getId() + " → " + newPos.getId());
            }
        }

        // Mensagem principal do evento
        System.out.println("\n GLOBAL POSITION SWAP! All eligible players' positions have been randomized!");
        System.out.println("   Triggered by " + player.getName());
        System.out.println("   Players affected: " + eligiblePlayers.size() + " / " + allPlayers.size());
        
        // Informar sobre jogadores protegidos (se houver)
        int protectedPlayers = allPlayers.size() - eligiblePlayers.size();
        if (protectedPlayers > 0) {
            System.out.println(" Protected players (in central room): " + protectedPlayers);
        }
        System.out.println();
    }

    /**
     * Embaralha uma lista de divisões usando o algoritmo Fisher-Yates.
     * Este método modifica a lista original in-place.
     * 
     * Complexidade: O(n) onde n é o tamanho da lista
     *
     * @param positions lista de divisões a embaralhar
     */
    private void shufflePositions(ArrayUnorderedList<Division> positions) {
        int size = positions.size();
        
        // Converter para array temporário para facilitar o shuffle
        Division[] tempArray = new Division[size];
        Iterator<Division> iterator = positions.iterator();
        int index = 0;
        
        while (iterator.hasNext()) {
            tempArray[index++] = iterator.next();
        }
        
        // Fisher-Yates shuffle algorithm
        for (int i = size - 1; i > 0; i--) {
            int randomIndex = (int) (Math.random() * (i + 1));
            
            // Swap
            Division temp = tempArray[i];
            tempArray[i] = tempArray[randomIndex];
            tempArray[randomIndex] = temp;
        }
        
        // Reconstruir a lista embaralhada
        positions = new ArrayUnorderedList<>();
        for (int i = 0; i < size; i++) {
            positions.addToRear(tempArray[i]);
        }
    }

    /**
     * Obtém a descrição do evento para o log e relatórios JSON.
     * Esta descrição é usada no relatório final da partida.
     *
     * @return Uma string que descreve a ação do evento.
     */
    @Override
    public String getDescription() {
        return "Global Position Swap Event (All Players)";
    }
}