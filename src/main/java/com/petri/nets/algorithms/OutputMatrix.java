package com.petri.nets.algorithms;

import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Place;
import com.petri.nets.model.Transition;

import java.util.List;
import java.util.Map;

public class OutputMatrix {

    public static int[][] calculate(CustomGraph graph) {
        Map<Integer, Place> places = graph.getPlaces();
        Map<Integer, Transition> transitions = graph.getTransitions();
        Map<String, Edge> edges = graph.getEdges();
        int placeCounter = 0;
        int transitionCounter = 0;
        int[][] tab = new int[places.size()][transitions.size()];
        List<Integer> idList;
        Edge edge;

        for (Place place : places.values()) {
            System.out.println(place);
            for (Transition transition : transitions.values()) {
                System.out.println(transition);
                idList = transition.getPredecessors();
                for (int i = 0; i < transition.getPredecessors().size(); i++) {
                    if (idList.get(i) == place.getID()) {
                        edge = edges.get(new Edge(place.getID(), transition.getID()).getKey());
                        tab[placeCounter][transitionCounter] = edge.getCapacity();
                    }
                }
                System.out.println("licznik przejść: " + transitionCounter);
                transitionCounter++;
            }
            transitionCounter = 0;
            System.out.println("licznik miejsc: " + transitionCounter);
            placeCounter++;
        }

        return tab;
    }
}
