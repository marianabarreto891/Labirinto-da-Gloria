package org.estg.ed.Events;

import org.estg.ed.Players.Player;

// necessita obrigatoriamente do gamemanager!!
// por implementar

/**
 * Evento que troca as posições de TODOS os jogadores de forma aleatória.
 * Acede à lista completa de jogadores através do GameManager (Singleton).
 */
public class SwapAllPositionsEvent extends Event {

    /**
     * Aplica o efeito de trocar aleatoriamente as posições de todos os jogadores.
     * O acesso à lista de jogadores é feito através do GameManager.
     * 
     * @param player O Player que ativou o evento ao atravessar o corredor.
     */

    @Override
    public void applyEffect(Player player) {
        // Acede à lista de todos os jogadores através do GameManager7
        /*
         * List<Player> allPlayers = GameManager.getInstance().getAllPlayers();
         * 
         * if (allPlayers == null || allPlayers.size() <= 1) {
         * System.out.println("Swap All failed: Insufficient players to swap positions."
         * );
         * return;
         * }
         * 
         * // 1. Coletar todas as posições atuais
         * List<Divisao> currentPositions = new ArrayList<>();
         * for (Player p : allPlayers) {
         * currentPositions.add(p.getCurrentDivision());
         * }
         * 
         * // 2. Embaralhar as posições para aleatoriedade
         * Collections.shuffle(currentPositions);
         * 
         * // 3. Atribuir as novas posições a cada jogador
         * for (int i = 0; i < allPlayers.size(); i++) {
         * Player p = allPlayers.get(i);
         * Divisao oldPos = p.getCurrentDivision();
         * Divisao newPos = currentPositions.get(i);
         * 
         * // Atribui a nova posição
         * p.setCurrentDivision(newPos);
         * 
         * // 4. Registar o evento para relatórios
         * p.registerEventPositionChange(this, oldPos);
         * }
         * 
         * System.out.
         * println("GLOBAL POSITION SWAP! All players' positions have been randomized. Triggered by "
         * + player.getName());
         * 
         * 
         */
    }

    /**
     * Obtém a descrição do evento para o log e relatórios.
     * 
     * @return Uma string que descreve a ação do evento.
     */
    @Override
    public String getDescription() {
        return "Troca de Posições Global (Todos os Jogadores)";
    }

}