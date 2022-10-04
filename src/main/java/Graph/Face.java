/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class Face {
    
    public ArrayList<Edge> adjacentEdges = new ArrayList<>();
    public boolean tag = false;
    public int id;
    public int priority = 0;
    
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
            System.out.print(e.id + " ");
        }
        System.out.println();
    }
    
    public int getVertexOfSmallestId(){
        Vertex v1 = adjacentEdges.get(0).vFrom;
        Vertex v2 = adjacentEdges.get(0).vTo;
        Vertex v3 = (adjacentEdges.get(1).vFrom != v1 && adjacentEdges.get(1).vFrom != v2) ? adjacentEdges.get(1).vFrom : adjacentEdges.get(1).vTo;
        
        if (v1.id < v2.id){
            return (v1.id < v3.id) ? v1.id : v3.id;
        }
        else{
            return (v2.id < v3.id) ? v2.id : v3.id;
        }
    }
    
}
