package mock;

import graph.components.Edge;
import graph.components.Face;

import graph.components.Vertex;
import java.util.ArrayList;
import java.util.List;

public class FacedTutteFragmentMock extends TutteFragmentMock{
    public FacedTutteFragmentMock(){
        super();
        addFaces();
    }

    private void addFaces() {
        final List<Edge> edges0 = new ArrayList<>();
        edges0.add(edges.get(1));
        edges0.add(edges.get(2));
        edges0.add(edges.get(5));
        edges0.add(edges.get(3));
        faces.add(new Face(0, edges0));

        final List<Edge> edges1 = new ArrayList<>();
        edges1.add(edges.get(3));
        edges1.add(edges.get(7));
        edges1.add(edges.get(10));
        edges1.add(edges.get(8));
        edges1.add(edges.get(4));
        faces.add(new Face(1, edges1));

        final List<Edge> edges2 = new ArrayList<>();
        edges2.add(edges.get(5));
        edges2.add(edges.get(6));
        edges2.add(edges.get(13));
        edges2.add(edges.get(12));
        edges2.add(edges.get(7));
        faces.add(new Face(2, edges2));

        final List<Edge> edges3 = new ArrayList<>();
        edges3.add(edges.get(8));
        edges3.add(edges.get(11));
        edges3.add(edges.get(16));
        edges3.add(edges.get(9));
        faces.add(new Face(3, edges3));

        final List<Edge> edges4 = new ArrayList<>();
        edges4.add(edges.get(10));
        edges4.add(edges.get(12));
        edges4.add(edges.get(14));
        edges4.add(edges.get(18));
        edges4.add(edges.get(11));
        faces.add(new Face(4, edges4));

        final List<Edge> edges5 = new ArrayList<>();
        edges5.add(edges.get(16));
        edges5.add(edges.get(18));
        edges5.add(edges.get(19));
        edges5.add(edges.get(20));
        edges5.add(edges.get(17));
        faces.add(new Face(5, edges5));

        final List<Edge> edges6 = new ArrayList<>();
        edges6.add(edges.get(13));
        edges6.add(edges.get(15));
        edges6.add(edges.get(21));
        edges6.add(edges.get(19));
        edges6.add(edges.get(14));
        faces.add(new Face(6, edges6));

        final List<Edge> edges7 = new ArrayList<>();
        edges7.add(edges.get(0));
        edges7.add(edges.get(1));
        edges7.add(edges.get(4));
        edges7.add(edges.get(9));
        edges7.add(edges.get(17));
        edges7.add(edges.get(22));
        faces.add(new Face(7, edges7));

        final List<Edge> edges8 = new ArrayList<>();
        edges8.add(edges.get(0));
        edges8.add(edges.get(23));
        edges8.add(edges.get(15));
        edges8.add(edges.get(6));
        edges8.add(edges.get(2));
        faces.add(new Face(8, edges8));

        final List<Edge> edges9 = new ArrayList<>();
        edges9.add(edges.get(20));
        edges9.add(edges.get(21));
        edges9.add(edges.get(23));
        edges9.add(edges.get(22));
        faces.add(new Face(9, edges9));

        edges.get(0).addFaces(faces.get(7), faces.get(8));
        edges.get(1).addFaces(faces.get(0), faces.get(7));
        edges.get(2).addFaces(faces.get(8), faces.get(0));
        edges.get(3).addFaces(faces.get(0), faces.get(1));
        edges.get(4).addFaces(faces.get(1), faces.get(7));
        edges.get(5).addFaces(faces.get(2), faces.get(0));
        edges.get(6).addFaces(faces.get(8), faces.get(2));
        edges.get(7).addFaces(faces.get(2), faces.get(1));
        edges.get(8).addFaces(faces.get(1), faces.get(3));
        edges.get(9).addFaces(faces.get(3), faces.get(7));
        edges.get(10).addFaces(faces.get(1), faces.get(4));
        edges.get(11).addFaces(faces.get(4), faces.get(3));
        edges.get(12).addFaces(faces.get(2), faces.get(4));
        edges.get(13).addFaces(faces.get(2), faces.get(6));
        edges.get(14).addFaces(faces.get(6), faces.get(4));
        edges.get(15).addFaces(faces.get(8), faces.get(6));
        edges.get(16).addFaces(faces.get(3), faces.get(5));
        edges.get(17).addFaces(faces.get(5), faces.get(7));
        edges.get(18).addFaces(faces.get(4), faces.get(5));
        edges.get(19).addFaces(faces.get(6), faces.get(5));
        edges.get(20).addFaces(faces.get(9), faces.get(5));
        edges.get(21).addFaces(faces.get(6), faces.get(9));
        edges.get(22).addFaces(faces.get(9), faces.get(7));
        edges.get(23).addFaces(faces.get(8), faces.get(9));
    }

