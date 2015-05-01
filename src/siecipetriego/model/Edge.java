/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siecipetriego.model;

/**
 *
 * @author Mateusz
 */
public class Edge {
    
    String condition;           // warunek przypisany do krawędzi
    int capacity;               // pojemność krawędzi- ilość tokenów które przechodza przez krawędź
    int sourceId;               // id wierzchołka źródła
    int destinationId;          // id wierzchołka docelowego
    
    public Edge(int source, int destination){
        condition = null;                                           
        capacity = 1;                                               
        sourceId = source;
        destinationId = destination;
    }
    
    public Edge(Edge edge){
        
    }
    
    public String getKey(){
        return "{"+ sourceId + "," + destinationId + "}";
    }
    
    public int getSourceId(){
        return sourceId;
    }
    
    public int getDestinationId(){
        return destinationId;
    }
    
    public int getCapacity(){
        return capacity;
    }
    
    public String getCondition(){
        return condition;
    }
    
    public void setCondition(String condition){
        this.condition = condition;
    }
    
    public void setCapacity(int capacity){
        this.capacity = capacity;
    }
    
    public String toString(){
        return "Edge: " + this.getKey();
    }
}