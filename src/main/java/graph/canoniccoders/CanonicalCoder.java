package graph.canoniccoders;

import graph.components.Edge;
import graph.components.Face;
import graph.components.AbstractGraph;
import graph.components.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class CanonicalCoder {

    public static final int CODE_FOR_VISITED = 0;

    private static LexCode getCodeWithSetup(final AbstractGraph g, final Face origin, final Face neigh, final int direction) {
        final List<Face> queue = new ArrayList<>();
        final Map<Face,Face> parent = new HashMap<>();
        final Set<Face> visited = new HashSet<>();
        final LexCode.LexCodeBuilder builder = LexCode.builder();

        builder.extendedAdd(origin.size());
        queue.add(origin);
        parent.put(origin, neigh);

        int queuePointer = 0;

        while(queue.size() < g.getFaces().size()) {
            final Face currentFace = queue.get(queuePointer);
            final List<Face> currentNeighs = currentFace.getIncidentFaces();
            final Face currentParent = parent.get(currentFace);
            final int indexOfParentInNeighs = currentFace.getIndexOfIncidentFace(currentParent);

            visited.add(currentFace);

            for(int i = 0; i < currentFace.size(); i++) {
                final int indexOfNeighInNeighs = (indexOfParentInNeighs + i*direction + currentFace.size()) % currentFace.size();
                final Face currentNeigh = currentNeighs.get(indexOfNeighInNeighs);

                if (!visited.contains(currentNeigh)) {
                    builder.extendedAdd(currentNeigh.size());
                    queue.add(currentNeigh);
                    parent.put(currentNeigh, currentFace);
                    visited.add(currentNeigh);
                } else {
                    builder.extendedAdd(CODE_FOR_VISITED);
                }
            }
            queuePointer++;
            if (queue.size() >= g.getFaces().size()) break;
        }

        return builder.shortAddAll(queue.stream().map(Face::size).collect(Collectors.toList())).build();
    }

    public static LexCode getCodeForVertex(final AbstractGraph g, final Vertex vertex) {
        LexCode bestCode = LexCode.getMaximumLexCode();

        for (final Edge edge : vertex.getAdjacentEdges()) {
            final Face leftFace = edge.getLeftFromPOV(vertex);
            final Face rightFace = edge.getRightFromPOV(vertex);
            final Face thirdFace = vertex.getThirdFace(leftFace, rightFace);

            if (leftFace.size() <= thirdFace.size() && rightFace.size() <= thirdFace.size()) {
                if (leftFace.size() <= rightFace.size()) {
                    final LexCode lexCode = getCodeWithSetup(g, leftFace, rightFace, 1);
                    if (lexCode.compareTo(bestCode) < 0) bestCode = lexCode;
                }
                if (rightFace.size() <= leftFace.size()) {
                    final LexCode lexCode = getCodeWithSetup(g, rightFace, leftFace, -1);
                    if (lexCode.compareTo(bestCode) < 0) bestCode = lexCode;
                }
            }
        }
        return bestCode;
    }

    public static LexCode getCodeForEdge(final AbstractGraph g, final Edge e) {
        final LexCode lexCode1;
        if (e.getLeftFace().size() <= e.getRightFace().size()) {
            lexCode1 = getCodeForFaceAndEdge(g, e.getLeftFace(), e);
        } else {
            lexCode1 = LexCode.getMaximumLexCode();
        }

        final LexCode lexCode2;
        if (e.getRightFace().size() <= e.getLeftFace().size()) {
            lexCode2 = getCodeForFaceAndEdge(g, e.getRightFace(), e);
        } else {
            lexCode2 = LexCode.getMaximumLexCode();
        }
        return (lexCode1.compareTo(lexCode2) < 0) ? lexCode1 : lexCode2;
    }

    public static LexCode getCodeForFaceAndEdge(final AbstractGraph g, final Face origin, final Edge border) {
        final Face neighbourFace = border.getOppositeFace(origin);
        final Face rightFace = border.getRightFromPOVofFace(origin).getThirdFace(origin, neighbourFace);
        final Face leftFace = border.getLeftFromPOVofFace(origin).getThirdFace(origin, neighbourFace);

        final LexCode lexCode1;
        if (rightFace.size() <= leftFace.size()) {
            lexCode1 = getCodeWithSetup(g, origin, neighbourFace, 1);
        } else {
            lexCode1 = LexCode.getMaximumLexCode();
        }

        final LexCode lexCode2;
        if (leftFace.size() <= rightFace.size()) {
            lexCode2 = getCodeWithSetup(g, origin, neighbourFace, -1);
        } else {
            lexCode2 = LexCode.getMaximumLexCode();
        }
        return (lexCode1.compareTo(lexCode2) < 0) ? lexCode1 : lexCode2;
    }

    public static LexCode getCodeForFace(final AbstractGraph g, final Face face) {
        final int smallestNeighSize = face.getIncidentFaces().stream().map(Face::size).min(Integer::compareTo).orElseThrow();
        final List<Face> smallestNeighs = face.getIncidentFaces().stream().filter(f -> f.size() == smallestNeighSize)
                .collect(Collectors.toList());

        LexCode bestCode = null;

        for (final Face neigh: smallestNeighs) {
            final int indexOfNeighInFace = face.getIndexOfIncidentFace(neigh);
            final int[] directions = {1, -1};
            final Face[] thirdFaces = {face.getIncidentFaces().get((indexOfNeighInFace + 1) % face.size()),
                    face.getIncidentFaces().get((indexOfNeighInFace -1 + face.size()) % face.size())};

            for (int i = 0; i < 2; i++) {
                if (thirdFaces[i].size() > thirdFaces[(i+1)%2].size()) {
                    continue;
                }

                final LexCode localCode = getCodeWithSetup(g, face, neigh, directions[i]);

                if (bestCode == null) {
                    bestCode = localCode;
                } else {
                    if (bestCode.compareTo(localCode) > 0) {
                        bestCode = localCode;
                    }
                }
            }
        }

        return Objects.requireNonNull(bestCode);
    }

    public static LexCode getCodeForGraph(AbstractGraph g) {
        final int minimalFaceSize = g.minimalFaceSize();
        final List<Face> minimalFaces = g.getFaces().stream().filter(f -> f.size() == minimalFaceSize).collect(Collectors.toList());

        LexCode bestCode = null;
        for (final Face face : minimalFaces) {
            final LexCode code = getCodeForFace(g, face);
            if (bestCode == null) {
                bestCode = code;
            } else {
                if (bestCode.compareTo(code) > 0) {
                    bestCode = code;
                }
            }
        }
        return bestCode;
    }
}
