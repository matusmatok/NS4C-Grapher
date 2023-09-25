package graph.canoniccoders;

import graph.components.Face;
import graph.components.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class CanonicalCoder {

    public static final int CODE_FOR_VISITED = 0;

    public static LexCode getExtendedCodeForFace(final Graph g, final Face f) {
        return getCodeForFace(g, f, true);
    }

    public static LexCode getCodeForFace(final Graph g, final Face f) {
        return getCodeForFace(g, f, false);
    }

    private static LexCode getCodeWithSetup(final Graph g, final Face origin, final Face neigh, final int direction,
                                            final boolean extension) {
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

    private static LexCode getCodeForFace(final Graph g, final Face face, final boolean extension) {
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

                final LexCode localCode = getCodeWithSetup(g, face, neigh, directions[i], extension);

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

    public static LexCode extendedCodeForGraph(Graph g) {
        return lexCodeForGraph(g, true);
    }
    public static LexCode lexCodeForGraph(Graph g) {
        return lexCodeForGraph(g, false);
    }

    private static LexCode lexCodeForGraph(Graph g, final boolean extension) {
        final int minimalFaceSize = g.minimalFaceSize();
        final List<Face> minimalFaces = g.getFaces().stream().filter(f -> f.size() == minimalFaceSize).collect(Collectors.toList());

        LexCode bestCode = null;
        for (final Face face : minimalFaces) {
            final LexCode code = extension ? getExtendedCodeForFace(g, face) : getCodeForFace(g, face);
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
