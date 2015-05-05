package com.petri.nets.algorithms;

import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Place;
import com.petri.nets.model.Transition;

import java.util.List;

public class IncidenceMatrix extends IOMatrix {

    public IncidenceMatrix(CustomGraph graph) {
        super(graph);
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

    @Override
    protected List<Integer> getVertices(Transition transition) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Edge getEdge(Transition transition, Place place) {
        throw new UnsupportedOperationException();
    }
}
