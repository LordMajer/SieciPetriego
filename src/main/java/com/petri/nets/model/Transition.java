package com.petri.nets.model;

import com.petri.nets.helpers.VertexType;

/**
 * @author Mateusz
 */
public class Transition extends Vertex {

    private static final VertexType VERTEX_TYPE = VertexType.TRANSITION;
    private static final int DEFAULT_PRIORITY = -1;

    private int priority = DEFAULT_PRIORITY;       // priorytet przejścia -1 oznacza brak priorytetu

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

    public static VertexType getVertexType() {
        return VERTEX_TYPE;
    }

    @Override
    public String toString() {
        return name;
    }
}
