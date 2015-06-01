package com.petri.nets.algorithms.properties;

import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Vertex;

public class Reversibility {
    private CustomGraph coverageGraph;
    private boolean isReversible = true;
    private Vertex initialVertex = null;

    public Reversibility(CustomGraph coverageGraph) {
        this.coverageGraph = coverageGraph;
    }

    public String calculate() {
        initialVertex = coverageGraph.getVertex(0);
        checkPaths(initialVertex, "");
        return isReversible ? "Sieć jest odwracalna" : "Sieć nie jest odwracalna";
    }

    private void checkPaths(Vertex vertex, String path) {
        if (!shouldBeStopped(vertex, path)) {
            for (Integer successorId : vertex.getSuccessors()) {
                Vertex successor = coverageGraph.getVertex(successorId);
                checkPaths(successor, path + ";" + vertex.getID() + ";");
            }
        }
    }

    private boolean shouldBeStopped(Vertex vertex, String path) {
        if (vertex.equals(initialVertex) && !path.isEmpty()) {
            return true;
        } else if (path.contains(";" + vertex.getID() + ";") || vertex.getSuccessors().isEmpty()) {
            isReversible = false;
            return true;
        }
        return false;
    }
}
