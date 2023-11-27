package graph.canoniccoders;

import graph.components.AbstractGraph;
import graph.components.Edge;
import graph.components.Face;
import graph.components.Vertex;
import graph.utils.EasyList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

public class CachingCoder {
    private final AbstractGraph graph;
    @Getter
    private final LexCode graphCode;
    private final Map<Face, FaceNode> faceRootedMap = new HashMap<>();

    @RequiredArgsConstructor
    private static class FaceNode {
        private final AbstractGraph graph;
        private final Face face;
        private final Map<Edge, FaceEdgeNode> subElements = new HashMap<>();
        private LexCode canonicalCode = null;
        private List<Edge> canonicalEdges = null;

        private LexCode getCodeForEdge(final Edge edge) {
            if (!subElements.containsKey(edge)) {
                final FaceEdgeNode edgeNode = new FaceEdgeNode(this, edge);
                subElements.put(edge, edgeNode);
            }
            return subElements.get(edge).getCanonicalCode();
        }

        private LexCode getCanonicalCode() {
            if (canonicalCode == null) {
                final int minimalFaceSize = face.getAdjacentEdges()
                        .stream().map(edge -> edge.getOppositeFace(face).size())
                        .min(Integer::compareTo).orElseThrow();
                LexCode bestCode = LexCode.getMaximumLexCode();

                for (final Edge e : face.getAdjacentEdges()) {
                    if (e.getOppositeFace(face).size() == minimalFaceSize) {
                        final LexCode localCode = getCodeForEdge(e);
                        final int comparisonResult = localCode.compareTo(bestCode);
                        if (comparisonResult < 0) {
                            bestCode = localCode;
                            canonicalEdges = EasyList.of(e);
                        } else if (comparisonResult == 0) {
                            canonicalEdges.add(e);
                        }
                    }
                }
                canonicalCode = bestCode;
            }
            return canonicalCode;
        }

        private List<Edge> getCanonicalEdges() {
            if (canonicalCode == null) {
                getCanonicalCode();
            }
            return canonicalEdges;
        }
    }

    @RequiredArgsConstructor
    private static class FaceEdgeNode {
        private final FaceNode parent;
        private final Edge edge;
        private final Map<Vertex, LexCode> lexCodes = new HashMap<>();
        private LexCode canonicalCode = null;
        private List<Vertex> canonicalVertices = null;
        @Getter
        private boolean vertexTransitive = false;

        final LexCode getCodeForVertex(final Vertex vertex) {
            if (lexCodes.containsKey(vertex)){
                return lexCodes.get(vertex);
            }
            final LexCode lexCode;
            if (edge.getLeftFromPOVofFace(parent.face) == vertex) {
                 lexCode = CanonicalCoder.noLadderExtension
                         ? CanonicalCoder.getNoLadderCodeWithSetup(parent.graph, parent.face, edge.getOppositeFace(parent.face), -1)
                         : CanonicalCoder.getCodeWithSetup(parent.graph, parent.face, edge.getOppositeFace(parent.face), -1);
            } else if (edge.getRightFromPOVofFace(parent.face) == vertex) {
                lexCode = CanonicalCoder.noLadderExtension
                        ? CanonicalCoder.getNoLadderCodeWithSetup(parent.graph, parent.face, edge.getOppositeFace(parent.face), 1)
                        : CanonicalCoder.getCodeWithSetup(parent.graph, parent.face, edge.getOppositeFace(parent.face), 1);
            } else {
                throw new IllegalArgumentException("Vertex " + vertex + " not present at edge " + edge);
            }

            lexCodes.put(vertex, lexCode);
            return lexCode;
        }

