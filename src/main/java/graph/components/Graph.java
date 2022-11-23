/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.components;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author ASUS
 */
public class Graph {
    @Getter private final ArrayList<Vertex> vertices;
    @Getter private final ArrayList<Face> faces;
    @Getter private final ArrayList<Edge> edges;
    
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
    
    public Face op3(Vertex v){
        ArrayList<Vertex> newV = new ArrayList<>();
        newV.add(new Vertex(v.getId()));
        newV.add(new Vertex(vertices.size()));
        newV.add(new Vertex(vertices.size() + 1));
        
        Face newFace = new Face(faces.size());
        
        for(int i=0; i<3; i++){
            Edge newE = new Edge(edges.size(), newV.get((i+2)%3), newV.get(i));
            newFace.addEdge(newE);
            Face leftFace = v.getAdjacentEdges().get(i).getLeftFromPOV(v);
            newE.addFaces(leftFace, newFace);
            leftFace.addEdgeAfter(v.getAdjacentEdges().get(i), newE);
            edges.add(newE);
            v.getAdjacentEdges().get(i).exchangeVertex(v, newV.get(i));
            newV.get(i).addEdge(newE);
            newV.get(i).addEdge(v.getAdjacentEdges().get(i));
        }
        
        for (int i=0; i<3; i++) newV.get(i).addEdge(edges.get(edges.size() - 3 + (i+1)%3));
        
        this.vertices.set(v.getId(), newV.get(0));
        this.vertices.add(newV.get(1));
        this.vertices.add(newV.get(2));
        this.faces.add(newFace);

        return newFace;
    }
    
    public void undoOp3(Face f){
        int smallestId = f.getVertexOfSmallestId();
        Vertex newVertex = vertices.get(smallestId);

        /*edges might have gotten mixed thix way. refactor it*/
        List<Edge> internalEdges = f.getAdjacentEdges();
        Edge e1 = vertices.get(smallestId).getNonInternalEdge(internalEdges);
        e1.exchangeVertex(vertices.get(smallestId), newVertex);
        Edge e2 = vertices.get(vertices.size() - 2).getNonInternalEdge(internalEdges);
        e2.exchangeVertex(vertices.get(vertices.size() - 2), newVertex);
        Edge e3 = vertices.get(vertices.size() - 1).getNonInternalEdge(internalEdges);
        e3.exchangeVertex(vertices.get(vertices.size() - 1), newVertex);

        newVertex.getAdjacentEdges().removeAll(newVertex.getAdjacentEdges());
        newVertex.addEdge(e1);
        newVertex.addEdge(e2);
        newVertex.addEdge(e3);
        
        for (Edge e: internalEdges){
            e.removeSelfFromOutterFace(f);
        }
        
        faces.remove(faces.size() - 1);
        for (int i=0; i<3; i++) edges.remove(edges.size() -1);
        vertices.remove(vertices.size() - 1);
        vertices.remove(vertices.size() - 1);
    }

    public Edge op4(Face formerFace, int idOfInitialEdge) {
        return operation(4, formerFace, idOfInitialEdge);
    }

    public void undoOp4(Edge middleEdge) {
        undoOperation(4, middleEdge);
    }

    public Edge op5(Face formerFace, int idOfInitialEdge) {
        return operation(5, formerFace, idOfInitialEdge);
    }

    public void undoOp5(Edge middleEdge) {
        undoOperation(5, middleEdge);
    }

