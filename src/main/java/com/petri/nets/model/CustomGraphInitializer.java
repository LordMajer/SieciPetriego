package com.petri.nets.model;

public class CustomGraphInitializer {
    public static void initialize(CustomGraph graph) {
        Place place = new Place(graph.getNewID(), graph.getNewName(Place.getVertexType()), new Position(100, 50));
        Transition transition = new Transition(graph.getNewID(), graph.getNewName(Transition.getVertexType()), new Position(100, 200));
        Edge edge = new Edge(place.getID(), transition.getID());

        graph.addVertex(place);
        graph.addVertex(transition);
        graph.addEdge(edge);
    }
    
    public static void initializeTest(CustomGraph graph) {
        Place place = new Place(graph.getNewID(), graph.getNewName(Place.getVertexType()), new Position(100, 100));
        Place place2 = new Place(graph.getNewID(), graph.getNewName(Place.getVertexType()), new Position(100, 100));

        Transition transition = new Transition(graph.getNewID(), graph.getNewName(Transition.getVertexType()), new Position(100, 200));
        Transition transition2 = new Transition(graph.getNewID(), graph.getNewName(Transition.getVertexType()), new Position(100, 200));
        Edge edge = new Edge(place.getID(), transition.getID());
        Edge edge2 = new Edge(transition.getID(), place2.getID());
        Edge edge3 = new Edge(place2.getID(), transition2.getID());
        Edge edge4 = new Edge(transition2.getID(), place.getID());

        graph.addVertex(place);
        graph.addVertex(transition);
        graph.addVertex(place2);
        graph.addVertex(transition2);

        place.setTokenCount(1);
        edge2.setCapacity(2);

        graph.addEdge(edge);
        graph.addEdge(edge2);
        graph.addEdge(edge3);
        graph.addEdge(edge4);
    }
}
