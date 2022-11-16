import graph.components.Edge;
import graph.components.Graph;
import graph.components.Vertex;
import java.util.ArrayList;
import java.util.List;

public class TutteFragmentMock extends Graph {
    public TutteFragmentMock(){
        super();
        init();
    }

    @Override
    public void init(){
        for (int i=0; i < 16; i++) {
            getVertices().add(new Vertex(i));
        }

        List<Vertex> vertices = getVertices();
        List<Edge> edges = getEdges();
        edges.add(new Edge(0, vertices.get(0), vertices.get(15)));
        edges.add(new Edge(1, vertices.get(0), vertices.get(1)));
        edges.add(new Edge(2, vertices.get(0), vertices.get(2)));
        edges.add(new Edge(3, vertices.get(1), vertices.get(3)));
        edges.add(new Edge(4, vertices.get(1), vertices.get(4)));
        edges.add(new Edge(5, vertices.get(2), vertices.get(3)));
        edges.add(new Edge(6, vertices.get(2), vertices.get(8)));
        edges.add(new Edge(7, vertices.get(3), vertices.get(6)));
        edges.add(new Edge(8, vertices.get(4), vertices.get(5)));
        edges.add(new Edge(9, vertices.get(4), vertices.get(9)));
        edges.add(new Edge(10, vertices.get(5), vertices.get(6)));
        edges.add(new Edge(11, vertices.get(5), vertices.get(11)));
        edges.add(new Edge(12, vertices.get(6), vertices.get(7)));
        edges.add(new Edge(13, vertices.get(7), vertices.get(8)));
        edges.add(new Edge(14, vertices.get(7), vertices.get(11)));
        edges.add(new Edge(15, vertices.get(8), vertices.get(14)));
        edges.add(new Edge(16, vertices.get(9), vertices.get(10)));
        edges.add(new Edge(17, vertices.get(9), vertices.get(12)));
        edges.add(new Edge(18, vertices.get(10), vertices.get(11)));
        edges.add(new Edge(19, vertices.get(11), vertices.get(13)));
        edges.add(new Edge(20, vertices.get(13), vertices.get(12)));
        edges.add(new Edge(21, vertices.get(13), vertices.get(14)));
        edges.add(new Edge(22, vertices.get(12), vertices.get(15)));
        edges.add(new Edge(23, vertices.get(14), vertices.get(15)));

        vertices.get(0).addEdge(edges.get(0));
        vertices.get(0).addEdge(edges.get(2));
        vertices.get(0).addEdge(edges.get(1));

        vertices.get(1).addEdge(edges.get(1));
        vertices.get(1).addEdge(edges.get(3));
        vertices.get(1).addEdge(edges.get(4));

        vertices.get(2).addEdge(edges.get(2));
        vertices.get(2).addEdge(edges.get(6));
        vertices.get(2).addEdge(edges.get(5));

        vertices.get(3).addEdge(edges.get(3));
        vertices.get(3).addEdge(edges.get(5));
        vertices.get(3).addEdge(edges.get(7));

        vertices.get(4).addEdge(edges.get(4));
        vertices.get(4).addEdge(edges.get(8));
        vertices.get(4).addEdge(edges.get(9));

        vertices.get(5).addEdge(edges.get(8));
        vertices.get(5).addEdge(edges.get(10));
        vertices.get(5).addEdge(edges.get(11));

        vertices.get(6).addEdge(edges.get(7));
        vertices.get(6).addEdge(edges.get(12));
        vertices.get(6).addEdge(edges.get(10));

        vertices.get(7).addEdge(edges.get(12));
        vertices.get(7).addEdge(edges.get(13));
        vertices.get(7).addEdge(edges.get(14));

        vertices.get(8).addEdge(edges.get(6));
        vertices.get(8).addEdge(edges.get(15));
        vertices.get(8).addEdge(edges.get(13));

        vertices.get(9).addEdge(edges.get(9));
        vertices.get(9).addEdge(edges.get(16));
        vertices.get(9).addEdge(edges.get(17));

        vertices.get(10).addEdge(edges.get(11));
        vertices.get(10).addEdge(edges.get(18));
        vertices.get(10).addEdge(edges.get(16));

        vertices.get(11).addEdge(edges.get(14));
        vertices.get(11).addEdge(edges.get(19));
        vertices.get(11).addEdge(edges.get(18));

        vertices.get(12).addEdge(edges.get(17));
        vertices.get(12).addEdge(edges.get(20));
        vertices.get(12).addEdge(edges.get(22));

        vertices.get(13).addEdge(edges.get(19));
        vertices.get(13).addEdge(edges.get(21));
        vertices.get(13).addEdge(edges.get(20));

        vertices.get(14).addEdge(edges.get(15));
        vertices.get(14).addEdge(edges.get(23));
        vertices.get(14).addEdge(edges.get(21));

        vertices.get(15).addEdge(edges.get(0));
        vertices.get(15).addEdge(edges.get(22));
        vertices.get(15).addEdge(edges.get(23));
    }
}
