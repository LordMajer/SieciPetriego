package com.petri.nets.algorithms.properties;

import com.petri.nets.algorithms.CoverageGraph;
import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Transition;
import com.petri.nets.model.Vertex;

public class NetLiveness {
    private static final String NET_LIVE_MESSAGE = "Sieć jest żywotna";
    private static final String NET_NOT_LIVE_MESSAGE = "Sieć nie jest żywotna";
    private CustomGraph baseGraph;

    public NetLiveness(CustomGraph baseGraph) {
        this.baseGraph = baseGraph;
    }

    public String calculate() {
        CustomGraph coverageGraph = new CoverageGraph(baseGraph).build();
        if (hasDeadStates(baseGraph) || hasNotLivedTransitions(coverageGraph)) {
            return NET_NOT_LIVE_MESSAGE;
        }
        return NET_LIVE_MESSAGE;
    }

    private boolean hasDeadStates(CustomGraph graph) {
        for (Vertex vertex: graph.getVertices().values()) {
            if (vertex.getSuccessors().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasNotLivedTransitions(CustomGraph graph) {
        for (Transition transition: baseGraph.getTransitions().values()) {
            if (!isLived(graph, transition)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLived(CustomGraph coverageGraph, Transition transition) {
        for (Edge edge : coverageGraph.getEdges().values()) {
            if (edge.getName().equals(transition.getName())) {
                return true;
            }
        }
        return false;
    }

}
