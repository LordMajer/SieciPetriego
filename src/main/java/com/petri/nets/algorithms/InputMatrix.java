package com.petri.nets.algorithms;

import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Place;
import com.petri.nets.model.Transition;

import java.util.List;

public class InputMatrix extends IOMatrix{


    public InputMatrix(CustomGraph graph) {
        super(graph);
    }

    @Override
    protected List<Integer> getVertices(Transition transition) {
        return transition.getSuccessors();
    }

    @Override
    protected Edge getEdge(Transition transition, Place place) {
        return new Edge(transition.getID(), place.getID());
    }
}
