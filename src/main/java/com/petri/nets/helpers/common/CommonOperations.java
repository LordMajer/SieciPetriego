package com.petri.nets.helpers.common;

import com.petri.nets.model.Edge;
import com.petri.nets.model.Transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonOperations {

    public static List<Edge> getEdgesContaining(Transition transition, Map<String, Edge> edges) {
        List<Edge> edgesContainingGivenTransition = new ArrayList<>();
        int id = transition.getID();
        for (Edge edge : edges.values()) {
            if (id == edge.getSourceId() || id == edge.getDestinationId()) {
                edgesContainingGivenTransition.add(edge);
            }
        }
        return edgesContainingGivenTransition;
    }

    public static List<Edge> getDestinationEdges(Transition transition, List<Edge> edges) {
        int id = transition.getID();
        List<Edge> destinationEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getDestinationId() == id) {
                destinationEdges.add(edge);
            }
        }
        return destinationEdges;
    }

    public static List<Edge> getSourceEdges(Transition transition, List<Edge> edges) {
        int id = transition.getID();
        List<Edge> sourceEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getSourceId() == id) {
                sourceEdges.add(edge);
            }
        }
        return sourceEdges;
    }

    public static List<Transition> getMaxPriorityTransitions(List<Transition> transitions) {
        List<Transition> maxPiorityTransitions = new ArrayList<>();
        int maxPriority = getMaxPriority(transitions);
        for (Transition transition : transitions) {
            if (transition.getPriority() == maxPriority) {
                maxPiorityTransitions.add(transition);
            }
        }
        return maxPiorityTransitions;
    }

    private static int getMaxPriority(List<Transition> transitions) {
        int max = Integer.MIN_VALUE;
        for (Transition transition : transitions) {
            int transitionPriority = transition.getPriority();
            if (transitionPriority > max) {
                max = transitionPriority;
            }
        }
        return max;
    }
}
