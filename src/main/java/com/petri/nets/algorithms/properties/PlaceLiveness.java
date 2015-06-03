package com.petri.nets.algorithms.properties;

import com.petri.nets.algorithms.CoverageGraph;
import com.petri.nets.model.*;

import java.util.Collection;

public class PlaceLiveness {
    private CustomGraph baseGraph;

    public PlaceLiveness(CustomGraph baseGraph) {
        this.baseGraph = baseGraph;
    }

    public String calculate(Place place) {
        CustomGraph coverageGraph = new CoverageGraph(baseGraph).build();
        return isLived(place, coverageGraph) ? "Miejsce jest żywotne": "Miejsce nie jest żywotne";
    }

    private boolean isLived(Place place, CustomGraph coverageGraph) {
        int id = determinePlaceOrder(place.getID(), baseGraph.getPlaces().values());
        for (Transition transition : coverageGraph.getTransitions().values()) {
            String[] states = transition.getName().split(",");
            if (!states[id].equals("0")) {
                return true;
            }
        }
        return false;
    }

    private int determinePlaceOrder(int idToFind, Collection<Place> places) {
        int i = 0;
        for (Place place : places) {
            if (place.getID() == idToFind) {
                return i;
            }
            i++;
        }
        return i;
    }
}