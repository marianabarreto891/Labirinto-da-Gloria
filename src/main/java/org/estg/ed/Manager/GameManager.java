package org.estg.ed.Manager;

import java.util.Iterator;

import org.estg.ed.Content.Content;
import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Hall.Hallway;
import org.estg.ed.Players.Player;

import Graphs.Network;
import Lists.ArrayUnorderedList;
import Lists.DoublyLinkedUnorderedList;

public class GameManager {
    private Network<Division> mazeNetwork;
    private ArrayUnorderedList<Player> players;
    private Division centralRoom;
    private int currentPlayerIndex;

    public GameManager(Network<Division> mazeNetwork, ArrayUnorderedList<Player> players, Division centralRoom) {
        this.mazeNetwork = mazeNetwork;
        this.players = players;
        this.centralRoom = centralRoom;
        this.currentPlayerIndex = 0;
    }

    public void startGame() {
        while (!isGameOver()) {
            Player currentPlayer = getPlayerByIndex(players, currentPlayerIndex);
            takeTurn(currentPlayer);
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
        announceWinner();
    }

    private Player getPlayerByIndex(ArrayUnorderedList<Player> players, int index) {
        Iterator<Player> iterator = players.iterator();
        int currentIndex = 0;
        while (iterator.hasNext()) {
            Player player = iterator.next();
            if (currentIndex == index) {
                return player;
            }
            currentIndex++;
        }
        return null;
    }

    private void takeTurn(Player player) {
        if (!player.canPlay()) {
            return;
        }
        Division currentDivision = player.getCurrentDivision();
        System.out.println(player.getName() + ", it's your turn. You are in division " + currentDivision.getId());
        if (currentDivision.getDt() == DivisionType.ENIGMA) {
            handleEnigma(player, (Content) currentDivision.getContent());
        } else if (currentDivision.getDt() == DivisionType.LEVER) {
            handleLever(player, (Content) currentDivision.getContent());
        } else {
            movePlayer(player);
        }
    }

    private void handleEnigma(Player player, Content enigma) {
        boolean success = enigma.interactType(player);
        if (success) {
            System.out.println("You solved the enigma! You can continue.");
        } else {
            System.out.println("You failed to solve the enigma. Try again next turn.");
        }
    }

    private void handleLever(Player player, Content lever) {
        boolean success = lever.interactType(player);
        if (success) {
            System.out.println("You activated the lever! You can continue.");
        } else {
            System.out.println("You failed to activate the lever. Try again next turn.");
        }
    }

    private void movePlayer(Player player) {
        if (!player.canPlay()) {
            return;
        }
        getNextDivision(player);
    }

    private DoublyLinkedUnorderedList<Hallway> getAvailableHallways(Division currentDivision) {
        DoublyLinkedUnorderedList<Hallway> availableHallways = new DoublyLinkedUnorderedList<>();

        // Usar iteratorBFS para obter as divisões adjacentes
        Iterator<Division> neighbors = mazeNetwork.iteratorBFS(currentDivision);

        while (neighbors.hasNext()) {
            Division neighbor = neighbors.next();
            // Obter o peso da aresta entre currentDivision e neighbor
            double weight = mazeNetwork.shortestPathWeight(currentDivision, neighbor);
            if (weight < Double.POSITIVE_INFINITY) {
                Hallway hallway = new Hallway(
                        currentDivision,
                        neighbor,
                        0,
                        weight);
                availableHallways.addToRear(hallway);
            }
        }

        return availableHallways;
    }

    private void getNextDivision(Player player) {
        Division currentDivision = player.getCurrentDivision();
        DoublyLinkedUnorderedList<Hallway> availableHallways = getAvailableHallways(currentDivision);
        Hallway chosenHallway = player.chooseNextMove(availableHallways);
        if (chosenHallway != null) {
            player.moveTo(chosenHallway.getDestination(), chosenHallway);
            chosenHallway.activateEvent(player);
        } else {
            System.out.println("No available moves. Skipping turn.");
        }
    }

    private boolean isGameOver() {
        for (Player player : players) {
            if (player.getCurrentDivision().equals(centralRoom)) {
                return true;
            }
        }
        return false;
    }

    private void announceWinner() {
        for (Player player : players) {
            if (player.getCurrentDivision().equals(centralRoom)) {
                System.out.println(player.getName() + " has reached the central room and won the game!");
                return;
            }
        }
    }
}
