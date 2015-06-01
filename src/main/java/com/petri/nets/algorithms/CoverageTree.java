package com.petri.nets.algorithms;

import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Transition;
import com.petri.nets.model.Vertex;

import java.util.List;
import java.util.Map;

public class CoverageTree extends GraphAlgorithmResolver {

    public CoverageTree(CustomGraph baseGraph) {
        super(baseGraph);
    }

    @Override
    protected boolean shouldContinueProcessing(int statesSize) {
        return true;
    }

    @Override
    protected boolean shouldUpdateTokenNumber(int value) {
        return value != Integer.MAX_VALUE;
    }

    @Override
    protected boolean shouldStopProcessingState(Map<Integer, Integer> previousState, List<Edge> destinationEdges) {
        return hasAllInfinitives(previousState) || !canStepBeDone(destinationEdges, previousState);
    }

    @Override
    protected void dealWithDuplicate(Map<Integer, Integer> newState, Vertex newStateVertex, Vertex previousStateVertex, Vertex duplicate, Vertex transition) {
        duplicate = new Transition(graph.getNewID(), getTextValueWithInfinitySign(newState) + " - Duplikat"); // duplikat
        graph.addVertex(duplicate);
        graph.addEdge(new Edge(previousStateVertex.getID(), duplicate.getID(), transition.getName()));
    }

    @Override
    protected void dealWithNonDuplicate(Map<Integer, Integer> newState, Vertex previousStateVertex, Vertex transition) {
        Map<Integer, Integer> stateWithInfinity = checkIfGreater(newState);
        Vertex newStateVertex;
        if (stateWithInfinity != null) {
            String stateWithInfinityTextValue = getTextValue(stateWithInfinity);
            newStateVertex = new Transition(graph.getNewID(), getTextValueWithInfinitySign(stateWithInfinity)); // wierzchołek z nieskończonością
            graph.addVertex(newStateVertex);
            graph.addEdge(new Edge(previousStateVertex.getID(), newStateVertex.getID(), transition.getName()));
            states.put(stateWithInfinityTextValue, newStateVertex);
            toResolve.addLast(stateWithInfinity);
            return;
        }
        newStateVertex = new Transition(graph.getNewID(), getTextValueWithInfinitySign(newState));
        graph.addVertex(newStateVertex);
        graph.addEdge(new Edge(previousStateVertex.getID(), newStateVertex.getID(), transition.getName()));
        states.put(getTextValue(newState), newStateVertex);
        toResolve.addLast(newState);
    }
}
