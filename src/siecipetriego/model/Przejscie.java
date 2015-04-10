/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siecipetriego.model;

/**
 *
 * @author Mateusz
 */
public class Przejscie extends Vertex{
    
    
    private int priority;       // priorytet przejścia -1 oznaczać bedzie brak priorytetu
    private int time;           // czas przejścia -1 oznaczać będzie brak czasu
    
    public Przejscie(int id){
        super(id);
        name = "T" + id;
        priority = -1;
        time = -1;
    }
    
    public void setPriority(int priority){
        this.priority = priority;
    }
    
    public void setTime(int time){
        this.time = time;
    }
    
    public int getPriority(){
        return priority;
    }
    
    public int getTime(){
        return time;
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
        return "Przejście: " + name + "\n ID: " + id + "\nPriorytet: " + priority + "\nCzas: " + time + "\n" + buffer.toString();
    }
}