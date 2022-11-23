/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.components;

import java.util.ArrayList;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ASUS
 */
public class Vertex {
    @Getter
    private final int id;
    @Getter
    private final ArrayList<Edge> adjacentEdges;

    /*UNION FIND variables*/
    @Getter @Setter
    private Vertex parent = null;
    @Getter @Setter
    private int subtreeDepth = 0;
    @Getter @Setter
    private int subtreeSize = 1;
    /*------*/

    public enum HamCycleVertexState {
        UNDISCOVERED,
        CHOICE,
        DETERMINISTIC_INCLUDE_REST,
        DETERMINISTIC_INCLUDE_REMAINING,
        DETERMINISTIC_EXCLUDE_REMAINING,
        COVERED
    }

    @Getter @Setter
    private HamCycleVertexState hamCycleState = HamCycleVertexState.UNDISCOVERED;
    
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
            System.out.print(e.getId() + " ");
        }
        System.out.println();
    }
    
    public Edge getNonInternalEdge(List<Edge> internalEdges){
        
        for (Edge e: adjacentEdges){
            if (!internalEdges.contains(e)) return e;
        }
        
        System.err.println("No noninternal edge.");
        return null;
    }

    public Edge getAnyUndiscoveredEdge(){
        for (Edge e: adjacentEdges) {
            if (e.getHamCycleState() == Edge.hamCycleEdgeState.UNDISCOVERED) return e;
        }
        throw new RuntimeException("Vertex has no undiscovered edge.");
    }

    public void exchangeEdge(Edge oldE, Edge newE) {
        for (int i=0; i<adjacentEdges.size(); i++) {
            if (adjacentEdges.get(i) == oldE) {
                adjacentEdges.set(i, newE);
                return;
            }
        }
        throw new RuntimeException("Trying to exchange a non-present edge.");
    }
}
