package mock;

import graph.components.Edge;
import graph.components.Face;

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
}
