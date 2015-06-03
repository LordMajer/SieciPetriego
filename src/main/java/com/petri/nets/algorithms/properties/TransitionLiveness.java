package com.petri.nets.algorithms.properties;

import com.petri.nets.algorithms.CoverageGraph;
import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Edge;
import com.petri.nets.model.Transition;

public class TransitionLiveness {
    CustomGraph baseGraph;
    LivenessState state = LivenessState.L0;

    public TransitionLiveness(CustomGraph baseGraph) {
        this.baseGraph = baseGraph;
    }

    public String calculate(Transition transition) {
        CustomGraph coverageGraph = new CoverageGraph(baseGraph).build();
        checkL1State(transition, coverageGraph);
        return state.getMessage();
    }

    private void checkL1State(Transition transition, CustomGraph coverageGraph) {
        for (Edge edge : coverageGraph.getEdges().values()) {
            if (edge.getName().equals(transition.getName())) {
                state = LivenessState.L1;
            }
        }
    }

    private static enum LivenessState {
        L0("L0 - żywotne : przejście jest martwe"),
        L1("L1 - żywotne : przejście potencjalnie wykonywalne");

        private String message;
         LivenessState(String message) {
             this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
