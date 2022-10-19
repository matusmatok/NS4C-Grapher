/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.components;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ASUS
 */
public class Face {
    
    @Getter
    private ArrayList<Edge> adjacentEdges = new ArrayList<>();
    @Getter @Setter
    private boolean tagged = false;
    @Getter
    private final int id;
    @Getter @Setter
    private int priority = 0;
    
    public Face(int id){
        this.id = id;
    }
    
    public void addEdge(Edge e){
        this.adjacentEdges.add(e);
    }
    
    public int getIndexOfE(Edge e){
        for (int i = 0; i<adjacentEdges.size(); i++){
            if (adjacentEdges.get(i) == e) return i;
        }
        return -1;
    }
    
    public int size(){
        return this.adjacentEdges.size();
    }
    
    public Edge getEdge(int index){
        return this.adjacentEdges.get(index);
    }
    
    //TODO
    public ArrayList<Face> getCommonNeighbours(Face other){
        
        return null;
    }
    
    public ArrayList<Face> getNeighbours(){
        ArrayList<Face> result = new ArrayList<>();
        for (Edge e: adjacentEdges){
            result.add(e.getOppositeFace(this));
        }
        return result;
    }
    
    public ArrayList<Edge> getAdjacentEdgesCOPY(){
        return new ArrayList<>(this.adjacentEdges);
    }
    
    public void addEdgeAfter(Edge currentEdge, Edge newEdge){
        ArrayList<Edge> newList = new ArrayList<>();
        for(Edge e: this.adjacentEdges){
            newList.add(e);
            if (e == currentEdge) newList.add(newEdge);
        }
        this.adjacentEdges = newList;
    }
    
    public void print(){
        System.out.print("Face " + id + " : ");
        for(Edge e: adjacentEdges){
            System.out.print(e.getId() + " ");
        }
        System.out.println();
    }
    
    public int getVertexOfSmallestId(){
        Vertex v1 = adjacentEdges.get(0).getVFrom();
        Vertex v2 = adjacentEdges.get(0).getVTo();
        Vertex v3 = (adjacentEdges.get(1).getVFrom() != v1 && adjacentEdges.get(1).getVFrom() != v2) ? adjacentEdges.get(1).getVFrom() : adjacentEdges.get(1).getVTo();
        
        if (v1.getId() < v2.getId()){
            return (v1.getId() < v3.getId()) ? v1.getId() : v3.getId();
        }
        else{
            return (v2.getId() < v3.getId()) ? v2.getId() : v3.getId();
        }
    }

    public void removeEdge(Edge e){
        adjacentEdges.remove(e);
    }
    
}
