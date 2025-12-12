package org.estg.ed.Events;

import java.util.Iterator;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Manager.GameManager;
import org.estg.ed.Players.Player;

import Lists.ArrayUnorderedList;


/**
 * A specific type of Event that swaps the position of the current player
 * with another chosen player on the board.
 * 
 * This event affects one other player and is used to introduce unpredictability
 * and interaction within the game. It is triggered when a player traverses a Hallway.
 * 
 * @author Joana Rodrigues 8240334
 */
public class SwapPositionEvent extends Event {

    private final GameManager gameManager;

    /**
     * Constructs a SwapPositionEvent with a reference to the GameManager.
     *
     * @param gameManager the game manager instance
     */
    public SwapPositionEvent(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Applies the effect of swapping positions between the player who triggered the event
     * and a target player determined randomly among other players.
     *
     * @param player The Player who activated the event
     */
    @Override
    public void applyEffect(Player player) {
        Player targetPlayer = findTargetPlayer(player);

        if (targetPlayer != null) {
            Division playerCurrentPos = player.getCurrentDivision(); 
            Division targetCurrentPos = targetPlayer.getCurrentDivision();

            player.setCurrentDivision(targetCurrentPos); 
            targetPlayer.setCurrentDivision(playerCurrentPos);
            
            player.registerEventPositionChange(this, playerCurrentPos); 
            targetPlayer.registerEventPositionChange(this, targetCurrentPos); 
            
            System.out.println(" POSITION SWAP! " + player.getName() + " swapped position with " 
                    + targetPlayer.getName() + "!");
            System.out.println("   " + player.getName() + " is now at division " + targetCurrentPos.getId()); 
            System.out.println("   " + targetPlayer.getName() + " is now at division " + playerCurrentPos.getId());
        } else {
            System.out.println("Swap failed: No other player available to swap positions.");
        }
    }

    /**
     * Retrieves a target player to swap positions with.
     * Selects a random player from all players excluding the current player.
     *
     * @param currentPlayer The player who activated the event
     * @return The target player for the swap, or {@code null} if none are suitable
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

        ArrayUnorderedList<Player> validTargets = new ArrayUnorderedList<>();
        for (Player p : allPlayers) {
            if (!p.equals(currentPlayer)) {
                validTargets.addToRear(p);
            }
        }

        if (validTargets.isEmpty()) {
            return null;
        }

        int randomIndex = (int) (Math.random() * validTargets.size());
        
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

        return targetPlayer; 
    }

    /**
     * Gets a description of the event for logs and reports.
     *
     * @return A string describing the event
     */
    @Override
    public String getDescription() {
        return "Swap Position Event";
    }
}