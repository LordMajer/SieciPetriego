package com.petri.nets.algorithms.properties;

import com.petri.nets.model.CustomGraph;

public class Reversibility {
    CustomGraph coverageGraph;

    public Reversibility(CustomGraph coverageGraph) {
        this.coverageGraph = coverageGraph;
    }

    public String calculate() {
        return "Sieć nie jest odwracalna";
    }
}
