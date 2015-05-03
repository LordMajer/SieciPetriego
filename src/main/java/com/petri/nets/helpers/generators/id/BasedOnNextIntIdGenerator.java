package com.petri.nets.helpers.generators.id;

public class BasedOnNextIntIdGenerator implements UniqueIdGenerator {

    private int currentValue = 0;

    @Override
    public int getNext() {
        return currentValue++;
    }
}
