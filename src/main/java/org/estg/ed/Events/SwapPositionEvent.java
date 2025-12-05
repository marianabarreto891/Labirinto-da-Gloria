package org.estg.ed.Events;

import org.estg.ed.Players.Player;
import org.estg.ed.teste1.Divisao;


/**
 * A specific type of Event that swaps the position of the current player
 * with another chosen player on the board.
 * This event is a core part of the game's unpredictability and interaction,
 * and is one of the random effects that can occur in Hallways.
 */
public class SwapPositionEvent extends Event {

    /**
     * Applies the effect of swapping positions between the player who triggered the event
     * and a target player determined by the game mode (Manual or Automatic).
     * The method updates the current position of both players but must ensure the
     * safe recoil position is not affected by the change.
     * * @param player The Player who activated the event by traversing the Hallway.
     */
    @Override
    public void applyEffect(Player player) {
        
        // Simulação de como você obterá o jogador alvo
        Player targetPlayer = findTargetPlayer(player); // Implementation relies on Game Manager context

        if (targetPlayer != null) {
            
            // 1. Store the current positions (the "old" positions)
            Divisao playerCurrentPos = player.getCurrentDivision(); 
            Divisao targetCurrentPos = targetPlayer.getCurrentDivision();

            // 2. Perform the swap using the safe method (setCurrentDivision), 
            // which preserves the previousRecoilPosition for the moveBack logic.
            player.setCurrentDivision(targetCurrentPos); 
            targetPlayer.setCurrentDivision(playerCurrentPos);
            
            // 3. Register the event in both players' histories
            player.registerEventPositionChange(this, playerCurrentPos); 
            targetPlayer.registerEventPositionChange(this, targetCurrentPos); 
            
            System.out.println(" POSITION SWAP! " + player.getName() + " swapped position with " + targetPlayer.getName() + "!");
        } else {
            System.out.println("Swap failed: No other player available to swap positions.");
        }
    }


    /**
     * Retrieves a target player to swap positions with. This logic will be implemented
     * based on the player type:
     * 1. **Manual mode:** Requires prompting the user for input.
     * 2. **Automatic mode:** Uses a predetermined strategy (e.g., random selection).
     * * @param currentPlayer The player who activated the event.
     * @return The target player for the swap, or {@code null} if none are suitable or available.
     */
    private Player findTargetPlayer(Player currentPlayer) {
        // NOTE: This is a placeholder. 
        // Real implementation requires access to the full list of players (Game Context).
        
        return null; // For now
    }

    
    /**
     * Gets a description of the event, useful for the game log and reports.
     * * @return A string describing the event's action.
     */
    @Override
    public String getDescription() {
        return "Swap Position Event";
    }
}