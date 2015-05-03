package com.petri.nets.algorithms;

import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Place;
import com.petri.nets.model.Transition;

import java.util.List;
import java.util.Map;

public abstract class IOMatrix {
    protected CustomGraph graph;
    private Map<Integer, Place> places;
    private Map<Integer, Transition> transitions;
    private Map<String, Edge> edges;

    public IOMatrix(CustomGraph graph) {
        this.graph = graph;
        this.places = graph.getPlaces();
        this.transitions = graph.getTransitions();
        this.edges = graph.getEdges();
    }

    public int[][] calculate() {
        int placeCounter = 0;
        int transitionCounter = 0;
        int[][] tab = new int[places.size()][transitions.size()];

        for (Place place : places.values()) {
            System.out.println(place);
            for (Transition transition : transitions.values()) {
                System.out.println(transition);
                List<Integer> idList = getVertices(transition);
                for (int i = 0; i < getVertices(transition).size(); i++) {
                    if (idList.get(i) == place.getID()) {
                        Edge edge = edges.get(getEdge(transition, place).getKey());
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

    protected abstract List<Integer> getVertices(Transition transition);

    protected abstract Edge getEdge(Transition transition, Place place);
}
