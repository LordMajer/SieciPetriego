package com.petri.nets.model;

import java.util.Map;

public class ModelValidator {

    public static String validate(CustomGraph graph) {
        Map<Integer, Vertex> vertices =  graph.getVertices();
        StringBuilder errorsBuilder = new StringBuilder();
        Vertex vertexToCheck;
        // przejście po wszystkich wierzchołkach i sprawdzenie czy są poprawnie połaczone.

        for (Vertex vertex : vertices.values()) {

            if (vertex instanceof Place) {

                // następnikami i poprzednikami moga być tylko przejścia
                // sprawdzanie następników:
                for (int successor : vertex.getSuccessors()) {
                    vertexToCheck = vertices.get(successor);
                    if (!(vertexToCheck instanceof Transition)) {
                        errorsBuilder.append("Wierzchołek typu miejsce: ")
                                .append(vertex)
                                .append(" posiada nielegalne połaczenie z wierzchołkiem: ")
                                .append(vertexToCheck)
                                .append("\n");
                    }
                }

                // sprawdzanie poprzedników:
                for (int predecessor : vertex.getPredecessors()) {
                    vertexToCheck = vertices.get(predecessor);
                    if (!(vertexToCheck instanceof Transition)) {
                        errorsBuilder.append("Wierzchołek typu miejsce: ")
                                .append(vertex)
                                .append(" posiada nielegalne połaczenie z wierzchołkiem: ")
                                .append(vertexToCheck)
                                .append("\n");
                    }
                }
            } else {
                // następnikami i poprzednikami moga być tylko miejsca.
                //sprawdzanie następników
                for (int successor : vertex.getSuccessors()) {
                    vertexToCheck = vertices.get(successor);
                    if (!(vertexToCheck instanceof Place)) {
                        errorsBuilder.append("Wierzchołek typu miejsce: ")
                                .append(vertex)
                                .append(" posiada nielegalne połaczenie z wierzchołkiem: ")
                                .append(vertexToCheck)
                                .append("\n");
                    }
                }

                // sprawdzanie poprzedników:
                for (int predecessor : vertex.getPredecessors()) {
                    vertexToCheck = vertices.get(predecessor);
                    if (!(vertexToCheck instanceof Place)) {
                        errorsBuilder.append("Wierzchołek typu miejsce: ")
                                .append(vertex)
                                .append(" posiada nielegalne połaczenie z wierzchołkiem: ")
                                .append(vertexToCheck)
                                .append("\n");
                    }
                }
            }
        }

        if (errorsBuilder.length() > 0) {
            return errorsBuilder.toString();
        } else {
            return null;
        }
    }
}
