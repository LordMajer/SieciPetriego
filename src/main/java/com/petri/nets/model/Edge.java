package com.petri.nets.model;

/**
 *
 * @author Mateusz
 */
public class Edge {

    private static final int DEFAULT_CAPACITY = 1;

    int capacity = DEFAULT_CAPACITY;               // pojemność krawędzi - ilość tokenów które przechodza przez krawędź
    int sourceId;                                  // id wierzchołka źródła
    int destinationId;                             // id wierzchołka docelowego

    public Edge(int sourceId, int destinationId) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
    }

    // TODO usunąć/zmodyfikować
    public Edge(Edge edge) {

    }

    public String getKey() {
        return "{" + sourceId + "," + destinationId + "}";
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    @Override
    public String toString() {
        return "Edge: " + this.getKey();
    }
}