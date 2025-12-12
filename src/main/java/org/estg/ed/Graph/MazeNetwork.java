package org.estg.ed.Graph;

import java.util.Iterator;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Hall.Hallway;

import Graphs.Network;
import Lists.ArrayUnorderedList;
import Lists.DoublyLinkedUnorderedList;

/**
 * MazeNetwork is a specialized extension of Network<Division> designed for the Labyrinth game.
 * <p>
 * It manages the graph structure of the maze, storing divisions as vertices and hallways as edges.
 * Provides methods for accessing start and central divisions, adjacent divisions, available hallways,
 * and calculating shortest paths.
 * </p>
 */
public class MazeNetwork extends Network<Division> {

    private Division startDivision;
    private Division centralRoom;
    public ArrayUnorderedList<Hallway> hallways;

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
     * @param centralRoom the central division
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
     * Adds a hallway to the maze network.
     * This method adds the hallway to the internal list and adds a weighted edge to the graph.
     *
     * @param hallway the hallway to add
     */
    public void addHallway(Hallway hallway) {
        if (hallway != null) {
            hallways.addToRear(hallway);
            this.addEdge(hallway.getOrigin(), hallway.getDestination(), hallway.getWeight());
        }
    }

    /**
     * Gets all hallways in the maze network.
     *
     * @return an unordered list of hallways
     */
    public ArrayUnorderedList<Hallway> getHallways() {
        return hallways;
    }

    /**
     * Finds a hallway connecting two divisions.
     *
     * @param origin the origin division
     * @param destination the destination division
     * @return the hallway connecting these divisions or null if not found
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
     * Returns all adjacent divisions from a given division.
     *
     * @param division the division to check
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
     * Returns hallways that are available from a given division.
     * Excludes hallways leading to entry divisions.
     *
     * @param division the current division
     * @return a list of available hallways
     */
    public DoublyLinkedUnorderedList<Hallway> getAvailableHallways(Division division) {
        DoublyLinkedUnorderedList<Hallway> availableHallways = new DoublyLinkedUnorderedList<>();

        for (Hallway hallway : hallways) {
            if (hallway.getOrigin().equals(division)) {
                Division destination = hallway.getDestination();
                if (!isEntryDivision(destination)) {
                    availableHallways.addToRear(hallway);
                }
            } else if (hallway.getDestination().equals(division)) {
                Division destination = hallway.getOrigin();
                if (!isEntryDivision(destination)) {
                    availableHallways.addToRear(hallway);
                }
            }
        }

        return availableHallways;
    }

    /**
     * Checks if a division is the entry division.
     *
     * @param division the division to check
     * @return true if it is the start division, false otherwise
     */
    public boolean isEntryDivision(Division division) {
        return division.equals(startDivision);
    }

    /**
     * Checks if a division is the central room.
     *
     * @param division the division to check
     * @return true if it is the central room, false otherwise
     */
    public boolean isCentralRoom(Division division) {
        return division.equals(centralRoom);
    }

    /**
     * Returns an iterator for the shortest path between two divisions.
     *
     * @param startDivision the starting division
     * @param targetDivision the target division
     * @return an iterator over divisions in the shortest path
     */
    public Iterator<Division> getShortestPath(Division startDivision, Division targetDivision) {
        return this.iteratorShortestPath(startDivision, targetDivision);
    }

    /**
     * Returns the distance of the shortest path between two divisions.
     *
     * @param startDivision the starting division
     * @param targetDivision the target division
     * @return the weight of the shortest path or POSITIVE_INFINITY if no path exists
     */
    public double getShortestPathDistance(Division startDivision, Division targetDivision) {
        return this.shortestPathWeight(startDivision, targetDivision);
    }

    /**
     * Returns the number of divisions (vertices) in the maze network.
     *
     * @return the count of divisions
     */
    public int getDivisionsCount() {
        return numVertices;
    }

    /**
     * Returns the number of hallways (edges) in the maze network.
     *
     * @return the count of hallways
     */
    public int getHallwaysCount() {
        return hallways.size();
    }

    /**
     * Checks if the maze is winnable (a path exists from start to central room).
     *
     * @return true if a path exists, false otherwise
     */
    public boolean isWinnable() {
        if (startDivision == null || centralRoom == null) {
            return false;
        }

        Iterator<Division> path = this.getShortestPath(startDivision, centralRoom);
        return path.hasNext();
    }

    /**
     * Returns a string representation of the maze network.
     *
     * @return a formatted string describing divisions, hallways, and status
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
