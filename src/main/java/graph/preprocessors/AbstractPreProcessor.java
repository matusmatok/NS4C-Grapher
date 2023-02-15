package graph.preprocessors;

import graph.components.Edge;
import graph.components.Edge.hamCycleEdgeState;
import graph.components.Graph;
import graph.components.Vertex;
import graph.components.Vertex.CycleVertexState;
import graph.utils.AbstractUnionFindSet;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractPreProcessor<T> {

    protected static final EnumMap<CycleVertexState, CycleVertexState> INCLUDE_EDGE_MAP = new EnumMap<>(CycleVertexState.class);
    static {
        INCLUDE_EDGE_MAP.put(CycleVertexState.UNDISCOVERED, CycleVertexState.CHOICE);
        INCLUDE_EDGE_MAP.put(CycleVertexState.CHOICE, CycleVertexState.DETERMINISTIC_EXCLUDE_REMAINING);
        INCLUDE_EDGE_MAP.put(CycleVertexState.DETERMINISTIC_INCLUDE_REMAINING, CycleVertexState.COVERED);
        INCLUDE_EDGE_MAP.put(CycleVertexState.DETERMINISTIC_INCLUDE_REST, CycleVertexState.DETERMINISTIC_INCLUDE_REMAINING);
        INCLUDE_EDGE_MAP.put(CycleVertexState.DETERMINISTIC_EXCLUDE_REMAINING, null);
        INCLUDE_EDGE_MAP.put(CycleVertexState.COVERED, null);
    }
    protected static final EnumMap<CycleVertexState, CycleVertexState> UNDO_INCLUDE_EDGE_MAP = new EnumMap<>(CycleVertexState.class);
    static {
        UNDO_INCLUDE_EDGE_MAP.put(CycleVertexState.CHOICE, CycleVertexState.UNDISCOVERED);
        UNDO_INCLUDE_EDGE_MAP.put(CycleVertexState.DETERMINISTIC_EXCLUDE_REMAINING, CycleVertexState.CHOICE);
        UNDO_INCLUDE_EDGE_MAP.put(CycleVertexState.COVERED, CycleVertexState.DETERMINISTIC_INCLUDE_REMAINING);
        UNDO_INCLUDE_EDGE_MAP.put(CycleVertexState.DETERMINISTIC_INCLUDE_REMAINING, CycleVertexState.DETERMINISTIC_INCLUDE_REST);
    }
    protected static final EnumMap<CycleVertexState, CycleVertexState> EXCLUDE_EDGE_MAP = new EnumMap<>(CycleVertexState.class);
    static {
        EXCLUDE_EDGE_MAP.put(CycleVertexState.UNDISCOVERED, CycleVertexState.DETERMINISTIC_INCLUDE_REST);
        EXCLUDE_EDGE_MAP.put(CycleVertexState.CHOICE, CycleVertexState.DETERMINISTIC_INCLUDE_REMAINING);
        EXCLUDE_EDGE_MAP.put(CycleVertexState.DETERMINISTIC_INCLUDE_REST, null);
        EXCLUDE_EDGE_MAP.put(CycleVertexState.DETERMINISTIC_INCLUDE_REMAINING, null);
        EXCLUDE_EDGE_MAP.put(CycleVertexState.DETERMINISTIC_EXCLUDE_REMAINING, CycleVertexState.COVERED);
        EXCLUDE_EDGE_MAP.put(CycleVertexState.COVERED, null);
    }
    protected static final EnumMap<CycleVertexState, CycleVertexState> UNDO_EXCLUDE_EDGE_MAP = new EnumMap<>(CycleVertexState.class);
    static {
        UNDO_EXCLUDE_EDGE_MAP.put(CycleVertexState.DETERMINISTIC_INCLUDE_REST, CycleVertexState.UNDISCOVERED);
        UNDO_EXCLUDE_EDGE_MAP.put(CycleVertexState.DETERMINISTIC_INCLUDE_REMAINING, CycleVertexState.CHOICE);
        UNDO_EXCLUDE_EDGE_MAP.put(CycleVertexState.COVERED, CycleVertexState.DETERMINISTIC_EXCLUDE_REMAINING);
    }

    protected final Set<Vertex> undiscoveredVertices = new HashSet<>(50);
    protected final Set<Vertex> choiceVertices = new HashSet<>(50);
    protected final Set<Vertex> deterministicVertices = new HashSet<>(50);

    protected final AbstractUnionFindSet unionFindSet;
    protected final Graph graph;

    public AbstractPreProcessor(Graph g, AbstractUnionFindSet unionFindSet) {
        graph = g;
        undiscoveredVertices.addAll(g.getVertices());
        this.unionFindSet = unionFindSet;
    }

    protected abstract boolean canCloseCycle(Vertex color);
    protected boolean cycleConstructible(){
        Optional<Vertex> vertexOpt = deterministicVertices.stream().findAny();
        if (!vertexOpt.isPresent()) {
            vertexOpt = choiceVertices.stream().findAny();
        }
        if (!vertexOpt.isPresent()) {
            throw new RuntimeException("No operands. Recursion should not have gotten this deep");
        }

        final Vertex vertex = vertexOpt.get();
        final Edge edge =  vertex.getAnyUndiscoveredEdge();
        final Vertex otherVertex = edge.getOppositeVertex(vertex);
        final Vertex vColor = unionFindSet.find(vertex);
        final Vertex oColor = unionFindSet.find(otherVertex);
        final boolean canInclude = vColor != oColor;
        final boolean canCloseCycle = canCloseCycle(vColor);

        final CycleVertexState otherCycleVertexState = otherVertex.getCycleState();

        switch (vertex.getCycleState()) {
            case DETERMINISTIC_EXCLUDE_REMAINING :
                switch (otherCycleVertexState){
                    case UNDISCOVERED:
                    case DETERMINISTIC_EXCLUDE_REMAINING:
                    case CHOICE:
                        if (excludeEdge(edge)) {
                            final boolean result = cycleConstructible();
                            undoExcludeEdge(edge);
                            return result;
                        }
                    default: return false;
                }
            case DETERMINISTIC_INCLUDE_REMAINING:
            case DETERMINISTIC_INCLUDE_REST:
                    switch (otherCycleVertexState) {
                        case UNDISCOVERED:
                        case DETERMINISTIC_INCLUDE_REST:
                        case DETERMINISTIC_INCLUDE_REMAINING:
                        case CHOICE:
                            if (canInclude) {
                                if (includeEdge(edge)) {
                                    unionFindSet.union(vColor, oColor);
                                    final boolean result = cycleConstructible();
                                    unionFindSet.disunion();
                                    undoIncludeEdge(edge);
                                    return result;
                                }
                            } else if (canCloseCycle){
                                if (includeEdge(edge)){
                                    finalTask();
                                    undoIncludeEdge(edge);
                                    return true;
                                }
                            }
                        default: return false;
                    }
            case CHOICE:
                switch (otherCycleVertexState) {
                    case UNDISCOVERED:
                    case CHOICE:
                        if (excludeEdge(edge)) {
                            final boolean result = cycleConstructible();
                            undoExcludeEdge(edge);
                            if (result) return true;
                        }

                        if (canInclude) {
                            if (includeEdge(edge)) {
                                unionFindSet.union(vColor, oColor);
                                final boolean result = cycleConstructible();
                                unionFindSet.disunion();
                                undoIncludeEdge(edge);
                                return result;
                            }
                        }
                        else if (canCloseCycle) {
                            if (includeEdge(edge)){
                                finalTask();
                                undoIncludeEdge(edge);
                                return true;
                            }
                        }
                    default: return false;
                }
            default: return false;
        }
    }

    protected void finalTask(){}

    protected boolean includeEdge(Edge e) {
        e.setHamCycleState(hamCycleEdgeState.INCLUDED);
        return edgeOperation(e, INCLUDE_EDGE_MAP);
    }
    protected boolean undoIncludeEdge(Edge e) {
        e.setHamCycleState(hamCycleEdgeState.UNDISCOVERED);
        return edgeOperation(e, UNDO_INCLUDE_EDGE_MAP);
    }
    protected boolean excludeEdge(Edge e) {
        e.setHamCycleState(hamCycleEdgeState.EXCLUDED);
        return edgeOperation(e, EXCLUDE_EDGE_MAP);
    }
    protected boolean undoExcludeEdge(Edge e) {
        e.setHamCycleState(hamCycleEdgeState.UNDISCOVERED);
        return edgeOperation(e, UNDO_EXCLUDE_EDGE_MAP);
    }

    private boolean edgeOperation(Edge e, EnumMap<CycleVertexState, CycleVertexState> map) {
        final Vertex vFrom = e.getVFrom();
        final Vertex vTo = e.getVTo();
        final CycleVertexState vFromState = vFrom.getCycleState();
        final CycleVertexState vToState = vTo.getCycleState();
        final CycleVertexState vFromNewState = map.get(vFromState);
        final CycleVertexState vToNewState = map.get(vToState);
        if (vFromNewState != null && vToNewState != null) {
            unsetState(vFrom, vFromState);
            setState(vFrom, vFromNewState);
            unsetState(vTo, vToState);
            setState(vTo, vToNewState);
            return true;
        }
        return false;
    }

    private void unsetState(Vertex v, CycleVertexState oldState) {
        switch (oldState) {
            case UNDISCOVERED :
                undiscoveredVertices.remove(v);
                break;
            case CHOICE:
                choiceVertices.remove(v);
                break;
            case DETERMINISTIC_INCLUDE_REST:
            case DETERMINISTIC_INCLUDE_REMAINING:
            case DETERMINISTIC_EXCLUDE_REMAINING:
                deterministicVertices.remove(v);
                break;
        }
    }
    private void setState(Vertex v, CycleVertexState newState) {
        switch (newState) {
            case CHOICE :
                choiceVertices.add(v);
                v.setCycleState(CycleVertexState.CHOICE);
                break;
            case DETERMINISTIC_EXCLUDE_REMAINING :
                deterministicVertices.add(v);
                v.setCycleState(CycleVertexState.DETERMINISTIC_EXCLUDE_REMAINING);
                break;
            case DETERMINISTIC_INCLUDE_REMAINING:
                deterministicVertices.add(v);
                v.setCycleState(CycleVertexState.DETERMINISTIC_INCLUDE_REMAINING);
                break;
            case DETERMINISTIC_INCLUDE_REST:
                deterministicVertices.add(v);
                v.setCycleState(CycleVertexState.DETERMINISTIC_INCLUDE_REST);
                break;
            case COVERED:
                v.setCycleState(CycleVertexState.COVERED);
                break;
            case UNDISCOVERED:
                undiscoveredVertices.add(v);
                v.setCycleState(CycleVertexState.UNDISCOVERED);
                break;
        }
    }

}
