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
import java.util.stream.Stream;

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

        calculateOperands();
    }

    @AllArgsConstructor
    @Getter
    public static class Operand {
        private Face face;
        private Edge edge;
        private Edge.OrientedEdge orientedEdge;

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;

            if (o instanceof Operand) {
                final Operand other = (Operand) o;
                return face == other.getFace() &&
                        edge == other.getEdge() &&
                        (orientedEdge == null || orientedEdge.equals(other.orientedEdge));
            }
            throw new IllegalArgumentException("Incomparable objects");
        }

        @Override
        public String toString() {
            return "Operand:\n" + face + edge + orientedEdge;
        }
    }

    public List<Operand> getOperandsA() {
        return operands.get("A");
    }

    public List<Operand> getOperandsB() {
        return operands.get("B");
    }

    public List<Operand> getOperandsC() {
        return operands.get("C");
    }

    public List<Operand> getOperandsD() {
        return operands.get("D");
    }

    public List<Operand> getOperandsE() {
        return operands.get("E");
    }

    public List<Operand> getOperandsF() {
        return operands.get("F");
    }

    public List<Operand> getOperandsG() {
        return operands.get("G");
    }

    public List<Operand> getOperandsH() {
        return operands.get("H");
    }
    private void calculateOperands() {
        final LexCode canonicalCode = coder.getGraphCode();
        if (canonicalCode.shortStartsAs(4,5,5,4,5)) {
            handleCaseA();
        } else if (canonicalCode.shortStartsAs(4, 5, 5, 4)) {
            handleCaseB();
        } else if (canonicalCode.shortStartsAs(4, 5, 5, 5, 5)) {
            handleCaseC();
        }
    }

    private void handleCaseA() {
        final List<Edge> middleB = graph.getEdges().stream().filter(this::isEdgeB).collect(Collectors.toList());
        final List<Edge> middleA = middleB.stream().filter(edge ->
                        coder.getCodeForFace(edge.getVFrom().getOppositeFace(edge)).shortStartsAs(4, 5, 5, 4, 5) ||
                                coder.getCodeForFace(edge.getVTo().getOppositeFace(edge)).shortStartsAs(4, 5, 5, 4, 5))
                .collect(Collectors.toList());

        for (final Edge middleEdgeB : middleB) {
            addOperandsA(middleEdgeB);
        }

        if (middleA.size() == 2) {
            final Edge middleEdge1 = middleA.get(0);
            final Face square1_1 = middleEdge1.getVFrom().getOppositeFace(middleEdge1);
            final Face square1_2 = middleEdge1.getVTo().getOppositeFace(middleEdge1);
            final Edge middleEdge2 = middleA.get(1);
            final Face square2_1 = middleEdge2.getVFrom().getOppositeFace(middleEdge2);
            final Face square2_2 = middleEdge2.getVTo().getOppositeFace(middleEdge2);

            final List<Face> squaresA = Arrays.stream(new Face[]{square1_1, square1_2, square2_1, square2_2})
                    .filter(square -> coder.getCodeForFace(square).shortStartsAs(4,5,5,4,5))
                    .collect(Collectors.toList());

            if (squaresA.size() == 2) {
                squaresA.stream().map(Face::getAdjacentEdges)
                        .flatMap(List::stream)
                        .map(edge -> edge.getLeftFace().size() == 4 ? edge.getRightFace() : edge.getLeftFace())
                        .filter(face -> face.size() == 5 &&
                                middleEdge1.getLeftFace() != face && middleEdge1.getRightFace() != face &&
                                middleEdge2.getLeftFace() != face && middleEdge2.getRightFace() != face)
                        .map(face -> face.getAdjacentEdges().stream().map(edge -> edge.getOppositeFace(face)).collect(Collectors.toList()))
                        .flatMap(List::stream)
                        .filter(face -> face.size() == 4 && !coder.getCodeForFace(face).shortStartsAs(4, 5, 5, 4, 5))
                        .forEach(this::addOperandsB);
            }

        } else if (middleA.size() == 1) {
            //B
            final Edge middleEdge = middleA.get(0);
            final Face square1 = middleEdge.getVFrom().getOppositeFace(middleEdge);
            final Face square2 = middleEdge.getVTo().getOppositeFace(middleEdge);
            final boolean bothSided = coder.getCodeForFace(square1).shortStartsAs(4, 5, 5, 4, 5) &&
                    coder.getCodeForFace(square2).shortStartsAs(4, 5, 5, 4, 5);

            //B*
            for (final Face squareA : new Face[]{square1, square2}) {
                if (coder.getCodeForFace(squareA).shortStartsAs(4, 5, 5, 4, 5)) {
                    final Face otherSquare = squareA == square1 ? square2 : square1;
                    if (!coder.getCodeForFace(otherSquare).shortStartsAs(4, 5, 5, 4, 5)) {
                        final Face faceA = squareA.getAdjacentEdges().stream()
                                .map(edge -> edge.getOppositeFace(squareA))
                                .filter(face -> face.size() == 5 && face != middleEdge.getRightFace() && face != middleEdge.getLeftFace())
                                .findAny()
                                .orElseThrow();

                        faceA.getAdjacentEdges().stream()
                                .map(edge -> edge.getOppositeFace(faceA))
                                .filter(face -> face.size() == 4 && face != squareA)
                                .forEach(this::addOperandsB);
                    }
                }
            }

            addOperandsB(square1);
            addOperandsB(square2);

            final LexCode secondGadgetLexCode = findSecondLowestGadget(square1, square2);

            if (secondGadgetLexCode.isMaximumLexCode()) return;
            if (secondGadgetLexCode.shortStartsAs(4, 5, 5, 4)) return;

            //C
            addOperandsC(middleEdge);

            if(secondGadgetLexCode.shortStartsAs(4, 5, 5, 5, 5)) return;

            //D
            final List<Edge> potentialOperandsD = new ArrayList<>(2);
            final List<Edge> potentialOperandsE = new ArrayList<>(4);
            final List<Edge> potentialOperandsG = new ArrayList<>(2);
            final List<Operand> potentialOperandsF = new ArrayList<>();

            for (final Face square : new Face[]{square1, square2}){
                final List<Edge> extrudingEdges = square.getExtrudingEdges();
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
                    final Vertex leftVertexPOV = oppositeEdge.getLeftFromPOVofFace(faceA);
                    final int indexOfOppositeEdgeInA = faceA.getIndexOfE(oppositeEdge);
                    final int indexOfAdditionalOrientationF;
                    final int indexOfOperandG;

                    if (leftVertexPOV.getOppositeFace(oppositeEdge) == square) {
                        indexOfOperandG = indexOfOppositeEdgeInA + 2;
                        indexOfAdditionalOrientationF = indexOfOppositeEdgeInA + 3;
                    } else {
                        indexOfOperandG = indexOfOppositeEdgeInA + 3;
                        indexOfAdditionalOrientationF = indexOfOppositeEdgeInA + 2;
                    }
                    potentialOperandsF.add(new Operand(faceA, oppositeEdge, null));
                    if (faceA.getEdge(indexOfAdditionalOrientationF + 2).getOppositeFace(faceA).size() != 4 &&
                            faceA.getEdge(indexOfAdditionalOrientationF + 3).getOppositeFace(faceA).size() != 4) {
                        potentialOperandsF.add(new Operand(faceA, faceA.getEdge(indexOfAdditionalOrientationF), null));
                    }

                    final Edge operandG = faceA.getEdge(indexOfOperandG);
                    if (operandG.getVFrom().getOppositeFace(operandG).size() > 4 && operandG.getVTo().getOppositeFace(operandG).size() > 4 && !bothSided) {
                        potentialOperandsG.add(operandG);
                    }
                }
            }

            for (final Edge edge : potentialOperandsD) {
                addOperandsD(edge);
            }

            if (secondGadgetLexCode.shortStartsAs(4, 5, 5, 5)) {
                final List<Face> facesD = graph.getFaces().stream()
                        .filter(face -> face.size() == 4 && coder.getCodeForFace(face).shortStartsAs(4, 5, 5, 5))
                        .collect(Collectors.toList());
                if (facesD.size() == 1) {
                    final List<Face> surroundingsD = facesD.get(0).getIncidentFaces().stream()
                            .filter(face -> face.size() == 5)
                            .collect(Collectors.toList());

                    if (!bothSided){
                        final List<Edge> potentialAdditionalOperandsE = potentialOperandsE.stream()
                                .filter(edge -> edge.getVFrom().getOppositeFace(edge).size() == 5 || edge.getVTo().getOppositeFace(edge).size() == 5)
                                .collect(Collectors.toList());

                        if (!potentialAdditionalOperandsE.isEmpty()) {
                            for (final Edge e : potentialOperandsE) {
                                if (surroundingsD.contains(e.getVFrom().getOppositeFace(e)) || surroundingsD.contains(e.getVTo().getOppositeFace(e))) {
                                    addOperandsE(e);
                                }
                            }
                        }
                    }

                    final List<Operand> potentialAdditionalOperandsF = potentialOperandsF.stream()
                            .filter(operand -> {
                                final int indexOfOrientation = operand.getFace().getIndexOfE(operand.getEdge());
                                final Face face = operand.getFace();
                                return operand.getEdge().getLeftFace().size() == 5 &&
                                        operand.getEdge().getRightFace().size() == 5 &&
                                        (face.getEdge(indexOfOrientation + 2).getOppositeFace(face).size() == 5 ||
                                                face.getEdge(indexOfOrientation + 3).getOppositeFace(face).size() == 5);
                            })
                            .collect(Collectors.toList());
                    if (!potentialAdditionalOperandsF.isEmpty()) {
                        for (final Operand potentialOperandF : potentialAdditionalOperandsF) {
                            final Face face = potentialOperandF.getFace();
                            final int indexOfOrientation = face.getIndexOfE(potentialOperandF.getEdge());
                            if (surroundingsD.contains(face.getEdge(indexOfOrientation + 2).getOppositeFace(face)) ||
                                    surroundingsD.contains(face.getEdge(indexOfOrientation + 3).getOppositeFace(face))) {
                                addOperandsF(face, potentialOperandF.getEdge());
                            }
                        }
                    }

                }
                return;
            }

            //E F
            for (final Edge edge : potentialOperandsE) {
                addOperandsE(edge);
            }

            for (final Operand operandF : potentialOperandsF) {
                addOperandsF(operandF.getFace(), operandF.getEdge());
            }

            addOperandsF(middleEdge.getLeftFace(), middleEdge);
            addOperandsF(middleEdge.getRightFace(), middleEdge);

            if (secondGadgetLexCode.shortStartsAs(4, 5, 5)) {
                final List<Face> facesEF = graph.getFaces().stream()
                        .filter(face -> face.size() == 4 && coder.getCodeForFace(face).shortStartsAs(4, 5, 5) &&
                                face != square1 && face != square2)
                        .collect(Collectors.toList());
                if (facesEF.size() == 1) {
                    final Face faceEF = facesEF.get(0);
                    final List<Face> surroundingsEF = faceEF.getIncidentFaces().stream().filter(face -> face.size() == 5)
                            .collect(Collectors.toList());
                    for (final Edge operandG : potentialOperandsG) {
                        if (surroundingsEF.contains(operandG.getVFrom().getOppositeFace(operandG)) ||
                                surroundingsEF.contains(operandG.getVTo().getOppositeFace(operandG))) {
                            addOperandsG(operandG);
                        }
                    }
                }
                return;
            }

            //G
            for (final Edge potentialG : potentialOperandsG) {
                addOperandsG(potentialG);
            }
        }
    }

    private void handleCaseB() {
        final List<Edge> middleB = graph.getEdges().stream().filter(this::isEdgeB).collect(Collectors.toList());
        for (final Edge middleEdge : middleB) {
            addOperandsA(middleEdge);
        }

        final List<Face> squares = graph.getFaces().stream().filter(face -> face.size() == 4).collect(Collectors.toList());
        for (final Face square : squares) {
            addOperandsB(square);
        }

        if (middleB.size() == 1) {
            final Edge middleEdge = middleB.get(0);
            final Face square1 = middleEdge.getVFrom().getOppositeFace(middleEdge);
            final Face square2 = middleEdge.getVTo().getOppositeFace(middleEdge);

            final LexCode secondLowest = findSecondLowestGadget(square1, square2);

            //C + D
            addOperandsC(middleEdge);
            if (secondLowest.shortStartsAs(4, 5, 5, 5)) return;

            for (final Face square : new Face[]{square1, square2}) {
                final List<Edge> extrudingEdges = square.getExtrudingEdges();
                final int indexOfMiddleEdge = extrudingEdges.indexOf(middleEdge);

                for (final int offset : new int[]{1,3}) {
                    addOperandsE(extrudingEdges.get((indexOfMiddleEdge + offset) % 4));
                }
            }

            addOperandsF(middleEdge.getLeftFace(), middleEdge);
            addOperandsF(middleEdge.getRightFace(), middleEdge);
        }
    }

    private void handleCaseC() {
        final List<Face> squares = graph.getFaces().stream()
                .filter(face -> face.size() == 4)
                .collect(Collectors.toList());
        squares.forEach(this::addOperandsB);

        final List<Face> middleC = squares.stream()
                .filter(face -> coder.getCodeForFace(face).shortStartsAs(4, 5, 5, 5, 5))
                .collect(Collectors.toList());
        if (middleC.size() == 1) {
            final Face middleSquare = middleC.get(0);

            final List<Edge> extrudingEdges = middleSquare.getExtrudingEdges();
            final List<Operand> potentialOperandsF = new ArrayList<>();

            for (final Edge extrudingEdge : extrudingEdges) {
                addOperandsD(extrudingEdge);

                for (final Vertex endVertex : new Vertex[]{extrudingEdge.getVFrom(), extrudingEdge.getVTo()}) {
                    final Face oppositeFace = endVertex.getOppositeFace(extrudingEdge);
                    if (oppositeFace.size() == 5) {
                        final int indexOfExtruding = endVertex.getAdjacentEdges().indexOf(extrudingEdge);
                        final Edge nextEdge = endVertex.getAdjacentEdges().get((indexOfExtruding + 1) % 3);
                        final int indexOfNext = oppositeFace.getIndexOfE(nextEdge);
                        final Edge oppositeEdge = oppositeFace.getEdge(indexOfNext + 2);
                        potentialOperandsF.add(new Operand(oppositeFace, oppositeEdge, null));
                    }
                }
            }

            final List<Face> facesD = squares.stream()
                    .filter(face -> coder.getCodeForFace(face).shortStartsAs(4, 5, 5, 5))
                    .filter(face -> face != middleSquare)
                    .collect(Collectors.toList());
            if (facesD.size() == 1) {
                final Face faceD = facesD.get(0);
                final List<Face> surroundingPentagons = faceD.getIncidentFaces().stream()
                        .filter(face -> face.size() == 5)
                        .collect(Collectors.toList());
                for (final Operand potentialOperand : potentialOperandsF) {
                    for (final Face surroundingFace : surroundingPentagons) {
                        if (potentialOperand.getEdge().getOppositeFace(potentialOperand.getFace()) == surroundingFace) {
                            addOperandsF(potentialOperand.getFace(), potentialOperand.getEdge());
                        }
                    }
                }
            }

            if (!facesD.isEmpty()) return;

            for (final Operand potentialOperand : potentialOperandsF) {
                addOperandsF(potentialOperand.getFace(), potentialOperand.getEdge());
            }

            for (final Edge extrudingEdge : extrudingEdges) {
                addOperandsF(extrudingEdge.getLeftFace(), extrudingEdge);
                addOperandsF(extrudingEdge.getRightFace(), extrudingEdge);
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
