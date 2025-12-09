package org.estg.ed.Maze;

import java.util.Iterator;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Enigma.Enigma;
import org.estg.ed.Enigma.EnigmaLoader;
import org.estg.ed.Enigma.EnigmaManager;
import org.estg.ed.Lever.Lever;

import Lists.ArrayUnorderedList;

/**
 * Manages the initialization and configuration of the maze.
 *
 * This class serves as a high-level coordinator. It uses specific loaders to fetch
 * raw data from files and then logic to populate the maze divisions with the appropriate
 * game content (Enigmas and Levers).
 */
public class MazeManager {
    private EnigmaManager enigmaManager;

    public MazeManager() {
        this.enigmaManager = new EnigmaManager();
    }

    /**
     * Orchestrates the complete loading process of the maze.
     *
     * @param mazeFilename    The file path for the maze structure JSON.
     * @param enigmasFilename The file path for the enigmas content JSON.
     * @return A fully initialized Maze object containing structure and content.
     */
    public Maze loadMazeWithContents(String mazeFilename, String enigmasFilename) {
        Maze maze = MazeLoader.loadMazeFromJSON(mazeFilename);
        ArrayUnorderedList<Enigma> enigmas = EnigmaLoader.loadEnigmasFromJSON(enigmasFilename);

        Iterator<Enigma> iterator = enigmas.iterator();
        while (iterator.hasNext()) {
            enigmaManager.addEnigma(iterator.next());
        }

        associateEnigmasToDivisions(maze);
        associateLeversToDivisions(maze);

        return maze;
    }

    /**
     * Associate enigmas to divisions.
     *
     * Scans all divisions in the maze. If a division's type is ENIGMA, a random
     * enigma is selected from the EnigmaManager and assigned to that division's content.
     *
     * @param maze The maze object being configured.
     */
    private void associateEnigmasToDivisions(Maze maze) {
        ArrayUnorderedList<Division> divisions = maze.getD();
        for (Division division : divisions) {
            if (division.getDt() == DivisionType.ENIGMA) {
                Enigma enigma = enigmaManager.getUnusedEnigma();
                if (enigma != null) {
                    division.setContent(enigma);
                }
            }
        }
    }

    /**
     * Auxiliary method that iterates through the maze divisions and generates new Lever objects for specific rooms.
     *
     * Scans all divisions. If a division's type is LEVER, a new instance of the
     * Lever class is created and assigned to that division.
     *
     * @param maze The maze object being configured.
     */
    private void associateLeversToDivisions(Maze maze) {
        ArrayUnorderedList<Division> divisions = maze.getD();
        for (Division division : divisions) {
            if (division.getDt() == DivisionType.LEVER) {
                Lever l = new Lever();
                if (l != null) {
                    division.setContent(l);
                }
            }
        }
    }

    /**
     * Getter for the EnigmaManager instance.
     *
     * @return The EnigmaManager instance used by this MazeManager.
     */
    public EnigmaManager getEnigmaManager() {
        return enigmaManager;
    }
}
