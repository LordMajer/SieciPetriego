package com.petri.nets.algorithms;

import com.petri.nets.model.CustomGraph;

public class IncidenceMatrix {

    public static int[][] calculate(CustomGraph graph) {

        int[][] inputMatrix = InputMatrix.calculate(graph);
        int[][] outputMatrix = OutputMatrix.calculate(graph);

        int[][] incidenceMatrix = new int[inputMatrix.length][inputMatrix[0].length];

        for (int i = 0; i < inputMatrix.length; i++) {
            for (int j = 0; j < inputMatrix[i].length; j++) {
                incidenceMatrix[i][j] = inputMatrix[i][j] - outputMatrix[i][j];
            }
        }

        return incidenceMatrix;
    }
}
