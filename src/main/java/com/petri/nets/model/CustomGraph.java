package com.petri.nets.model;

import com.petri.nets.helpers.VertexType;
import com.petri.nets.helpers.generators.id.BasedOnNextIntIdGenerator;
import com.petri.nets.helpers.generators.id.UniqueIdGenerator;
import com.petri.nets.helpers.generators.name.BasedOnClassDistinctionNameGenerator;
import com.petri.nets.helpers.generators.name.UniqueNameGenerator;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Mateusz
 */
public class CustomGraph implements Serializable {

    private Map<Integer, Vertex> vertices = new HashMap<>();
    private Map<String, Edge> edges = new HashMap<>();
    private UniqueIdGenerator uniqueIdGenerator = new BasedOnNextIntIdGenerator();                                    // unikatowe id dla wierzchołków
    private UniqueNameGenerator uniqueNameGenerator = new BasedOnClassDistinctionNameGenerator();

    /**
     * Zwraca wszystkie wierzcholki nalezące do grafu
     *
     * @return
     */
    public Map<Integer, Vertex> getVertices() {
        return vertices;
    }

    /**
     * Zwraca wszystkie krawędzie należące do grafu
     *
     * @return
     */
    public Map<String, Edge> getEdges() {
        return edges;
    }

    /**
     * Pobranie wierzchołka o podanym id
     *
     * @param id
     * @return
     */
    public Vertex getVertex(int id) {
        return vertices.get(id);
    }

    /**
     * Pobranie krawedzi o podanym kluczu
     *
     * @param key
     * @return
     */
    public Edge getEdge(String key) {
        return edges.get(key);
    }

    /**
     * Dodawanie krawędzi do grafu
     *
     * @param edge
     */
    public void addEdge(Edge edge) {

        Vertex sourceVertex = vertices.get(edge.getSourceId());
        sourceVertex.addSuccessor(edge.getDestinationId());
        Vertex destinationVertex = vertices.get(edge.getDestinationId());
        destinationVertex.addPredecessor(edge.getSourceId());

        edges.put(edge.getKey(), edge);
    }

    /**
     * Dodawanie wierzchołka do grafu
     *
     * @param vertex
     */
    public void addVertex(Vertex vertex) {
        vertices.put(vertex.getID(), vertex);
    }

    /**
     * Usuwanie krawędzi z grafu
     *
     * @param edge
     */
    public void removeEdge(Edge edge) {
        if (edge == null) {
            System.out.println("Krawędź została wcześniej usunięta");
            return;
        }
        Vertex sourceVertex = vertices.get(edge.getSourceId());
        Vertex destinationVertex = vertices.get(edge.getDestinationId());
        if (sourceVertex != null && destinationVertex != null) {
            sourceVertex.removeSuccessor(edge.getDestinationId());
            destinationVertex.removePredecessor(edge.getSourceId());
        }
        edges.remove(edge.getKey());
    }

    /**
     * Usuwanie wierzchołka z grafu
     *
     * @param vertex
     */
    public void removeVertex(Vertex vertex) {
        Edge tempEdge;
        Vertex temp = vertices.get(vertex.getID());

        // usuwanie wszystkich krawedzi dolączonych do wierzcholka
        List<Integer> tempSuccessors = new ArrayList<>();
        tempSuccessors.addAll(temp.getSuccessors());
        for (int successor : tempSuccessors) {                      // usuwanie krawedzi wychodzacych 
            System.out.println("usuwanie krawedzi wychodzacej: ");
            tempEdge = new Edge(temp.getID(), successor);
            removeEdge(tempEdge);
        }

        /*for(Vertex v : vertices.values()){                              // usuwanie krawędzi wchodzacych do wierzcholka
         tempEdge = new Edge(v.getID(),temp.getID());
         edges.remove(tempEdge.getKey());
         }*/
        List<Integer> tempPredecessors = new ArrayList<>();
        tempPredecessors.addAll(temp.getPredecessors());
        for (int predecessor : tempPredecessors) {
            System.out.println("Usuwanie krawedzi wchodzacej: ");
            tempEdge = new Edge(predecessor, temp.getID());
            removeEdge(tempEdge);
        }

        vertices.remove(vertex.getID());                                // usunięcie wierzchołka.
    }

    /**
     * Generowanie unikalnego ID dla wierzchołka- po prostu kolejna liczba
     *
     * @return
     */
    public int getNewID() {
        return uniqueIdGenerator.getNext();
    }

    /**
     * Generowanie unikalnej nazwy dla wierzchołka w zależności od jego typu
     *
     * @return
     */
    public String getNewName(VertexType vertexType) {
        return uniqueNameGenerator.getNext(vertexType);
    }

    public Map<Integer, Place> getPlaces() {
        Map<Integer, Place> places = new TreeMap<>();
        for (Vertex vertex : vertices.values()) {
            if (vertex instanceof Place) {
                places.put(vertex.getID(), (Place) vertex);
            }
        }
        return places;
    }

    public Map<Integer, Transition> getTransitions() {
        Map<Integer, Transition> transition = new TreeMap<>();
        for (Vertex vertex : vertices.values()) {
            if (vertex instanceof Transition) {
                transition.put(vertex.getID(), (Transition) vertex);
            }
        }
        return transition;
    }

    public static void main(String[] args) {
        CustomGraph graph = new CustomGraph();

        System.out.println(graph.getVertices());
        System.out.println("koniec wierzcholkow");
        System.out.println(graph.getEdges());
    }
}
