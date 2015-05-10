package com.petri.nets.model;

/**
 *
 * @author Mateusz
 */
public class Edge {

    private static final int DEFAULT_CAPACITY = 1;

    private int capacity = DEFAULT_CAPACITY;               // pojemność krawędzi - ilość tokenów które przechodza przez krawędź
    private int sourceId;                                  // id wierzchołka źródła
    private int destinationId;                             // id wierzchołka docelowego
    private String name;

    public Edge(int sourceId, int destinationId) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.name = "";
    }

    public Edge(int sourceId, int destinationId, String name) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Edge: " + this.getKey();
    }
}