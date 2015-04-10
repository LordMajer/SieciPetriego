/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siecipetriego.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author Mateusz
 */
public class CustomGraph{
    
    HashMap<Integer, Vertex> vertices;
    HashMap<String, Edge> edges;
    int licznik;                                    // unikatowe id dla wierzchołków
    int miejscaCount;
    int przejsciaCount;
    
    
    public CustomGraph(){
        vertices = new HashMap<Integer, Vertex>();
        edges = new HashMap<String, Edge>();
        licznik = 3;
        miejscaCount = 0;
        przejsciaCount = 0;
        initialize();
    }
    
    /**
     * Metoda inicjalizująca- dodaje jedno miejsce jedno przeście i dwie krawędzie
     */
    public void initialize(){
        Miejsce miejsce = new Miejsce(1);
        Przejscie przejscie = new Przejscie(2);
        Edge edge = new Edge(miejsce.getID(),przejscie.getID());
        
        vertices.put(miejsce.getID(), miejsce);
        vertices.put(przejscie.getID(), przejscie);
        addEdge(edge);
    }
    
    /**
     * Zwraca wszystkie wierzcholki nalezące do grafu
     * @return 
     */
    public HashMap<Integer, Vertex> getVertices(){
        return vertices;
    }
    
    /**
     * Zwraca wszystkie krawędzie należące do grafu
     * @return 
     */
    public HashMap<String, Edge> getEdges(){
        return edges;
    }
    
    /**
     * Pobranie wierzchołka o podanym id
     * @param id
     * @return 
     */
    public Vertex getVertex(int id){
        return vertices.get(id);
    }
    
    /**
     * Pobranie krawedzi o podanym kluczu
     * @param key
     * @return 
     */
    public Edge getEdge(String key){
        return edges.get(key);
    }
    
    /**
     * Dodawanie krawędzi do grafu
     * @param edge 
     */
    public void addEdge(Edge edge){
        
        Vertex sourceVertex = vertices.get(edge.getSourceId());
        sourceVertex.addSuccessor(edge.getDestinationId());
        Vertex destinationVertex = vertices.get(edge.getDestinationId());
        destinationVertex.addPredecessor(edge.getSourceId());
        
        edges.put(edge.getKey(), edge);
    }
    
    /**
     * Dodawanie wierzchołka do grafu
     * @param vertex 
     */
    public void addVertex(Vertex vertex){
        if(vertex instanceof Miejsce){                                          // aktualizacja ilości miejsc i przejść
            miejscaCount++;
        }else{
            przejsciaCount++;
        }
        vertices.put(vertex.getID(), vertex);
    }
    
    /**
     * Usuwanie krawędzi z grafu
     * @param edge 
     */
    public void removeEdge(Edge edge){
        Vertex sourceVertex = vertices.get(edge.getSourceId());
        sourceVertex.removeSuccessor(edge.getDestinationId());
        Vertex destinationVertex = vertices.get(edge.getDestinationId());
        destinationVertex.removePredecessor(edge.getSourceId());
        edges.remove(edge.getKey());
    }
    
    /**
     * Usuwanie wierzchołka z grafu
     * @param vertex 
     */
    public void removeVertex(Vertex vertex){
        
        Edge tempEdge;
        Vertex temp = vertices.get(vertex.getID());
        
        if(vertex instanceof Miejsce){                                  // aktualizacja ilości miejsc i przejść
            miejscaCount--;
        }else{
            przejsciaCount--;
        }
        
        // usuwanie wszystkich krawedzi dolączonych do wierzcholka
        
        for(int successor : temp.getSuccessors()){                      // usuwanie krawedzi wychodzacych 
            System.out.println("usuwanie krawedzi wychodzacej: ");
            tempEdge = new Edge(temp.getID(),successor);
            edges.remove(tempEdge.getKey());
        }
        
        /*for(Vertex v : vertices.values()){                              // usuwanie krawędzi wchodzacych do wierzcholka
            tempEdge = new Edge(v.getID(),temp.getID());
            edges.remove(tempEdge.getKey());
        }*/
        
        for(int predecessor : temp.getPredecessors()){
            System.out.println("Usuwanie krawedzi wchodzacej: ");
            tempEdge = new Edge(predecessor,temp.getID());
            edges.remove(tempEdge.getKey());
        }
        
        vertices.remove(vertex.getID());                                // usunięcie wierzchołka.
    }
    
    /**
     * Generowanie unikalnego ID dla wierzchołka- po prostu kolejna liczba
     * @return 
     */
    public int getNewID(){
        return licznik++;
    }
    
