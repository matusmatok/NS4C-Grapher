package graph.components;

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
}
