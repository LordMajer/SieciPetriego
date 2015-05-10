package com.petri.nets.algorithms;

import com.petri.nets.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CoverageTree {

    private static final int VERTICES_LIMIT = 50;
    private CustomGraph coverageTree;
    private Map<String, Vertex> states;
    private Map<String, Edge> edges;
    private Map<Integer, Transition> transitions;
    private Map<Integer, Place> places;

    public CoverageTree(CustomGraph baseGraph) {
        this.coverageTree = new CustomGraph();
        this.edges = baseGraph.getEdges();
        this.transitions = baseGraph.getTransitions();
        this.places = baseGraph.getPlaces();
        this.states = new TreeMap<>();
    }

    public CustomGraph buildCoverageTree() {
        Map<Integer, Integer> initialState = getInitialState();
        Vertex initialVertex = new Transition(coverageTree.getNewID(), getTextValue(initialState));
        coverageTree.addVertex(initialVertex);
        states.put(getTextValue(initialState), initialVertex);
        resolveTransitions(initialState);
        return coverageTree;
    }

    private Map<Integer, Integer> getInitialState() {
        Map<Integer, Integer> initialState = new TreeMap<>();
        for (Place place : places.values()) {
            initialState.put(place.getID(), place.getTokenCount());
        }
        return initialState;
    }

    private String getTextValue(Map<Integer, Integer> idToTokenMap) {
        StringBuilder stateBuilder = new StringBuilder();
        for (Integer token : idToTokenMap.values()) {
            stateBuilder.append(token)
                    .append(",");
        }
        stateBuilder.deleteCharAt(stateBuilder.length() - 1);
        return stateBuilder.toString();
    }

    private void resolveTransitions(Map<Integer, Integer> state) {
        for (Transition transition : transitions.values()) {
            resolveTransition(new TreeMap<>(state), transition);
            if (states.size() > VERTICES_LIMIT) { // Aby uniknąć pętli nieskończonej
                return;
            }
        }
    }

    private void resolveTransition(Map<Integer, Integer> previousState, Transition transition) {
        List<Edge> edgesContainingTransition = getEdgesContaining(transition);                      // Pobranie krawędzi zawierających przejście
        List<Edge> destinationEdges = getDestinationEdges(transition, edgesContainingTransition);   // Krawędzie prowadzące do przejścia
        if (hasAllInfinitives(previousState) || !canStepBeDone(destinationEdges, previousState)) {
            return;
        }
        List<Edge> sourceEdges = getSourceEdges(transition, edgesContainingTransition);             // Krawędzie prowadzące od przejścia
        Map<Integer, Integer> newState = resolveState(previousState, sourceEdges, destinationEdges);
        String newStateTextValue = getTextValue(newState);
        Vertex newStateVertex = states.get(newStateTextValue);                                      // Pobranie z mapy stanów archiwalnych odpowiedniego wierzchołka (o ile istnieje)
        String previousStateTextValue = getTextValue(previousState);                                // Wyznaczenie reprezentacji dla stanu poprzedniego
        Vertex previousStateVertex = states.get(previousStateTextValue);                            // Pobranie wierzchołka z mapy stanów archiwalnych
        if (newStateVertex != null) {   // Sprawdzenie czy duplikat
            newStateVertex = new Transition(coverageTree.getNewID(), newStateTextValue + " - Duplikat", resolvePosition(newState)); // duplikat
            coverageTree.addVertex(newStateVertex);
            coverageTree.addEdge(new Edge(previousStateVertex.getID(), newStateVertex.getID(), transition.getName()));
            return;
        }
        Map<Integer, Integer> stateWithInfinity = checkIfGreater(newState);
        if (stateWithInfinity != null) {
            String stateWithInfinityTextValue = getTextValue(stateWithInfinity);
            newStateVertex = new Transition(coverageTree.getNewID(), stateWithInfinityTextValue, resolvePosition(stateWithInfinity)); // wierzchołek z nieskończonością
            coverageTree.addVertex(newStateVertex);
            coverageTree.addEdge(new Edge(previousStateVertex.getID(), newStateVertex.getID(), transition.getName()));
            states.put(stateWithInfinityTextValue, newStateVertex);
            resolveTransitions(stateWithInfinity);
            return;
        }
        newStateVertex = new Transition(coverageTree.getNewID(), newStateTextValue, resolvePosition(newState));
        coverageTree.addVertex(newStateVertex);
        coverageTree.addEdge(new Edge(previousStateVertex.getID(), newStateVertex.getID(), transition.getName()));
        states.put(newStateTextValue, newStateVertex);
        resolveTransitions(newState);
    }

    private Map<Integer, Integer> checkIfGreater(Map<Integer, Integer> currentState) {
        Map<Integer, Integer> newState = new TreeMap<>();
        String[] allStates = states.keySet().toArray(new String[states.size()]);
        int i;
        for (String state : allStates) {
            String[] tokens = state.split(",");
            i = 0;
            for (Map.Entry<Integer, Integer> token : currentState.entrySet()) {
                if (token.getValue() == Integer.valueOf(tokens[i])) {
                    newState.put(token.getKey(), token.getValue());
                } else if (token.getValue() > Integer.valueOf(tokens[i])) {
                    newState.put(token.getKey(), Integer.MAX_VALUE);
                } else {
                    break;
                }
                if (i == tokens.length - 1) {
                    return newState;
                }
                i++;
            }
        }
        return null;
    }

    private List<Edge> getEdgesContaining(Transition transition) {
        List<Edge> edgesContainingGivenTransition = new ArrayList<>();
        int id = transition.getID();
        for (Edge edge : edges.values()) {
            if (id == edge.getSourceId() || id == edge.getDestinationId()) {
                edgesContainingGivenTransition.add(edge);
            }
        }
        return edgesContainingGivenTransition;
    }

    private List<Edge> getDestinationEdges(Transition transition, List<Edge> edges) {
        int id = transition.getID();
        List<Edge> destinationEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getDestinationId() == id) {
                destinationEdges.add(edge);
            }
        }
        return destinationEdges;
    }

    private List<Edge> getSourceEdges(Transition transition, List<Edge> edges) {
        int id = transition.getID();
        List<Edge> sourceEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getSourceId() == id) {
                sourceEdges.add(edge);
            }
        }
        return sourceEdges;
    }

    private boolean canStepBeDone(List<Edge> destinationEdges, Map<Integer, Integer> idToTokenMap) {
        for (Edge edge : destinationEdges) {                                                            // Sprawdzenie czy każde miejsce połączone z przejściem posiada odpowiednią ilość tokenów
            if (edge.getCapacity() > idToTokenMap.get(edge.getSourceId())) {
                return false;
            }
        }
        return true;
    }

    private boolean hasAllInfinitives(Map<Integer, Integer> idToTokenMap) {
    for (Integer tokenCount : idToTokenMap.values()) {
        if (tokenCount != Integer.MAX_VALUE) {
            return false;
        }
    }
        return true;
    }

    private Map<Integer, Integer> resolveState(Map<Integer, Integer> previousState, List<Edge> sourceEdges, List<Edge> destinationEdges) {
        Map<Integer, Integer> newState = new TreeMap<>(previousState);
        for (Edge edge : destinationEdges) {
            int id = edge.getSourceId();
            int tokenToTake = edge.getCapacity();
            int currentValue = previousState.get(id);
            if (currentValue != Integer.MAX_VALUE) {
                newState.put(id, currentValue - tokenToTake);
            }
        }
        for (Edge edge : sourceEdges) {
            int id = edge.getDestinationId();
            int tokenToTake = edge.getCapacity();
            int currentValue = previousState.get(id);
            if (currentValue != Integer.MAX_VALUE) {
                newState.put(id, currentValue + tokenToTake);
            }
        }
        return newState;
    }

    private Position resolvePosition(Map<Integer, Integer> state) {
        int x = states.size()%2 == 0 ? 0 : 100;
        int y = states.size()*100;
        return new Position(x, y);
    }
}
