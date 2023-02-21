package mock;

public class ExtendedTenTuttovTotoMock extends TenTuttovTotoMock{

    public ExtendedTenTuttovTotoMock() {
        super();
        substituteVertexForGadget(getVertices().get(31));
        substituteVertexForGadget(getVertices().get(30));
    }
}
