package org.estg.ed.Events;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Players.Player;
import org.estg.ed.Manager.GameManager;

import java.util.Iterator;
import Lists.ArrayUnorderedList;


/**
 * A specific type of Event that swaps the position of the current player
 * with another chosen player on the board.
 * This event is a core part of the game's unpredictability and interaction,
 * and is one of the random effects that can occur in Hallways.
 */
public class SwapPositionEvent extends Event {

    private final GameManager gameManager;

    /**
     * Constructs a SwapPositionEvent with a reference to the GameManager.
     * The GameManager is needed to access the full list of players.
     *
     * @param gameManager the game manager instance
     */
    public SwapPositionEvent(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Applies the effect of swapping positions between the player who triggered the event
     * and a target player determined by the game mode (Manual or Automatic).
     * The method updates the current position of both players but must ensure the
     * safe recoil position is not affected by the change.
     *
     * @param player The Player who activated the event by traversing the Hallway.
     */
    @Override
    public void applyEffect(Player player) {
        // Find a target player to swap with
        Player targetPlayer = findTargetPlayer(player);

        if (targetPlayer != null) {
            
            // 1. Store the current positions (the "old" positions)
            Division playerCurrentPos = player.getCurrentDivision(); 
            Division targetCurrentPos = targetPlayer.getCurrentDivision();

            // 2. Perform the swap using the safe method (setCurrentDivision), 
            // which preserves the previousRecoilPosition for the moveBack logic.
            player.setCurrentDivision(targetCurrentPos); 
            targetPlayer.setCurrentDivision(playerCurrentPos);
            
            // 3. Register the event in both players' histories
            player.registerEventPositionChange(this, playerCurrentPos); 
            targetPlayer.registerEventPositionChange(this, targetCurrentPos); 
            
            System.out.println(" POSITION SWAP! " + player.getName() + " swapped position with " 
                    + targetPlayer.getName() + "!");
            System.out.println("   " + player.getName() + " is now at division " + playerCurrentPos.getId());
            System.out.println("   " + targetPlayer.getName() + " is now at division " + targetCurrentPos.getId());
        } else {
            System.out.println("Swap failed: No other player available to swap positions.");
        }
    }


    /**
     * Retrieves a target player to swap positions with. 
     * Selects a random player from the list of all players (excluding the current player).
     * 
     * Strategy: Random selection for unpredictability
     *
     * @param currentPlayer The player who activated the event.
     * @return The target player for the swap, or {@code null} if none are suitable or available.
     */
    private Player findTargetPlayer(Player currentPlayer) {
        if (gameManager == null) {
            System.err.println("Error: GameManager not set in SwapPositionEvent");
            return null;
        }

        ArrayUnorderedList<Player> allPlayers = gameManager.getAllPlayers();
        
        if (allPlayers == null || allPlayers.size() <= 1) {
            System.out.println("Swap failed: Insufficient players to swap positions.");
            return null;
        }

        // Create a list of valid targets (all players except current)
        ArrayUnorderedList<Player> validTargets = new ArrayUnorderedList<>();
        for (Player p : allPlayers) {
            if (!p.equals(currentPlayer)) {
                validTargets.addToRear(p);
            }
        }

        if (validTargets.isEmpty()) {
            return null;
        }

        // Select a random target
        int randomIndex = (int) (Math.random() * validTargets.size());
        
        // Iterate to the random index
        Player targetPlayer = null;
        Iterator<Player> targetIterator = validTargets.iterator();
        int currentIndex = 0;
        
        while (targetIterator.hasNext()) {
            targetPlayer = targetIterator.next();
            if (currentIndex == randomIndex) {
                return targetPlayer;
            }
            currentIndex++;
        }

        return targetPlayer; // Return the last one if we didn't break
    }

    
    /**
     * Gets a description of the event, useful for the game log and reports.
     *
     * @return A string describing the event's action.
     */
    @Override
    public String getDescription() {
        return "Swap Position Event";
    }
}