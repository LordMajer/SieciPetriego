package com.petri.nets.algorithms;

import com.petri.nets.helpers.common.CommonOperations;
import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Transition;
import com.petri.nets.model.Vertex;

import java.util.List;
import java.util.Map;

public class CoverageGraph extends GraphAlgorithmResolver{

    public CoverageGraph(CustomGraph baseGraph) {
        super(baseGraph);
    }

    @Override
    protected void resolveTransition(Map<Integer, Integer> previousState, Transition transition) {
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
            graph.addEdge(new Edge(previousStateVertex.getID(), newStateVertex.getID(), transition.getName())); // połączenie z 'duplikatem'
            return;
        }
        Map<Integer, Integer> stateWithInfinity = checkIfGreater(newState);
        if (stateWithInfinity != null) {
            String stateWithInfinityTextValue = getTextValue(stateWithInfinity);
            newStateVertex = new Transition(graph.getNewID(), getTextValueWithInfinitySign(stateWithInfinity)); // wierzchołek z nieskończonością
            graph.addVertex(newStateVertex);
            graph.addEdge(new Edge(previousStateVertex.getID(), newStateVertex.getID(), transition.getName()));
            states.put(stateWithInfinityTextValue, newStateVertex);
            resolveTransitions(stateWithInfinity);
            return;
        }
        newStateVertex = new Transition(graph.getNewID(), getTextValueWithInfinitySign(newState));
        graph.addVertex(newStateVertex);
        graph.addEdge(new Edge(previousStateVertex.getID(), newStateVertex.getID(), transition.getName()));
        states.put(newStateTextValue, newStateVertex);
        resolveTransitions(newState);
    }

    @Override
    protected boolean shouldUpdateTokenNumber(int value) {
        return value != Integer.MAX_VALUE;
    }
}
