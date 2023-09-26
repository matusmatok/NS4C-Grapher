/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.components;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ASUS
 */
public class Edge {
    @Getter @Setter
    private Vertex vFrom;
    @Getter @Setter
    private Vertex vTo;
    @Getter @Setter
    private Face rightFace;
    @Getter @Setter
    private Face leftFace;
    @Getter
    private final int id;

    public enum hamCycleEdgeState {
        UNDISCOVERED,
        INCLUDED,
        EXCLUDED
    }

    @Getter @Setter
    private hamCycleEdgeState hamCycleState;

    public Edge(int id, Vertex from, Vertex to){
        this.id = id;
        this.vFrom = from;
        this.vTo = to;
        setHamCycleState(hamCycleEdgeState.UNDISCOVERED);
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
    
    public void exchangeVertex(Vertex oldV, Vertex newV){
        if (this.vFrom == oldV) this.vFrom = newV;
        else if (this.vTo == oldV) this.vTo = newV;
        else throw new RuntimeException("Trying to exchange a vertex not adjacent to this edge.");
    }
    public void exchangeFace(Face oldF, Face newF) {
        if (leftFace == oldF) leftFace = newF;
        else if (rightFace == oldF) rightFace = newF;
        else throw new RuntimeException("Trying to exchange a face not adjacent to this edge.");
    }
    
    public Face getLeftFromPOV(Vertex v){
        if (v == this.vFrom) return this.leftFace;
        else if (v == this.vTo) return this.rightFace;
        else throw new RuntimeException("Trying to get left face from the point of view of a vertex not present in this edge.");
    }

    public Face getRightFromPOV(Vertex v){
        if (v == this.vFrom) return this.rightFace;
        else if (v == this.vTo) return this.leftFace;
        else throw new RuntimeException("Trying to get left face from the point of view of a vertex not present in this edge.");
    }

    public Face getFaceOfBiggerId() {
        return leftFace.getId() > rightFace.getId() ? leftFace : rightFace;
    }

    public void addFaces(Face left, Face right){
        this.leftFace = left;
        this.rightFace = right;
    }

    public Vertex getLeftFromPOVofFace(Face f) {
        if (f == rightFace) return vFrom;
        if (f == leftFace) return vTo;
        return null;
    }

    public Vertex getRightFromPOVofFace(Face f) {
        if (f == rightFace) return vTo;
        if (f == leftFace) return vFrom;
        return null;
    }
    
    public Face getUntagedFace(){
        if (!rightFace.isTagged()) return rightFace;
        if (!leftFace.isTagged()) return leftFace;
        return null;
    }
    
    public Face getOppositeFace(Face f){
        if (rightFace == f) return leftFace;
        else return rightFace;
    }
    
    public void print(){
        System.out.print(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Edge ").append(id).append(" : ").append(vFrom.getId()).append(" ").append(vTo.getId())
                .append(" Left: ").append(leftFace.getId()).append(" Right: ").append(rightFace.getId()).append("\n");
        return sb.toString();
    }

    public Vertex getOppositeVertex(Vertex v) {
        if (vFrom == v) return vTo;
        return vFrom;
    }

    /*normal form implies that vFrom.id() < vTo.id()*/
    public void toNormalForm() {
        if (vFrom.getId() > vTo.getId()) {
            Vertex tmp = vFrom;
            vFrom = vTo;
            vTo = tmp;
            Face tmpf = leftFace;
            leftFace = rightFace;
            rightFace = tmpf;
        }
    }
}
