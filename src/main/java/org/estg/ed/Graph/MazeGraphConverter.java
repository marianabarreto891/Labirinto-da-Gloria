package org.estg.ed.Graph;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Hall.Hallway;
import org.estg.ed.Maze.Maze;

import Graphs.Network;
import Lists.ArrayUnorderedList;

public class MazeGraphConverter {

    public static Network<Division> convertMazeToNetwork(Maze maze) {
        Network<Division> network = new Network<>();
        ArrayUnorderedList<Division> divisions = maze.getD();
        ArrayUnorderedList<Hallway> halls = maze.getH();

        // Adicionar divisões ao grafo
        for (Division division : divisions) {
            network.addVertex(division);
        }

        // Adicionar corredores ao grafo
        for (Hallway hall : halls) {
            network.addEdge(hall.getOrigin(), hall.getDestination(), hall.getWeight());
        }

        return network;
    }
}
