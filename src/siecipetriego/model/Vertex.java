/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siecipetriego.model;

import java.util.ArrayList;

/**
 *
 * @author Mateusz
 */
public class Vertex {
    
    protected String name;
    protected int id;
    protected ArrayList<Integer> successors;              // przechowuje listę id podłączonych do węzła następników
    protected ArrayList<Integer> predecessors;            // przechowuje listę id podłączonych do wezła poprzedników
    
    protected Position position;
    protected double width;
    protected double height;
    
    public Vertex(int id){
        this.id = id;
        name = "V" + id;
        successors = new ArrayList<Integer>();
        predecessors = new ArrayList<Integer>();
        position = new Position();
        width = 120;
        height = 30;
    }
    
    public String getName(){
        return name;
    }
    
    public int getID(){
        return id;
    }
    
    public double getWidth(){
        return width;
    }
    
    public double getHeight(){
        return height;
    }
    
    public double getX(){
        return position.getX();
    }
    
    public double getY(){
        return position.getY();
    }
    
    public ArrayList<Integer> getSuccessors(){
        return successors;
    }
    
    public ArrayList<Integer> getPredecessors(){
        return predecessors;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setID(int id){
        this.id = id;
    }
    
    public void setWidth(int width){
        this.width = width;
    }
    
    public void setHeight(int height){
        this.height = height;
    }
    
    public void setX(int x){
        position.setX(x);
    }
    
    public void setY(int y){
        position.setY(y);
    }
    
    public void addSuccessor(int successorId){
        successors.add(successorId);
    }
    
    public void addPredecessor(int predecessorId){
        predecessors.add(predecessorId);
    }
    
    public void removeSuccessor(int successorId){
        successors.remove(successors.indexOf(successorId));
    }
    
    public void removePredecessor(int predecessorId){
        predecessors.remove(predecessors.indexOf(predecessorId));
    }
}