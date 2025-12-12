package org.estg.ed.Players;

import java.util.Iterator;
import java.util.Scanner;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Events.Event;
import org.estg.ed.Events.SwapPositionEvent;
import org.estg.ed.Hall.Hallway;

import Exceptions.EmptyCollectionException;
import Interfaces.UnorderedListADT;
import Lists.DoublyLinkedUnorderedList;

/**
 * Represents a Player participating in the Labyrinth of Glory game simulator.
 * This abstract class serves as the base for concrete player types (Human and
 * Bot)
 * and encapsulates the common state, position, and core logic for movement,
 * effects, and reporting.
 * It is designed to track player progression and manage temporary effects from
 * random events.
 */
public abstract class Player implements Comparable<Player> {

    private final String name;
    private Division currentPosition;
    private final UnorderedListADT<Object> pathHistory;
    private int turnsBlocked;
    public Division previousRecoilPosition;
    private int extraMoves;
    private int enigmasSolvedCount;
    public int totalTurnsBlockedAccumulated;

    public Scanner sc;

    /**
     * Constructs a new Player instance, initializing their position and status.
     * * @param name The unique identifier of the player.
     * 
     * @param initialPosition The entry Division where the player begins the game.
     */
    public Player(String name, Division initialPosition, Scanner sc) {
        this.name = name;
        this.currentPosition = initialPosition;
        this.pathHistory = new DoublyLinkedUnorderedList<>();
        this.pathHistory.addToRear(initialPosition);
        this.turnsBlocked = 0;
        this.totalTurnsBlockedAccumulated = 0;
        this.previousRecoilPosition = initialPosition;
        this.extraMoves = 0;

         this.sc = sc;
    }

    public abstract String answerEnigma(String question, String[] options);

    public abstract String answerLever();

    /**
     * Gets the immutable name of the player.
     * 
     * @return The player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current Division the player is occupying.
     * 
     * @return The current Division object.
     */
    public Division getCurrentDivision() {
        return currentPosition;
    }

    /**
     * Updates the player's current position. Used primarily by the Game controller
     * or the SwapPositionEvent logic (where recoil position should not be updated).
     * 
     * @param newPosition The Division the player is moving to.
     */
    public void setCurrentDivision(Division newPosition) {
        this.currentPosition = newPosition;
    }

