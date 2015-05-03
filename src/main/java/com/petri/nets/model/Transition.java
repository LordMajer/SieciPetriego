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

    @Override // TODO Zmiana na zwracanie nazwy wierzchołka (na razie może zostać, bo łatwiej weryfikować poprawność działania aplikacji)
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Nastepniki: ");
        // nastepniki:
        for (int successor : successors) {
            builder.append(successor)
                    .append(" ");
        }
        builder.append("\nPoprzedniki: ");
        // poprzedniki:
        for (int predecessor : predecessors) {
            builder.append(predecessor)
                    .append(" ");
        }
        builder.append("\n");
        return "Przejście: " + name + "\n ID: " + id + "\nPriorytet: " + priority + "\n" + builder.toString();
    }
}
