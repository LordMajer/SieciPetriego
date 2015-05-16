package com.petri.nets.algorithms;

import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Transition;
import com.petri.nets.model.Vertex;

import java.util.List;
import java.util.Map;

public class ReachabilityGraph extends GraphAlgorithmResolver{

    public ReachabilityGraph(CustomGraph baseGraph) {
        super(baseGraph);
    }

    @Override
    protected boolean shouldUpdateTokenNumber(int value) {
        return true;
    }

    @Override
    protected boolean shouldStopProcessingState(Map<Integer, Integer> previousState, List<Edge> destinationEdges) {
        return !canStepBeDone(destinationEdges, previousState);
    }

    @Override
    protected void dealWithDuplicate(Map<Integer, Integer> newState, Vertex newStateVertex, Vertex previousStateVertex, Vertex duplicate, Vertex transition) {
        graph.addEdge(new Edge(previousStateVertex.getID(), newStateVertex.getID(), transition.getName()));
    }

    @Override
    protected void dealWithNonDuplicate(Map<Integer, Integer> newState, Vertex previousStateVertex, Vertex transition) {
        Vertex newStateVertex = new Transition(graph.getNewID(), getTextValueWithInfinitySign(newState));
        graph.addVertex(newStateVertex);
        graph.addEdge(new Edge(previousStateVertex.getID(), newStateVertex.getID(), transition.getName()));
        states.put(getTextValue(newState), newStateVertex);
        resolveTransitions(newState);
    }
}