    /**
     * Sprawdzenie czy wierzchołki są polaczone poprawnie.
     * @return 
     */
    public String validateModel(){
        StringBuffer errorsbuffer = new StringBuffer();
        Vertex vertexToCheck;
        // przejście po wszystkich wierzchołkach i sprawdzenie czy są poprawnie połaczone.
        
        for(Vertex vertex : vertices.values()){
            
            if(vertex instanceof Miejsce){
                
                // następnikami i poprzednikami moga być tylko przejścia
                // sprawdzanie następników:
                for(int successor : vertex.getSuccessors()){
                    vertexToCheck = vertices.get(successor);
                    if(!(vertexToCheck instanceof Przejscie)){
                        errorsbuffer.append("Wierzchołek typu miejsce: "
                                + vertex + " posiada nielegalne połaczenie z wierzchołkiem: " + vertexToCheck + "\n");
                    }
                }
                
                // sprawdzanie poprzedników:
                for(int predecessor : vertex.getPredecessors()){
                    vertexToCheck = vertices.get(predecessor);
                    if(!(vertexToCheck instanceof Przejscie)){
                        errorsbuffer.append("Wierzchołek typu miejsce: "
                                + vertex + " posiada nielegalne połaczenie z wierzchołkiem: " + vertexToCheck + "\n");
                    }
                }
            } else {
                // następnikami i poprzednikami moga być tylko miejsca.
                //sprawdzanie następników
                for(int successor : vertex.getSuccessors()){
                    vertexToCheck = vertices.get(successor);
                    if(!(vertexToCheck instanceof Miejsce)){
                        errorsbuffer.append("Wierzchołek typu miejsce: "
                                + vertex + " posiada nielegalne połaczenie z wierzchołkiem: " + vertexToCheck + "\n");
                    }
                }
                
                // sprawdzanie poprzedników:
                for(int predecessor : vertex.getPredecessors()){
                    vertexToCheck = vertices.get(predecessor);
                    if(!(vertexToCheck instanceof Miejsce)){
                        errorsbuffer.append("Wierzchołek typu miejsce: "
                                + vertex + " posiada nielegalne połaczenie z wierzchołkiem: " + vertexToCheck + "\n");
                    }
                }
            }
        }
        
        if(errorsbuffer.length() > 0){
            return errorsbuffer.toString();
        }else{
            return null;
        }
    }
    
    public TreeMap<Integer, Miejsce> getPlaces(){
        TreeMap<Integer, Miejsce> places = new TreeMap<Integer, Miejsce>();
        
        for(Vertex vertex : vertices.values()){
            if(vertex instanceof Miejsce){
                places.put(vertex.getID(), (Miejsce)vertex);
            }
        }
        return places;
    }
    
    public TreeMap<Integer, Przejscie> getPassages(){
        TreeMap<Integer, Przejscie> passages = new TreeMap<Integer, Przejscie>();
        
        for(Vertex vertex : vertices.values()){
            if(vertex instanceof Przejscie){
                passages.put(vertex.getID(), (Przejscie)vertex);
            }
        }
        return passages;
    }
    
    public int[][] macierzWejsc(){
        
        TreeMap<Integer, Miejsce> places;            // treemap zachowuje kolejnosc kluczy
        TreeMap<Integer, Przejscie> passages;
        int placeLicznik = 0;
        int passageLicznik = 0;
        int[][] tab = new int[miejscaCount][przejsciaCount];
        ArrayList<Integer> idList;
        Edge edge;
        
        places = getPlaces();
        passages = getPassages();
        
        for(Miejsce miejsce : places.values()){
            System.out.println(miejsce);
            for(Przejscie przejscie : passages.values()){
                System.out.println(przejscie);
                idList = przejscie.getSuccessors();
                for(int i = 0; i < przejscie.getSuccessors().size(); i++){
                    if(idList.get(i) == miejsce.getID()){
                        edge = edges.get(new Edge(przejscie.getID(), miejsce.getID()).getKey());
                        tab[placeLicznik][passageLicznik] = edge.getCapacity();
                    }
                }
                passageLicznik++;
            }
            placeLicznik++;
        }
        
        return tab;
    }
    
    public int[][] macierzWyjsc(){
        TreeMap<Integer, Miejsce> places;            // treemap zachowuje kolejnosc kluczy
        TreeMap<Integer, Przejscie> passages;
        int placeLicznik = 0;
        int passageLicznik = 0;
        int[][] tab = new int[miejscaCount][przejsciaCount];
        ArrayList<Integer> idList;
        Edge edge;
        
        return tab;
    }
    
    public int[][] macierzIncydencji(){
        TreeMap<Integer, Miejsce> places;            // treemap zachowuje kolejnosc kluczy
        TreeMap<Integer, Przejscie> passages;
        int placeLicznik = 0;
        int passageLicznik = 0;
        int[][] tab = new int[miejscaCount][przejsciaCount];
        ArrayList<Integer> idList;
        Edge edge;
        
        return tab;
    }
    
    public static void main(String[] args){
        CustomGraph graph = new CustomGraph();
        
        
        System.out.println(graph.getVertices());
        System.out.println("koniec wierzcholkow");
        System.out.println(graph.getEdges());
    }
}