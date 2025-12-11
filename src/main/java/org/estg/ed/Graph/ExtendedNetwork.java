package org.estg.ed.Graph;

import Graphs.Network;

public class ExtendedNetwork<T> extends Network<T> {

    public ExtendedNetwork() {
        super();
    }

    public ExtendedNetwork(int size) {
        super(size);
    }

    /**
     * Returns the number of vertices in this network.
     *
     * @return the number of vertices
     */
    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Returns the adjacency matrix.
     *
     * @return the adjacency matrix
     */
    public double[][] getAdjMatrix() {
        return adjMatrix;
    }

    /**
     * Returns the vertex at the specified index.
     *
     * @param index the index of the vertex to retrieve
     * @return the vertex at the specified index
     */
    public T getVertex(int index) {
        if (index >= 0 && index < numVertices) {
            return vertices[index];
        }
        return null;
    }

}
