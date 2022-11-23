/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.components;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ASUS
 */
public class Face {
    
    @Getter
    private List<Edge> adjacentEdges = new ArrayList<>();
    @Getter @Setter
    private boolean tagged = false;
    @Getter
    private final int id;
    @Getter @Setter
    private int priority = 0;
    
    public Face(int id){
        this.id = id;
    }

    public Face(int id, List<Edge> edges) {
        this(id);
        adjacentEdges = edges;
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
    
    public ArrayList<Edge> getAdjacentEdgesCOPY(){
        return new ArrayList<>(this.adjacentEdges);
    }
    
    public void addEdgeAfter(Edge currentEdge, Edge newEdge){
        List<Edge> newList = new ArrayList<>();
        for(Edge e: this.adjacentEdges){
            newList.add(e);
            if (e == currentEdge) newList.add(newEdge);
        }
        this.adjacentEdges = newList;
    }

    public void addEdgeBefore(Edge currentEdge, Edge newEdge ) {
        List<Edge> newList = new ArrayList<>();
        for(Edge e: this.adjacentEdges){
            if (e == currentEdge) newList.add(newEdge);
            newList.add(e);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Face ").append(id).append(" : ");
        for (Edge e: adjacentEdges) sb.append(e.getId()).append(" ");
        sb.append("\n");
        return sb.toString();
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

    public List<Face> getIncidentFaces(){
        return adjacentEdges.stream().map((e) -> {return e.getOppositeFace(this);}).collect(Collectors.toList());
    }

    public int getIndexOfIncidentFace(Face f){
        List<Face> incidentFaces = getIncidentFaces();
        for (int i=0; i<incidentFaces.size(); i++){
            if (incidentFaces.get(i) == f) return i;
        }
        throw new RuntimeException("Face not found");
    }

    public void removeEdge(Edge e){
        adjacentEdges.remove(e);
    }

    /*Normal form implies that the sequence representing the cyclical sequence of edges starts with an edge of smallest id.*/
    public void toNormalForm() {
        Edge minimal = adjacentEdges.stream().min((e1,e2) -> e1.getId() < e2.getId() ? 0 : 1).get();
        while (adjacentEdges.get(0) != minimal) {
            adjacentEdges.add(adjacentEdges.remove(0));
        }
    }
    
}
