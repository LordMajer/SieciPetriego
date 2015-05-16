package com.petri.nets.model;

import java.io.Serializable;

public class Position implements Serializable {
    
    private double x;
    private double y;
    
    public Position(){
        this.x = 0;
        this.y = 0;
    }
    
    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public Position(Position position){
        this.x = position.getX();
        this.y = position.getY();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString(){
        return "Pozycja: " + x + "," + y + "\n";
    }
}