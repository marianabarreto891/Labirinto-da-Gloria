package org.estg.ed.Maze;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Hall.Hallway;

import Lists.ArrayUnorderedList;

//@author Mariana Barreto 8240344
/**
 * Represents the physical structure of the labyrinth in the game.
 *
 * This class acts as a container for the entire graph data structure, holding
 * the list of all Divisions (nodes) and Hallways (edges) that make up the map.
 * It also provides utility methods to locate specific key rooms, such as the central room.
 */
public class Maze {
    private int id;
    private String name;
    private ArrayUnorderedList<Hallway> h;
    private ArrayUnorderedList<Division> d;


    /**
     * Create a new Maze with the specified structure.
     *
     * @param id   The unique identifier for the maze.
     * @param name The name of the maze.
     * @param h    The list of hallways (edges) connecting the rooms.
     * @param d    The list of divisions (nodes) in the map.
     */
    public Maze(int id, String name, ArrayUnorderedList<Hallway> h, ArrayUnorderedList<Division> d) {
        this.id = id;
        this.name = name;
        this.h = h;
        this.d = d;
    }


    /**
     * Retrieves the unique identifier of the maze.
     *
     * @return The maze ID.
     */
    public int getId() {
        return id;
    }


    /**
     * Updates the unique identifier of the maze.
     *
     * @param id The new maze ID.
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Retrieves the name of the maze.
     *
     * @return The name string.
     */
    public String getName() {
        return name;
    }


    /**
     * Updates the name of the maze.
     *
     * @param name The new name string.
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Retrieves the list of hallways (edges) in the maze.
     *
     * @return An unordered list of Hallway objects.
     */
    public ArrayUnorderedList<Hallway> getH() {
        return h;
    }


    /**
     * Updates the list of hallways in the maze.
     *
     * @param h The new list of Hallway objects.
     */
    public void setH(ArrayUnorderedList<Hallway> h) {
        this.h = h;
    }


    /**
     * Retrieves the list of divisions in the maze.
     *
     * @return An unordered list of Division objects.
     */
    public ArrayUnorderedList<Division> getD() {
        return d;
    }


    /**
     * Updates the list of divisions in the maze.
     *
     * @param d The new list of Division objects.
     */
    public void setD(ArrayUnorderedList<Division> d) {
        this.d = d;
    }


    /**
     * Returns a string representation of the Maze object.
     *
     * Includes the ID, name, and string representations of the hallway and division lists.
     *
     * @return A formatted string describing the maze.
     */
    @Override
    public String toString() {
        return "Maze [id=" + id + ", name=" + name + ", h=" + h + ", d=" + d + "]";
    }


   /**
     * Searches for and returns the Central Room (the goal of the game).
     *
     * Iterates through the list of divisions to find the one marked with
     * the specific type DivisionType.CENTRAL.
     *
     * @return The Division object representing the central room, or null if not found.
     */
    public Division getCentralRoom() {
        for (Division division : d) {
            if (division.getDt() == DivisionType.CENTRAL) {
                return division;
            }
        }
        return null;
    }

}