    /**
     * Checks if the player is allowed to move this turn, managing the block
     * duration. If blocked, the turn is skipped, and the block duration is
     * decremented.
     * 
     * @return True if the player is free to play this turn, false if blocked.
     */
    public boolean canPlay() {
        if (this.turnsBlocked > 0) {
            this.turnsBlocked--;
            System.out.println(this.name + " is blocked. Skipping turn. " + this.turnsBlocked + " turn(s) remaining.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the player has extra moves to use this turn.
     * 
     * @return The number of extra moves available.
     */
    public int getExtraMoves() {
        return extraMoves;
    }

    /**
     * Increments the count of enigmas successfully solved by the player.
     */
    public void incrementEnigmasSolved() {
        this.enigmasSolvedCount++;
    }

    /**
     * Consumes one extra move after a successful action.
     */
    public void useExtraMove() {
        if (this.extraMoves > 0) {
            this.extraMoves--;
        }
    }

    /**
     * Attempts to move the player to a new destination, updating the path history
     * and recording the previous position as the safe recoil position.
     * 
     * @param destination The target Division for the movement.
     * @param hallway The Hallway traversed to reach the destination.
     */
    public void moveTo(Division destination, Hallway hallway) {
       
        this.previousRecoilPosition = this.currentPosition;

        this.currentPosition = destination;
        this.pathHistory.addToRear(hallway); 
        this.pathHistory.addToRear(destination); 
    }

    /**
     * Required by SkipTurnEvent. Sets the number of turns the player must skip.
     * Updates both the current block counter and the total accumulated blocked
     * turns for reporting.
     * 
     * @param turns The number of turns to skip.
     */
    public void setTurnsToSkip(int turns) {
        this.turnsBlocked += turns;
        this.totalTurnsBlockedAccumulated += turns;
        System.out.println(this.name + " is now blocked for " + this.turnsBlocked + " total turns.");
    }

    /**
     * Required by ExtraMoveEvent. Grants the player additional moves.
     * 
     * @param moves The number of extra moves gained.
     */
    public void addExtraMoves(int moves) {
        this.extraMoves += moves;
        System.out.println(this.name + " gained " + moves + " extra move(s). Total: " + this.extraMoves);
    }

    /**
     * Implements the logic to force the player to move back, removing steps from
     * the path history.
     * The move is strictly limited by the previousRecoilPosition (the
     * position before the last swap or move).
     * 
     * @param numberOfDivisions The number of divisions to move back.
     */
    public void moveBack(int numberOfDivisions) {
        int divisionsReceded = 0;

        System.out.println(this.name + " must move back " + numberOfDivisions + " division(s). Recoil limit: "
                + this.previousRecoilPosition.getId() + ".");

        try {
            
            while (this.pathHistory.size() > 1 && !(this.pathHistory.last() instanceof Division)
                    && !(this.pathHistory.last() instanceof Hallway)) {
                Object removedEvent = this.pathHistory.removeLast();
                System.out.println(
                        "LOG: Evento " + removedEvent.getClass().getSimpleName() + " removido antes do recuo.");
            }

            while (divisionsReceded < numberOfDivisions && this.pathHistory.size() > 1) {

                
                Object lastElement = this.pathHistory.last();

                if (lastElement instanceof Division lastDivisao) {

                    if (lastDivisao.equals(this.previousRecoilPosition)) {
                        System.out.println(this.name + " reached the recoil limit ("
                                + this.previousRecoilPosition.getId() + "). Stopping move back.");
                        break;
                    }

                    this.pathHistory.removeLast(); 
                    this.pathHistory.removeLast(); 

                    Object newPosition = this.pathHistory.last();

                    if (newPosition instanceof Division divisao) {
                        this.currentPosition = divisao;
                        divisionsReceded++;
                        System.out.println(this.name + " receded to " + this.currentPosition.getId() + " ("
                                + divisionsReceded + "/" + numberOfDivisions + ").");
                    } else {
                        System.err.println(
                                "Error: Path History is corrupted. Stopping move back.");
                        break;
                    }
                }
            }
        } catch (EmptyCollectionException e) {
            System.err.println("Error: Path History is empty. Cannot move back.");
        }

        if (divisionsReceded < numberOfDivisions) {
            System.out.println(this.name + " finished recuando. Total receded: " + divisionsReceded + " division(s).");
        }
    }

    /**
     * Registers a non-movement position change event (like Swap) in the path
     * history.
     * This method is used when the player's position is changed externally,
     * ensuring
     * the previousRecoilPosition remains untouched.
     * 
     * @param event The event object that caused the position change
     * (SwapPositionEvent).
     * @param oldPosition The Divisao the player was at before the swap.
     */
    public void registerEventPositionChange(SwapPositionEvent event, Division oldPosition) {
        this.pathHistory.addToRear(event);

        System.out.println("LOG: Swap registered for " + this.name + " from " + oldPosition.getId() + " to "
                + this.currentPosition.getId());
    }

    /**
     * Overload que permite registar um evento genérico que alterou a posição do jogador.
     * Usado por eventos como SwapAllPositionsEvent.
     *
     * @param event The Event object that caused the position change.
     * @param oldPosition The Division the player was at before the change.
     */
    public void registerEventPositionChange(Event event, Division oldPosition) {
        this.pathHistory.addToRear(event);

        System.out.println("LOG: Event registered for " + this.name + " from " + oldPosition.getId() + " to "
                + this.currentPosition.getId());
    }

    /**
     * Returns the number of elements currently recorded in the path history.
     * 
     * @return The size of the pathHistory list.
     */
    public int getPathHistorySize() {
        return this.pathHistory.size();
    }

    /**
     * Defines how the player chooses their next move.
     * The implementation varies by concrete subclass: Human or Bot.
     * 
     * @param availableHallways The hallways available from the current position.
     * @return The chosen Hallway for the next move.
     */
    public abstract Hallway chooseNextMove(DoublyLinkedUnorderedList<Hallway> availableHallways);

    /**
     * Compares this Player object with the specified Player for order.
     * The ranking criteria prioritize performance metrics in the following order:
     * * 1. Enigmas Solved Count (Higher is better/comes first).
     * 2. Total Turns Blocked Accumulated (Lower is better/comes first).
     * 3. Player Name (Alphabetical order as final tiebreaker).
     *
     * @param other The Player object to be compared.
     * @return A negative integer, zero, or a positive integer as this player
     *         is less than, equal to, or greater than the specified player.
     */
    @Override
    public int compareTo(Player other) {
        if (this.enigmasSolvedCount != other.enigmasSolvedCount) {
            return other.enigmasSolvedCount - this.enigmasSolvedCount;
        }

        if (this.totalTurnsBlockedAccumulated != other.totalTurnsBlockedAccumulated) {
            return this.totalTurnsBlockedAccumulated - other.totalTurnsBlockedAccumulated;
        }

    return this.name.compareTo(other.name);

    }

    
    /**
     * Generates a complete record of the player's journey in JSON
     * format.
     * Includes path history, obstacles faced, enigmas solved, and effects applied.
     * * @return A JSON formatted String representing the player's game record.
     */
    public String generateJSONReport() {
        StringBuilder json = new StringBuilder();

        json.append("{\n");
        json.append("  \"player\": \"").append(this.name).append("\",\n");

        json.append("  \"pathHistory\": [\n");

        Iterator<Object> iterator = this.pathHistory.iterator();
        boolean firstItem = true; 

        while (iterator.hasNext()) {
            Object item = iterator.next();
            
            
            if (item == null) {
               
                continue; 
            }
            
            
            if (!firstItem) {
                json.append(",\n");
            }
            
            String itemType = item.getClass().getSimpleName();

            json.append("    {\n");
            json.append("      \"type\": \"").append(itemType).append("\",\n");
            
            json.append("      \"details\": \"").append(item.toString().replace("\"", "\\\"")).append("\"\n");
            json.append("    }");
            
            firstItem = false; 
        }
        json.append("\n  ],\n");

        json.append("  \"turnsBlockedTotal\": ").append(this.totalTurnsBlockedAccumulated).append(",\n");
        json.append("  \"enigmasSolved\": ").append(this.enigmasSolvedCount).append(",\n");

        json.append("}");
        return json.toString();
    }

}