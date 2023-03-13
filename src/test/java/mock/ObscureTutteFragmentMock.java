package mock;

import graph.components.Vertex;

public class ObscureTutteFragmentMock extends FacedTutteFragmentMock{

    public ObscureTutteFragmentMock() {
        super();
        substituteVertexForGadget(getVertices().get(4));
        substituteVertexForGadget(getVertices().get(8));
        for (Vertex v: getVertices()) setPositive(v);
        setNegative(getVertices().get(0));
        setNegative(getVertices().get(1));
        setNegative(getVertices().get(2));
        setNegative(getVertices().get(12));
        setNegative(getVertices().get(14));
        setNegative(getVertices().get(15));
        setNegative(getVertices().get(19));
        setNegative(getVertices().get(23));
    }

}
