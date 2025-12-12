package org.estg.ed.Maze;

import java.util.Iterator;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Enigma.Enigma;
import org.estg.ed.Enigma.EnigmaLoader;
import org.estg.ed.Lever.Lever;

import Lists.ArrayUnorderedList;

//@author Mariana Barreto 8240344
/**
 * Class responsible for loading mazes and populating them with content,
 * such as enigmas and levers.
 */
public class MazeManager {

    /**
     * Loads a maze from a JSON file and populates it with enigmas and levers.
     * 
     * @param mazeFilename The JSON file of the maze
     * @param enigmasFilename The JSON file containing enigmas
     * @return A Maze object with its content assigned
     */
    public Maze loadMazeWithContents(String mazeFilename, String enigmasFilename) {
        Maze maze = MazeLoader.loadMazeFromJSON(mazeFilename);
        ArrayUnorderedList<Enigma> enigmas = EnigmaLoader.loadEnigmasFromJSON(enigmasFilename);
        associateEnigmasToDivisions(maze, enigmas);
        associateLeversToDivisions(maze);
        return maze;
    }

    /**
     * Associates a random enigma to each division of type ENIGMA in the maze.
     * 
     * @param maze The maze containing divisions
     * @param enigmas The list of available enigmas
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
     * Associates a lever to each division of type LEVER in the maze.
     * 
     * @param maze The maze containing divisions
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
     * Returns a random enigma from the list and removes it to avoid duplicates.
     * 
     * @param enigmas The list of available enigmas
     * @return A random Enigma, or null if none are available
     */
    private Enigma getRandomEnigma(ArrayUnorderedList<Enigma> enigmas) {
        if (enigmas.isEmpty()) {
            return null;
        }

        int randomIndex = (int) (Math.random() * enigmas.size());
        Iterator<Enigma> iterator = enigmas.iterator();
        Enigma randomEnigma = null;
        int currentIndex = 0;

        while (iterator.hasNext()) {
            Enigma e = iterator.next();
            if (currentIndex == randomIndex) {
                randomEnigma = e;
                iterator.remove();
                break;
            }
            currentIndex++;
        }

        return randomEnigma;
    }
}
