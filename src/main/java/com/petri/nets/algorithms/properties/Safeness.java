package com.petri.nets.algorithms.properties;

import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Vertex;

public class Safeness {
    CustomGraph coverageGraph;

    public Safeness(CustomGraph coverageGraph) {
        this.coverageGraph = coverageGraph;
    }

    public String calculate() {
        for (Vertex vertex : coverageGraph.getTransitions().values()) {
            String[] states = vertex.getName().split(",");
            for (String state : states) {
                if (state.equals("\u221E") || Integer.valueOf(state) > 1) {
                    return "Sieć nie jest bezpieczna";
                }
            }
        }
        return "Sieć jest bezpieczna";
    }
}
