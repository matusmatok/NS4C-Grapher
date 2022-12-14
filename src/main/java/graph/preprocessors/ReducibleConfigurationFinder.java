package graph.preprocessors;

import graph.components.Edge;
import graph.components.Face;
import graph.components.Graph;
import graph.components.Vertex;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ReducibleConfigurationFinder {

    private final Graph graph;
    private List<FaceAdjacentAmbiguousVertices> cachedFaces;

    @Getter
    @AllArgsConstructor
    private class FaceAdjacentAmbiguousVertices {
        private List<Vertex> adjacentVertices;

        boolean isReducible() {
            return adjacentVertices.stream().filter(vertex -> vertex.getWeight() == 0).count() % 2 == 1;
        }
    }


    public ReducibleConfigurationFinder(Graph graph) {
        this.graph = graph;
    }

    public boolean containsPolarIndependentReducibleConfig() {
        return containsTriangle() || containsLadder();
    }

    public void buildCache(Set<Vertex> ambiguousVertices) {
        cachedFaces = new ArrayList<>();
        final List<Face> squares = graph.getFaces().stream().filter(face -> face.size() == 4).collect(Collectors.toList());
        final Set<Vertex> vertexSet = new HashSet<>();
        for (Face square : squares) {
            for (Edge edge : square.getAdjacentEdges()) {
                vertexSet.add(edge.getVTo());
                vertexSet.add(edge.getVFrom());
            }

            final List<Vertex> localAmbiguousVertices = new ArrayList<>();
            for (Vertex vertex : vertexSet) {
                if (ambiguousVertices.contains(vertex)) localAmbiguousVertices.add(vertex);
            }
            if (!localAmbiguousVertices.isEmpty()) cachedFaces.add(new FaceAdjacentAmbiguousVertices(localAmbiguousVertices));
            vertexSet.clear();
        }
    }

    public boolean containsPolarDependentReducibleConfig() {
        for (FaceAdjacentAmbiguousVertices face : cachedFaces) {
            if (face.isReducible()) return true;
        }
        return false;
    }

    private boolean containsTriangle() {
        return graph.getFaces().stream().filter(face -> face.size() == 3).findAny().isPresent();
    }

    private boolean containsLadder() {
        for (Edge edge : graph.getEdges()) {
            final Face leftFace = edge.getLeftFace();
            final Face rightFace = edge.getRightFace();
            if (leftFace.size() == 4 && rightFace.size() == 4) {
                if (edge.getVTo().getThirdFace(leftFace, rightFace).size() >4
                 && edge.getVFrom().getThirdFace(leftFace,rightFace).size() >4)
                    return true;
            }
        }
        return false;
    }
}
