package com.petri.nets.helpers.generators.name;

import com.petri.nets.helpers.VertexType;

import java.util.HashMap;
import java.util.Map;

public class BasedOnClassDistinctionNameGenerator implements UniqueNameGenerator {

    private static final int INITIAL_VALUE = 1;

    Map<VertexType, Integer> counter = new HashMap<>();

    public BasedOnClassDistinctionNameGenerator() {
        counter.put(VertexType.PLACE, INITIAL_VALUE);
        counter.put(VertexType.TRANSITION, INITIAL_VALUE);
    }

    @Override
    public String getNext(VertexType vertexType) {
        int currentValue = counter.get(vertexType);
        counter.put(vertexType, currentValue + 1);
        return vertexType.getPrefix() + currentValue;
    }
}
