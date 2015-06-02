package com.petri.nets.algorithms.properties;

import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Vertex;

import java.util.HashSet;
import java.util.Set;

public class Reversibility {
    private CustomGraph coverageGraph;
    private Vertex initialVertex = null;
    private Set<Integer> reversibleVertices = new HashSet<>();
    private Set<String> loopPaths = new HashSet<>(); // ścieżki z pętlą, które na etapie procesowania wierzchołka nie wiadomo czy będą odwracalne

    public Reversibility(CustomGraph coverageGraph) {
        this.coverageGraph = coverageGraph;
    }

    public String calculate() {
        initialVertex = coverageGraph.getVertex(0);
        reversibleVertices.add(initialVertex.getID());
        checkPaths(initialVertex, "");
        checkLoopPaths();
        return reversibleVertices.size() == coverageGraph.getTransitions().size() ? "Sieć jest odwracalna" : "Sieć nie jest odwracalna";
    }

    private void checkPaths(Vertex vertex, String path) {
        if (!shouldBeStopped(vertex, path)) {
            for (Integer successorId : vertex.getSuccessors()) {
                Vertex successor = coverageGraph.getVertex(successorId);
                checkPaths(successor, path + vertex.getID() + ",");
            }
        }
    }

    private boolean shouldBeStopped(Vertex vertex, String path) {
        if (vertex.equals(initialVertex) && !path.isEmpty()) {
            path.substring(0, path.length() - 1);
            addToReversibleVertices(path);
            return true;
        } else if (path.contains("," + vertex.getID()) || vertex.getSuccessors().isEmpty()) { // pierwszy na ścieżce jest wierzchołek początkowy - sprawdzamy go wcześniej
            path.substring(0, path.length() - 1);
            if (reversibleVertices.contains(vertex.getID())) {
                addToReversibleVertices(path);
            }
            loopPaths.add(path);
            return true;
        }
        return false;
    }

    private void addToReversibleVertices(String path) {
        String[] vertices = path.split(",");
        for (String v : vertices) {
            reversibleVertices.add(Integer.valueOf(v));
        }
    }

    private void checkLoopPaths() {
        for (String path : loopPaths) {
            String[] tab = path.split(",");
            if (reversibleVertices.contains(tab[tab.length - 1])) {
                addToReversibleVertices(path);
            }
        }
    }
}
