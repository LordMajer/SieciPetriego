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
    int capacity;               // pojemność krawędzi - ilość tokenów które przechodza przez krawędź
    int sourceId;               // id wierzchołka źródła
    int destinationId;          // id wierzchołka docelowego
    
    public Edge(int sourceId, int destinationId){
        this.condition = null;                                           
        this.capacity = 1;                                               
        this.sourceId = sourceId;
        this.destinationId = destinationId;
    }
    
    // Pytanie: Za co to jest odpowiedzialne? 
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
    
    @Override
    public String toString(){
        return "Edge: " + this.getKey();
    }
}