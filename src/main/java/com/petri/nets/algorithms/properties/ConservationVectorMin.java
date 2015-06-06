/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.petri.nets.algorithms.properties;

import com.petri.nets.model.CustomGraph;
import com.petri.nets.model.Vertex;

/**
 *
 * @author LordMajer
 */
public class ConservationVectorMin {
    CustomGraph coverageGraph;
    int[] vector;

    public ConservationVectorMin(CustomGraph coverageGraph, int[] vector) {
        this.coverageGraph = coverageGraph;
        this.vector = vector;
    }
    
    public String calculate() {
        int sum = 0;
        int i = 0;
            
        
        // liczenie dla pierwszego stanu z listy:
        for (Vertex vertex : coverageGraph.getTransitions().values()) {
            String[] states = vertex.getName().split(",");
            
            
            
            int currentSum = 0;
            int j = 0;
            for (String state : states) {
                if (state.equals("\u221E")) {
                    return "Sieć nie jest zachowawcza względem wektora: \n" + printVector();
                } else {
                    currentSum += Integer.valueOf(state) * vector[j];
                }
                j++;
            }
            if (i++ == 0) {
                sum = currentSum;
            } else {
                if (currentSum != sum) {
                    return "Sieć nie jest zachowawcza względem wektora: \n" + printVector();
                }
            }
        }
        return "Sieć jest zachowawcza względem vektora: \n" + printVector();
    }
    
    public String printVector(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for(int liczba : vector){
            buffer.append(liczba + " ");
        }
        buffer.deleteCharAt(buffer.length()-1);     // usuniecie ostatniej spacji
        buffer.append("]");
        
        return buffer.toString();
    }
    
    public static void main(String[] args){
        ConservationVectorMin censervation= new ConservationVectorMin(new CustomGraph(), new int[]{1,2,3});
        System.out.println(censervation.printVector());
    }
}
