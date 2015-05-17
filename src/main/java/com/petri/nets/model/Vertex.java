package com.petri.nets.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Vertex implements Serializable {

    private static final int DEFAULT_WIDTH = 80;
    private static final int DEFAULT_HEIGHT = 80;

    protected String name;
    protected int id;
    protected List<Integer> successors = new ArrayList<>();              // przechowuje listę id podłączonych do węzła następników
    protected List<Integer> predecessors = new ArrayList<>();            // przechowuje listę id podłączonych do wezła poprzedników
    protected Position position;
    protected double width = DEFAULT_WIDTH;
    protected double height = DEFAULT_HEIGHT;

    public Vertex(int id) {
        this.id = id;
        this.name = "V" + id;
        this.position = new Position();
    }

    public Vertex(int id, Position position) {
        this.id = id;
        this.name = "V" + id;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public List<Integer> getSuccessors() {
        return successors;
    }

    public List<Integer> getPredecessors() {
        return predecessors;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setX(double x) {
        position.setX(x);
    }

    public void setY(double y) {
        position.setY(y);
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void addSuccessor(int successorId) {
        successors.add(successorId);
    }

    public void addPredecessor(int predecessorId) {
        predecessors.add(predecessorId);
    }

    public void removeSuccessor(int successorId) {
        successors.remove(successors.indexOf(successorId));
    }

    public void removePredecessor(int predecessorId) {
        predecessors.remove(predecessors.indexOf(predecessorId));
    }
}