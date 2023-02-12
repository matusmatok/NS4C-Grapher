package graph.generators;

import graph.canoniccoders.AbstractCanonicCoder;
import graph.canoniccoders.BFSCoder;
import graph.components.Edge;
import graph.components.Face;
import graph.components.Graph;
import graph.generators.OperandProvider.Operand;
import graph.processors.AbstractProcessor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TopVerifyingGenerator extends AbstractGenerator{

    private static final OperandProvider operandProvider = new OperandProvider();
    private static final AbstractCanonicCoder coder = new BFSCoder();
    private final Set<String> discoveredGraphs = new HashSet<>();

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
            return;
        }

        final List<Operand> op3Operands = operandProvider.getOp3Operands(graph);
        for (Operand operand : op3Operands) {

            final Face newFace = graph.op3(operand.getVertexId());
            List<Integer> lexCode = coder.getLexCodeForFace(newFace, graph);

            if (coder.isMinimal(graph, lexCode)) {
                String lexCodeString = lexCode.stream().map(Object::toString).collect(
                        Collectors.joining(" "));
                if (!discoveredGraphs.contains(lexCodeString)) {
                    discoveredGraphs.add(lexCodeString);
                    generate();
                }
            }
            graph.undoOp3(graph.getFaces().get(graph.getFaces().size() - 1));
        }

        final List<Operand> op4Operands = operandProvider.getOp4Operands(graph);
        for (Operand operand : op4Operands) {

            final Edge newEdge = graph.op4(operand.getFaceId(), operand.getEdgeId());
            List<Integer> lexCode = coder.getLexCodeForFace(newEdge.getFaceOfBiggerId(), graph);

            if (coder.isMinimal(graph,lexCode)) {
                String lexCodeString = lexCode.stream().map(Object::toString).collect(
                        Collectors.joining(" "));
                if (!discoveredGraphs.contains(lexCodeString)) {
                    discoveredGraphs.add(lexCodeString);
                    generate();
                }
            }
            graph.undoOp4(newEdge);
        }

        final List<Operand> op5Operands = operandProvider.getOp5Operands(graph);
        for (Operand operand : op5Operands) {

            final Edge newEdge = graph.op5(operand.getFaceId(), operand.getEdgeId());
            List<Integer> lexCode = coder.getLexCodeForFace(newEdge.getFaceOfBiggerId(), graph);

            if (coder.isMinimal(graph,lexCode)) {
                String lexCodeString = lexCode.stream().map(Object::toString).collect(
                        Collectors.joining(" "));
                if (!discoveredGraphs.contains(lexCodeString)) {
                    discoveredGraphs.add(lexCodeString);
                    generate();
                }
            }
            graph.undoOp5(newEdge);
        }
    }
}
