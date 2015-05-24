package com.petri.nets.algorithms.properties;

import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Vertex;

public class Conservation {
    CustomGraph coverageGraph;

    public Conservation(CustomGraph coverageGraph) {
        this.coverageGraph = coverageGraph;
    }

    public String calculate() {
        int sum = 0;
        int i = 0;
        for (Vertex vertex : coverageGraph.getTransitions().values()) {
            String[] states = vertex.getName().split(",");
            int currentSum = 0;
            for (String state : states) {
                if (state.equals("\u221E")) {
                    return "Sieć nie jest zachowawcza";
                } else {
                    currentSum += Integer.valueOf(state);
                }
            }
            if (i++ == 0) {
                sum = currentSum;
            } else {
                if (currentSum != sum) {
                    return "Sieć nie jest zachowawcza";
                }
            }
        }
        return "Sieć jest zachowawcza";
    }
}
