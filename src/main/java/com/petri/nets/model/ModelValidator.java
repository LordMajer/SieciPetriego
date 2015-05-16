package com.petri.nets.model;

import java.util.Map;

public class ModelValidator {

    public static String validate(CustomGraph graph) {
        int placeCounter = 0;
        int transitionCounter = 0;
        Map<Integer, Vertex> vertices = graph.getVertices();
        StringBuilder errorsBuilder = new StringBuilder();
        Vertex vertexToCheck;
        // przejście po wszystkich wierzchołkach i sprawdzenie czy są poprawnie połaczone.

        if (vertices.isEmpty()) {
            return "Model nie został wprowadzony";
        }
        for (Vertex vertex : vertices.values()) {
            if (vertex instanceof Place) {
                // następnikami i poprzednikami moga być tylko przejścia
                // TODO sprawdzanie tylko poprzedników, po to, żeby nie duplikować informacji. Nie zadziała jeżeli będzie możliwe odłączenie krawędzi
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
                placeCounter++;
            } else {
                // sprawdzanie poprzedników:
                for (int predecessor : vertex.getPredecessors()) {
                    vertexToCheck = vertices.get(predecessor);
                    if (!(vertexToCheck instanceof Place)) {
                        errorsBuilder.append("Wierzchołek typu przejście: ")
                                .append(vertex)
                                .append(" posiada nielegalne połaczenie z wierzchołkiem: ")
                                .append(vertexToCheck)
                                .append("\n");
                    }
                }
                transitionCounter++;
            }
        }
        if (placeCounter == 0) {
            errorsBuilder.append("Nie wprowadzono żadnych wierzchołków typu miejsce")
            .append("\n");
        }
        if (transitionCounter == 0) {
            errorsBuilder.append("Nie wprowadzono żadnych wierzchołków typu przejście")
                    .append("\n");
        }
        if (errorsBuilder.length() > 0) {
            return errorsBuilder.toString();
        } else {
            return null;
        }
    }
}
