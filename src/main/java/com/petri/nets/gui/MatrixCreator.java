package com.petri.nets.gui;

import com.petri.nets.algorithms.IncidenceMatrix;
import com.petri.nets.algorithms.InputMatrix;
import com.petri.nets.algorithms.OutputMatrix;
import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Place;
import com.petri.nets.model.Transition;

import javax.swing.*;
import java.util.Map;

public class MatrixCreator {

    private CustomGraph customGraph;
    private Map<Integer, Transition> transitions;
    private Map<Integer, Place> places;

    public MatrixCreator(CustomGraph customGraph) {
        this.customGraph = customGraph;
        transitions = customGraph.getTransitions();
        places = customGraph.getPlaces();
    }

    public JTable generateInputMatrix() {
        return getJTable(new InputMatrix(customGraph).calculate());
    }

    public JTable generatOutputMatrix() {
        return getJTable(new OutputMatrix(customGraph).calculate());
    }

    public JTable generateIncidenceMatrix() {
        return getJTable(new IncidenceMatrix(customGraph).calculate());
    }

    // TODO dodać nagłówek i zablokować edycję
    private JTable getJTable(int[][] matrix) {
        JTable jTable = new JTable(matrix.length + 1, matrix[0].length + 1);
        setTransitionHeaders(jTable);
        setRowHeaders(jTable);
        setMatrixValues(jTable, matrix);
        return jTable;
    }

    private void setTransitionHeaders(JTable jTable) {
        int i = 0;
        for (Transition transition : transitions.values()) {
            jTable.setValueAt(transition.getName(), 0, ++i);
        }
    }

    private void setRowHeaders(JTable jTable) {
        int i = 0;
        for (Place place : places.values()) {
            jTable.setValueAt(place.getName(), ++i, 0);
        }
    }

    private void setMatrixValues(JTable jTable, int[][]matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                jTable.setValueAt(matrix[i][j], i + 1, j + 1);
            }
        }
    }
}
