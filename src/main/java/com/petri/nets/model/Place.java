/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.petri.nets.model;

/**
 *
 * @author Mateusz
 */
public class Place extends Vertex {

    private int tokenCount;             // określa ile aktualnie znajduje się w tym miejscu tokenów
    private String type;                // określa typ miejsca          null bęzie oznaczał brak konkretnego typu
    private int capacity;               // określa pojemność miejsca    (-1) będzie oznaczało nieskonczoność 

    public Place(int id) {
        super(id);
        this.name = "P" + id;
        this.tokenCount = 0;
        this.capacity = -1;
        this.type = null;
    }

    public Place(Place place) {
        super(place.getID());
        this.name = place.getName();
        this.tokenCount = place.getTokenCount();
        this.capacity = place.getCapacity();
        this.type = place.getType();
    }

    public int getTokenCount() {
        return tokenCount;
    }

    public String getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setTokenCount(int count) {
        tokenCount = count;
    }

    public void setType(String t) {
        type = t;
    }

    public void setCapacity(int number) {
        capacity = number;
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
        return "Miejsce: " + name + " tokenów: " + tokenCount + "\ntyp: " + type + "\npojemność: " + capacity + "\n" + builder.toString();
    }
}
