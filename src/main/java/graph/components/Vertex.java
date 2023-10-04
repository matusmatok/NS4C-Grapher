/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.components;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    public enum CycleVertexState {
        UNDISCOVERED,
        CHOICE,
        DETERMINISTIC_INCLUDE_REST,
        DETERMINISTIC_INCLUDE_REMAINING,
        DETERMINISTIC_EXCLUDE_REMAINING,
        COVERED
    }

    public enum ConsistentTwoFactorState {
        P_UNDISCOVERED,
        P_CHOICE,
        P_DETERMINISTIC_INCLUDE_REST,
        P_DETERMINISTIC_INCLUDE_REMAINING,
        P_DETERMINISTIC_EXCLUDE_REMAINING,
        P_COVERED,
        N_UNDISCOVERED,
        N_END_PATH_CHOICE,
        N_IN_EX_CHOICE,
        N_DETERMINISTIC_INCLUDE,
        N_DETERMINISTIC_COVER_EXCLUDE,
        N_DETERMINISTIC_EXCLUDE,
        N_COVERED,
        N_EXCLUDED
    }

    @Getter @Setter
    private CycleVertexState cycleState = CycleVertexState.UNDISCOVERED;
    @Getter @Setter
    private ConsistentTwoFactorState consistentTwoFactorState;
    @Getter @Setter
    private int weight;

    public Vertex(int id){
        this.id = id;
        this.adjacentEdges = new ArrayList<>();
    }
    
    public void addEdge(Edge e){
        this.adjacentEdges.add(e);
    }

    public void addAllEdges(final Edge e0, final Edge e1, final Edge e2) {
        if (!adjacentEdges.isEmpty()) throw new UnsupportedOperationException("Vertex already has edges assigned");
        adjacentEdges.add(e0);
        adjacentEdges.add(e1);
        adjacentEdges.add(e2);
    }
    
    public Face getThirdFace(Face f1, Face f2){
        for (Edge e: adjacentEdges){
            Face temp = e.getOppositeFace(f1);
            if (temp != f2) return temp;
        }
        return null;
    }

    public Face getOppositeFace(final Edge edge) {
        if (edge.getVFrom() != this && edge.getVTo() != this) {
            throw new IllegalArgumentException("Edge is not adjacent to this vertex");
        }
        for (final Edge e : adjacentEdges) {
            if (e.getLeftFace() != edge.getLeftFace() && e.getLeftFace() != edge.getRightFace()) return e.getLeftFace();
        }
        throw new UnknownError();
    }

    public Edge getOppositeEdge(final Face face) {
        return getAdjacentEdges().stream()
                .filter(edge -> edge.getLeftFace() != face && edge.getRightFace() != face)
                .findAny()
                .orElseThrow();
    }

    public void setSelfPositive() {
        this.weight = 1;
        this.consistentTwoFactorState = ConsistentTwoFactorState.P_UNDISCOVERED;
    }

    public void setSelfNegative() {
        this.weight = 0;
        this.consistentTwoFactorState = ConsistentTwoFactorState.N_UNDISCOVERED;
    }

    public void setSelfPseudoNegative() {
        this.weight = 0;
        this.consistentTwoFactorState = ConsistentTwoFactorState.P_UNDISCOVERED;
    }

    public void print(){
        System.out.print(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertex ").append(id).append(" : ");
        for (Edge e: adjacentEdges) sb.append(e.getId()).append(" ");
        if (consistentTwoFactorState != null) sb.append(weight == 0 ? " N" : " P");
        sb.append("\n");
        return sb.toString();
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

    /*Normal form implies that the sequence representing the cyclical sequence of edges adjacent to this vertex starts with an edge of the lowest id*/
    public void toNormalForm() {
        Edge minimal = adjacentEdges.stream().min((e1,e2) -> e1.getId() < e2.getId() ? 0 : 1).get();
        while (adjacentEdges.get(0) != minimal) {
            adjacentEdges.add(adjacentEdges.remove(0));
        }
    }
}
