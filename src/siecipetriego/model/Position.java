/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siecipetriego.model;

/**
 *
 * @author Mateusz
 */
public class Position {
    
    private double x;
    private double y;
    
    public Position(){
        this.x = 10;
        this.y = 10;
    }
    
    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public Position(Position position){
        this.x = position.getX();
        this.y = position.getY();
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public void setX(int x){
        this.x = x;
    }
    
    public void setY(int y){
        this.y = y;
    }
    
    @Override
    public String toString(){
        return "Pozycja: " + x + "," + y + "\n";
    }
}