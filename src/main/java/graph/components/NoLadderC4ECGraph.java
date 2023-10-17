package graph.components;

import graph.components.Edge.OrientedEdge;
import graph.utils.EasyList;
import java.util.ArrayList;
import java.util.List;

public class NoLadderC4ECGraph extends AbstractGraph{
    public NoLadderC4ECGraph() {
        super();
    }

    @Override
    public void init() {
        super.init();

        for (int i = 0; i < 14; i++) {
            vertices.add(new Vertex(i));
        }

        for (int i = 0; i < 9; i++) {
            faces.add(new Face(i));
        }

        edges.add(new Edge(0, getVertex(0), getVertex(1), getFace(1), getFace(0)));
        edges.add(new Edge(1, getVertex(0), getVertex(2), getFace(2), getFace(1)));
        edges.add(new Edge(2, getVertex(2), getVertex(3), getFace(3), getFace(1)));
        edges.add(new Edge(3, getVertex(3), getVertex(4), getFace(4), getFace(1)));
        edges.add(new Edge(4, getVertex(1), getVertex(4), getFace(1), getFace(5)));
        edges.add(new Edge(5, getVertex(0), getVertex(12), getFace(0), getFace(2)));
        edges.add(new Edge(6, getVertex(2), getVertex(5), getFace(2), getFace(3)));
        edges.add(new Edge(7, getVertex(3), getVertex(6), getFace(3), getFace(4)));
        edges.add(new Edge(8, getVertex(4), getVertex(8), getFace(4), getFace(5)));
        edges.add(new Edge(9, getVertex(1), getVertex(13), getFace(5), getFace(0)));
        edges.add(new Edge(10, getVertex(5), getVertex(9), getFace(2), getFace(6)));
        edges.add(new Edge(11, getVertex(5), getVertex(6), getFace(6), getFace(3)));
        edges.add(new Edge(12, getVertex(6), getVertex(7), getFace(6), getFace(4)));
        edges.add(new Edge(13, getVertex(7), getVertex(8), getFace(7), getFace(4)));
        edges.add(new Edge(14, getVertex(7), getVertex(10), getFace(6), getFace(7)));
        edges.add(new Edge(15, getVertex(8), getVertex(11), getFace(7), getFace(5)));
        edges.add(new Edge(16, getVertex(9), getVertex(10), getFace(8), getFace(6)));
        edges.add(new Edge(17, getVertex(10), getVertex(11), getFace(8), getFace(7)));
        edges.add(new Edge(18, getVertex(9), getVertex(12), getFace(2), getFace(8)));
        edges.add(new Edge(19, getVertex(11), getVertex(13), getFace(8), getFace(5)));
        edges.add(new Edge(20, getVertex(12), getVertex(13), getFace(0), getFace(8)));

        getVertex(0).addEdge(getEdge(0));
        getVertex(0).addEdge(getEdge(1));
        getVertex(0).addEdge(getEdge(5));

        getVertex(1).addEdge(getEdge(0));
        getVertex(1).addEdge(getEdge(9));
        getVertex(1).addEdge(getEdge(4));

        getVertex(2).addEdge(getEdge(1));
        getVertex(2).addEdge(getEdge(2));
        getVertex(2).addEdge(getEdge(6));

        getVertex(3).addEdge(getEdge(2));
        getVertex(3).addEdge(getEdge(3));
        getVertex(3).addEdge(getEdge(7));

        getVertex(4).addEdge(getEdge(3));
        getVertex(4).addEdge(getEdge(4));
        getVertex(4).addEdge(getEdge(8));

        getVertex(5).addEdge(getEdge(6));
        getVertex(5).addEdge(getEdge(11));
        getVertex(5).addEdge(getEdge(10));

        getVertex(6).addEdge(getEdge(7));
        getVertex(6).addEdge(getEdge(12));
        getVertex(6).addEdge(getEdge(11));

        getVertex(7).addEdge(getEdge(12));
        getVertex(7).addEdge(getEdge(13));
        getVertex(7).addEdge(getEdge(14));

        getVertex(8).addEdge(getEdge(8));
        getVertex(8).addEdge(getEdge(15));
        getVertex(8).addEdge(getEdge(13));

        getVertex(9).addEdge(getEdge(10));
        getVertex(9).addEdge(getEdge(16));
        getVertex(9).addEdge(getEdge(18));

        getVertex(10).addEdge(getEdge(14));
        getVertex(10).addEdge(getEdge(17));
        getVertex(10).addEdge(getEdge(16));

        getVertex(11).addEdge(getEdge(15));
        getVertex(11).addEdge(getEdge(19));
        getVertex(11).addEdge(getEdge(17));

        getVertex(12).addEdge(getEdge(5));
        getVertex(12).addEdge(getEdge(18));
        getVertex(12).addEdge(getEdge(20));

        getVertex(13).addEdge(getEdge(9));
        getVertex(13).addEdge(getEdge(20));
        getVertex(13).addEdge(getEdge(19));

        getFace(0).addEdge(getEdge(0));
        getFace(0).addEdge(getEdge(5));
        getFace(0).addEdge(getEdge(20));
        getFace(0).addEdge(getEdge(9));

        getFace(1).addEdge(getEdge(0));
        getFace(1).addEdge(getEdge(4));
        getFace(1).addEdge(getEdge(3));
        getFace(1).addEdge(getEdge(2));
        getFace(1).addEdge(getEdge(1));

        getFace(2).addEdge(getEdge(1));
        getFace(2).addEdge(getEdge(6));
        getFace(2).addEdge(getEdge(10));
        getFace(2).addEdge(getEdge(18));
        getFace(2).addEdge(getEdge(5));

        getFace(3).addEdge(getEdge(2));
        getFace(3).addEdge(getEdge(7));
        getFace(3).addEdge(getEdge(11));
        getFace(3).addEdge(getEdge(6));

        getFace(4).addEdge(getEdge(3));
        getFace(4).addEdge(getEdge(8));
        getFace(4).addEdge(getEdge(13));
        getFace(4).addEdge(getEdge(12));
        getFace(4).addEdge(getEdge(7));

        getFace(5).addEdge(getEdge(4));
        getFace(5).addEdge(getEdge(9));
        getFace(5).addEdge(getEdge(19));
        getFace(5).addEdge(getEdge(15));
        getFace(5).addEdge(getEdge(8));

        getFace(6).addEdge(getEdge(10));
        getFace(6).addEdge(getEdge(11));
        getFace(6).addEdge(getEdge(12));
        getFace(6).addEdge(getEdge(14));
        getFace(6).addEdge(getEdge(16));

        getFace(7).addEdge(getEdge(13));
        getFace(7).addEdge(getEdge(15));
        getFace(7).addEdge(getEdge(17));
        getFace(7).addEdge(getEdge(14));

        getFace(8).addEdge(getEdge(16));
        getFace(8).addEdge(getEdge(17));
        getFace(8).addEdge(getEdge(19));
        getFace(8).addEdge(getEdge(20));
        getFace(8).addEdge(getEdge(18));
    }

