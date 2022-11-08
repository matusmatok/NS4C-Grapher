package graph.generators;

import graph.*;
import graph.canoniccoders.AbstractCanonicCoder;
import graph.canoniccoders.BFSCoder;
import graph.components.Face;
import graph.components.Graph;
import graph.components.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import graph.canoniccoders.LexCoder;

public class SimpleGenerator {
    private final Map<String, Boolean> discoveredGraphs = new HashMap<>();

    private static final AbstractCanonicCoder coder = new BFSCoder();

    private final int maxDepth = 22;

    public void generate(Graph g){
        int graphDegree = g.getVertices().size();
        if (graphDegree > maxDepth) return;

        for (int i = 0; i < graphDegree; i++) {
            Vertex v = g.getVertices().get(i);
            Face newFace = g.op3(v);

            List<Integer> lexCode = coder.getLexCodeForFace(newFace, g);
            if (coder.isMinimal(g, lexCode)){
                String lexCodeString = lexCode.stream().map(Object::toString).collect(
                        Collectors.joining(" "));
                if (!discoveredGraphs.containsKey(lexCodeString)){
                    discoveredGraphs.put(lexCodeString, true);
                    generate(g);
                }
            }
            g.undoOp3(g.getFaces().get(g.getFaces().size() - 1));
        }
    }

    public void printMap(){
        ArrayList<String> lexCodes = new ArrayList<>(discoveredGraphs.keySet());
        System.out.println(lexCodes.stream().collect(Collectors.joining("\n")));
    }

}
