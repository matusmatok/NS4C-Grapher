package graph.generators;

import graph.canoniccoders.CanonicalCoder;
import graph.canoniccoders.LexCode;
import graph.components.Edge;
import graph.components.Face;
import graph.components.NoLadderC4ECGraph;
import graph.components.Vertex;
import graph.utils.EasyList;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NoLadderC4ECOperandProvider {

    private final NoLadderC4ECGraph graph;

    public NoLadderC4ECOperandProvider(final NoLadderC4ECGraph graph) {
        this.graph = graph;
    }

    @AllArgsConstructor
    @Getter
    public static class Operand {
        private Face face;
        private Edge edge;
        private Edge.OrientedEdge orientedEdge;
    }

    private List<Operand> getOperandsA() {
        final List<Operand> result = new EasyList<>();
        final Set<String> knownOperands = new HashSet<>();

        for (final Edge e : graph.getEdges()) {
            if (e.getRightFace().size() == 5 && e.getLeftFace().size() == 5) {
                if (e.getVTo().getOppositeFace(e).size() == 4 && e.getVFrom().getOppositeFace(e).size() == 4) {
                    final Vertex[] vertices = {e.getVFrom(), e.getVTo()};
                    for (final Vertex vertex : vertices) {
                        for (final Edge edge : vertex.getAdjacentEdges()) {
                            if (e != edge) {
                                final LexCode localCode = CanonicalCoder.getCodeForEdge(graph, edge);
                                final String stringCode = localCode.toString();
                                if (!knownOperands.contains(stringCode)) {
                                    knownOperands.add(stringCode);
                                    result.add(new Operand(null, null, edge.getOriented(vertex)));
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private List<Operand> getOperandsB() {
        final List<Operand> result = new EasyList<>();
        final List<Face> squares = graph.getFaces().stream().filter(f -> f.size() == 4).collect(Collectors.toList());
        final Set<String> knownOperands = new HashSet<>();

        for (final Face square : squares) {
            for (int i = 0; i < 2; i++) {
                final LexCode lexCode1 = CanonicalCoder.getCodeForEdge(graph, square.getEdge(i));
                final LexCode lexCode2 = CanonicalCoder.getCodeForEdge(graph, square.getEdge(i + 2));

                final LexCode code = lexCode1.compareTo(lexCode2) < 0 ? lexCode1 : lexCode2;
                final String stringCode = code.toString();

                if (!knownOperands.contains(stringCode)) {
                    knownOperands.add(stringCode);
                    result.add(new Operand(square, square.getEdge(i), null));
                }
            }
        }

        return result;
    }
}
