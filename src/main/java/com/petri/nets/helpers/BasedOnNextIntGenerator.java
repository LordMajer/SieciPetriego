package com.petri.nets.helpers;

/**
 * Created by Mateusz on 2015-05-03.
 */
public class BasedOnNextIntGenerator implements UniqueIdGenerator {
    private int currentValue = 0;

    @Override
    public int getNext() {
        return currentValue++;
    }
}
