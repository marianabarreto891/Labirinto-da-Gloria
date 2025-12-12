package org.estg.ed.Maze;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Graph.MazeGraphConverter;
import org.estg.ed.Graph.MazeNetwork;
import org.estg.ed.Hall.Hallway;
import org.estg.ed.Hall.HallwayManager;

import Lists.ArrayUnorderedList;

/**
 * Class responsible for managing and editing maze maps.
 * Supports creating and saving new maps in JSON format.
 */
public class MazeEditor {
    
    private Maze currentMaze;
    private MazeNetwork currentNetwork;

    /**
     * Constructor to initialize a new empty maze.
     */
    public MazeEditor() {
        ArrayUnorderedList<Hallway> h = new ArrayUnorderedList<>();
        ArrayUnorderedList<Division> d = new ArrayUnorderedList<>();
        this.currentMaze = new Maze(1, "New Unsaved Map", h, d);
        this.currentNetwork = MazeGraphConverter.convertMazeToNetwork(currentMaze);
        System.out.println("New empty map created.");
    }
    
    /**
     * Loads an existing maze for editing.
     * 
     * @param filename The JSON file name of the maze
     */
    public void loadMaze(String filename) {
        this.currentMaze = MazeLoader.loadMazeFromJSON(filename);
        
        if (this.currentMaze != null) {
            this.currentNetwork = MazeGraphConverter.convertMazeToNetwork(currentMaze);
            System.out.println("Map '" + currentMaze.getName() + "' loaded for editing.");
        } else {
            System.err.println("ERROR: Failed to load map from file: " + filename);
        }
    }

    /**
     * Adds a new division (vertex) to the maze.
     * 
     * @param id The unique ID of the new division
     * @param type The type of division (ENTRY, ENIGMA, etc.)
     */
    public void addDivision(int id, DivisionType type) {
        if (findDivision(id) != null) {
            System.err.println("ERROR: Division with ID " + id + " already exists.");
            return;
        }
        
        Division newDiv = new Division(id, type);
        currentMaze.getD().addToRear(newDiv); 
        currentNetwork.addVertex(newDiv);    
        System.out.println("Division " + id + " (" + type + ") added.");
    }

    /**
     * Connects two divisions with a hallway (edge).
     * 
     * @param id1 ID of the origin division
     * @param id2 ID of the destination division
     * @param weight The weight/cost of the hallway
     * @param eventId The ID of the associated event (0 for none)
     */
    public void connectDivisions(int id1, int id2, double weight, int eventId) {
        Division div1 = findDivision(id1);
        Division div2 = findDivision(id2);
        
        if (div1 == null || div2 == null) {
             System.err.println("ERROR: One or both divisions not found.");
             return;
        }
        
        Hallway newHall = HallwayManager.createHallway(div1, div2, eventId, weight);
        currentMaze.getH().addToRear(newHall);     
        currentNetwork.addEdge(div1, div2, weight); 
        currentNetwork.hallways.addToRear(newHall); 

        System.out.println("Hallway (" + id1 + " <-> " + id2 + ", Weight: " + weight + ") created.");
    }

    /**
     * Sets the metadata of the maze, including ID and name.
     * 
     * @param id The ID of the maze
     * @param name The name of the maze
     */
    public void setMetadata(int id, String name) {
        currentMaze.setId(id);
        currentMaze.setName(name);
        System.out.println("Metadata updated to ID: " + id + ", Name: " + name);
    }
    
    /**
     * Saves the current maze state to a JSON file.
     * 
     * @param filename The file name (e.g., "new_map.json")
     */
    public void saveMaze(String filename) {
        MazeSaver.saveMazeToJSON(currentMaze, filename);
        System.out.println(" Map '" + currentMaze.getName() + "' saved successfully to " + filename + ".");
    }
    
    /**
     * Finds a division by its ID in the maze.
     * 
     * @param id The ID of the division
     * @return The found Division or null if not found
     */
    private Division findDivision(int id) {
        for (Division div : currentMaze.getD()) {
            if (div.getId() == id) {
                return div;
            }
        }
        return null;
    }

    /**
     * Returns the current maze being edited.
     * Useful for inspection in the interactive editor console.
     * 
     * @return The current Maze object
     */
    public Maze getCurrentMaze() {
        return currentMaze;
    }
}
