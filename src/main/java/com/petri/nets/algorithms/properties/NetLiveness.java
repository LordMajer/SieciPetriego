package com.petri.nets.algorithms.properties;

import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Transition;

public class NetLiveness {
    CustomGraph coverageGraph;

    public NetLiveness(CustomGraph coverageGraph) {
        this.coverageGraph = coverageGraph;
    }

    public String calculate() {
        for (Transition transition: coverageGraph.getTransitions().values()) {
            if (!checkLiveness(transition)) {
                return "Sieć nie jest żywotna";
            }
        }
        return "Sieć jest żywotna";
    }

    private boolean checkLiveness(Transition transition) {
        for (Edge edge : coverageGraph.getEdges().values()) {
            if (edge.getName().equals(transition.getName())) {
                return true;
            }
        }
        return false;
    }

}
