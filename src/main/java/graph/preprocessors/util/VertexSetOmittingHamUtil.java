package graph.preprocessors.util;

import graph.components.AbstractGraph;
import graph.components.Edge;
import graph.components.Vertex;
import graph.preprocessors.AbstractPreProcessor;
import graph.utils.SimpleUnionFindSet;

import java.rmi.UnexpectedException;
import java.util.List;
import java.util.Set;

public abstract class VertexSetOmittingHamUtil extends AbstractPreProcessor<Vertex> {
    protected final List<Vertex> omittedVertices;
    protected final Edge specialEdge;

    private VertexSetOmittingHamUtil(final AbstractGraph g, final List<Vertex> omittedVertices, final Edge specialEdge) {
        super(g, new SimpleUnionFindSet());
        this.omittedVertices = omittedVertices;
        this.specialEdge = specialEdge;
    }

    @Override
    protected boolean canCloseCycle(Vertex color) {
        return color.getSubtreeSize() == graph.getVertices().size() - omittedVertices.size();
    }

    public abstract boolean hasHamProperty();

    boolean excludeSet() {
        for (final Vertex vertex : omittedVertices) {
            final boolean result = excludeVertex(vertex);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    private boolean excludeVertex(final Vertex vertex) {
        for (final Edge edge : vertex.getAdjacentEdges()) {
            final Vertex opposite = edge.getOppositeVertex(vertex);
            if (edge.getHamCycleState() != Edge.hamCycleEdgeState.EXCLUDED) {
                if (opposite.getCycleState() != Vertex.CycleVertexState.UNDISCOVERED) {
                    return false;
                } else {
                    edge.setHamCycleState(Edge.hamCycleEdgeState.EXCLUDED);
                    if (!omittedVertices.contains(opposite)) {
                        deterministicVertices.add(opposite);
                        undiscoveredVertices.remove(opposite);
                        opposite.setCycleState(Vertex.CycleVertexState.DETERMINISTIC_INCLUDE_REST);
                    }
                }
            }
        }
        return true;
    }

    void undoExcludeSet() {
        for (final Vertex vertex : omittedVertices) {
            undoExcludeVertex(vertex);
        }
    }

    private void undoExcludeVertex(final Vertex vertex) {
        for (final Edge edge : vertex.getAdjacentEdges()) {
            final Vertex opposite = edge.getOppositeVertex(vertex);
            opposite.setCycleState(Vertex.CycleVertexState.UNDISCOVERED);
            deterministicVertices.remove(opposite);
            undiscoveredVertices.add(opposite);
            edge.setHamCycleState(Edge.hamCycleEdgeState.UNDISCOVERED);
        }
    }

    public static Vertex getIncludedVertex(final AbstractGraph graph, final List<Vertex> omittedVertices) {
        for (final Vertex vertex : graph.getVertices()) {
            if (!omittedVertices.contains(vertex)) return vertex;
        }
        throw new RuntimeException("Seems like you omitted all the vertices.");
    }

    public static boolean isUnavoidConstructable(final AbstractGraph graph, final List<Vertex> omittedVertices, final Edge unavoidableEdge) {
        final VertexSetOmittingHamUtil util = new VertexSetOmittingHamUtil(graph, omittedVertices, unavoidableEdge) {

            @Override
            public boolean hasHamProperty() {
                boolean res = excludeSet();

                if (!res) {
                    undoExcludeSet();
                    return false;
                }

                if (specialEdge.getHamCycleState() == Edge.hamCycleEdgeState.UNDISCOVERED) {
                    if (specialEdge.getVTo().getCycleState() != Vertex.CycleVertexState.UNDISCOVERED ||
                            specialEdge.getVFrom().getCycleState() != Vertex.CycleVertexState.UNDISCOVERED) {
                        undoExcludeSet();
                        return false;
                    } else {
                        excludeEdge(specialEdge);
                    }
                }

                res = cycleConstructable(getIncludedVertex(graph, omittedVertices));

                undoExcludeEdge(specialEdge);
                undoExcludeSet();
                return res;
            }
        };

        return util.hasHamProperty();
    }

    public static boolean isUntouchConstructable(final AbstractGraph graph, final List<Vertex> omittedVertices, final Edge untouchableEdge) {
        final VertexSetOmittingHamUtil util = new VertexSetOmittingHamUtil(graph, omittedVertices, untouchableEdge) {
            @Override
            public boolean hasHamProperty() {
                if (omittedVertices.contains(specialEdge.getVTo()) || omittedVertices.contains(specialEdge.getVFrom())) {
                    return false;
                }

                boolean res = excludeSet();

                if (!res) {
                    undoExcludeSet();
                    return false;
                }

                includeEdge(specialEdge);

                res = cycleConstructable(getIncludedVertex(graph, omittedVertices));

                undoIncludeEdge(specialEdge);
                undoExcludeSet();
                return res;
            }
        };

        return util.hasHamProperty();
    }
}
