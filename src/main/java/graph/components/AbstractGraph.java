/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.components;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractGraph {
    @Getter protected final ArrayList<Vertex> vertices;
    @Getter protected final ArrayList<Face> faces;
    @Getter protected final ArrayList<Edge> edges;
    private boolean initialized = false;
    
    public AbstractGraph(){
        vertices = new ArrayList<>();
        faces = new ArrayList<>();
        edges = new ArrayList<>();
    }
    
    public void init(){
        if (initialized) throw new UnsupportedOperationException("Graph instance already initialised");
        initialized = true;
    }

    public Vertex getVertex(int id) {
        return vertices.get(id);
    }

    public Edge getEdge(int id) {
        return edges.get(id);
    }

    public Edge getEdge(final int vertexId1, final int vertexId2) {
        return getEdge(getVertex(vertexId1), getVertex(vertexId2));
    }

    public Edge getEdge(final Vertex v1, final Vertex v2) {
        for (final Edge e : v1.getAdjacentEdges()) {
            if (e.getOppositeVertex(v1) == v2) return e;
        }
        throw new IllegalArgumentException("Vertices " + v1.getId() + " " + v2.getId() + " are not incident.");
    }

    public Face getFace(int id) {
        return faces.get(id);
    }

    public Face getFace(final int vertexId1, final int vertexId2, final int vertexId3) {
        return getFace(getVertex(vertexId1), getVertex(vertexId2), getVertex(vertexId3));
    }

    public Face getFace(final Vertex v1, final Vertex v2, final Vertex v3) {
        final Edge e1 = getEdge(v1, v2);
        final Edge e2 = getEdge(v2, v3);

        for (final Face face1 : new Face[]{e1.getRightFace(), e1.getLeftFace()}) {
            for (final Face face2 : new Face[]{e2.getRightFace(), e2.getLeftFace()}) {
                if (face1 == face2) return face1;
            }
        }
        throw new IllegalArgumentException("Vertices are not adjacent to a common face.");
    }

    protected Face op3(int vertexId) {
        return op3(vertices.get(vertexId));
    }
    protected Face op3(Vertex v){
        ArrayList<Vertex> newV = new ArrayList<>();
        newV.add(new Vertex(v.getId()));
        newV.add(new Vertex(vertices.size()));
        newV.add(new Vertex(vertices.size() + 1));
        
        Face newFace = new Face(faces.size());

        try {
            for (int i = 0; i < 3; i++) {
                Edge newE = new Edge(edges.size(), newV.get((i + 2) % 3), newV.get(i));
                newFace.addEdge(newE);
                Face leftFace = v.getAdjacentEdges().get(i).getLeftFromPOV(v);
                newE.addFaces(leftFace, newFace);
                leftFace.addEdgeAfter(v.getAdjacentEdges().get(i), newE);
                edges.add(newE);
                v.getAdjacentEdges().get(i).exchangeVertex(v, newV.get(i));
                newV.get(i).addEdge(newE);
                newV.get(i).addEdge(v.getAdjacentEdges().get(i));
            }
        }
        catch (Exception e) {
            System.out.println("Vertex " + v.getId());
            print();
            throw new RuntimeException();
        }
        
        for (int i=0; i<3; i++) newV.get(i).addEdge(edges.get(edges.size() - 3 + (i+1)%3));
        
        this.vertices.set(v.getId(), newV.get(0));
        this.vertices.add(newV.get(1));
        this.vertices.add(newV.get(2));
        this.faces.add(newFace);

        return newFace;
    }
    
    protected void undoOp3(Face f){
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

    protected Edge op4(int faceId, int idOfInitialEdge) {
        return op4(faces.get(faceId), idOfInitialEdge);
    }
    protected Edge op4(Face formerFace, int idOfInitialEdge) {
        return operation(4, formerFace, idOfInitialEdge);
    }

    protected void undoOp4(Edge middleEdge) {
        undoOperation(4, middleEdge);
    }

    protected Edge op5(int faceId, int idOfInitialEdge) {
        return op5(faces.get(faceId),idOfInitialEdge);
    }
    protected Edge op5(Face formerFace, int idOfInitialEdge) {
        return operation(5, formerFace, idOfInitialEdge);
    }

    protected void undoOp5(Edge middleEdge) {
        undoOperation(5, middleEdge);
    }

    private Edge operation(int type, Face formerFace, int idOfInitialEdge) {
        if (type<4 || type>5) throw new RuntimeException("Unsupported type");
        if (type > formerFace.size()) throw new RuntimeException("The face is too small to perform this operation on.");
        final Vertex newStartVertex = new Vertex(vertices.size());
        vertices.add(newStartVertex);
        final Vertex newEndVertex = new Vertex(vertices.size());
        vertices.add(newEndVertex);

        List<Edge> formerFaceEdges = formerFace.getAdjacentEdges();
        final Edge newMiddleEdge = new Edge(edges.size(), newStartVertex,newEndVertex);
        edges.add(newMiddleEdge);

        final int indexOfInitialEdge = formerFace.getIndexOfE(edges.get(idOfInitialEdge));

        final Edge oldPreviousEdge = formerFaceEdges.get(indexOfInitialEdge);
        List<Edge> newFaceEdges = new ArrayList<>();

        newFaceEdges.add(formerFaceEdges.get((indexOfInitialEdge +1) % formerFaceEdges.size()));
        formerFaceEdges.set((indexOfInitialEdge +1) % formerFaceEdges.size(), newMiddleEdge);

        final Edge oldFollowingEdge = formerFaceEdges.get((indexOfInitialEdge + type - 2) % formerFaceEdges.size());

        if (type == 5) {
            newFaceEdges.add(formerFaceEdges.remove((indexOfInitialEdge + 2) % formerFaceEdges.size()));
        }

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

    private void undoOperation(int type, Edge middleEdge) {
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
        final Edge newFollowingEdge = newFace.getEdge((indexOfMiddleEdgeInNewFace - 1 + newFace.size()) % newFace.size());
        final Edge oldPreviousEdge = oldFace.getEdge((indexOfMiddleEdgeInOldFace - 1 + oldFace.size()) % oldFace.size());
        final Edge oldFollowingEdge = oldFace.getEdge((indexOfMiddleEdgeInOldFace + 1) % oldFace.size());

        final Vertex newStartVertex = middleEdge.getLeftFromPOVofFace(oldFace);
        final Vertex newEndVertex = middleEdge.getRightFromPOVofFace(oldFace);

        final Edge oppositeEdge = newFace.getEdge((indexOfMiddleEdgeInNewFace + 2) % newFace.size());
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

    public List<CubeTripole> getCubes() {
        return this.vertices.stream().filter(vertex
                -> vertex.getAdjacentEdges().stream().map(edge
                    -> edge.getLeftFromPOV(vertex)).filter(face -> face.size() == 4)
                .count() == 3)
                .map(CubeTripole::new).collect(Collectors.toList());
    }
    
    public void untagFaces(){
        for (Face f: faces) f.setTagged(false);
    }

    public void printFacePriorities(){
        for (Face f: faces) System.out.print(f.getPriority() + " ");
        System.out.println();
    }
    
    public void print(){
        System.out.print(this);
    }

    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append("--BEGIN--\n").append("VERTICES:\n");
       for (Vertex v: vertices) sb.append(v.toString());
       sb.append("EDGES:\n");
       for (Edge e: edges) sb.append(e.toString());
       sb.append("FACES:\n");
       for (Face f: faces) sb.append(f.toString());
       sb.append("--END--\n");
       return sb.toString();
    }

    public void printHamCycleStates() {
        for (Vertex v: vertices) System.out.println("Vertex " + v.getId() + " State: " + v.getCycleState());
        for (Edge e: edges) System.out.println("Edge " + e.getId() + " State: " + e.getHamCycleState());
    }

    public int minimalFaceSize(){
        return faces.stream().map(Face::size).min(Integer::compareTo).get();
    }

    /*Normal form implies that each vertex, edge or face is in normal form.*/
    public void toNormalForm() {
        for (Vertex v : vertices) v.toNormalForm();
        for (Edge e : edges) e.toNormalForm();
        for (Face f : faces) f.toNormalForm();
    }


    public void setPositive(Vertex vertex) {
        vertex.setSelfPositive();
    }

    public void setNegative(Vertex vertex) {
        vertex.setSelfNegative();
    }
}
