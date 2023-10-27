package graph.generators;

import graph.canoniccoders.CachingCoder;
import graph.canoniccoders.CanonicalCoder;
import graph.canoniccoders.LexCode;
import graph.components.Edge;
import graph.components.Face;
import graph.components.NoLadderC4ECGraph;
import graph.components.Vertex;
import graph.utils.EasyList;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public class NoLadderC4ECOperandProvider {

    private final NoLadderC4ECGraph graph;
    private final CachingCoder coder;
    private final Map<String, List<Operand>> operands = new HashMap<>();
    private final Map<String, Set<LexCode>> operandOrbits = new HashMap<>();

    public NoLadderC4ECOperandProvider(final NoLadderC4ECGraph graph, final CachingCoder coder) {
        this.graph = graph;
        this.coder = coder;
        operands.put("A", new ArrayList<>());
        operands.put("B", new ArrayList<>());
        operands.put("C", new ArrayList<>());
        operands.put("D", new ArrayList<>());
        operands.put("E", new ArrayList<>());
        operands.put("F", new ArrayList<>());
        operands.put("G", new ArrayList<>());
        operands.put("H", new ArrayList<>());

        operandOrbits.put("A", new HashSet<>());
        operandOrbits.put("B", new HashSet<>());
        operandOrbits.put("C", new HashSet<>());
        operandOrbits.put("D", new HashSet<>());
        operandOrbits.put("E", new HashSet<>());
        operandOrbits.put("F", new HashSet<>());
        operandOrbits.put("G", new HashSet<>());
        operandOrbits.put("H", new HashSet<>());
    }

    @AllArgsConstructor
    @Getter
    public static class Operand {
        private Face face;
        private Edge edge;
        private Edge.OrientedEdge orientedEdge;
    }

    private void calculateOperands() {
        final LexCode canonicalCode = coder.getGraphCode();
        if (canonicalCode.shortStartsAs(4,5,5,4,5)) {
            final List<Edge> middleB = graph.getEdges().stream().filter(this::isEdgeB).collect(Collectors.toList());
            final List<Edge> middleA = middleB.stream().filter(edge ->
                            coder.getCodeForFace(edge.getVFrom().getOppositeFace(edge)).shortStartsAs(4, 5, 5, 4, 5) ||
                                    coder.getCodeForFace(edge.getVTo().getOppositeFace(edge)).shortStartsAs(4, 5, 5, 4, 5))
                    .collect(Collectors.toList());

            for (final Edge middleEdgeB : middleB) {
                addOperandsA(middleEdgeB);
            }

            if (middleA.size() == 1) {
                //B
                final Edge middleEdge = middleA.get(0);
                final Face square1 = middleEdge.getVFrom().getOppositeFace(middleEdge);
                final Face square2 = middleEdge.getVTo().getOppositeFace(middleEdge);
                final LexCode secondGadgetLexCode = findSecondLowestGadget(square1, square2);

                addOperandsB(square1);
                addOperandsB(square2);

                if (secondGadgetLexCode.isMaximumLexCode()) return;
                if (secondGadgetLexCode.shortStartsAs(4, 5, 5, 4)) return;

                //C
                addOperandsC(middleEdge);

                if(secondGadgetLexCode.shortStartsAs(4, 5, 5, 5, 5)) return;

                //D
                final List<Edge> potentialOperandsD = new ArrayList<>(2);
                final List<Edge> potentialOperandsE = new ArrayList<>(4);
                final List<Edge> potentialOperandsG = new ArrayList<>(2);
                final List<Face> facesA = new ArrayList<>(2);

                for (final Face square : new Face[]{square1, square2}){
                    final List<Edge> extrudingEdges = square.getAdjacentEdges().stream()
                            .map(edge -> edge.getLeftFromPOVofFace(square1).getOppositeEdge(square1))
                            .collect(Collectors.toList());
                    final int middleEdgeIndex = extrudingEdges.indexOf(middleEdge);

                    for (int i : new int[]{1,3}) {
                        final Edge currentEdge = extrudingEdges.get((middleEdgeIndex + i) % 4);
                        if (currentEdge.getLeftFace().size() == 5 && currentEdge.getRightFace().size() == 5) {
                            potentialOperandsD.add(currentEdge);
                        } else {
                            potentialOperandsE.add(currentEdge);
                        }
                    }

                    final Edge oppositeEdge = extrudingEdges.get((middleEdgeIndex + 2) % 4);
                    if (oppositeEdge.getRightFace().size() == 5 || oppositeEdge.getLeftFace().size() == 5) {
                        potentialOperandsE.add(oppositeEdge);
                        final Face faceA = oppositeEdge.getRightFace().size() == 5 ?
                                oppositeEdge.getRightFace() : oppositeEdge.getLeftFace();
                        facesA.add(faceA);
                        potentialOperandsG.add(faceA.getEdge(faceA.getIndexOfE(oppositeEdge) + 2));
                    }
                }

                for (final Edge edge : potentialOperandsD) {
                    addOperandsD(edge);
                }

                if (secondGadgetLexCode.shortStartsAs(4, 5, 5, 5)) return;

                //E F
                for (final Edge edge : potentialOperandsE) {
                    addOperandsE(edge);
                }

                for (int i = 0; i < facesA.size(); i++) {
                    final int potentialEdgeGIndex = facesA.get(i).getIndexOfE(potentialOperandsG.get(i));
                    addOperandsF(facesA.get(i), potentialOperandsG.get(i));
                    addOperandsF(facesA.get(i), facesA.get(i).getEdge(potentialEdgeGIndex + 3));
                }

                addOperandsF(middleEdge.getLeftFace(), middleEdge);
                addOperandsF(middleEdge.getRightFace(), middleEdge);

                if (secondGadgetLexCode.shortStartsAs(4, 5, 5)) return;

                //G
                for (final Edge potentialG : potentialOperandsG) {
                    addOperandsG(potentialG);
                }
            }
        }
    }

    private LexCode findSecondLowestGadget(final Face firstExcluded, final Face secondExcluded) {
        return graph.getFaces().stream()
                .filter(face -> face.size() == 4 && face != firstExcluded && face != secondExcluded)
                .map(coder::getCodeForFace)
                .min(LexCode::compareTo).orElseGet(LexCode::getMaximumLexCode);
    }

    private LexCode findSecondLowestGadget(final Face excluded) {
        return graph.getFaces().stream()
                .filter(face -> face.size() == 4 && face != excluded)
                .map(coder::getCodeForFace)
                .min(LexCode::compareTo).orElseGet(LexCode::getMaximumLexCode);
    }

    private boolean isEdgeB(final Edge edge) {
        if (edge.getRightFace().size() == 5 && edge.getLeftFace().size() == 5) {
            return edge.getVTo().getOppositeFace(edge).size() == 4 && edge.getVFrom().getOppositeFace(edge).size() == 4;
        }
        return false;
    }

    private void addOperandsA(final Edge edge) {
        final Vertex from = edge.getVFrom();
        for (final Edge potentialEdge : from.getAdjacentEdges()) {
            if (potentialEdge != edge) {
                final LexCode lexCode = coder.getCodeForEdge(potentialEdge);
                if (!operandOrbits.get("A").contains(lexCode)) {
                    operandOrbits.get("A").add(lexCode);
                    operands.get("A").add(new Operand(null, null, potentialEdge.getOriented(from)));
                }
            }
        }

        final Vertex to = edge.getVTo();
        for (final Edge potentialEdge : to.getAdjacentEdges()) {
            if (potentialEdge != edge) {
                final LexCode lexCode = coder.getCodeForEdge(potentialEdge);
                if (!operandOrbits.get("A").contains(lexCode)) {
                    operandOrbits.get("A").add(lexCode);
                    operands.get("A").add(new Operand(null, null, potentialEdge.getOriented(to)));
                }
            }
        }
    }

    private void addOperandsB(final Face face) {
        final Edge orientation1 = face.getEdge(0);
        final Edge orientation2 = face.getEdge(1);
        final LexCode lexCode1 = coder.getCodeForFaceAndEdge(face, orientation1);
        final LexCode lexCode2 = coder.getCodeForFaceAndEdge(face, orientation2);

        if (!operandOrbits.get("B").contains(lexCode1)) {
            operandOrbits.get("B").add(lexCode1);
            operands.get("B").add(new Operand(face, orientation1, null));
        }

        if (!operandOrbits.get("B").contains(lexCode2)) {
            operandOrbits.get("B").add(lexCode2);
            operands.get("B").add(new Operand(face, orientation2, null));
        }
    }

    private void addOperandsC(final Edge edge) {
        final LexCode lexCode = coder.getCodeForEdge(edge);
        if (!operandOrbits.get("C").contains(lexCode)) {
            operandOrbits.get("C").add(lexCode);
            operands.get("C").add(new Operand(null, edge, null));
        }
    }

    private void addOperandsD(final Edge edge) {
        final LexCode lexCode = coder.getCodeForEdge(edge);
        if (!operandOrbits.get("D").contains(lexCode)) {
            operandOrbits.get("D").add(lexCode);
            operands.get("D").add(new Operand(null, edge, null));
        }
    }

    private void addOperandsE(final Edge edge) {
        final LexCode lexCode = coder.getCodeForEdge(edge);
        if (!operandOrbits.get("E").contains(lexCode)) {
            operandOrbits.get("E").add(lexCode);
            operands.get("E").add(new Operand(null, edge, null));
        }
    }

    private void addOperandsF(final Face face, final Edge edge) {
        final LexCode lexCode = coder.getCodeForFaceAndEdge(face, edge);
        if (!operandOrbits.get("F").contains(lexCode)) {
            operandOrbits.get("F").add(lexCode);
            operands.get("F").add(new Operand(face, edge, null));
        }
    }

    private void addOperandsG(final Edge edge) {
        final LexCode lexCode = coder.getCodeForEdge(edge);
        if (!operandOrbits.get("G").contains(lexCode)) {
            operandOrbits.get("G").add(lexCode);
            operands.get("G").add(new Operand(null, edge, null));
        }
    }
}
