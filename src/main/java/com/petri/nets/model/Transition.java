package com.petri.nets.model;

import com.petri.nets.helpers.VertexType;

import java.io.Serializable;

public class Transition extends Vertex implements Serializable {

    private static final VertexType VERTEX_TYPE = VertexType.TRANSITION;
    private static final int DEFAULT_PRIORITY = 0;

    private int priority = DEFAULT_PRIORITY;       // priorytet przejścia -1 oznacza brak priorytetu
    private boolean isPriority = false;

    public Transition(int id, String name) {
        super(id);
        this.name = name;
    }

    public Transition(int id, String name, Position position) {
        super(id, position);
        this.name = name;
    }

    public Transition(Transition transition) {
        super(transition.getID());
        this.name = transition.getName();
        this.priority = transition.getPriority();
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(boolean isPriority) {
        this.isPriority = isPriority;
    }

    public boolean isPriority() {
        return isPriority;
    }

    public static VertexType getVertexType() {
        return VERTEX_TYPE;
    }

    @Override
    public String toString() {
        if (isPriority) {
            return name + "\nPriorytet: " + priority;
        } else {
            return name;
        }
    }
}
