package com.petri.nets.model;

import com.petri.nets.helpers.VertexType;
import com.petri.nets.helpers.generators.name.BasedOnClassDistinctionNameGenerator;
import com.petri.nets.helpers.generators.id.BasedOnNextIntIdGenerator;
import com.petri.nets.helpers.generators.id.UniqueIdGenerator;
import com.petri.nets.helpers.generators.name.UniqueNameGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Mateusz
 */
public class CustomGraph {

    private Map<Integer, Vertex> vertices = new HashMap<>();
    private Map<String, Edge> edges = new HashMap<>();
    private UniqueIdGenerator uniqueIdGenerator = new BasedOnNextIntIdGenerator();                                    // unikatowe id dla wierzchołków
    private UniqueNameGenerator uniqueNameGenerator = new BasedOnClassDistinctionNameGenerator();

    public CustomGraph() {
        initialize();
    }

    /**
     * Metoda inicjalizująca- dodaje jedno miejsce jedno przeście i dwie
     * krawędzie
     */
    public void initialize() {
        Place place = new Place(getNewID(), getNewName(Place.getVertexType()), new Position(100, 100));
        Transition transition = new Transition(getNewID(), getNewName(Transition.getVertexType()), new Position(100, 150));
        Edge edge = new Edge(place.getID(), transition.getID());

        vertices.put(place.getID(), place);
        vertices.put(transition.getID(), transition);
        addEdge(edge);
    }

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
        Vertex sourceVertex = vertices.get(edge.getSourceId());
        sourceVertex.removeSuccessor(edge.getDestinationId());
        Vertex destinationVertex = vertices.get(edge.getDestinationId());
        destinationVertex.removePredecessor(edge.getSourceId());
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

