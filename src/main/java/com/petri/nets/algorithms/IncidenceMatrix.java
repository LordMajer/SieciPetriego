package com.petri.nets.algorithms;

import com.petri.nets.model.CustomGraph;

public class IncidenceMatrix {

    CustomGraph graph;

    public IncidenceMatrix(CustomGraph graph) {
        this.graph = graph;
    }

    public int[][] calculate() {
        int[][] inputMatrix = new InputMatrix(graph).calculate();
        int[][] outputMatrix = new OutputMatrix(graph).calculate();
        int[][] incidenceMatrix = new int[inputMatrix.length][inputMatrix[0].length];
        for (int i = 0; i < inputMatrix.length; i++) {
            for (int j = 0; j < inputMatrix[i].length; j++) {
                incidenceMatrix[i][j] = inputMatrix[i][j] - outputMatrix[i][j];
            }
        }
        return incidenceMatrix;
    }
}
