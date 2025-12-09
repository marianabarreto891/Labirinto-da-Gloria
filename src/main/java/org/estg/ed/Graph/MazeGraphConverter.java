package org.estg.ed.Graph;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Hall.Hallway;
import org.estg.ed.Maze.Maze;

import Graphs.Network;
import Lists.ArrayUnorderedList;

/**
 * Class responsible for transforming the Maze data model into a graph structure.
 *
 * This class bridges the gap between the loaded Maze object and the Network data structure used for graph algorithms
 * 
 */
public class MazeGraphConverter {


    /**
     * Converts a Maze instance into a Network (weighted graph).
     *
     * This method iterates through all the divisions in the maze to add them as vertices
     * in the graph. It then iterates through all the hallways to establish weighted edges
     * connecting these vertices.
     *
     * @param maze The Maze object containing the lists of rooms and paths.
     * @return A fully constructed Network of Divisions representing the maze topology.
     */
    public static Network<Division> convertMazeToNetwork(Maze maze) {

        Network<Division> network = new Network<>();

        ArrayUnorderedList<Division> divisions = maze.getD();

        ArrayUnorderedList<Hallway> halls = maze.getH();

        for (Division division : divisions) {
            network.addVertex(division);
        }

        for (Hallway hall : halls) {
            network.addEdge(hall.getOrigin(), hall.getDestination(), hall.getWeight());
        }

        return network;
    }
}
