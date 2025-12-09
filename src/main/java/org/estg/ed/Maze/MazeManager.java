
package org.estg.ed.Maze;

import java.util.Iterator;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Enigma.Enigma;
import org.estg.ed.Enigma.EnigmaLoader;
import org.estg.ed.Lever.Lever;

import Lists.ArrayUnorderedList;

/**
 * Manages the initialization and configuration of the maze.
 *
 * This class serves as a high-level coordinator. It uses specific loaders to fetch
 * raw data from files and then logic
 * to populate the maze divisions with the appropriate game content (Enigmas and Levers).
 */
public class MazeManager {

    /**
     * Orchestrates the complete loading process of the maze.
     *
     *
     * @param mazeFilename    The file path for the maze structure JSON.
     * @param enigmasFilename The file path for the enigmas content JSON.
     * @return A fully initialized Maze object containing structure and content.
     */
    public Maze loadMazeWithContents(String mazeFilename, String enigmasFilename) {
        Maze maze = MazeLoader.loadMazeFromJSON(mazeFilename);

        ArrayUnorderedList<Enigma> enigmas = EnigmaLoader.loadEnigmasFromJSON(enigmasFilename);

        associateEnigmasToDivisions(maze, enigmas);

        associateLeversToDivisions(maze);

        return maze;
    }

    /**
     * Associate enigmas to divisions.
     *
     * Scans all divisions in the maze. If a division's type is ENIGMA, a random
     * enigma is selected from the provided list and assigned to that division's content.
     *
     * @param maze    The maze object being configured.
     * @param enigmas The list of available enigmas loaded from file.
     */
    private void associateEnigmasToDivisions(Maze maze, ArrayUnorderedList<Enigma> enigmas) {
        ArrayUnorderedList<Division> divisions = maze.getD();

        for (Division division : divisions) {
            if (division.getDt() == DivisionType.ENIGMA) {
                Enigma enigma = getRandomEnigma(enigmas);
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
     * Auxiliary method to select a random enigma from the provided list.
     *
     * Since the list is unordered and does not support direct index access,
     * this method calculates a random index and iterates to that position.
     *
     * @param enigmas The list of available enigmas.
     * @return A randomly selected Enigma object, or null if the list is empty.
     */
    private Enigma getRandomEnigma(ArrayUnorderedList<Enigma> enigmas) {
        if (enigmas.isEmpty()) {
            return null;
        }

        int randomIndex = (int) (Math.random() * enigmas.size());
        Iterator<Enigma> iterator = enigmas.iterator();
        Enigma randomEnigma = null;

        for (int i = 0; i <= randomIndex && iterator.hasNext(); i++) {
            randomEnigma = iterator.next();
        }

    return randomEnigma;
    }


}