    private static void setupB(final Face square, final Edge orientation, final List<Vertex> vertices,
                               final List<Edge> internalEdges, final List<Edge> extrudingEdges) {
        final int orientationsID = square.getIndexOfE(orientation);
        for (int i = 0; i < 4; i++) {
            final int actualIndex = (orientationsID - i + 4) % 4;
            final Edge internalEdge = square.getEdge(actualIndex);
            final Vertex vertex = internalEdge.getRightFromPOVofFace(square);
            final Edge extrudingEdge = vertex.getOppositeEdge(square);
            vertices.add(vertex);
            internalEdges.add(internalEdge);
            extrudingEdges.add(extrudingEdge);
        }
    }

    private void operationDEG(final Edge edge) {
        final Face operandFace = edge.getRightFace();
        final Edge operandEdge = operandFace.getEdge(operandFace.getIndexOfE(edge) - 1);

        op4(operandFace, operandEdge.getId());
    }

    private void reductionDEG() {
        undoOp4(getEdge(getEdges().size() - 3));
    }

    public void operationA(final OrientedEdge orientedEdge) {
        final Edge correspondingEdge = orientedEdge.getCorrespondingEdge();
        final Face rightFace = correspondingEdge.getRightFromPOV(orientedEdge.getOrigin());
        final Edge operandRightFaceEdge = rightFace.getEdge(rightFace.getIndexOfE(correspondingEdge) + 1);
        final Face operandFace = operandRightFaceEdge.getOppositeFace(rightFace);
        final Edge operandEdge = operandFace.getEdge(operandFace.getIndexOfE(operandRightFaceEdge) - 1);

        op5(operandFace, operandEdge.getId());
    }

    public void reductionA() {
        undoOp5(getEdge(getEdges().size() - 3));
    }

