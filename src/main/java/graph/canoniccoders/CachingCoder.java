package graph.canoniccoders;

import graph.components.AbstractGraph;
import graph.components.Edge;
import graph.components.Face;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        private final Map<Edge, LexCode> subElements = new HashMap<>();
        private LexCode lexCode = null;

        private LexCode getCodeForEdge(final Edge edge) {
            if (!subElements.containsKey(edge)) {
                subElements.put(edge, CanonicalCoder.getCodeForFaceAndEdge(graph, face, edge));
            }
            return subElements.get(edge);
        }

        private LexCode getCanonicalCode() {
            if (lexCode == null) {
                final int minimalFaceSize = face.getAdjacentEdges()
                        .stream().map(edge -> edge.getOppositeFace(face).size())
                        .min(Integer::compareTo).orElseThrow();
                LexCode bestCode = LexCode.getMaximumLexCode();

                for (final Edge e : face.getAdjacentEdges()) {
                    if (!subElements.containsKey(e)) {
                        final Face oppositeFace = e.getOppositeFace(face);
                        if (oppositeFace.size() == minimalFaceSize) {
                            final LexCode localCode = CanonicalCoder.getCodeForFaceAndEdge(graph, face, e);
                            subElements.put(e, localCode);

                            if (localCode.compareTo(bestCode) < 0) {
                                bestCode = localCode;
                            }
                        }
                    } else {
                        final LexCode localCode = subElements.get(e);
                        if (localCode.compareTo(bestCode) < 0) {
                            bestCode = localCode;
                        }
                    }
                }

                lexCode = bestCode;
            }
            return lexCode;
        }
    }

    public CachingCoder(final AbstractGraph graph) {
        final List<Face> smallestFaces = graph.getFaces()
                .stream().filter(face -> face.size() == graph.minimalFaceSize())
                .collect(Collectors.toList());
        LexCode bestCode = LexCode.getMaximumLexCode();

        for (final Face face : smallestFaces) {
            final FaceNode currentFaceNode = new FaceNode(graph, face);
            faceRootedMap.put(face, new FaceNode(graph, face));
            final LexCode localCode = currentFaceNode.getCanonicalCode();
            if (localCode.compareTo(bestCode) < 0) {
                bestCode = localCode;
            }
        }
        this.graphCode = bestCode;
        this.graph = graph;
    }

    public LexCode getCodeForFace(final Face face) {
        if (!faceRootedMap.containsKey(face)) {
            faceRootedMap.put(face, new FaceNode(graph, face));
        }
        return faceRootedMap.get(face).getCanonicalCode();
    }

    public LexCode getCodeForFaceAndEdge(final Face face, final Edge edge) {
        if (!faceRootedMap.containsKey(face)) {
            faceRootedMap.put(face, new FaceNode(graph, face));
        }
        return faceRootedMap.get(face).getCodeForEdge(edge);
    }

    public LexCode getCodeForEdge(final Edge edge) {
        final LexCode leftCode = getCodeForFaceAndEdge(edge.getLeftFace(), edge);
        final LexCode rightCode = getCodeForFaceAndEdge(edge.getRightFace(), edge);
        return leftCode.compareTo(rightCode) < 0 ? leftCode : rightCode;
    }

}