    protected void substituteVertexForGadget(Vertex vertex) {
        final List<Vertex> newVertices = new ArrayList<>();
        for (int i=0; i < 6; i++) {
            newVertices.add(new Vertex(getVertices().size() + newVertices.size()));
            if (i%2 == 0) setPositive(newVertices.get(i));
            else setNegative(newVertices.get(i));
        }

        final Face face0 = vertex.getAdjacentEdges().get(0).getLeftFromPOV(vertex);
        final Face face1 = vertex.getAdjacentEdges().get(1).getLeftFromPOV(vertex);
        final Face face2 = vertex.getAdjacentEdges().get(2).getLeftFromPOV(vertex);

        final List<Edge> newEdges = new ArrayList<>();
        newEdges.add(new Edge(newEdges.size() + getEdges().size(), newVertices.get(0), newVertices.get(1))); //0
        newEdges.add(new Edge(newEdges.size() + getEdges().size(), newVertices.get(1), newVertices.get(2))); //1
        newEdges.add(new Edge(newEdges.size() + getEdges().size(), newVertices.get(0), newVertices.get(5))); //2
        newEdges.add(new Edge(newEdges.size() + getEdges().size(), newVertices.get(1), vertex)); //3
        newEdges.add(new Edge(newEdges.size() + getEdges().size(), newVertices.get(2), newVertices.get(3))); //4
        newEdges.add(new Edge(newEdges.size() + getEdges().size(), newVertices.get(5), vertex)); //5
        newEdges.add(new Edge(newEdges.size() + getEdges().size(), vertex, newVertices.get(3))); //6
        newEdges.add(new Edge(newEdges.size() + getEdges().size(), newVertices.get(5), newVertices.get(4))); //7
        newEdges.add(new Edge(newEdges.size() + getEdges().size(), newVertices.get(4), newVertices.get(3))); //8

        face0.addEdgeAfter(vertex.getAdjacentEdges().get(0), newEdges.get(2));
        face0.addEdgeAfter(newEdges.get(2), newEdges.get(7));
        face1.addEdgeAfter(vertex.getAdjacentEdges().get(1), newEdges.get(1));
        face1.addEdgeAfter(newEdges.get(1), newEdges.get(0));
        face2.addEdgeAfter(vertex.getAdjacentEdges().get(2), newEdges.get(8));
        face2.addEdgeAfter(newEdges.get(8), newEdges.get(4));

        final List<Face> newFaces = new ArrayList<>();
        newFaces.add(new Face(newFaces.size() + getFaces().size()));
        newFaces.get(0).addEdge(newEdges.get(0));
        newFaces.get(0).addEdge(newEdges.get(3));
        newFaces.get(0).addEdge(newEdges.get(5));
        newFaces.get(0).addEdge(newEdges.get(2));

        newFaces.add(new Face(newFaces.size() + getFaces().size()));
        newFaces.get(1).addEdge(newEdges.get(1));
        newFaces.get(1).addEdge(newEdges.get(4));
        newFaces.get(1).addEdge(newEdges.get(6));
        newFaces.get(1).addEdge(newEdges.get(3));

        newFaces.add(new Face(newFaces.size() + getFaces().size()));
        newFaces.get(2).addEdge(newEdges.get(5));
        newFaces.get(2).addEdge(newEdges.get(6));
        newFaces.get(2).addEdge(newEdges.get(8));
        newFaces.get(2).addEdge(newEdges.get(7));

        final List<Edge> vertexEdges = vertex.getAdjacentEdges();
        vertexEdges.get(0).exchangeVertex(vertex, newVertices.get(0));
        vertexEdges.get(1).exchangeVertex(vertex, newVertices.get(2));
        vertexEdges.get(2).exchangeVertex(vertex, newVertices.get(4));

        newVertices.get(0).addEdge(vertexEdges.get(0));
        newVertices.get(0).addEdge(newEdges.get(0));
        newVertices.get(0).addEdge(newEdges.get(2));

        newVertices.get(1).addEdge(newEdges.get(0));
        newVertices.get(1).addEdge(newEdges.get(1));
        newVertices.get(1).addEdge(newEdges.get(3));

        newVertices.get(2).addEdge(vertexEdges.get(1));
        newVertices.get(2).addEdge(newEdges.get(4));
        newVertices.get(2).addEdge(newEdges.get(1));

        newVertices.get(3).addEdge(newEdges.get(4));
        newVertices.get(3).addEdge(newEdges.get(8));
        newVertices.get(3).addEdge(newEdges.get(6));

        newVertices.get(4).addEdge(vertexEdges.get(2));
        newVertices.get(4).addEdge(newEdges.get(7));
        newVertices.get(4).addEdge(newEdges.get(8));

        newVertices.get(5).addEdge(newEdges.get(5));
        newVertices.get(5).addEdge(newEdges.get(7));
        newVertices.get(5).addEdge(newEdges.get(2));

        vertex.exchangeEdge(vertexEdges.get(0),newEdges.get(3));
        vertex.exchangeEdge(vertexEdges.get(1),newEdges.get(6));
        vertex.exchangeEdge(vertexEdges.get(2),newEdges.get(5));

        newEdges.get(0).addFaces(face1, newFaces.get(0));
        newEdges.get(1).addFaces(face1, newFaces.get(1));
        newEdges.get(2).addFaces(newFaces.get(0), face0);
        newEdges.get(3).addFaces(newFaces.get(1), newFaces.get(0));
        newEdges.get(4).addFaces(face2, newFaces.get(1));
        newEdges.get(5).addFaces(newFaces.get(0), newFaces.get(2));
        newEdges.get(6).addFaces(newFaces.get(1), newFaces.get(2));
        newEdges.get(7).addFaces(newFaces.get(2), face0);
        newEdges.get(8).addFaces(newFaces.get(2), face2);

        getFaces().addAll(newFaces);

        getVertices().addAll(newVertices);
        getEdges().addAll(newEdges);
    }

}
