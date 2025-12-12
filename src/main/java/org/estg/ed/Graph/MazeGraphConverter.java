package org.estg.ed.Graph;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Hall.Hallway;
import org.estg.ed.Maze.Maze;

import Lists.ArrayUnorderedList;


//@author Mariana Barreto 8240344
/**
 * Responsible for converting a Maze data model into a graph representation.
 * 
 * This class transforms the Maze object into a MazeNetwork, making it suitable
 * for graph algorithms and network operations.
 * 
 */
public class MazeGraphConverter {

    /**
     * Converts a Maze instance into a MazeNetwork (weighted graph).
     *
     * @param maze the Maze object to convert
     * @return a fully constructed MazeNetwork
     */
    public static MazeNetwork convertMazeToNetwork(Maze maze) {

        MazeNetwork network = new MazeNetwork();

        ArrayUnorderedList<Division> divisions = maze.getD();
        ArrayUnorderedList<Hallway> halls = maze.getH();

        for (Division division : divisions) {
            network.addVertex(division);

            if (division.getDt() == DivisionType.CENTRAL) {
                network.setCentralRoom(division);
            }
            if (division.getDt() == DivisionType.ENTRY && network.getStartDivision() == null) {
                network.setStartDivision(division);
            }
        }

        for (Hallway hall : halls) {
            network.addEdge(hall.getOrigin(), hall.getDestination(), hall.getWeight());
            network.hallways.addToRear(hall); 
        }

        return network;
    }
}










