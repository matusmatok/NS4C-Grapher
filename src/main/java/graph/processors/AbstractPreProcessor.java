package graph.processors;

import graph.components.Edge;
import graph.components.Edge.hamCycleEdgeState;
import graph.components.Graph;
import graph.components.Vertex;
import graph.components.Vertex.HamCycleVertexState;

import java.util.*;

public abstract class AbstractPreProcessor<T> {

    protected static final EnumMap<HamCycleVertexState, HamCycleVertexState> INCLUDE_EDGE_MAP = new EnumMap<>(HamCycleVertexState.class);
    static {
        INCLUDE_EDGE_MAP.put(HamCycleVertexState.UNDISCOVERED,HamCycleVertexState.CHOICE);
        INCLUDE_EDGE_MAP.put(HamCycleVertexState.CHOICE,HamCycleVertexState.DETERMINISTIC_EXCLUDE_REMAINING);
        INCLUDE_EDGE_MAP.put(HamCycleVertexState.DETERMINISTIC_INCLUDE_REMAINING, HamCycleVertexState.COVERED);
        INCLUDE_EDGE_MAP.put(HamCycleVertexState.DETERMINISTIC_INCLUDE_REST,HamCycleVertexState.DETERMINISTIC_INCLUDE_REMAINING);
        INCLUDE_EDGE_MAP.put(HamCycleVertexState.DETERMINISTIC_EXCLUDE_REMAINING, null);
        INCLUDE_EDGE_MAP.put(HamCycleVertexState.COVERED, null);
    }
    protected static final EnumMap<HamCycleVertexState, HamCycleVertexState> UNDO_INCLUDE_EDGE_MAP = new EnumMap<>(HamCycleVertexState.class);
    static {
        UNDO_INCLUDE_EDGE_MAP.put(HamCycleVertexState.CHOICE, HamCycleVertexState.UNDISCOVERED);
        UNDO_INCLUDE_EDGE_MAP.put(HamCycleVertexState.DETERMINISTIC_EXCLUDE_REMAINING, HamCycleVertexState.CHOICE);
        UNDO_INCLUDE_EDGE_MAP.put(HamCycleVertexState.COVERED, HamCycleVertexState.DETERMINISTIC_INCLUDE_REMAINING);
        UNDO_INCLUDE_EDGE_MAP.put(HamCycleVertexState.DETERMINISTIC_INCLUDE_REMAINING,HamCycleVertexState.DETERMINISTIC_INCLUDE_REST);
    }
    protected static final EnumMap<HamCycleVertexState, HamCycleVertexState> EXCLUDE_EDGE_MAP = new EnumMap<>(HamCycleVertexState.class);
    static {
        EXCLUDE_EDGE_MAP.put(HamCycleVertexState.UNDISCOVERED, HamCycleVertexState.DETERMINISTIC_INCLUDE_REST);
        EXCLUDE_EDGE_MAP.put(HamCycleVertexState.CHOICE, HamCycleVertexState.DETERMINISTIC_INCLUDE_REMAINING);
        EXCLUDE_EDGE_MAP.put(HamCycleVertexState.DETERMINISTIC_INCLUDE_REST, null);
        EXCLUDE_EDGE_MAP.put(HamCycleVertexState.DETERMINISTIC_INCLUDE_REMAINING, null);
        EXCLUDE_EDGE_MAP.put(HamCycleVertexState.DETERMINISTIC_EXCLUDE_REMAINING, HamCycleVertexState.COVERED);
        EXCLUDE_EDGE_MAP.put(HamCycleVertexState.COVERED, null);
    }
    protected static final EnumMap<HamCycleVertexState, HamCycleVertexState> UNDO_EXCLUDE_EDGE_MAP = new EnumMap<>(HamCycleVertexState.class);
    static {
        UNDO_EXCLUDE_EDGE_MAP.put(HamCycleVertexState.DETERMINISTIC_INCLUDE_REST, HamCycleVertexState.UNDISCOVERED);
        UNDO_EXCLUDE_EDGE_MAP.put(HamCycleVertexState.DETERMINISTIC_INCLUDE_REMAINING, HamCycleVertexState.CHOICE);
        UNDO_EXCLUDE_EDGE_MAP.put(HamCycleVertexState.COVERED, HamCycleVertexState.DETERMINISTIC_EXCLUDE_REMAINING);
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

    public abstract List<T> getResult();

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

        final HamCycleVertexState otherVertexState = otherVertex.getHamCycleState();

        switch (vertex.getHamCycleState()) {
            case DETERMINISTIC_EXCLUDE_REMAINING :
                switch (otherVertexState){
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
                    switch (otherVertexState) {
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
                switch (otherVertexState) {
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

    private boolean edgeOperation(Edge e, EnumMap<HamCycleVertexState,HamCycleVertexState> map) {
        final Vertex vFrom = e.getVFrom();
        final Vertex vTo = e.getVTo();
        final HamCycleVertexState vFromState = vFrom.getHamCycleState();
        final HamCycleVertexState vToState = vTo.getHamCycleState();
        final HamCycleVertexState vFromNewState = map.get(vFromState);
        final HamCycleVertexState vToNewState = map.get(vToState);
        if (vFromNewState != null && vToNewState != null) {
            unsetState(vFrom, vFromState);
            setState(vFrom, vFromNewState);
            unsetState(vTo, vToState);
            setState(vTo, vToNewState);
            return true;
        }
        return false;
    }

    private void unsetState(Vertex v, HamCycleVertexState oldState) {
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
    private void setState(Vertex v, HamCycleVertexState newState) {
        switch (newState) {
            case CHOICE :
                choiceVertices.add(v);
                v.setHamCycleState(HamCycleVertexState.CHOICE);
                break;
            case DETERMINISTIC_EXCLUDE_REMAINING :
                deterministicVertices.add(v);
                v.setHamCycleState(HamCycleVertexState.DETERMINISTIC_EXCLUDE_REMAINING);
                break;
            case DETERMINISTIC_INCLUDE_REMAINING:
                deterministicVertices.add(v);
                v.setHamCycleState(HamCycleVertexState.DETERMINISTIC_INCLUDE_REMAINING);
                break;
            case DETERMINISTIC_INCLUDE_REST:
                deterministicVertices.add(v);
                v.setHamCycleState(HamCycleVertexState.DETERMINISTIC_INCLUDE_REST);
                break;
            case COVERED:
                v.setHamCycleState(HamCycleVertexState.COVERED);
                break;
            case UNDISCOVERED:
                undiscoveredVertices.add(v);
                v.setHamCycleState(HamCycleVertexState.UNDISCOVERED);
                break;
        }
    }

}
