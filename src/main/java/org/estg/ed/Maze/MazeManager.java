package org.estg.ed.Maze;

import java.util.Iterator;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Enigma.Enigma;
import org.estg.ed.Enigma.EnigmaLoader;
import org.estg.ed.Lever.Lever;

import Lists.ArrayUnorderedList;

public class MazeManager {
    public Maze loadMazeWithContents(String mazeFilename, String enigmasFilename) {
        Maze maze = MazeLoader.loadMazeFromJSON(mazeFilename);

        ArrayUnorderedList<Enigma> enigmas = EnigmaLoader.loadEnigmasFromJSON(enigmasFilename);

        associateEnigmasToDivisions(maze, enigmas);

        associateLeversToDivisions(maze);

        return maze;
    }

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
            //System.out.println(e);
            currentIndex++;
        }

        return randomEnigma;
    }
}