import graph.canoniccoders.obsolete.BFSCoder;
import graph.canoniccoders.LexCode;
import graph.components.Face;
import graph.components.SimpleGraph;
import graph.generators.cubicplanar.TopVerifyingGenerator;
import injections.LexCodeCollectingProcessor;
import mock.ExtendedTutteFragmentMock;
import mock.FacedTutteFragmentMock;
import mock.TutteFragmentMock;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class GeneratorTest {
    private static final TutteFragmentMock tutteFragment = new FacedTutteFragmentMock();
    private final TutteFragmentMock extendedTutteFragment = new ExtendedTutteFragmentMock();
    private static final SimpleGraph graph = new SimpleGraph();
    static {
        graph.init();
        graph.op3(graph.getVertices().get(0));
    }
    private static final LexCodeCollectingProcessor processor = new LexCodeCollectingProcessor(graph);
    private static final BFSCoder coder = new BFSCoder();
    private static final TopVerifyingGenerator generator = new TopVerifyingGenerator(graph, processor, 16);
    private static final TopVerifyingGenerator generatorExtended = new TopVerifyingGenerator(graph, processor, 28);

    @Test
    public void testIfContains() {
        generator.generate();
        final String tutteFragmentLexCode = coder.getLexCodeForFace(tutteFragment.getFaces()
                    .stream().filter(face -> face.size() == 4)
                    .findAny().get(), tutteFragment).toString();
        assertTrue(processor.getLexCodes().contains(tutteFragmentLexCode));
        processor.getLexCodes().clear();

        String extendedTutteFragmentLexCode = "9 9 9 9 9 9 9 9 9 9 9 9 9";
        final List<Face> size4Faces = extendedTutteFragment.getFaces().stream().filter(face -> face.size() == 4).collect(Collectors.toList());
        assertNotEquals(0, size4Faces.size());
        for (Face initFace : size4Faces) {
            final LexCode currentCode = coder.getLexCodeForFace(initFace, extendedTutteFragment);
            if (coder.isMinimal(extendedTutteFragment, currentCode)) {
                extendedTutteFragmentLexCode = currentCode.toString();
            }
        }
        assertNotEquals("9 9 9 9 9 9 9 9 9 9 9 9 9", extendedTutteFragmentLexCode);
        generatorExtended.generate();
        assertTrue(processor.getLexCodes().contains(extendedTutteFragmentLexCode));
    }
}
