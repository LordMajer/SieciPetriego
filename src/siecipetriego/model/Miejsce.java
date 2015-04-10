/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siecipetriego.model;

/**
 *
 * @author Mateusz
 */
public class Miejsce extends Vertex{
    
    private int tokenCount;             // określa ile aktualnie znajduje się w tym miejscu tokenów
    private String type;                // określa typ miejsca          null bęzie oznaczał brak konkretnego typu
    private int capacity;               // określa pojemność miejsca    (-1) będzie oznaczało nieskonczoność 
    
    public Miejsce(int id){
        
        super(id);
        name = "P" + id;
        tokenCount = 0;
        capacity = -1;
        type = null;
    }
 
// gettery
    
    public int getTokenCount(){
        return tokenCount;
    }
    
    public String getType(){
        return type;
    }
    
    public int getCapacity(){
        return capacity;
    }
    
// settery:
    
    public void setTokenCount(int count){
        tokenCount = count;
    }
    
    public void setType(String t){
        type = t;
    }
    
    public void  setCapacity(int number){
        capacity = number;
    }
    
    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("Nastepniki: " );
        // nastepniki:
        for(int successor : successors){
            buffer.append(successor + " ");
        }
        buffer.append("\nPoprzedniki: ");
        // poprzedniki:
        for(int predecessor : predecessors){
            buffer.append(predecessor + " ");
        }
        buffer.append("\n");
        return "Miejsce: " + name + " tokenów: " + tokenCount + "\ntyp: " + type + "\npojemność: " + capacity + "\n" + buffer.toString();
    }
}