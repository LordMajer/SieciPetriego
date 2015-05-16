package com.petri.nets.simulation;

import com.petri.nets.helpers.common.CommonOperations;
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
            List<Edge> edges = CommonOperations.getEdgesContaining(transition, graph.getEdges());
            List<Edge> destinationEdges = CommonOperations.getDestinationEdges(transition, edges);
            if (canStepBeDone(destinationEdges)) {
                possibleMovements.add(transition);
            }
        }
        return possibleMovements;
    }

    public CustomGraph takeStep(Transition transition) {
        List<Edge> edges = CommonOperations.getEdgesContaining(transition, graph.getEdges());
        List<Edge> sourceEdges = CommonOperations.getSourceEdges(transition, edges);
        List<Edge> destinationEdges = CommonOperations.getDestinationEdges(transition, edges);
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
