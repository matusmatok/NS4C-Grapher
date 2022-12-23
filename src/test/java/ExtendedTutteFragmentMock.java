import graph.components.Edge;
import graph.components.Face;
import graph.components.Vertex;

import java.util.ArrayList;
import java.util.List;

public class ExtendedTutteFragmentMock extends FacedTutteFragmentMock{
    public ExtendedTutteFragmentMock() {
        super();
        for (Vertex v: getVertices()) setPositive(v);
        //substituteVertexForGadget(getVertices().get(3));
        substituteVertexForGadget(getVertices().get(4));
        substituteVertexForGadget(getVertices().get(8));
        //substituteVertexForGadget(getVertices().get(13));
        setNegative(getVertices().get(1));
        setNegative(getVertices().get(14));
    }

    private void substituteVertexForGadget(Vertex vertex) {
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
        newFaces.get(0).addEdge(getEdges().get(0));
        newFaces.get(0).addEdge(getEdges().get(3));
        newFaces.get(0).addEdge(getEdges().get(5));
        newFaces.get(0).addEdge(getEdges().get(2));

        newFaces.add(new Face(newFaces.size() + getFaces().size()));
        newFaces.get(1).addEdge(getEdges().get(1));
        newFaces.get(1).addEdge(getEdges().get(4));
        newFaces.get(1).addEdge(getEdges().get(6));
        newFaces.get(1).addEdge(getEdges().get(3));

        newFaces.add(new Face(newFaces.size() + getFaces().size()));
        newFaces.get(2).addEdge(getEdges().get(5));
        newFaces.get(2).addEdge(getEdges().get(6));
        newFaces.get(2).addEdge(getEdges().get(8));
        newFaces.get(2).addEdge(getEdges().get(7));

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
