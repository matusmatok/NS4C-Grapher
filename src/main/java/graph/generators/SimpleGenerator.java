package graph.generators;

import graph.*;
import graph.components.Face;
import graph.components.Graph;
import graph.components.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleGenerator {
    private final Map<String, Boolean> discoveredGraphs = new HashMap<>();

    private final int maxDepth = 8;

    public void generate(Graph g){
        int graphDegree = g.getVertices().size();
        if (graphDegree > maxDepth) return;

        for (int i = 0; i < graphDegree; i++) {
            Vertex v = g.getVertices().get(i);
            Face newFace = g.op3(v);
            ArrayList<Integer> lexCode = LexCoder.getLexCodeForFace(newFace, g);
            if (LexCoder.isMinimal(g, lexCode)){
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
