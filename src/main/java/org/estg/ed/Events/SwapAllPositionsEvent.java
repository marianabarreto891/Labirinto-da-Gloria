package org.estg.ed.Events;

import java.util.Iterator;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Manager.GameManager;
import org.estg.ed.Players.Player;

import Exceptions.EmptyCollectionException; 
import Lists.ArrayUnorderedList;

//@author Joana Rodrigues 8240334
/**
 * Event that randomly swaps the positions of all eligible players.
 * 
 * The event affects all players except those in the central division. 
 * If only one eligible player exists, no position change occurs.
 * Positions are shuffled using the Fisher-Yates algorithm.
 * 
 */
public class SwapAllPositionsEvent extends Event {

    private final GameManager gameManager;

    /**
     * Constructor that receives a reference to the GameManager.
     *
     * @param gameManager reference to the game's manager
     */
    public SwapAllPositionsEvent(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Applies the effect of randomly swapping the positions of all eligible players.
     *
     * @param player the player triggering the event
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

        Division centralRoom = gameManager.getMazeNetwork().getCentralRoom();

        ArrayUnorderedList<Player> eligiblePlayers = new ArrayUnorderedList<>();
        for (Player p : allPlayers) {
            if (!p.getCurrentDivision().equals(centralRoom)) {
                eligiblePlayers.addToRear(p);
            }
        }

        if (eligiblePlayers.isEmpty()) {
            System.out.println("Swap All skipped: All players are in the central room.");
            return;
        }

        if (eligiblePlayers.size() == 1) {
            System.out.println("Swap All triggered, but only one eligible player. No position change.");
            return;
        }

        ArrayUnorderedList<Division> currentPositions = new ArrayUnorderedList<>();
        for (Player p : eligiblePlayers) {
            currentPositions.addToRear(p.getCurrentDivision());
        }

        shufflePositions(currentPositions);

        Iterator<Player> swapIterator = eligiblePlayers.iterator();
        Iterator<Division> positionIterator = currentPositions.iterator(); 

        while (swapIterator.hasNext() && positionIterator.hasNext()) {
            Player p = swapIterator.next();
            Division oldPos = p.getCurrentDivision();
            Division newPos = positionIterator.next();

            p.setCurrentDivision(newPos);

            p.registerEventPositionChange(this, oldPos);

            if (!oldPos.equals(newPos)) {
                System.out.println(p.getName() + ": " + oldPos.getId() + " → " + newPos.getId());
            }
        }

        System.out.println("\n GLOBAL POSITION SWAP! All eligible players' positions have been randomized!");
        System.out.println("   Triggered by " + player.getName());
        System.out.println("   Players affected: " + eligiblePlayers.size() + " / " + allPlayers.size());

        int protectedPlayers = allPlayers.size() - eligiblePlayers.size();
        if (protectedPlayers > 0) {
            System.out.println(" Protected players (in central room): " + protectedPlayers);
        }
        System.out.println();
    }

    /**
     * Shuffles a list of divisions using the Fisher-Yates algorithm.
     *
     * @param positions list of divisions to shuffle
     */
    private void shufflePositions(ArrayUnorderedList<Division> positions) {
        int size = positions.size();
        
        if (size <= 1) return;

        Division[] tempArray = new Division[size];
        Iterator<Division> iterator = positions.iterator();
        int index = 0;
        
        while (iterator.hasNext()) {
            tempArray[index++] = iterator.next();
        }
        
        for (int i = size - 1; i > 0; i--) {
            int randomIndex = (int) (Math.random() * (i + 1));
            Division temp = tempArray[i];
            tempArray[i] = tempArray[randomIndex];
            tempArray[randomIndex] = temp;
        }
        
        try {
            while (positions.size() > 0) {
                positions.removeFirst(); 
            }
        } catch (EmptyCollectionException e) {
        }
        
        for (int i = 0; i < size; i++) {
            positions.addToRear(tempArray[i]);
        }
    }

    /**
     * Returns a description of the event for logs and reports.
     *
     * @return a string describing the event action
     */
    @Override
    public String getDescription() {
        return "Global Position Swap Event (All Players)";
    }
}