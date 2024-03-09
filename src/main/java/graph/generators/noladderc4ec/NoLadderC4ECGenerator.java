package graph.generators.noladderc4ec;

import graph.canoniccoders.CachingCoder;
import graph.components.Edge;
import graph.components.Edge.OrientedEdge;
import graph.components.Face;
import graph.components.NoLadderC4ECGraph;
import graph.components.Vertex;
import graph.generators.noladderc4ec.NoLadderC4ECOperandProvider.Operand;
import graph.processors.Processor;
import graph.utils.GlobalProgressBar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoLadderC4ECGenerator {

    private final NoLadderC4ECGraph graph;
    private final Processor processor;
    private final int depth;

    public NoLadderC4ECGenerator(NoLadderC4ECGraph graph, Processor processor, int depth) {
        if (depth % 2 != 0) throw new IllegalArgumentException("Depth must be an even number.");
        this.graph = graph;
        this.processor = processor;
        this.depth = depth;
    }

    public boolean verifyTwoFactors() {
        return false;
    }

    public void generate() {
        GlobalProgressBar.startProgressBar();
        if (graph.getVertices().size() != 14) throw new IllegalArgumentException();

        graph.operationB(graph.getFace(3), graph.getVertex(2));
        recursiveGenerate(new CachingCoder(graph));
        graph.reductionB(graph.getFace(3), graph.getVertex(2));

        graph.operationB(graph.getFace(3), graph.getVertex(3));
        recursiveGenerate(new CachingCoder(graph));
        graph.reductionB(graph.getFace(3), graph.getVertex(3));

        graph.operationC(graph.getEdge(6, 7));
            graph.operationB(graph.getFace(9), graph.getVertex(6));
            recursiveGenerate(new CachingCoder(graph));
            graph.reductionB(graph.getFace(9), graph.getVertex(6));

            graph.operationD(graph.getEdge(0, 2));
            recursiveGenerate(new CachingCoder(graph));
            graph.reductionD();
        graph.reductionC();

        graph.operationD(graph.getEdge(3, 4));
        recursiveGenerate(new CachingCoder(graph));
        graph.reductionD();

        graph.operationF(graph.getFace(4), graph.getEdge(12));
        recursiveGenerate(new CachingCoder(graph));
        graph.reductionF();
        GlobalProgressBar.terminate();
    }

    private void recursiveGenerate(final CachingCoder coder) {
        if (graph.getVertices().size() >= depth) {
            processor.hasProperty();
            return;
        }

        if (graph.getVertices().size() == 28) {
            GlobalProgressBar.step();
        }

        graph.toNormalForm();
        final String graphCode = graph.toString();

        final NoLadderC4ECOperandProvider provider = new NoLadderC4ECOperandProvider(graph, coder);
        for (final Operand operandA : provider.getOperandsA()) {
            final OrientedEdge orientedEdge = operandA.getOrientedEdge();
            graph.operationA(orientedEdge);
            final CachingCoder childCoder = new CachingCoder(graph);
            final Face square = orientedEdge.getCorrespondingEdge().getLeftFace().size() == 4 ?
                    orientedEdge.getCorrespondingEdge().getLeftFace() :
                    orientedEdge.getCorrespondingEdge().getRightFace();
            if (childCoder.getGraphCode().equals(childCoder.getLexCode(square))) {
                recursiveGenerate(childCoder);
            }
            graph.reductionA();
        }

        if (depth >= graph.getVertices().size() + 6) {
            for (final Operand operandB : provider.getOperandsB()) {
                final Face originFace = operandB.getFace();
                graph.operationB(originFace, operandB.getVertex());
                final Face newFace = graph.getFace(graph.getFaces().size() - 1);
                final CachingCoder childCoder = new CachingCoder(graph);
                if (childCoder.getGraphCode().equals(childCoder.getLexCode(originFace)) || childCoder.getGraphCode().equals(childCoder.getLexCode(newFace))) {
                    recursiveGenerate(childCoder);
                }
                graph.reductionB(originFace, operandB.getVertex());
            }
        }

        for (final Operand operandC : provider.getOperandsC()) {
            graph.operationC(operandC.getEdge());
            final Face newFace = graph.getFace(graph.getFaces().size() - 1);
            final CachingCoder childCoder = new CachingCoder(graph);
            if (childCoder.getGraphCode().equals(childCoder.getLexCode(newFace))) {
                recursiveGenerate(childCoder);
            }
            graph.reductionC();
        }

        for (final Operand operandD : provider.getOperandsD()) {
            graph.operationD(operandD.getEdge());
            final Face newFace = graph.getFace(graph.getFaces().size() - 1);
            final CachingCoder childCoder = new CachingCoder(graph);
            if (childCoder.getGraphCode().equals(childCoder.getLexCode(newFace))) {
                recursiveGenerate(childCoder);
            }
            graph.reductionD();
        }

        for (final Operand operandE : provider.getOperandsE()) {
            graph.operationE(operandE.getEdge());
            final Face newFace = graph.getFace(graph.getFaces().size() - 1);
            final CachingCoder childCoder = new CachingCoder(graph);
            if (childCoder.getGraphCode().equals(childCoder.getLexCode(newFace))) {
                final Edge oppositeEdge = graph.getEdge(graph.getEdges().size() - 3);
                final Set<Edge> canonicalEdges = childCoder.getCanonicalEdges(newFace);
                if (canonicalEdges.contains(operandE.getEdge()) || canonicalEdges.contains(oppositeEdge)) {
                    recursiveGenerate(childCoder);
                } else {
                    final Face oppositeThroughOperand = operandE.getEdge().getOppositeFace(newFace);
                    final Face oppositeThroughOpposite = oppositeEdge.getOppositeFace(newFace);
                    final Face pentagon = oppositeThroughOpposite.size() == 5 ? oppositeThroughOpposite : oppositeThroughOperand;
                    if (pentagon.getIncidentFaces().stream().filter(face -> face.size() == 4).count() > 1) {
                        recursiveGenerate(childCoder);
                    }
                }
            }
            graph.reductionE();
        }

        if (depth >= graph.getVertices().size() + 4) {
            for (final Operand operandF : provider.getOperandsF()) {
                graph.operationF(operandF.getFace(), operandF.getEdge());
                final Face newFace = graph.getFace(graph.getFaces().size() - 1);
                final CachingCoder childCoder = new CachingCoder(graph);
                if (childCoder.getLexCode(newFace).equals(childCoder.getGraphCode())) {
                    recursiveGenerate(childCoder);
                }
                graph.reductionF();
            }
        }

        for (final Operand operandG : provider.getOperandsG()) {
            graph.operationG(operandG.getEdge());
            final Face newFace = graph.getFace(graph.getFaces().size() - 1);
            final CachingCoder childCoder = new CachingCoder(graph);
            if (childCoder.getLexCode(newFace).equals(childCoder.getGraphCode())) {
                final Edge operandEdge = operandG.getEdge();
                final Edge oppositeEdge = graph.getEdge(graph.getEdges().size() - 3);
                final Set<Edge> canonicalEdges = childCoder.getCanonicalEdges(newFace);
                if (canonicalEdges.contains(operandEdge) || canonicalEdges.contains(oppositeEdge)) {
                    recursiveGenerate(childCoder);
                } else {
                    final int index = newFace.getIndexOfE(operandEdge);
                    final Face other1 = newFace.getEdge(index + 1).getOppositeFace(newFace);
                    final Face other2 = newFace.getEdge(index + 3).getOppositeFace(newFace);
                    final List<Face> other1Neighbours = new ArrayList<>(other1.getIncidentFaces());
                    other1Neighbours.retainAll(other2.getIncidentFaces());
                    if (other1Neighbours.size() > 3) {
                        recursiveGenerate(childCoder);
                    }
                }
            }
            graph.reductionG();
        }

        for (final Operand operandH : provider.getOperandsH()) {
            graph.operationH(operandH.getFace(), operandH.getEdge());
            final Face newFace = graph.getFace(graph.getFaces().size() - 1);
            final CachingCoder childCoder = new CachingCoder(graph);
            if (childCoder.getLexCode(newFace).equals(childCoder.getGraphCode())) {
                final Edge borderEdge = graph.getEdge(graph.getEdges().size() - 3);
                final Set<Edge> canonicalEdges = childCoder.getCanonicalEdges(newFace);
                if (canonicalEdges.contains(borderEdge)) {
                    recursiveGenerate(childCoder);
                } else {
                    final Set<Vertex> canonicalEdgeCanonicalVertices = new HashSet<>();
                    for (final Edge canonicalEdge : canonicalEdges) {
                        canonicalEdgeCanonicalVertices.addAll(childCoder.getCanonicalVertices(newFace, canonicalEdge));
                    }
                    if (canonicalEdgeCanonicalVertices.contains(borderEdge.getVTo()) ||
                            canonicalEdgeCanonicalVertices.contains(borderEdge.getVFrom())) {
                        final Edge canonicalEdge = canonicalEdges.iterator().next();
                        final Face oppositeCanonicalFace = canonicalEdge.getOppositeFace(newFace);
                        final int indexOfCanonical = newFace.getIndexOfE(canonicalEdge);
                        final Face opposite1 = newFace.getEdge(indexOfCanonical + 2).getOppositeFace(newFace);
                        final Face opposite2 = newFace.getEdge(indexOfCanonical + 3).getOppositeFace(newFace);

                        final Set<Face> opposite1Faces = new HashSet<>(opposite1.getIncidentFaces());
                        final Set<Face> opposite2Faces = new HashSet<>(opposite2.getIncidentFaces());
                        opposite1Faces.retainAll(oppositeCanonicalFace.getIncidentFaces());
                        opposite2Faces.retainAll(oppositeCanonicalFace.getIncidentFaces());

                        if (opposite1Faces.size() > 2 || opposite2Faces.size() > 2) {
                            recursiveGenerate(childCoder);
                        }
                    }
                }
            }
            graph.reductionH();
        }
    }
}
