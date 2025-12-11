package org.estg.ed.Events;

import java.util.Iterator;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Manager.GameManager;
import org.estg.ed.Players.Player;

import Exceptions.EmptyCollectionException; 
import Lists.ArrayUnorderedList;

/**
 * Evento que troca as posições de TODOS os jogadores de forma aleatória.
 * ...
 */
public class SwapAllPositionsEvent extends Event {

    private final GameManager gameManager;

    /**
     * Construtor que recebe referência ao GameManager.
     * ...
     */
    public SwapAllPositionsEvent(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Aplica o efeito de trocar aleatoriamente as posições de todos os jogadores.
     * ...
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

        // 3. Embaralhar as posições usando algoritmo Fisher-Yates (AQUI ESTÁ A CORREÇÃO)
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
        System.out.println("   Triggered by " + player.getName());
        System.out.println("   Players affected: " + eligiblePlayers.size() + " / " + allPlayers.size());
        
        // Informar sobre jogadores protegidos (se houver)
        int protectedPlayers = allPlayers.size() - eligiblePlayers.size();
        if (protectedPlayers > 0) {
            System.out.println(" Protected players (in central room): " + protectedPlayers);
        }
        System.out.println();
    }


    /**
     * Embaralha uma lista de divisões usando o algoritmo Fisher-Yates.
     * * CORRIGIDO: Garante que a lista 'positions' é modificada in-place com os elementos embaralhados.
     *
     * @param positions lista de divisões a embaralhar
     */
    private void shufflePositions(ArrayUnorderedList<Division> positions) {
        int size = positions.size();
        
        if (size <= 1) return; // Nada para embaralhar

        // 1. Converter para array temporário para facilitar o shuffle
        Division[] tempArray = new Division[size];
        Iterator<Division> iterator = positions.iterator();
        int index = 0;
        
        // Preencher o array temporário
        while (iterator.hasNext()) {
            tempArray[index++] = iterator.next();
        }
        
        // 2. Fisher-Yates shuffle algorithm (embaralha o array)
        for (int i = size - 1; i > 0; i--) {
            int randomIndex = (int) (Math.random() * (i + 1));
            
            // Swap
            Division temp = tempArray[i];
            tempArray[i] = tempArray[randomIndex];
            tempArray[randomIndex] = temp;
        }
        
        // 3. Limpar todos os elementos da lista original de forma segura
        try {
            // Este é o método mais seguro para limpar ArrayUnorderedList/DoublyLinkedUnorderedList
            while (positions.size() > 0) {
                // Remove o primeiro elemento (lança EmptyCollectionException no final)
                positions.removeFirst(); 
            }
        } catch (EmptyCollectionException e) {
            // Ignorar, a lista está vazia
        }
        
        // 4. Re-adicionar os elementos EMBARALHADOS de volta à lista original 'positions'
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