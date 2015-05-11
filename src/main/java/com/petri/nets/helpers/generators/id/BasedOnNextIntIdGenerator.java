package com.petri.nets.helpers.generators.id;

import java.io.Serializable;

public class BasedOnNextIntIdGenerator implements UniqueIdGenerator, Serializable {

    private int currentValue = 0;

    @Override
    public int getNext() {
        return currentValue++;
    }
}