        final LexCode getCanonicalCode() {
            if (canonicalCode != null) {
                return canonicalCode;
            }
            final LexCode vFromCode = getCodeForVertex(edge.getVFrom());
            final LexCode vToCode = getCodeForVertex(edge.getVTo());

            final int result = vFromCode.compareTo(vToCode);
            if (result < 0) {
                this.canonicalCode = vFromCode;
                this.canonicalVertices = List.of(edge.getVFrom());
            } else if (result > 0) {
                this.canonicalCode = vToCode;
                this.canonicalVertices = List.of(edge.getVTo());
            } else {
                this.canonicalCode = vFromCode;
                this.canonicalVertices = List.of(edge.getVFrom(), edge.getVTo());
                vertexTransitive = true;
            }
            return this.canonicalCode;
        }

        final List<Vertex> getCanonicalVertices() {
            if (canonicalCode == null) {
                getCanonicalCode();
            }
            return canonicalVertices;
        }
    }

    public CachingCoder(final AbstractGraph graph) {
        final List<Face> smallestFaces = graph.getFaces()
                .stream().filter(face -> face.size() == graph.minimalFaceSize())
                .collect(Collectors.toList());
        LexCode bestCode = LexCode.getMaximumLexCode();

        for (final Face face : smallestFaces) {
            final FaceNode currentFaceNode = new FaceNode(graph, face);
            faceRootedMap.put(face, currentFaceNode);
            final LexCode localCode = currentFaceNode.getCanonicalCode();
            if (localCode.compareTo(bestCode) < 0) {
                bestCode = localCode;
            }
        }
        this.graphCode = bestCode;
        this.graph = graph;
    }

    public LexCode getLexCode(final Face face) {
        if (!faceRootedMap.containsKey(face)) {
            faceRootedMap.put(face, new FaceNode(graph, face));
        }
        return faceRootedMap.get(face).getCanonicalCode();
    }

    public LexCode getLexCode(final Face face, final Edge edge) {
        if (!faceRootedMap.containsKey(face)) {
            faceRootedMap.put(face, new FaceNode(graph, face));
        }
        return faceRootedMap.get(face).getCodeForEdge(edge);
    }

    public LexCode getLexCode(final Face face, final Vertex vertex) {
        return vertex.getAdjacentEdges().stream()
                .filter(edge -> edge.getLeftFace() == face || edge.getRightFace() == face)
                .map(edge -> getLexCode(face, edge, vertex))
                .min(LexCode::compareTo)
                .orElseThrow();
    }

    public LexCode getLexCode(final Face face, final Edge edge, final Vertex vertex) {
        if (!faceRootedMap.containsKey(face)) {
            faceRootedMap.put(face, new FaceNode(graph, face));
        }
        final FaceNode faceNode = faceRootedMap.get(face);
        if (!faceNode.subElements.containsKey(edge)) {
            faceNode.subElements.put(edge, new FaceEdgeNode(faceNode, edge));
        }
        final FaceEdgeNode faceEdgeNode = faceNode.subElements.get(edge);
        return faceEdgeNode.getCodeForVertex(vertex);
    }

    public LexCode getLexCode(final Edge edge) {
        final LexCode leftCode = getLexCode(edge.getLeftFace(), edge);
        final LexCode rightCode = getLexCode(edge.getRightFace(), edge);
        return leftCode.compareTo(rightCode) < 0 ? leftCode : rightCode;
    }

    public boolean isEdgeVertexTransitive(final Edge edge) {
        final Face face = edge.getRightFace().size() < edge.getLeftFace().size() ? edge.getRightFace() : edge.getLeftFace();
        getLexCode(face, edge);

        final FaceEdgeNode faceEdgeNode = faceRootedMap.get(face).subElements.get(edge);
        return faceEdgeNode.isVertexTransitive();
    }

    public Set<Vertex> getCanonicalEdgeAndVertex(final Face face) {
        final Set<Vertex> resultSet = new HashSet<>();
        final List<Edge> canonicalEdges = faceRootedMap.get(face).getCanonicalEdges();
        final Map<Edge, FaceEdgeNode> subElements = faceRootedMap.get(face).subElements;

        for (final Edge edge : canonicalEdges) {
            final List<Vertex> canonicalVertices = subElements.get(edge).getCanonicalVertices();
            resultSet.addAll(canonicalVertices);
        }
        return resultSet;
    }
}
