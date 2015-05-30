package com.petri.nets.algorithms.properties;

import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Transition;

public class TransitionLiveness {
    CustomGraph coverageGraph;
    Transition transition;
    LivenessState state = LivenessState.L0;

    public TransitionLiveness(CustomGraph coverageGraph, Transition transition) {
        this.coverageGraph = coverageGraph;
        this.transition = transition;
    }

    public String calculate() {
        checkL1State();
        return state.getMessage();
    }

    private void checkL1State() {
        for (Edge edge : coverageGraph.getEdges().values()) {
            if (edge.getName().equals(transition.getName())) {
                state = LivenessState.L1;
            }
        }
    }

    private static enum LivenessState {
        L0("L0 - żywotne : przejście jest martwe"),
        L1("L1 - żywotne : przejście potencjalnie wykonywalne"),
        L2("L2 - żywotne"),
        L3("L3 - żywotne");

        private String message;
         LivenessState(String message) {
             this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
