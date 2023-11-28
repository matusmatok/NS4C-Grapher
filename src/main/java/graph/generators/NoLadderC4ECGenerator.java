package graph.generators;

import graph.components.NoLadderC4ECGraph;
import graph.generators.NoLadderC4ECOperandProvider.Operand;
import graph.processors.AbstractProcessor;

import java.util.List;

public class NoLadderC4ECGenerator {

    private final NoLadderC4ECGraph graph;
    private final AbstractProcessor processor;
    private final int depth;

    public NoLadderC4ECGenerator(NoLadderC4ECGraph graph, AbstractProcessor processor, int depth) {
        this.graph = graph;
        this.processor = processor;
        this.depth = depth;
    }

    public boolean verifyTwoFactors() {
        return false;
    }

    public void generate() {
        if (graph.getVertices().size() != 14) throw new IllegalArgumentException();

        graph.operationB(graph.getFace(3), graph.getVertex(2));
        recursiveGenerate();
        graph.reductionB(graph.getFace(3), graph.getVertex(2));

        graph.operationB(graph.getFace(3), graph.getVertex(3));
        recursiveGenerate();
        graph.reductionB(graph.getFace(3), graph.getVertex(3));

        graph.operationC(graph.getEdge(6, 7));
            graph.operationB(graph.getFace(9), graph.getVertex(6));
            recursiveGenerate();
            graph.reductionB(graph.getFace(9), graph.getVertex(6));

            graph.operationD(graph.getEdge(0, 2));
            recursiveGenerate();
            graph.reductionD();
        graph.reductionC();

        graph.operationD(graph.getEdge(3, 4));
        recursiveGenerate();
        graph.reductionD();

        graph.operationF(graph.getFace(4), graph.getEdge(12));
        recursiveGenerate();
        graph.reductionF();
    }

    private void recursiveGenerate() {
        if (graph.getVertices().size() >= depth) {
            processor.hasProperty();
        }
    }
}
