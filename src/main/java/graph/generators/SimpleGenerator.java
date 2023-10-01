package graph.generators;

import graph.canoniccoders.obsolete.AbstractCanonicCoder;
import graph.canoniccoders.obsolete.BFSCoder;
import graph.canoniccoders.LexCode;
import graph.components.Face;
import graph.components.AbstractGraph;
import graph.components.SimpleGraph;
import graph.components.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import graph.processors.AbstractProcessor;

public class SimpleGenerator extends AbstractGenerator {
    private final Map<String, Boolean> discoveredGraphs = new HashMap<>();

    private static final AbstractCanonicCoder coder = new BFSCoder();

    public SimpleGenerator(SimpleGraph graph, AbstractProcessor processor) {
        super(graph, processor, 22);
    }


    @Override
    public boolean verifyTwoFactors() {
        throw new RuntimeException("not implemented yet.");
    }

    public void generate(){
        int graphDegree = graph.getVertices().size();
        if (graphDegree > depth) return;

        for (int i = 0; i < graphDegree; i++) {
            Vertex v = graph.getVertices().get(i);
            Face newFace = graph.op3(v);

            LexCode lexCode = coder.getLexCodeForFace(newFace, graph);
            if (coder.isMinimal(graph, lexCode)){
                String lexCodeString = lexCode.toString();
                if (!discoveredGraphs.containsKey(lexCodeString)){
                    discoveredGraphs.put(lexCodeString, true);
                    generate();
                }
            }
            graph.undoOp3(graph.getFaces().get(graph.getFaces().size() - 1));
        }
    }

    public void printMap(){
        ArrayList<String> lexCodes = new ArrayList<>(discoveredGraphs.keySet());
        System.out.println(lexCodes.stream().collect(Collectors.joining("\n")));
    }

}
