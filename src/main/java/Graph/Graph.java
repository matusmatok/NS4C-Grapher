/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

/**
 *
 * @author ASUS
 */
public class Graph {
    ArrayList<Vertex> vertices;
    ArrayList<Face> faces;
    ArrayList<Edge> edges;
    
    public Graph(){
        vertices = new ArrayList<>();
        faces = new ArrayList<>();
        edges = new ArrayList<>();
    }
    
    public void init(){
        for (int i=0; i<4; i++){
            vertices.add(new Vertex(i));
        }
        
        edges.add(new Edge(0, vertices.get(0), vertices.get(1)));
        edges.add(new Edge(1, vertices.get(0), vertices.get(3)));
        edges.add(new Edge(2, vertices.get(0), vertices.get(2)));
        edges.add(new Edge(3, vertices.get(2), vertices.get(3)));
        edges.add(new Edge(4, vertices.get(1), vertices.get(3)));
        edges.add(new Edge(5, vertices.get(1), vertices.get(2)));
        
        for (int i=0; i<4; i++){
            faces.add(new Face(i));
        }
        
        faces.get(0).addEdge(edges.get(0));
        faces.get(0).addEdge(edges.get(5));
        faces.get(0).addEdge(edges.get(2));
        
        faces.get(1).addEdge(edges.get(0));
        faces.get(1).addEdge(edges.get(1));
        faces.get(1).addEdge(edges.get(4));
        
        faces.get(2).addEdge(edges.get(1));
        faces.get(2).addEdge(edges.get(2));
        faces.get(2).addEdge(edges.get(3));
        
        faces.get(3).addEdge(edges.get(3));
        faces.get(3).addEdge(edges.get(5));
        faces.get(3).addEdge(edges.get(4));
        
        edges.get(0).addFaces(faces.get(1), faces.get(0));
        edges.get(1).addFaces(faces.get(2), faces.get(1));
        edges.get(2).addFaces(faces.get(0), faces.get(2));
        edges.get(3).addFaces(faces.get(3), faces.get(2));
        edges.get(4).addFaces(faces.get(1), faces.get(3));
        edges.get(5).addFaces(faces.get(3), faces.get(0));
        
        vertices.get(0).addEdge(edges.get(2));
        vertices.get(0).addEdge(edges.get(1));
        vertices.get(0).addEdge(edges.get(0));
        
        vertices.get(1).addEdge(edges.get(0));
        vertices.get(1).addEdge(edges.get(4));
        vertices.get(1).addEdge(edges.get(5));
        
        vertices.get(2).addEdge(edges.get(2));
        vertices.get(2).addEdge(edges.get(5));
        vertices.get(2).addEdge(edges.get(3));
        
        vertices.get(3).addEdge(edges.get(4));
        vertices.get(3).addEdge(edges.get(1));
        vertices.get(3).addEdge(edges.get(3));
    }
    
    public void op3(Vertex v){
        ArrayList<Vertex> newV = new ArrayList<>();
        newV.add(new Vertex(v.id));
        newV.add(new Vertex(vertices.size()));
        newV.add(new Vertex(vertices.size() + 1));
        
        Face newFace = new Face(faces.size());
        
        for(int i=0; i<3; i++){
            Edge newE = new Edge(edges.size(), newV.get((i+2)%3), newV.get(i));
            newFace.addEdge(newE);
            Face leftFace = v.adjacentEdges.get(i).getLeftFromPOV(v);
            newE.addFaces(leftFace, newFace);
            leftFace.addEdgeAfter(v.adjacentEdges.get(i), newE);
            edges.add(newE);
            v.adjacentEdges.get(i).exchange(v, newV.get(i));
            newV.get(i).addEdge(newE);
            newV.get(i).addEdge(v.adjacentEdges.get(i));    
        }
        
        for (int i=0; i<3; i++) newV.get(i).addEdge(edges.get(edges.size() - 3 + (i+1)%3));
        
        this.vertices.set(v.id, newV.get(0));
        this.vertices.add(newV.get(1));
        this.vertices.add(newV.get(2));
        this.faces.add(newFace);
        
    }
    
    public void undoOp3(Face f){
        int smallestId = f.getVertexOfSmallestId();
        Vertex newVertex = new Vertex(smallestId);
        
        ArrayList<Edge> internalEdges = f.adjacentEdges;
        Edge e1 = vertices.get(smallestId).getNonInternalEdge(internalEdges);
        e1.exchange(vertices.get(smallestId), newVertex);
        Edge e2 = vertices.get(vertices.size() - 2).getNonInternalEdge(internalEdges);
        e2.exchange(vertices.get(vertices.size() - 2), newVertex);
        Edge e3 = vertices.get(vertices.size() - 1).getNonInternalEdge(internalEdges);
        e3.exchange(vertices.get(vertices.size() - 1), newVertex);
        
        newVertex.addEdge(e1);
        newVertex.addEdge(e2);
        newVertex.addEdge(e3);
        
        for (Edge e: internalEdges){
            e.removeSelfFromOutterFace(f);
        }
        
        faces.remove(faces.size() - 1);
        for (int i=0; i<3; i++) edges.remove(edges.size() -1);
        vertices.set(smallestId, newVertex);
        vertices.remove(vertices.size() - 1);
        vertices.remove(vertices.size() - 1);
        
        
    }
    
    public void untagFaces(){
        for (Face f: faces) f.tag = false;
    }

    public void printFacePriorities(){
        for (Face f: faces) System.out.print(f.priority + " ");
        System.out.println();
    }
    
    public void print(){
        for (Vertex v: vertices) v.print();
        for (Edge e: edges) e.print();
        for (Face f: faces) f.print();
    }
}
