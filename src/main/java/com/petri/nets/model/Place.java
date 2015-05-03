package com.petri.nets.model;

import com.petri.nets.helpers.VertexType;

/**
 * @author Mateusz
 */
public class Place extends Vertex {

    private static final VertexType VERTEX_TYPE = VertexType.PLACE;
    private static final int DEFAULT_TOKEN_COUNT = 0;
    private static final int DEFAULT_CAPACITY = -1;

    private int tokenCount = DEFAULT_TOKEN_COUNT;             // określa ile aktualnie znajduje się w tym miejscu tokenów
    private int capacity = DEFAULT_CAPACITY;                  // określa pojemność miejsca    (-1) będzie oznaczało nieskonczoność

    public Place(int id, String name) {
        super(id);
        this.name = name;
    }

    public Place(int id, String name, Position position) {
        super(id, position);
        this.name = name;
    }

    public Place(Place place) {
        super(place.getID());
        this.name = place.getName();
        this.tokenCount = place.getTokenCount();
        this.capacity = place.getCapacity();
    }

    public int getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(int tokenCount) {
        this.tokenCount = tokenCount;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
        return "Miejsce: " + name + " tokenów: " + tokenCount + "\npojemność: " + capacity + "\n" + builder.toString();
    }
}
