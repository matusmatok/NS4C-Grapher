import graph.components.Edge;
import graph.components.Vertex;
import graph.components.Vertex.ConsistentTwoFactorState;
import java.util.ArrayList;
import java.util.List;

public class ExtendedTutteFragmentMock extends TutteFragmentMock{
    public ExtendedTutteFragmentMock() {
        super();
        for (Vertex v: getVertices()) setPositive(v);
        substituteVertexForGadget(getVertices().get(3));
        substituteVertexForGadget(getVertices().get(4));
        substituteVertexForGadget(getVertices().get(8));
        substituteVertexForGadget(getVertices().get(13));
    }

    private void substituteVertexForGadget(Vertex vertex) {
        final List<Vertex> newVertices = new ArrayList<>();
        for (int i=0; i < 6; i++) {
            newVertices.add(new Vertex(getVertices().size() + newVertices.size()));
            if (i%2 == 0) setPositive(newVertices.get(i));
            else setNegative(newVertices.get(i));
        }

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

        getVertices().addAll(newVertices);
        getEdges().addAll(newEdges);
    }

}
