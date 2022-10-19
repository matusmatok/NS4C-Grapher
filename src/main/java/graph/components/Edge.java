/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.components;

import lombok.Getter;

/**
 *
 * @author ASUS
 */
public class Edge {
    @Getter
    private Vertex vFrom;
    @Getter
    private Vertex vTo;
    @Getter
    private Face rightFace;
    @Getter
    private Face leftFace;
    @Getter
    private final int id;
    
    public Edge(int id, Vertex from, Vertex to){
        this.id = id;
        this.vFrom = from;
        this.vTo = to;
    }
    
    public Edge(int id, Vertex from, Vertex to, Face right, Face left){
        this(id, from, to);
        this.rightFace = right;
        this.leftFace = left;
    }
    
    public void removeSelfFromOutterFace(Face innerFace){
        if (leftFace == innerFace) rightFace.removeEdge(this);
        else leftFace.removeEdge(this);
    }
    
    public void exchange(Vertex oldV, Vertex newV){
        if (this.vFrom == oldV) this.vFrom = newV;
        else if (this.vTo == oldV) this.vTo = newV;
        else System.err.println("Trying to a exchange vertex not present in this edge.");
    }
    
    public Face getLeftFromPOV(Vertex v){
        if (v == this.vFrom) return this.leftFace;
        if (v == this.vTo) return this.rightFace;
        System.err.println("Trying to get left face from the point of view of a vertex not present in this edge.");
        return null;
    }
    
    public void addFaces(Face f1, Face f2){
        this.leftFace = f1;
        this.rightFace = f2;
    }
    
    public Face getUntagedFace(){
        if (!rightFace.isTagged()) return rightFace;
        if (!leftFace.isTagged()) return leftFace;
        System.err.println("Edge.getUntagedFace() : both Faces are untaged.");
        return null;
    }
    
    public Face getOppositeFace(Face f){
        if (rightFace == f) return leftFace;
        else return rightFace;
    }
    
    public void print(){
        System.out.println("Edge " + id + " : " + vFrom.getId() + " " + vTo.getId() + " L: " + leftFace.getId() + " R: " + rightFace.getId());
    }
}
