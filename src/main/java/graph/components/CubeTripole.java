package graph.components;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CubeTripole {
    @Getter
    private final Vertex centralVertex;
    private final Set<Vertex> stepOneVertices;
    private final Set<Vertex> stepTwoVertices;
    @Getter
    private final Set<Vertex> allVertices;

    public CubeTripole(Vertex central) {
        this.centralVertex = central;
        stepOneVertices = new HashSet<>();
        stepOneVertices.addAll(central.getAdjacentEdges().stream().map((edge -> edge.getOppositeVertex(central))).collect(Collectors.toList()));
        stepTwoVertices = new HashSet<>();
        stepTwoVertices.addAll(stepOneVertices.stream().flatMap(vertex
                -> vertex.getAdjacentEdges().stream().map(edge
                    -> edge.getOppositeVertex(vertex)))
                .collect(Collectors.toList()));
        stepTwoVertices.remove(central);
        allVertices = new HashSet<>();
        allVertices.add(central);
        allVertices.addAll(stepOneVertices);
        allVertices.addAll(stepTwoVertices);
    }

    public void distributePolarity() {
        centralVertex.setSelfPositive();
        for(Vertex v : stepTwoVertices) v.setSelfPositive();
        for(Vertex v : stepOneVertices) v.setSelfPositive();
        stepOneVertices.stream().findAny().ifPresentOrElse(Vertex::setSelfNegative, () -> {throw new RuntimeException("stepOneVertices is empty");});
    }
}
