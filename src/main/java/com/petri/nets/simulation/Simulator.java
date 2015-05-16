package com.petri.nets.simulation;

import com.petri.nets.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulator {

    private CustomGraph graph;
    private int stepNumber = 1;

    public Simulator(CustomGraph graph) {
        this.graph = graph;
    }

    public List<Transition> getPossibleSteps() {
        List<Transition> possibleMovements = new ArrayList<>();
        for (Transition transition : graph.getTransitions().values()) {
            List<Edge> edges = getEdgesContaining(transition);
            List<Edge> destinationEdges = getDestinationEdges(transition, edges);
            if (canStepBeDone(destinationEdges)) {
                possibleMovements.add(transition);
            }
        }
        return possibleMovements;
    }

    public CustomGraph takeStep(Transition transition) {
        List<Edge> edges = getEdgesContaining(transition);
        List<Edge> sourceEdges = getSourceEdges(transition, edges);
        List<Edge> destinationEdges = getDestinationEdges(transition, edges);
        resolveState(sourceEdges, destinationEdges);
        stepNumber++;
        return graph;
    }

    private boolean canStepBeDone(List<Edge> destinationEdges) {
        for (Edge edge : destinationEdges) {                                                            // Sprawdzenie czy każde miejsce połączone z przejściem posiada odpowiednią ilość tokenów
            if (edge.getCapacity() > ((Place) graph.getVertex(edge.getSourceId())).getTokenCount()) {
                return false;
            }
        }
        return true;
    }

    private List<Edge> getEdgesContaining(Transition transition) {
        List<Edge> edgesContainingGivenTransition = new ArrayList<>();
        int id = transition.getID();
        for (Edge edge : graph.getEdges().values()) {
            if (id == edge.getSourceId() || id == edge.getDestinationId()) {
                edgesContainingGivenTransition.add(edge);
            }
        }
        return edgesContainingGivenTransition;
    }

    private List<Edge> getDestinationEdges(Transition transition, List<Edge> edges) {
        int id = transition.getID();
        List<Edge> destinationEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getDestinationId() == id) {
                destinationEdges.add(edge);
            }
        }
        return destinationEdges;
    }

    private List<Edge> getSourceEdges(Transition transition, List<Edge> edges) {
        int id = transition.getID();
        List<Edge> sourceEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getSourceId() == id) {
                sourceEdges.add(edge);
            }
        }
        return sourceEdges;
    }

    private void resolveState(List<Edge> sourceEdges, List<Edge> destinationEdges) {
        for (Edge edge : destinationEdges) {
            int id = edge.getSourceId();
            int tokenToTake = edge.getCapacity();
            Place vertex = (Place) graph.getVertex(id);
            int currentTokenNumber = vertex.getTokenCount();
            vertex.setTokenCount(currentTokenNumber - tokenToTake);
        }
        for (Edge edge : sourceEdges) {
            int id = edge.getDestinationId();
            int tokenToTake = edge.getCapacity();
            Place vertex = (Place) graph.getVertex(id);
            int currentTokenNumber = vertex.getTokenCount();
            vertex.setTokenCount(currentTokenNumber + tokenToTake);
        }
    }

    public static void main(String[] args) {
        CustomGraph customGraph = new CustomGraph();
        CustomGraphInitializer.initializeTest(customGraph);
        Simulator simulator = new Simulator(customGraph);
        System.out.println(simulator.getPossibleSteps().toString());
        simulator.takeStep(simulator.getPossibleSteps().get(0));
        System.out.println(simulator.getPossibleSteps().toString());
        simulator.takeStep(simulator.getPossibleSteps().get(0));
        System.out.println(simulator.getPossibleSteps().toString());
        simulator.takeStep(simulator.getPossibleSteps().get(0));
        System.out.println(simulator.getPossibleSteps().toString());
    }

    public CustomGraph getGraph() {
        return graph;
    }

    public int getStepNumber() {
        return stepNumber;
    }
}
