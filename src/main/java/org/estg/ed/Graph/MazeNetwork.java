package org.estg.ed.Graph;

import java.util.Iterator;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Hall.Hallway;

import Graphs.Network;
import Lists.ArrayUnorderedList;
import Lists.DoublyLinkedUnorderedList;

/**
 * MazeNetwork is a specialized extension of Network<Division> designed specifically
 * for the Labyrinth game. It manages the graph structure of the maze and provides
 * additional methods for maze-specific operations.
 * 
 * @author Your Group
 */
public class MazeNetwork extends Network<Division> {

    private Division startDivision;
    private Division centralRoom;
    private ArrayUnorderedList<Hallway> hallways;

    /**
     * Constructs a new MazeNetwork with default capacity.
     */
    public MazeNetwork() {
        super();
        this.hallways = new ArrayUnorderedList<>();
    }

    /**
     * Constructs a new MazeNetwork with a specified initial capacity.
     *
     * @param capacity the initial capacity of the network
     */
    public MazeNetwork(int capacity) {
        super(capacity);
        this.hallways = new ArrayUnorderedList<>();
    }

    /**
     * Sets the starting division (entry point) of the maze.
     *
     * @param startDivision the entry division
     */
    public void setStartDivision(Division startDivision) {
        this.startDivision = startDivision;
    }

    /**
     * Gets the starting division of the maze.
     *
     * @return the entry division
     */
    public Division getStartDivision() {
        return startDivision;
    }

    /**
     * Sets the central room (goal) of the maze.
     *
     * @param centralRoom the goal division
     */
    public void setCentralRoom(Division centralRoom) {
        this.centralRoom = centralRoom;
    }

    /**
     * Gets the central room of the maze.
     *
     * @return the central division
     */
    public Division getCentralRoom() {
        return centralRoom;
    }

    /**
     * Adds a hallway (edge with event) to the maze network.
     * This method stores the hallway for later retrieval and event management.
     *
     * @param hallway the hallway to add
     */
    public void addHallway(Hallway hallway) {
        if (hallway != null) {
            hallways.addToRear(hallway);
            // Add the weighted edge to the network graph
            this.addEdge(hallway.getOrigin(), hallway.getDestination(), hallway.getWeight());
        }
    }

    /**
     * Retrieves all hallways in the maze.
     *
     * @return an unordered list of all hallways
     */
    public ArrayUnorderedList<Hallway> getHallways() {
        return hallways;
    }

    /**
     * Finds a hallway between two divisions.
     *
     * @param origin the origin division
     * @param destination the destination division
     * @return the hallway connecting these divisions, or null if not found
     */
    public Hallway findHallway(Division origin, Division destination) {
        for (Hallway hallway : hallways) {
            if ((hallway.getOrigin().equals(origin) && hallway.getDestination().equals(destination))
                    || (hallway.getOrigin().equals(destination) && hallway.getDestination().equals(origin))) {
                return hallway;
            }
        }
        return null;
    }

    /**
     * Gets all adjacent divisions from a given division.
     * Returns divisions that are directly connected by hallways.
     *
     * @param division the current division
     * @return a list of adjacent divisions
     */
    public DoublyLinkedUnorderedList<Division> getAdjacentDivisions(Division division) {
        DoublyLinkedUnorderedList<Division> adjacent = new DoublyLinkedUnorderedList<>();
        int divisionIndex = getIndex(division);

        if (!indexIsValid(divisionIndex)) {
            return adjacent;
        }

        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[divisionIndex][i] < Double.POSITIVE_INFINITY) {
                adjacent.addToRear(vertices[i]);
            }
        }

        return adjacent;
    }

    /**
     * Gets all adjacent hallways from a given division.
     * Returns hallways that originate from the given division and don't lead to entry divisions.
     * 
     * CORRIGIDO: Agora apenas retorna corredores diretos (adjacentes)
     *
     * @param division the current division
     * @return a list of available hallways
     */
    public DoublyLinkedUnorderedList<Hallway> getAvailableHallways(Division division) {
        DoublyLinkedUnorderedList<Hallway> availableHallways = new DoublyLinkedUnorderedList<>();

        // Percorrer todos os corredores e adicionar os que saem desta divisão
        for (Hallway hallway : hallways) {
            // Verifica se o corredor sai da divisão atual
            if (hallway.getOrigin().equals(division)) {
                Division destination = hallway.getDestination();
                
                // Não adicionar se o destino for uma entrada (exceto a sala central)
                if (!isEntryDivision(destination)) {
                    availableHallways.addToRear(hallway);
                }
            }
            // Também verificar na direção inversa (grafo não direcionado)
            else if (hallway.getDestination().equals(division)) {
                Division destination = hallway.getOrigin();
                
                // Não adicionar se o destino for uma entrada (exceto a sala central)
                if (!isEntryDivision(destination)) {
                    availableHallways.addToRear(hallway);
                }
            }
        }

        return availableHallways;
    }

    /**
     * Checks if a division is an entry division.
     * Entry divisions are only the start division, not the central room.
     *
     * @param division the division to check
     * @return true if the division is an entry point, false otherwise
     */
    public boolean isEntryDivision(Division division) {
        return division.equals(startDivision);
    }

    /**
     * Checks if a division is the central room (goal).
     *
     * @param division the division to check
     * @return true if the division is the central room, false otherwise
     */
    public boolean isCentralRoom(Division division) {
        return division.equals(centralRoom);
    }

    /**
     * Gets the shortest path between two divisions.
     *
     * @param startDivision the starting division
     * @param targetDivision the target division
     * @return an iterator of divisions in the shortest path
     */
    public Iterator<Division> getShortestPath(Division startDivision, Division targetDivision) {
        return this.iteratorShortestPath(startDivision, targetDivision);
    }

    /**
     * Calculates the distance (weight) of the shortest path between two divisions.
     *
     * @param startDivision the starting division
     * @param targetDivision the target division
     * @return the weight of the shortest path, or POSITIVE_INFINITY if no path exists
     */
    public double getShortestPathDistance(Division startDivision, Division targetDivision) {
        return this.shortestPathWeight(startDivision, targetDivision);
    }

    /**
     * Returns the number of divisions (vertices) in the maze network.
     *
     * @return the number of divisions
     */
    public int getDivisionsCount() {
        return numVertices;
    }

    /**
     * Returns the number of hallways (edges) in the maze network.
     *
     * @return the number of hallways
     */
    public int getHallwaysCount() {
        return hallways.size();
    }

    /**
     * Checks if the maze is properly connected from the start to the central room.
     *
     * @return true if a path exists from start to central room, false otherwise
     */
    public boolean isWinnable() {
        if (startDivision == null || centralRoom == null) {
            return false;
        }

        Iterator<Division> path = this.getShortestPath(startDivision, centralRoom);
        return path.hasNext(); // If there's at least one element, a path exists
    }

    /**
     * Gets a string representation of the maze network including divisions and hallways.
     *
     * @return a formatted string describing the network
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MAZE NETWORK ===\n");
        sb.append("Divisions: ").append(numVertices).append("\n");
        sb.append("Hallways: ").append(hallways.size()).append("\n");
        sb.append("Start Division: ").append(startDivision != null ? startDivision.getId() : "None").append("\n");
        sb.append("Central Room: ").append(centralRoom != null ? centralRoom.getId() : "None").append("\n");
        sb.append("Winnable: ").append(isWinnable()).append("\n");
        sb.append("\n").append(super.toString());
        return sb.toString();
    }
}