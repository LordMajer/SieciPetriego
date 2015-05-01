/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siecipetriego.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mateusz
 */
public class Vertex {
    
    protected String name;
    protected int id;
    protected List<Integer> successors;              // przechowuje listę id podłączonych do węzła następników
    protected List<Integer> predecessors;            // przechowuje listę id podłączonych do wezła poprzedników
    
    protected Position position;
    protected double width;
    protected double height;
    
    public Vertex(int id){
        this.id = id;
        this.name = "V" + id;
        this.successors = new ArrayList<>();
        this.predecessors = new ArrayList<>();
        this.position = new Position();
        this.width = 120;
        this.height = 30;
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
    
    public List<Integer> getSuccessors(){
        return successors;
    }
    
    public List<Integer> getPredecessors(){
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
    
    public void setPosition(Position position){
        this.position = position;
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