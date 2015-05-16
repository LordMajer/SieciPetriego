package com.petri.nets.algorithms;

import com.petri.nets.model.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class GraphAlgorithmResolver {

    protected static final int VERTICES_LIMIT = 50;
    protected CustomGraph graph;
    protected Map<String, Vertex> states;
    protected Map<String, Edge> edges;
    protected Map<Integer, Transition> transitions;
    protected Map<Integer, Place> places;

    public GraphAlgorithmResolver(CustomGraph baseGraph) {
        this.graph = new CustomGraph();
        this.edges = baseGraph.getEdges();
        this.transitions = baseGraph.getTransitions();
        this.places = baseGraph.getPlaces();
        this.states = new TreeMap<>();
    }

    public CustomGraph build() {
        Map<Integer, Integer> initialState = getInitialState();
        Vertex initialVertex = new Transition(graph.getNewID(), getTextValue(initialState));
        graph.addVertex(initialVertex);
        states.put(getTextValue(initialState), initialVertex);
        resolveTransitions(initialState);
        return graph;
    }

    private Map<Integer, Integer> getInitialState() {
        Map<Integer, Integer> initialState = new TreeMap<>();
        for (Place place : places.values()) {
            initialState.put(place.getID(), place.getTokenCount());
        }
        return initialState;
    }

    protected String getTextValue(Map<Integer, Integer> idToTokenMap) {
        StringBuilder stateBuilder = new StringBuilder();
        for (Integer token : idToTokenMap.values()) {
            stateBuilder.append(token)
                    .append(",");
        }
        stateBuilder.deleteCharAt(stateBuilder.length() - 1);
        return stateBuilder.toString();
    }

    protected String getTextValueWithInfinitySign(Map<Integer, Integer> idToTokenMap) {
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

    protected void resolveTransitions(Map<Integer, Integer> state) {
        for (Transition transition : transitions.values()) {
            resolveTransition(new TreeMap<>(state), transition);
            if (states.size() > VERTICES_LIMIT) { // Aby uniknąć pętli nieskończonej
                return;
            }
        }
    }

    protected Map<Integer, Integer> resolveState(Map<Integer, Integer> previousState, List<Edge> sourceEdges, List<Edge> destinationEdges) {
        Map<Integer, Integer> newState = new TreeMap<>(previousState);
        for (Edge edge : destinationEdges) {
            int id = edge.getSourceId();
            int tokenToTake = edge.getCapacity();
            int currentValue = previousState.get(id);
            if (shouldUpdateTokenNumber(currentValue)) {
                newState.put(id, currentValue - tokenToTake);
            }
        }
        for (Edge edge : sourceEdges) {
            int id = edge.getDestinationId();
            int tokenToTake = edge.getCapacity();
            int currentValue = previousState.get(id);
            if (shouldUpdateTokenNumber(currentValue)) {
                newState.put(id, currentValue + tokenToTake);
            }
        }
        return newState;
    }

    protected boolean canStepBeDone(List<Edge> destinationEdges, Map<Integer, Integer> idToTokenMap) {
        for (Edge edge : destinationEdges) {                                                            // Sprawdzenie czy każde miejsce połączone z przejściem posiada odpowiednią ilość tokenów
            if (edge.getCapacity() > idToTokenMap.get(edge.getSourceId())) {
                return false;
            }
        }
        return true;
    }

    protected Map<Integer, Integer> checkIfGreater(Map<Integer, Integer> currentState) {
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

    protected boolean hasAllInfinitives(Map<Integer, Integer> idToTokenMap) {
        for (Integer tokenCount : idToTokenMap.values()) {
            if (tokenCount != Integer.MAX_VALUE) {
                return false;
            }
        }
        return true;
    }

    protected abstract void resolveTransition(Map<Integer, Integer> previousState, Transition transition);

    protected abstract boolean shouldUpdateTokenNumber(int value);
}
