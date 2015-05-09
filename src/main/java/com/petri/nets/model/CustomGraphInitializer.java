package com.petri.nets.model;

public class CustomGraphInitializer {
    public static void initialize(CustomGraph graph) {
        Place place = new Place(graph.getNewID(), graph.getNewName(Place.getVertexType()), new Position(100, 100));
        Transition transition = new Transition(graph.getNewID(), graph.getNewName(Transition.getVertexType()), new Position(100, 200));
        Edge edge = new Edge(place.getID(), transition.getID());

        graph.addVertex(place);
        graph.addVertex(transition);
        graph.addEdge(edge);
    }
}
