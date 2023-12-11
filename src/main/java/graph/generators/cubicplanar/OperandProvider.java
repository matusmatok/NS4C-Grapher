package graph.generators.cubicplanar;

import graph.components.Edge;
import graph.components.Face;
import graph.components.AbstractGraph;
import graph.components.Vertex;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OperandProvider {

    @AllArgsConstructor
    @Getter
    public class Operand {
        private int vertexId;
        private int edgeId;
        private int faceId;
    }

    public List<Operand> getOp3Operands(AbstractGraph graph){
        return getOp3OperandsAll(graph);
    }

    public List<Operand> getOp3OperandsAll(AbstractGraph graph) {
        return graph.getVertices()
                .stream().map((vertex -> new Operand(vertex.getId(), -1, -1)))
                .collect(Collectors.toList());
    }

    public List<Operand> getOp3Operands2Triangles(AbstractGraph graph) {
        final List<Face> triangles = graph.getFaces().stream().filter((f) -> f.size() == 3).collect(Collectors.toList());

        if (triangles.size() == 0) {
            return graph.getVertices()
                    .stream().map((vertex -> new Operand(vertex.getId(), -1, -1)))
                    .collect(Collectors.toList());
        }
        else if (triangles.size() == 1) {
            final Face triangle = triangles.get(0);
            final List<Edge> faceEdges = triangle.getAdjacentEdges();
            final Set<Vertex> vertexSet = new HashSet<>();

            for (Edge edge : faceEdges) {
                if (!vertexSet.contains(edge.getVFrom())) {
                    vertexSet.add(edge.getVFrom());
                    for (Edge vertexEdge : edge.getVFrom().getAdjacentEdges()) {
                        if (!faceEdges.contains(vertexEdge)) {
                            vertexSet.add(vertexEdge.getOppositeVertex(edge.getVFrom()));
                            break;
                        }
                    }
                }
                else if (!vertexSet.contains(edge.getVTo())) {
                    vertexSet.add(edge.getVTo());
                    for (Edge vertexEdge : edge.getVTo().getAdjacentEdges()) {
                        if (!faceEdges.contains(vertexEdge)) {
                            vertexSet.add(vertexEdge.getOppositeVertex(edge.getVTo()));
                            break;
                        }
                    }
                }
            }
            return vertexSet.stream().map(vertex -> new Operand(vertex.getId(), -1,-1)).collect(Collectors.toList());
        }
        else if (triangles.size() == 2) {
            final Set<Edge> outgoingEdgesOne = getOutgoingEdges(triangles.get(0));
            final Set<Edge> outgoingEdgesTwo = getOutgoingEdges(triangles.get(1));
            outgoingEdgesOne.retainAll(outgoingEdgesTwo);

            final List<Vertex> result = new ArrayList<>();
            while (!outgoingEdgesOne.isEmpty()) {
                final Edge intersectingEdge = outgoingEdgesOne.stream().findAny().get();
                result.add(intersectingEdge.getVTo());
                result.add(intersectingEdge.getVFrom());
                outgoingEdgesOne.remove(intersectingEdge);
            }
            return result.stream().map(vertex -> new Operand(vertex.getId(), -1, -1)).collect(Collectors.toList());
        }
        else { // triangles > 2
            return List.of();
        }
    }

    public List<Operand> getOp3OperandsOneTriangle(AbstractGraph graph) {
        final List<Face> triangles = graph.getFaces().stream().filter((f) -> f.size() == 3).collect(Collectors.toList());

        if (triangles.size() == 0) {
            return graph.getVertices()
                    .stream().map((vertex -> new Operand(vertex.getId(), -1, -1)))
                    .collect(Collectors.toList());
        } else if (triangles.size() == 1) {
            final Set<Vertex> result = new HashSet<>();
            final Face triangle = triangles.get(0);
            final List<Edge> faceEdges = triangle.getAdjacentEdges();
            for (Edge e: faceEdges) {
                result.add(e.getVFrom());
                result.add(e.getVTo());
            }
            return result.stream().map(vertex -> new Operand(vertex.getId(), -1, -1)).collect(Collectors.toList());
        } else {
            throw new RuntimeException("too many triangles");
        }
    }

    private Set<Edge> getOutgoingEdges(Face face) {
        final List<Edge> faceEdges = face.getAdjacentEdges();
        final Set<Edge> outgoingEdges = new HashSet<>();
        final Set<Vertex> vertexSet = new HashSet<>();

        for (Edge faceEdge : faceEdges) {
            if (!vertexSet.contains(faceEdge.getVFrom())) {
                vertexSet.add(faceEdge.getVFrom());
                for (Edge vertexEdge : faceEdge.getVFrom().getAdjacentEdges()) {
                    if (!faceEdges.contains(vertexEdge)) {
                        outgoingEdges.add(vertexEdge);
                        break;
                    }
                }
            }
            else if (!vertexSet.contains(faceEdge.getVTo())) {
                vertexSet.add(faceEdge.getVTo());
                for (Edge vertexEdge : faceEdge.getVTo().getAdjacentEdges()) {
                    if (!faceEdges.contains(vertexEdge)) {
                        outgoingEdges.add(vertexEdge);
                        break;
                    }
                }
            }
        }
        return outgoingEdges;
    }

    public List<Operand> getOp4OperandsAll(AbstractGraph graph) {
        final List<Face> squaresOrBigger = graph.getFaces().stream().filter(f -> f.size() > 3).collect(Collectors.toList());

        final Set<Edge> processedEdges = new HashSet<>();
        final List<Operand> result = new ArrayList<>();

        for (Face face : squaresOrBigger) {
            final List<Edge> edges = face.getAdjacentEdges();
            for (int i = 0; i < face.size(); i++) {
                if (!processedEdges.contains(edges.get(i))) {
                    processedEdges.add(edges.get(i));
                    result.add(new Operand(-1, edges.get((i + edges.size() - 1) % edges.size()).getId(), face.getId()));
                }
            }
        }
        return result;
    }

    public List<Operand> getOp4Operands(AbstractGraph graph) {
        final List<Face> triangles = graph.getFaces().stream().filter((f) -> f.size() == 3).collect(Collectors.toList());

        if (triangles.size() == 0) {
            final Set<Edge> processedEdges = new HashSet<>();
            final List<Operand> result = new ArrayList<>();

            for (Face face : graph.getFaces()) {
                final List<Edge> edges = face.getAdjacentEdges();
                for (int i = 0; i < face.size(); i++) {
                    if (!processedEdges.contains(edges.get(i))) {
                        processedEdges.add(edges.get(i));
                        result.add(new Operand(-1, edges.get((i + edges.size() - 1) % edges.size()).getId(), face.getId()));
                    }
                }
            }
            return result;
        }
        else if (triangles.size() == 1) {
            final Face triangle = triangles.get(0);
            final List<Operand> result = new ArrayList<>();
            for (Edge e: triangle.getAdjacentEdges()) {
                final Face oppositeFace = e.getOppositeFace(triangle);
                final int indexOfOther = oppositeFace.getAdjacentEdges()
                        .get((oppositeFace.getIndexOfE(e) - 2 + oppositeFace.size()) %oppositeFace.size()).getId();
                result.add(new Operand(-1, e.getId(), oppositeFace.getId()));
                result.add(new Operand(-1, indexOfOther, oppositeFace.getId()));
            }
            return result;
        }
        else if (triangles.size() == 2) {
            final Set<Edge> outgoingEdgesOne = getOutgoingEdges(triangles.get(0));
            final Set<Edge> outgoingEdgesTwo = getOutgoingEdges(triangles.get(1));
            outgoingEdgesOne.retainAll(outgoingEdgesTwo);

            final List<Operand> result = new ArrayList<>();
            while(!outgoingEdgesOne.isEmpty()) {
                final Edge intersectingEdge = outgoingEdgesOne.stream().findAny().get();
                final Face operandFace = intersectingEdge.getRightFace();
                final int edgeId = operandFace.getIndexOfE(intersectingEdge);
                final Edge operandEdge = operandFace.getEdge((edgeId + 1) % operandFace.size());
                result.add(new Operand(-1, operandEdge.getId(), operandFace.getId()));
                outgoingEdgesOne.remove(intersectingEdge);
            }
            return result;
        }
        else {
            return List.of();
        }
    }

    public List<Operand> getOp5OperandsAll(AbstractGraph graph) {
        final List<Operand> result = new ArrayList<>();

        for (Face face : graph.getFaces()) {
            if (face.size() < 5) continue;
            final List<Edge> edges = face.getAdjacentEdges();
            for (int i = 0; i < face.size(); i++) {
                result.add(new Operand(-1, edges.get(i).getId(), face.getId()));
            }
        }
        return result;
    }

    public List<Operand> getOp5Operands(AbstractGraph graph) {
        if (graph.getFaces().stream().filter((f) -> f.size() == 3).collect(Collectors.toList()).size() > 0) return new ArrayList<>();
        final List<Face> squares = graph.getFaces().stream().filter((f) -> f.size() == 4).collect(Collectors.toList());

        if (squares.size() > 2) {
            return new ArrayList<>();
        }
        else {
            if (squares.size() == 0) {
                final List<Operand> result = new ArrayList<>();
                final List<Face> hexagonsAndBigger = graph.getFaces().stream().filter(face -> face.size() >= 6).collect(Collectors.toList());

                for (Face bigFace : hexagonsAndBigger) {
                    result.addAll(bigFace.getAdjacentEdges()
                            .stream().map(edge -> new Operand(-1, edge.getId(), bigFace.getId()))
                            .collect(Collectors.toList()));
                }
                return result;
            }
            else if (squares.size() == 1) {
                final List<Operand> result = new ArrayList<>();
                final Face square = squares.get(0);

                for (Edge edge : square.getAdjacentEdges()) {
                    final Face oppositeFace = edge.getOppositeFace(square);

                    if (oppositeFace.size() > 5) {
                        result.add(new Operand(-1, edge.getId(), oppositeFace.getId()));
                        final int indexOfSecondStart = (oppositeFace.getIndexOfE(edge) - 3 + oppositeFace.size()) % oppositeFace.size();
                        result.add(new Operand(-1, oppositeFace.getEdge(indexOfSecondStart).getId(), oppositeFace.getId()));
                    }
                }
                return result;
            }
            else {
                final List<Face> squareOneIncidentFaces = squares.get(0).getIncidentFaces();
                final List<Face> squareTwoIncidentFaces = squares.get(1).getIncidentFaces();

//                if (squareOneIncidentFaces.contains(squares.get(1)) || squareTwoIncidentFaces.contains(squares.get(0))) {
//                    return new ArrayList<>();
//                }
//
//                squareOneIncidentFaces.retainAll(squareTwoIncidentFaces);
//                final List<Operand> result = new ArrayList<>();
//
//                for (Face face : squareOneIncidentFaces) {
//
//                    final Set<Edge> squareAdjacentEdges = new HashSet<>(squares.get(0).getAdjacentEdges());
//                    squareAdjacentEdges.addAll(squares.get(1).getAdjacentEdges());
//
//                    final List<Edge> adjacentEdges = face.getAdjacentEdges();
//                    for (int i = 0; i < adjacentEdges.size(); i++) {
//
//                        if (squareAdjacentEdges.contains(adjacentEdges.get(i))
//                                && squareAdjacentEdges.contains(adjacentEdges.get((i + 3) % adjacentEdges.size()))) {
//                            result.add(new Operand(-1, adjacentEdges.get(i).getId(), face.getId()));
//                        }
//                    }
//                }
//                return result;
                if (squareOneIncidentFaces.contains(squares.get(1)) || squareTwoIncidentFaces.contains(squares.get(0))) {
                    return new ArrayList<>();
                }
                squareOneIncidentFaces.retainAll(squareTwoIncidentFaces);
                if (squareOneIncidentFaces.isEmpty()) return List.of();

                if (squareOneIncidentFaces.size() > 1) return List.of();

                final Face mutualFace = squareOneIncidentFaces.get(0);
                if (mutualFace.size() < 6) return List.of();

                final List<Operand> result = new ArrayList<>();
                for (Edge e: mutualFace.getAdjacentEdges()) {
                    if (e.getOppositeFace(mutualFace) == squares.get(0) || e.getOppositeFace(mutualFace) == squares.get(1)) {
                        result.add(new Operand(-1, e.getId(), mutualFace.getId()));
                        final int indexOfSecondStart = (mutualFace.getIndexOfE(e) - 3 + mutualFace.size()) % mutualFace.size();
                        result.add(new Operand(-1, mutualFace.getEdge(indexOfSecondStart).getId(), mutualFace.getId()));
                    }
                }
                return result;
            }
        }
    }
}
