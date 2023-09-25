package injections;

import graph.canoniccoders.obsolete.BFSCoder;
import graph.components.Graph;
import graph.processors.UnavoidableEdgeProcessor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class LexCodeCollectingProcessor extends UnavoidableEdgeProcessor {

    @Getter
    private final Set<String> lexCodes = new HashSet<>();
    private final BFSCoder coder = new BFSCoder();

    public LexCodeCollectingProcessor(Graph graph) {
        super(graph);
    }

    @Override
    public boolean hasProperty() {
        lexCodes.add(coder.getLexCodeForFace(graph.getFaces().get(graph.getFaces().size() - 1), graph).toString());
        return false;
    }
}
