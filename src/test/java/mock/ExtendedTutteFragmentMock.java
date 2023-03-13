package mock;

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


}
