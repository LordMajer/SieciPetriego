package com.petri.nets.algorithms.properties;

import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Vertex;

public class Boundedness {
    CustomGraph coverageGraph;

    public Boundedness(CustomGraph coverageGraph) {
        this.coverageGraph = coverageGraph;
    }

    public String calculate() {
        int bound = 0;
        for (Vertex vertex : coverageGraph.getTransitions().values()) {
            String[] states = vertex.getName().split(",");
            for (String state : states) {
                if (state.equals("\u221E")) {
                    return "Sieć jest nieograniczona";
                } else {
                    int currentValue = Integer.valueOf(state);
                    if (currentValue > bound) {
                        bound = currentValue;
                    }
                }
            }
        }
        return "Sieć jest " + bound + "-ograniczona";
    }
}
