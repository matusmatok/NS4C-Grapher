package mock;

import java.util.ArrayList;

public class TenTuttovTotoMock extends ExtendedTutteFragmentMock {

    public TenTuttovTotoMock() {
        super();
        op3(15);
        op4(9, 23);
        op4(17, 45);
        op4(18, 50);
        final int[] newVertices = {15,28,29,30,31,32,33,34,35};
        for (int i = 0; i < newVertices.length; i++) {
            setPositive(getVertices().get(newVertices[i]));
        }
    }
}
