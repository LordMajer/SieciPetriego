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
    
    String condition;
    int capacity;
    int sourceId;
    int destinationId;
    
    public Edge(int source, int destination){
        condition = null;                                           // warunek który może być przypisany krawędzi
        capacity = 1;                                               // ilość tokenów które przechodza przez krawędź 
        sourceId = source;
        destinationId = destination;
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
}