    /**
     * Sprawdzenie czy wierzchołki są polaczone poprawnie.
     *
     * @return
     */
    public String validateModel() {
        StringBuilder errorsBuilder = new StringBuilder();
        Vertex vertexToCheck;
        // przejście po wszystkich wierzchołkach i sprawdzenie czy są poprawnie połaczone.

        for (Vertex vertex : vertices.values()) {

            if (vertex instanceof Place) {

                // następnikami i poprzednikami moga być tylko przejścia
                // sprawdzanie następników:
                for (int successor : vertex.getSuccessors()) {
                    vertexToCheck = vertices.get(successor);
                    if (!(vertexToCheck instanceof Transition)) {
                        errorsBuilder.append("Wierzchołek typu miejsce: ")
                                .append(vertex)
                                .append(" posiada nielegalne połaczenie z wierzchołkiem: ")
                                .append(vertexToCheck)
                                .append("\n");
                    }
                }

                // sprawdzanie poprzedników:
                for (int predecessor : vertex.getPredecessors()) {
                    vertexToCheck = vertices.get(predecessor);
                    if (!(vertexToCheck instanceof Transition)) {
                        errorsBuilder.append("Wierzchołek typu miejsce: ")
                                .append(vertex)
                                .append(" posiada nielegalne połaczenie z wierzchołkiem: ")
                                .append(vertexToCheck)
                                .append("\n");
                    }
                }
            } else {
                // następnikami i poprzednikami moga być tylko miejsca.
                //sprawdzanie następników
                for (int successor : vertex.getSuccessors()) {
                    vertexToCheck = vertices.get(successor);
                    if (!(vertexToCheck instanceof Place)) {
                        errorsBuilder.append("Wierzchołek typu miejsce: ")
                                .append(vertex)
                                .append(" posiada nielegalne połaczenie z wierzchołkiem: ")
                                .append(vertexToCheck)
                                .append("\n");
                    }
                }

                // sprawdzanie poprzedników:
                for (int predecessor : vertex.getPredecessors()) {
                    vertexToCheck = vertices.get(predecessor);
                    if (!(vertexToCheck instanceof Place)) {
                        errorsBuilder.append("Wierzchołek typu miejsce: ")
                                .append(vertex)
                                .append(" posiada nielegalne połaczenie z wierzchołkiem: ")
                                .append(vertexToCheck)
                                .append("\n");
                    }
                }
            }
        }

        if (errorsBuilder.length() > 0) {
            return errorsBuilder.toString();
        } else {
            return null;
        }
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

    public int[][] inputMatrix() {

        Map<Integer, Place> places;            // treemap zachowuje kolejnosc kluczy
        Map<Integer, Transition> transitions;
        int placeCounter = 0;
        int transitionCounter = 0;
        int[][] tab = new int[getPlaces().size()][getTransitions().size()];
        List<Integer> idList;
        Edge edge;

        places = getPlaces();
        transitions = getTransitions();

        for (Place miejsce : places.values()) {
            System.out.println(miejsce);
            for (Transition przejscie : transitions.values()) {
                System.out.println(przejscie);
                idList = przejscie.getSuccessors();
                for (int i = 0; i < przejscie.getSuccessors().size(); i++) {
                    if (idList.get(i) == miejsce.getID()) {
                        edge = edges.get(new Edge(przejscie.getID(), miejsce.getID()).getKey());
                        tab[placeCounter][transitionCounter] = edge.getCapacity();
                    }
                }
                System.out.println("licznik przejść: " + transitionCounter);
                transitionCounter++;
            }
            transitionCounter = 0;
            System.out.println("licznik miejsc: " + transitionCounter);
            placeCounter++;
        }

        return tab;
    }

    public int[][] outputMatrix() {
        Map<Integer, Place> places;            // treemap zachowuje kolejnosc kluczy
        Map<Integer, Transition> transitions;
        int placeCounter = 0;
        int transitionCounter = 0;
        int[][] tab = new int[getPlaces().size()][getTransitions().size()];
        List<Integer> idList;
        Edge edge;

        places = getPlaces();
        transitions = getTransitions();

        for (Place place : places.values()) {
            System.out.println(place);
            for (Transition transition : transitions.values()) {
                System.out.println(transition);
                idList = transition.getPredecessors();
                for (int i = 0; i < transition.getPredecessors().size(); i++) {
                    if (idList.get(i) == place.getID()) {
                        edge = edges.get(new Edge(place.getID(), transition.getID()).getKey());
                        tab[placeCounter][transitionCounter] = edge.getCapacity();
                    }
                }
                System.out.println("licznik przejść: " + transitionCounter);
                transitionCounter++;
            }
            transitionCounter = 0;
            System.out.println("licznik miejsc: " + transitionCounter);
            placeCounter++;
        }

        return tab;
    }

    public int[][] incidenceMatrix() {

        int[][] inputMatrix = inputMatrix();
        int[][] outputMatrix = outputMatrix();

        int[][] incidenceMatrix = new int[inputMatrix.length][inputMatrix[0].length];

        for (int i = 0; i < inputMatrix.length; i++) {
            for (int j = 0; j < inputMatrix[i].length; j++) {
                incidenceMatrix[i][j] = inputMatrix[i][j] - outputMatrix[i][j];
            }
        }

        return incidenceMatrix;
    }

    public Object[] getPlacesIDs() {
        Map<Integer, Place> places = getPlaces();
        Object[] ids = new Object[places.size()];
        int i = 0;
        for (Place place : places.values()) {
            ids[i] = place.getName();
            i++;
        }

        return ids;
    }

    public Object[] getTransitionsIDs() {
        Map<Integer, Transition> transitions = getTransitions();
        Object[] ids = new Object[transitions.size()];
        int i = 0;
        for (Transition transition : transitions.values()) {
            ids[i] = transition.getName();
            i++;
        }

        return ids;
    }

    public static void main(String[] args) {
        CustomGraph graph = new CustomGraph();

        System.out.println(graph.getVertices());
        System.out.println("koniec wierzcholkow");
        System.out.println(graph.getEdges());
    }
}