    public Edge operation(int type, Face formerFace, int idOfInitialEdge) {
        if (type<4 || type>5) throw new RuntimeException("Unsupported type");
        if (type > formerFace.size()) throw new RuntimeException("The face is too small to perform this operation on.");
        final Vertex newStartVertex = new Vertex(vertices.size());
        vertices.add(newStartVertex);
        final Vertex newEndVertex = new Vertex(vertices.size());
        vertices.add(newEndVertex);

        List<Edge> formerFaceEdges = formerFace.getAdjacentEdges();
        final Edge newMiddleEdge = new Edge(edges.size(), newStartVertex,newEndVertex);
        edges.add(newMiddleEdge);

        int indexOfInitialEdge = 0;
        for(Edge e: formerFaceEdges) {
            if (e.getId() == idOfInitialEdge) break;
            indexOfInitialEdge++;
        }
        final Edge oldPreviousEdge = formerFaceEdges.get(indexOfInitialEdge);
        List<Edge> newFaceEdges = new ArrayList<>();

        newFaceEdges.add(formerFaceEdges.get((indexOfInitialEdge +1) % formerFaceEdges.size()));
        formerFaceEdges.set((indexOfInitialEdge +1) % formerFaceEdges.size(), newMiddleEdge);
        if (type == 5) {
            newFaceEdges.add(formerFaceEdges.remove((indexOfInitialEdge + 2) % formerFaceEdges.size()));
        }

        final Edge oldFollowingEdge = formerFaceEdges.get((indexOfInitialEdge + 2) % formerFaceEdges.size());
        oldPreviousEdge.exchangeVertex(oldPreviousEdge.getRightFromPOVofFace(formerFace), newStartVertex);
        oldFollowingEdge.exchangeVertex(oldFollowingEdge.getLeftFromPOVofFace(formerFace), newEndVertex);

        final Vertex mostLeftOldVertex = newFaceEdges.get(0).getLeftFromPOVofFace(formerFace);
        final Vertex mostRightOldVertex = newFaceEdges.get(newFaceEdges.size() -1).getRightFromPOVofFace(formerFace);
        final Edge newFollowingEdge = new Edge(edges.size(), mostRightOldVertex, newEndVertex);
        edges.add(newFollowingEdge);
        newFaceEdges.add(newFollowingEdge);
        newFaceEdges.add(newMiddleEdge);
        final Edge newPreviousEdge = new Edge(edges.size(), newStartVertex, mostLeftOldVertex);
        edges.add(newPreviousEdge);
        newFaceEdges.add(newPreviousEdge);

        mostLeftOldVertex.exchangeEdge(oldPreviousEdge, newPreviousEdge);
        mostRightOldVertex.exchangeEdge(oldFollowingEdge, newFollowingEdge);

        newPreviousEdge.exchangeVertex(mostLeftOldVertex, newStartVertex);
        newFollowingEdge.exchangeVertex(mostRightOldVertex, newEndVertex);

        newStartVertex.addEdge(newPreviousEdge);
        newStartVertex.addEdge(newMiddleEdge);
        newStartVertex.addEdge(oldPreviousEdge);

        newEndVertex.addEdge(oldFollowingEdge);
        newEndVertex.addEdge(newMiddleEdge);
        newEndVertex.addEdge(newFollowingEdge);

        final Face newFace = new Face(faces.size(), newFaceEdges);
        faces.add(newFace);
        final Face previousOtherFace = oldPreviousEdge.getOppositeFace(formerFace);
        final Face followingOtherFace = oldFollowingEdge.getOppositeFace(formerFace);
        previousOtherFace.addEdgeBefore(oldPreviousEdge,newPreviousEdge);
        followingOtherFace.addEdgeAfter(oldFollowingEdge,newFollowingEdge);

        newMiddleEdge.setLeftFace(newFace);
        newMiddleEdge.setRightFace(formerFace);

        newFollowingEdge.setRightFace(newFace);
        newFollowingEdge.setLeftFace(followingOtherFace);

        newPreviousEdge.setRightFace(newFace);
        newPreviousEdge.setLeftFace(previousOtherFace);

        newFaceEdges.get(0).exchangeFace(formerFace, newFace);
        if (type == 5) newFaceEdges.get(1).exchangeFace(formerFace, newFace);

        return newMiddleEdge;
    }

