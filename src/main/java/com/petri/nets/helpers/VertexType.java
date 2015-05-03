package com.petri.nets.helpers;

public enum VertexType {

    PLACE("P"),
    TRANSITION("T");

    private Class type;
    private String prefix;

    VertexType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
