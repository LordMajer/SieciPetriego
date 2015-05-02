package com.petri.nets.model;

/**
 * @author Mateusz
 */
public class Place extends Vertex {

    private static final int DEFAULT_TOKEN_COUNT = 0;
    private static final int DEFAULT_CAPACITY = -1;

    private int tokenCount = DEFAULT_TOKEN_COUNT;             // określa ile aktualnie znajduje się w tym miejscu tokenów
    @Deprecated // TODO usunąć pole type
    private String type;                                      // określa typ miejsca          null bęzie oznaczał brak konkretnego typu
    private int capacity = DEFAULT_CAPACITY;                  // określa pojemność miejsca    (-1) będzie oznaczało nieskonczoność

    public Place(int id) {
        super(id);
        this.name = "P" + id;
    }

    public Place(int id, Position position) {
        super(id, position);
        this.name = "P" + id;
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

    public void setTokenCount(int tokenCount) {
        this.tokenCount = tokenCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
