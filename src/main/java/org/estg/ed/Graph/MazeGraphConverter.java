package org.estg.ed.Graph;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Hall.Hallway;
import org.estg.ed.Maze.Maze;

import Lists.ArrayUnorderedList;

/**
 * Class responsible for transforming the Maze data model into a graph structure.
 *
 * This class bridges the gap between the loaded Maze object and the Network data structure used for graph algorithms
 * 
 */
public class MazeGraphConverter {

    /**
     * Converts a Maze instance into a MazeNetwork (weighted graph).
     *
     * @param maze O objeto Maze.
     * @return A fully constructed MazeNetwork (CORRIGIDO o tipo de retorno).
     */
    public static MazeNetwork convertMazeToNetwork(Maze maze) { // ⬅️ ALTERADO: Retorna MazeNetwork

        MazeNetwork network = new MazeNetwork(); // ⬅️ ALTERADO: Instancia MazeNetwork

        ArrayUnorderedList<Division> divisions = maze.getD();
        ArrayUnorderedList<Hallway> halls = maze.getH();

        // 1. Adicionar Vértices e configurar divisões especiais
        for (Division division : divisions) {
            network.addVertex(division);
            
            // É essencial configurar as salas especiais (Start/Central)
            if (division.getDt() == DivisionType.CENTRAL) {
                network.setCentralRoom(division);
            }
            if (division.getDt() == DivisionType.ENTRY && network.getStartDivision() == null) {
                network.setStartDivision(division);
            }
        }

        // 2. Adicionar Arestas (Corredores)
        for (Hallway hall : halls) {
            network.addEdge(hall.getOrigin(), hall.getDestination(), hall.getWeight());
            
            // Adiciona o Hallway à lista interna do MazeNetwork
            network.hallways.addToRear(hall); 
        }

        return network; // Retorna o objeto MazeNetwork
    }
}

