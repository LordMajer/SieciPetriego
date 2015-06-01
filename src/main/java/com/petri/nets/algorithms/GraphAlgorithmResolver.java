package com.petri.nets.algorithms;

import com.petri.nets.helpers.common.CommonOperations;
import com.petri.nets.model.*;

import java.util.*;

public abstract class GraphAlgorithmResolver {

    protected static final int VERTICES_LIMIT = 100;
    protected CustomGraph graph;
    protected Map<String, Vertex> states;
    protected Map<String, Edge> edges;
    protected Map<Integer, Transition> transitions;
    protected Map<Integer, Place> places;
    protected LinkedList<Map<Integer, Integer>> toResolve = new LinkedList<>();
    private boolean priority;

    public GraphAlgorithmResolver(CustomGraph baseGraph) {
        this.graph = new CustomGraph();
        this.edges = baseGraph.getEdges();
        this.transitions = baseGraph.getTransitions();
        this.places = baseGraph.getPlaces();
        this.priority = baseGraph.isPriority();
        this.states = new TreeMap<>();
    }

    public CustomGraph build() {
        Map<Integer, Integer> initialState = getInitialState();
        Vertex initialVertex = new Transition(graph.getNewID(), getTextValue(initialState));
        graph.addVertex(initialVertex);
        states.put(getTextValue(initialState), initialVertex);
        toResolve.addLast(initialState);
        while (!toResolve.isEmpty() && shouldContinueProcessing(states.size())) {
            resolveTransitions(toResolve.getFirst());
        }
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

    abstract protected boolean shouldContinueProcessing(int statesSize);

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
        List<Transition> possibleSteps = getPossibleSteps(state);
        if (priority) { // Jeżeli to sieć priorytetowa to można przejść tylko przed wierzchołki o max priorytecie
            possibleSteps = CommonOperations.getMaxPriorityTransitions(possibleSteps);
        }
        for (Transition transition : possibleSteps) { // Wykonanie możliwych kroków
            resolveTransition(new TreeMap<>(state), transition);
        }
        toResolve.removeFirst();
    }

    private List<Transition> getPossibleSteps(Map<Integer, Integer> state) {
        List<Transition> possibleSteps = new ArrayList<>();
        for (Transition transition : transitions.values()) { // Wyszukiwanie możliwych przejść
            List<Edge> edgesContainingTransition = CommonOperations.getEdgesContaining(transition, edges);                      // Pobranie krawędzi zawierających przejście
            List<Edge> destinationEdges = CommonOperations.getDestinationEdges(transition, edgesContainingTransition);   // Krawędzie prowadzące do przejścia
            if (!shouldStopProcessingState(state, destinationEdges)) {
                possibleSteps.add(transition);
            }
        }
        return possibleSteps;
    }

    protected void resolveTransition(Map<Integer, Integer> previousState, Transition transition) {
        List<Edge> edgesContainingTransition = CommonOperations.getEdgesContaining(transition, edges);                      // Pobranie krawędzi zawierających przejście
        List<Edge> destinationEdges = CommonOperations.getDestinationEdges(transition, edgesContainingTransition);   // Krawędzie prowadzące do przejścia
        List<Edge> sourceEdges = CommonOperations.getSourceEdges(transition, edgesContainingTransition);             // Krawędzie prowadzące od przejścia
        Map<Integer, Integer> newState = resolveState(previousState, sourceEdges, destinationEdges);
        String newStateTextValue = getTextValue(newState);
        Vertex newStateVertex = states.get(newStateTextValue);                                      // Pobranie z mapy stanów archiwalnych odpowiedniego wierzchołka (o ile istnieje)
        String previousStateTextValue = getTextValue(previousState);                                // Wyznaczenie reprezentacji dla stanu poprzedniego
        Vertex previousStateVertex = states.get(previousStateTextValue);                            // Pobranie wierzchołka z mapy stanów archiwalnych
        if (newStateVertex != null) {   // Sprawdzenie czy duplikat
            dealWithDuplicate(newState, newStateVertex, previousStateVertex, newStateVertex, transition);
            return;
        }
        dealWithNonDuplicate(newState, previousStateVertex, transition);
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

    protected abstract boolean shouldUpdateTokenNumber(int value);

    protected abstract boolean shouldStopProcessingState(Map<Integer, Integer> previousState, List<Edge> destinationEdges);

    protected abstract void dealWithDuplicate(Map<Integer, Integer> newState, Vertex newStateVertex, Vertex previousStateVertex, Vertex duplicate, Vertex transition);

    protected abstract void dealWithNonDuplicate(Map<Integer, Integer> newState, Vertex previousStateVertex, Vertex transition);
}