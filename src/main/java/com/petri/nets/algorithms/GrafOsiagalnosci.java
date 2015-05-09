package com.petri.nets.algorithms;

import com.petri.nets.model.*;

import java.util.*;

public class GrafOsiagalnosci {
    private static final int ITERATION_LIMIT = 50;
    private CustomGraph grafOsiagalnosci;
    private CustomGraph baseGraph;
    private Map<String, Vertex> states;
    private Map<String, Edge> edges;
    private Map<Integer, Transition> transitions;
    private Map<Integer, Place> places;

    public GrafOsiagalnosci(CustomGraph baseGraph) {
        this.grafOsiagalnosci = new CustomGraph();
        this.baseGraph = baseGraph;
        this.edges = baseGraph.getEdges();
        this.transitions = baseGraph.getTransitions();
        this.places = baseGraph.getPlaces();
        this.states = new TreeMap<>();
    }

    public CustomGraph buildGrafOsiagalnosci() {
        Map<Integer, Integer> initialState = getInitialState(baseGraph);
        Vertex initialVertex = new Transition(grafOsiagalnosci.getNewID(), getState(initialState));
        grafOsiagalnosci.addVertex(initialVertex);
        states.put(getState(initialState), initialVertex);
        resolveTransitions(initialState);
        return grafOsiagalnosci;
    }

    private Map<Integer, Integer> getInitialState(CustomGraph graph) {
        Map<Integer, Integer> initialState = new TreeMap<>();
        for (Place place : places.values()) {
            initialState.put(place.getID(), place.getTokenCount());
        }
        return initialState;
    }

    private void resolveTransitions(Map<Integer, Integer> state) {
        for (Transition transition : transitions.values()) {
            // TODO check if this really work
            resolveTransition(new TreeMap<>(state), transition);
            if (states.size() > ITERATION_LIMIT) {
                return;
            }
        }
    }

    private void resolveTransition(Map<Integer, Integer> previousState, Transition transition) {
        List<Edge> edgesContainingTransition = getEdgesContaining(transition);
        List<Edge> destinationEdges = getDestinationEdges(transition, edgesContainingTransition);
        if (!canStepBeDone(destinationEdges, previousState)) {
            return;
        }
        List<Edge> sourceEdges = getSourceEdges(transition, edgesContainingTransition);
        Map<Integer, Integer> newState = resolveState(previousState, sourceEdges, destinationEdges);
        String state = getState(newState);
        Vertex vertexContainingState = states.get(state);
        String previousStateString = getState(previousState);
        Vertex previousStateVertex = states.get(previousStateString);
        if (vertexContainingState == null) {
            Vertex newStateVertex = new Transition(grafOsiagalnosci.getNewID(), state);
            grafOsiagalnosci.addVertex(newStateVertex);
            grafOsiagalnosci.addEdge(new Edge(previousStateVertex.getID(), newStateVertex.getID()));
            states.put(state, newStateVertex);
            resolveTransitions(newState);
        } else {
            grafOsiagalnosci.addEdge(new Edge(previousStateVertex.getID(), vertexContainingState.getID()));
        }
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
        for (Edge edge : destinationEdges) {
            if (edge.getCapacity() > idToTokenMap.get(edge.getSourceId())) {
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
            newState.put(id, currentValue - tokenToTake);
        }
        for (Edge edge : sourceEdges) {
            int id = edge.getDestinationId();
            int tokenToTake = edge.getCapacity();
            int currentValue = previousState.get(id);
            newState.put(id, currentValue + tokenToTake);
        }
        return newState;
    }

    private String getState(Map<Integer, Integer> idToTokenMap) {
        StringBuilder stateBuilder = new StringBuilder();
        for (Integer token : idToTokenMap.values()) {
            stateBuilder.append(token)
                    .append(",");
        }
        stateBuilder.deleteCharAt(stateBuilder.length() - 1);
        return stateBuilder.toString();
    }
}
