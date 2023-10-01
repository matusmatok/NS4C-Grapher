package graph.components;

public class SimpleGraph extends AbstractGraph{
    public SimpleGraph() {
        super();
    }

    @Override
    public void init(){
        super.init();
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

    @Override
    public Face op3(int vertexId) {
        return super.op3(vertexId);
    }

    @Override
    public Face op3(Vertex v) {
        return super.op3(v);
    }

    @Override
    public void undoOp3(Face f) {
        super.undoOp3(f);
    }

    @Override
    public Edge op4(int faceId, int idOfInitialEdge) {
        return super.op4(faceId, idOfInitialEdge);
    }

    @Override
    public Edge op4(Face formerFace, int idOfInitialEdge) {
        return super.op4(formerFace, idOfInitialEdge);
    }

    @Override
    public void undoOp4(Edge middleEdge) {
        super.undoOp4(middleEdge);
    }

    @Override
    public Edge op5(int faceId, int idOfInitialEdge) {
        return super.op5(faceId, idOfInitialEdge);
    }

    @Override
    public Edge op5(Face formerFace, int idOfInitialEdge) {
        return super.op5(formerFace, idOfInitialEdge);
    }

    @Override
    public void undoOp5(Edge middleEdge) {
        super.undoOp5(middleEdge);
    }
}