    public void operationB(final Face square, final Edge orientation) {
        final List<Vertex> vertices = new ArrayList<>(4);
        final List<Edge> internalEdges = new ArrayList<>(4);
        final List<Edge> extrudingEdges = new ArrayList<>(4);

        setupB(square, orientation, vertices, internalEdges, extrudingEdges);

        final Vertex v5 = new Vertex(getVertices().size());
        final Vertex v6 = new Vertex(getVertices().size() + 1);
        final Vertex v7 = new Vertex(getVertices().size() + 2);
        final Vertex v8 = new Vertex(getVertices().size() + 3);
        final Vertex v9 = new Vertex(getVertices().size() + 4);
        final Vertex v10 = new Vertex(getVertices().size() + 5);

        //exchange vertices in extruding edges
        extrudingEdges.get(0).exchangeVertex(vertices.get(0), v5);
        extrudingEdges.get(1).exchangeVertex(vertices.get(1), v8);
        extrudingEdges.get(2).exchangeVertex(vertices.get(2), v6);

        //add new edges
        final Edge n1 = new Edge(getEdges().size(), vertices.get(0), v5);
        final Edge n2 = new Edge(getEdges().size() + 1, vertices.get(1), v10);
        final Edge n3 = new Edge(getEdges().size() + 2, vertices.get(2), v6);
        final Edge n4 = new Edge(getEdges().size() + 3, v5, v7);
        final Edge n5 = new Edge(getEdges().size() + 4, v6, v9);
        final Edge n6 = new Edge(getEdges().size() + 5, v7, v8);
        final Edge n7 = new Edge(getEdges().size() + 6, v8, v9);
        final Edge n8 = new Edge(getEdges().size() + 7, v9, v10);
        final Edge n9 = new Edge(getEdges().size() + 8, v7, v10);

        //exchange edges in former vertices
        vertices.get(0).exchangeEdge(extrudingEdges.get(0), n1);
        vertices.get(1).exchangeEdge(extrudingEdges.get(1), n2);
        vertices.get(2).exchangeEdge(extrudingEdges.get(2), n3);

        //add edges to new vertices
        v5.addAllEdges(n1, n4, extrudingEdges.get(0));
        v6.addAllEdges(n3, extrudingEdges.get(2), n5);
        v7.addAllEdges(n4, n9, n6);
        v8.addAllEdges(n6, n7, extrudingEdges.get(1));
        v9.addAllEdges(n5, n7, n8);
        v10.addAllEdges(n2, n8, n9);

        //add new faces
        final Face f2 = new Face(getFaces().size(), EasyList.of(n3, n5, n8, n2, internalEdges.get(1)));
        final Face f3 = new Face(getFaces().size() + 1, EasyList.of(internalEdges.get(0), n2, n9, n4, n1));
        final Face f4 = new Face(getFaces().size() + 2, EasyList.of(n9, n8, n7, n6));

        //add edges to old faces
        final Face outsideFace0 = internalEdges.get(0).getOppositeFace(square);
        outsideFace0.removeEdge(internalEdges.get(0));
        outsideFace0.addEdgeAfter(extrudingEdges.get(0), n4);
        outsideFace0.addEdgeAfter(n4, n6);

        final Face outsideFace1 = internalEdges.get(1).getOppositeFace(square);
        outsideFace1.removeEdge(internalEdges.get(1));
        outsideFace1.addEdgeAfter(extrudingEdges.get(1), n7);
        outsideFace1.addEdgeAfter(n7, n5);

        final Face outsideFace2 = internalEdges.get(2).getOppositeFace(square);
        outsideFace2.addEdgeAfter(extrudingEdges.get(2), n3);

        final Face outsideFace3 = internalEdges.get(3).getOppositeFace(square);
        outsideFace3.addEdgeAfter(internalEdges.get(3), n1);

        //exchange faces in internal edges
        internalEdges.get(0).exchangeFace(outsideFace0, f3);
        internalEdges.get(1).exchangeFace(outsideFace1, f2);

        //add faces to new edges
        n1.setLeftFace(f3);
        n1.setRightFace(outsideFace3);

        n2.setLeftFace(f2);
        n2.setRightFace(f3);

        n3.setLeftFace(outsideFace2);
        n3.setRightFace(f2);

        n4.setLeftFace(f3);
        n4.setRightFace(outsideFace0);

        n5.setLeftFace(outsideFace1);
        n5.setRightFace(f2);

        n6.setLeftFace(f4);
        n6.setRightFace(outsideFace0);

        n7.setLeftFace(f4);
        n7.setRightFace(outsideFace1);

        n8.setLeftFace(f4);
        n8.setRightFace(f2);

        n9.setLeftFace(f3);
        n9.setRightFace(f4);

        //add new components to the graph's lists
        getVertices().addAll(EasyList.of(v5, v6, v7, v8, v9, v10));
        getEdges().addAll(EasyList.of(n1, n2, n3, n4, n5, n6, n7, n8, n9));
        getFaces().addAll(EasyList.of(f2, f3, f4));
    }

