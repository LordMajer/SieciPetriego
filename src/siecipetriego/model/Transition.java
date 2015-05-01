/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siecipetriego.model;

/**
 *
 * @author Mateusz
 */
public class Transition extends Vertex {

    private int priority;       // priorytet przejścia -1 oznacza brak priorytetu

    public Transition(int id) {
        super(id);
        this.name = "T" + id;
        this.priority = -1;
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

    @Override
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
