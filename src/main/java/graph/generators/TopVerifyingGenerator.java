package graph.generators;

import graph.canoniccoders.obsolete.AbstractCanonicCoder;
import graph.canoniccoders.obsolete.BFSCoder;
import graph.canoniccoders.CanonicalCoder;
import graph.canoniccoders.LexCode;
import graph.components.Edge;
import graph.components.Face;
import graph.components.Graph;
import graph.generators.OperandProvider.Operand;
import graph.processors.AbstractProcessor;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TopVerifyingGenerator extends AbstractGenerator{

    private static final OperandProvider operandProvider = new OperandProvider();
    private static final AbstractCanonicCoder coder = new BFSCoder();
    private final Set<String> discoveredGraphs = new HashSet<>();
    @Getter
    private int topLevelGraphs = 0;

    public TopVerifyingGenerator(Graph graph, AbstractProcessor processor, int depth) {
        super(graph, processor, depth);
    }

    @Override
    public boolean verifyTwoFactors() {
        if (processor.hasProperty()) graph.print();
        return false;
    }

    @Override
    public void generate() {

        if (graph.getVertices().size() >= depth) {
            verifyTwoFactors();
            topLevelGraphs++;
            return;
        }

        final List<Operand> op3Operands = operandProvider.getOp3OperandsAll(graph);
        for (Operand operand : op3Operands) {

            final Face newFace = graph.op3(operand.getVertexId());
            LexCode localCode = CanonicalCoder.getCodeForFace(graph, newFace);
            LexCode globalCode = CanonicalCoder.getCodeForGraph(graph);

            if (localCode.equals(globalCode)) {
                String lexCodeString = localCode.toString();
                if (!discoveredGraphs.contains(lexCodeString)) {
                    discoveredGraphs.add(lexCodeString);
                    generate();
                }
            }
            graph.undoOp3(graph.getFaces().get(graph.getFaces().size() - 1));
        }

        final List<Operand> op4Operands = operandProvider.getOp4OperandsAll(graph);
        for (Operand operand : op4Operands) {

            final Edge newEdge = graph.op4(operand.getFaceId(), operand.getEdgeId());
            LexCode localCode = CanonicalCoder.getCodeForFace(graph, newEdge.getFaceOfBiggerId());
            LexCode globalCode = CanonicalCoder.getCodeForGraph(graph);

            if (localCode.equals(globalCode)) {
                String lexCodeString = localCode.toString();
                if (!discoveredGraphs.contains(lexCodeString)) {
                    discoveredGraphs.add(lexCodeString);
                    generate();
                }
            }
            graph.undoOp4(newEdge);
        }

        final List<Operand> op5Operands = operandProvider.getOp5OperandsAll(graph);
        for (Operand operand : op5Operands) {

            final Edge newEdge = graph.op5(operand.getFaceId(), operand.getEdgeId());
            LexCode localCode = CanonicalCoder.getCodeForFace(graph, newEdge.getFaceOfBiggerId());
            LexCode globalCode = CanonicalCoder.getCodeForGraph(graph);

            if (localCode.equals(globalCode)) {
                String lexCodeString = localCode.toString();
                if (!discoveredGraphs.contains(lexCodeString)) {
                    discoveredGraphs.add(lexCodeString);
                    generate();
                }
            }
            graph.undoOp5(newEdge);
        }
    }
}