    public void reductionB(final Face square, final Edge orientation) {
        final List<Vertex> vertices = new ArrayList<>(4);
        final List<Edge> internalEdges = new ArrayList<>(4);
        final List<Edge> extrudingEdges = new ArrayList<>(4);

        setupB(square, orientation, vertices, internalEdges, extrudingEdges);

        final int nrOfFaces = getFaces().size();
        final Face f2 = getFace(nrOfFaces - 3);
        final Face f3 = getFace(nrOfFaces - 2);
        final Face f4 = getFace(nrOfFaces - 1);

        final int nrOfVertices = getVertices().size();
        final Vertex v5 = getVertex(nrOfVertices - 6);
        final Vertex v6 = getVertex(nrOfVertices - 5);
        final Vertex v8 = getVertex(nrOfVertices - 3);

        final int nrOfEdges = getEdges().size();
        final Edge n1 = extrudingEdges.get(0);
        final Edge n3 = extrudingEdges.get(2);
        final Edge n4 = getEdge(nrOfEdges - 6);
        final Edge n5 = getEdge(nrOfEdges - 5);
        final Edge n6 = getEdge(nrOfEdges - 4);
        final Edge n7 = getEdge(nrOfEdges - 3);

        final Edge e1 = v5.getNonInternalEdge(EasyList.of(n1, n4));
        final Edge e2 = v8.getNonInternalEdge(EasyList.of(n6, n7));
        final Edge e3 = v6.getNonInternalEdge(EasyList.of(n3, n5));

        final Face o0 = n4.getOppositeFace(f3);
        final Face o1 = n7.getOppositeFace(f4);
        final Face o2 = n3.getOppositeFace(f2);
        final Face o3 = n1.getOppositeFace(f3);

        //exchange extruding vertices
        vertices.get(0).exchangeEdge(n1, e1);
        e1.exchangeVertex(v5, vertices.get(0));

        vertices.get(1).exchangeEdge(extrudingEdges.get(1), e2);
        e2.exchangeVertex(v8, vertices.get(1));

        vertices.get(2).exchangeEdge(n3, e3);
        e3.exchangeVertex(v6, vertices.get(2));

        //exchange faces in internal edges
        internalEdges.get(0).exchangeFace(f3, o0);
        o0.addEdgeAfter(e1, internalEdges.get(0));

        internalEdges.get(1).exchangeFace(f2, o1);
        o1.addEdgeAfter(e2, internalEdges.get(1));

        //remove edges from outer faces
        o0.removeEdge(n4);
        o0.removeEdge(n6);

        o1.removeEdge(n7);
        o1.removeEdge(n5);

        o2.removeEdge(n3);

        o3.removeEdge(n1);

        for(int i = 0; i < 6; i++) getVertices().remove(getVertices().size() - 1);
        for(int i = 0; i < 9; i++) getEdges().remove(getEdges().size() - 1);
        for(int i = 0; i < 3; i++) getFaces().remove(getFaces().size() - 1);
    }

    public void operationC(final Edge edge) {
        final Face operandFace = edge.getLeftFace();
        op4(operandFace, operandFace.getEdge(operandFace.getIndexOfE(edge) - 1).getId());
    }

    public void reductionC() {
        undoOp4(getEdge(getEdges().size() - 3));
    }

    public void operationD(final Edge edge) {
        operationDEG(edge);
    }

    public void reductionD() {
        reductionDEG();
    }

    public void operationE(final Edge edge) {
        operationDEG(edge);
    }

    public void reductionE() {
        reductionDEG();
    }

    public void operationF(final Face pentagon, final Edge orientation) {
        op4(pentagon, orientation.getId());
        op4(pentagon, getEdge(getEdges().size() - 3).getId());
    }

    public void reductionF() {
        undoOp4(getEdge(getEdges().size() - 3));
        undoOp4(getEdge(getEdges().size() - 3));
    }

    public void operationG(final Edge edge) {
        operationDEG(edge);
    }

    public void reductionG() {
        reductionDEG();
    }

    public void operationH(final Face face, final Edge orientation) {
        op5(face, orientation.getId());
    }

    public void reductionH() {
        undoOp5(getEdge(getEdges().size() - 3));
    }
}