    public void undoOperation(int type, Edge middleEdge) {
        final Face newFace;
        final Face oldFace;
        if (middleEdge.getLeftFace().getId() < middleEdge.getRightFace().getId()) {
            oldFace = middleEdge.getLeftFace();
            newFace = middleEdge.getRightFace();
        }
        else {
            oldFace = middleEdge.getRightFace();
            newFace = middleEdge.getLeftFace();
        }

        int indexOfMiddleEdgeInOldFace = oldFace.getIndexOfE(middleEdge);
        int indexOfMiddleEdgeInNewFace = newFace.getIndexOfE(middleEdge);

        final Edge newPreviousEdge = newFace.getEdge((indexOfMiddleEdgeInNewFace + 1) % newFace.size());
        System.out.println("newPreviousEdge " + newPreviousEdge.getId());
        final Edge newFollowingEdge = newFace.getEdge((indexOfMiddleEdgeInNewFace - 1 + newFace.size()) % newFace.size());
        System.out.println("newFollowingEdge " + newFollowingEdge.getId());
        final Edge oldPreviousEdge = oldFace.getEdge((indexOfMiddleEdgeInOldFace - 1 + oldFace.size()) % oldFace.size());

        System.out.println("oldPreviousEdge  " + oldPreviousEdge.getId());
        final Edge oldFollowingEdge = oldFace.getEdge((indexOfMiddleEdgeInOldFace + 1) % oldFace.size());
        System.out.println("oldFollowingEdge " + oldFollowingEdge.getId());

        final Vertex newStartVertex = middleEdge.getLeftFromPOVofFace(oldFace);
        System.out.println("newStartVertex " + newStartVertex.getId());
        final Vertex newEndVertex = middleEdge.getRightFromPOVofFace(oldFace);
        System.out.println("newEndVertex " + newEndVertex.getId());

        final Edge oppositeEdge = newFace.getEdge((indexOfMiddleEdgeInNewFace + 2) % newFace.size());
        System.out.println("oppositeEdge " + oppositeEdge.getId());
        oppositeEdge.exchangeFace(newFace, oldFace);
        final Vertex mostLeftVertexOfOldEdges = oppositeEdge.getLeftFromPOVofFace(oldFace);
        mostLeftVertexOfOldEdges.exchangeEdge(newPreviousEdge, oldPreviousEdge);
        oldPreviousEdge.exchangeVertex(newStartVertex, mostLeftVertexOfOldEdges);
        oldFace.getAdjacentEdges().set(indexOfMiddleEdgeInOldFace, oppositeEdge);
        final Vertex mostRightVertexOfOldEdges;
        if (type == 5) {
            final Edge oppositeEdge2 = newFace.getEdge((indexOfMiddleEdgeInNewFace - 2 +newFace.size()) % newFace.size());
            oppositeEdge2.exchangeFace(newFace, oldFace);
            oldFace.addEdgeAfter(oppositeEdge, oppositeEdge2);
            mostRightVertexOfOldEdges = oppositeEdge2.getRightFromPOVofFace(oldFace);
        }
        else {
            mostRightVertexOfOldEdges = oppositeEdge.getRightFromPOVofFace(oldFace);
        }
        mostRightVertexOfOldEdges.exchangeEdge(newFollowingEdge, oldFollowingEdge);
        oldFollowingEdge.exchangeVertex(newEndVertex, mostRightVertexOfOldEdges);

        newPreviousEdge.removeSelfFromOutterFace(newFace);
        newFollowingEdge.removeSelfFromOutterFace(newFace);

        faces.remove(faces.size() - 1);
        vertices.remove(vertices.size() - 1);
        vertices.remove(vertices.size() - 1);
        edges.remove(edges.size() - 1);
        edges.remove(edges.size() - 1);
        edges.remove(edges.size() - 1);
    }
    
    public void untagFaces(){
        for (Face f: faces) f.setTagged(false);
    }

    public void printFacePriorities(){
        for (Face f: faces) System.out.print(f.getPriority() + " ");
        System.out.println();
    }
    
    public void print(){
        for (Vertex v: vertices) v.print();
        for (Edge e: edges) e.print();
        for (Face f: faces) f.print();
    }

    public void printHamCycleStates() {
        for (Vertex v: vertices) System.out.println("Vertex " + v.getId() + " State: " + v.getHamCycleState());
        for (Edge e: edges) System.out.println("Edge " + e.getId() + " State: " + e.getHamCycleState());
    }

    public int minimalFaceSize(){
        return faces.stream().map(Face::size).min(Integer::compareTo).get();
    }
}
