package com.petri.nets.algorithms;

import com.petri.nets.helpers.common.CommonOperations;
import com.petri.nets.model.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CoverageGraph {

    private static final int VERTICES_LIMIT = 50;
    private CustomGraph coverageGraph;
    private Map<String, Vertex> states;
    private Map<String, Edge> edges;
    private Map<Integer, Transition> transitions;
    private Map<Integer, Place> places;

    public CoverageGraph(CustomGraph baseGraph) {
        this.coverageGraph = new CustomGraph();
        this.edges = baseGraph.getEdges();
        this.transitions = baseGraph.getTransitions();
        this.places = baseGraph.getPlaces();
        this.states = new TreeMap<>();
    }

    public CustomGraph buildCoverageGraph() {
        Map<Integer, Integer> initialState = getInitialState();
        Vertex initialVertex = new Transition(coverageGraph.getNewID(), getTextValueWithInfinitySign(initialState));
        coverageGraph.addVertex(initialVertex);
        states.put(getTextValue(initialState), initialVertex);
        resolveTransitions(initialState);
        return coverageGraph;
    }

    private Map<Integer, Integer> getInitialState() {
        Map<Integer, Integer> initialState = new TreeMap<>();
        for (Place place : places.values()) {
            initialState.put(place.getID(), place.getTokenCount());
        }
        return initialState;
    }

    private String getTextValueWithInfinitySign(Map<Integer, Integer> idToTokenMap) {
        StringBuilder stateBuilder = new StringBuilder();
        for (Integer token : idToTokenMap.values()) {
            if (!token.equals(Integer.MAX_VALUE)) {
                stateBuilder.append(token);
            } else {
                stateBuilder.append("\u221E");
            }
            stateBuilder.append(",");
        }
        stateBuilder.deleteCharAt(stateBuilder.length() - 1);
        return stateBuilder.toString();
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
        List<Edge> edgesContainingTransition = CommonOperations.getEdgesContaining(transition, edges);                      // Pobranie krawędzi zawierających przejście
        List<Edge> destinationEdges = CommonOperations.getDestinationEdges(transition, edgesContainingTransition);   // Krawędzie prowadzące do przejścia
        if (hasAllInfinitives(previousState) || !canStepBeDone(destinationEdges, previousState)) {
            return;
        }
        List<Edge> sourceEdges = CommonOperations.getSourceEdges(transition, edgesContainingTransition);             // Krawędzie prowadzące od przejścia
        Map<Integer, Integer> newState = resolveState(previousState, sourceEdges, destinationEdges);
        String newStateTextValue = getTextValue(newState);
        Vertex newStateVertex = states.get(newStateTextValue);                                      // Pobranie z mapy stanów archiwalnych odpowiedniego wierzchołka (o ile istnieje)
        String previousStateTextValue = getTextValue(previousState);                                // Wyznaczenie reprezentacji dla stanu poprzedniego
        Vertex previousStateVertex = states.get(previousStateTextValue);                            // Pobranie wierzchołka z mapy stanów archiwalnych
        if (newStateVertex != null) {   // Sprawdzenie czy duplikat
            coverageGraph.addEdge(new Edge(previousStateVertex.getID(), newStateVertex.getID(), transition.getName())); // połączenie z 'duplikatem'
            return;
        }
        Map<Integer, Integer> stateWithInfinity = checkIfGreater(newState);
        if (stateWithInfinity != null) {
            String stateWithInfinityTextValue = getTextValue(stateWithInfinity);
            newStateVertex = new Transition(coverageGraph.getNewID(), getTextValueWithInfinitySign(stateWithInfinity)); // wierzchołek z nieskończonością
            coverageGraph.addVertex(newStateVertex);
            coverageGraph.addEdge(new Edge(previousStateVertex.getID(), newStateVertex.getID(), transition.getName()));
            states.put(stateWithInfinityTextValue, newStateVertex);
            resolveTransitions(stateWithInfinity);
            return;
        }
        newStateVertex = new Transition(coverageGraph.getNewID(), getTextValueWithInfinitySign(newState));
        coverageGraph.addVertex(newStateVertex);
        coverageGraph.addEdge(new Edge(previousStateVertex.getID(), newStateVertex.getID(), transition.getName()));
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
                if (token.getValue().equals(Integer.valueOf(tokens[i]))) {
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
}
