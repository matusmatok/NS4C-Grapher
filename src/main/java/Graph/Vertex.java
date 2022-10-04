/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import java.util.ArrayList;

/**
 *
 * @author ASUS
 */
public class Vertex {
    int id;
    ArrayList<Edge> adjacentEdges;
    
    public Vertex(int id){
        this.id = id;
        this.adjacentEdges = new ArrayList<>();
    }
    
    public void addEdge(Edge e){
        this.adjacentEdges.add(e);
    }
    
    public Face getThirdFace(Face f1, Face f2){
        for (Edge e: adjacentEdges){
            Face temp = e.getOppositeFace(f1);
            if (temp != f2) return temp;
        }
        return null;
    }
    
    public void print(){
        System.out.print("Vertex " + id + " : ");
        for(Edge e: adjacentEdges){
            System.out.print(e.id + " ");
        }
        System.out.println();
    }
    
    public Edge getNonInternalEdge(ArrayList<Edge> internalEdges){
        
        for (Edge e: adjacentEdges){
            if (!internalEdges.contains(e)) return e;
        }
        
        System.err.println("No noninternal edge.");
        return null;
    }
}
