package graph.builders;

import graph.components.Edge;
import graph.components.Graph;
import graph.components.Vertex;
import graph.components.Vertex.ConsistentTwoFactorState;
import graph.utils.AbstractUnionFindSet;
import graph.utils.CountingUnionFindSet;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import lombok.NonNull;

abstract public class AbstractTwoFactorBuilder {
    protected static final EnumMap<ConsistentTwoFactorState, ConsistentTwoFactorState> INCLUDE_EDGE_MAP = new EnumMap<>(Vertex.ConsistentTwoFactorState.class);
    static {
        INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_UNDISCOVERED, Vertex.ConsistentTwoFactorState.P_CHOICE);
        INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_CHOICE, Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_EXCLUDE_REMAINING);
        INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_INCLUDE_REMAINING, Vertex.ConsistentTwoFactorState.P_COVERED);
        INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_INCLUDE_REST, Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_INCLUDE_REMAINING);
        INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_EXCLUDE_REMAINING, null);
        INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_COVERED, null);

        INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_UNDISCOVERED, Vertex.ConsistentTwoFactorState.N_END_PATH_CHOICE);
        INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_END_PATH_CHOICE, Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_COVER_EXCLUDE);
        INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_IN_EX_CHOICE, Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_INCLUDE);
        INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_INCLUDE, Vertex.ConsistentTwoFactorState.N_COVERED);
        INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_COVER_EXCLUDE, null);
        INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_EXCLUDE, null);
        INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_COVERED, null);
        INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_EXCLUDED, null);
    }
    protected static final EnumMap<Vertex.ConsistentTwoFactorState, Vertex.ConsistentTwoFactorState> UNDO_INCLUDE_EDGE_MAP = new EnumMap<>(Vertex.ConsistentTwoFactorState.class);
    static {
        UNDO_INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_CHOICE, Vertex.ConsistentTwoFactorState.P_UNDISCOVERED);
        UNDO_INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_EXCLUDE_REMAINING, Vertex.ConsistentTwoFactorState.P_CHOICE);
        UNDO_INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_COVERED, Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_INCLUDE_REMAINING);
        UNDO_INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_INCLUDE_REMAINING, Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_INCLUDE_REST);

        UNDO_INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_END_PATH_CHOICE, Vertex.ConsistentTwoFactorState.N_UNDISCOVERED);
        UNDO_INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_COVER_EXCLUDE, Vertex.ConsistentTwoFactorState.N_END_PATH_CHOICE);
        UNDO_INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_INCLUDE, Vertex.ConsistentTwoFactorState.N_IN_EX_CHOICE);
        UNDO_INCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_COVERED, Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_INCLUDE);
    }
    protected static final EnumMap<Vertex.ConsistentTwoFactorState, Vertex.ConsistentTwoFactorState> EXCLUDE_EDGE_MAP = new EnumMap<>(Vertex.ConsistentTwoFactorState.class);
    static {
        EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_UNDISCOVERED, Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_INCLUDE_REST);
        EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_CHOICE, Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_INCLUDE_REMAINING);
        EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_INCLUDE_REST, null);
        EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_INCLUDE_REMAINING, null);
        EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_EXCLUDE_REMAINING, Vertex.ConsistentTwoFactorState.P_COVERED);
        EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_COVERED, null);

        EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_UNDISCOVERED, Vertex.ConsistentTwoFactorState.N_IN_EX_CHOICE);
        EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_END_PATH_CHOICE, Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_INCLUDE);
        EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_IN_EX_CHOICE, Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_EXCLUDE);
        EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_INCLUDE, null);
        EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_COVER_EXCLUDE, Vertex.ConsistentTwoFactorState.N_COVERED);
        EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_EXCLUDE, Vertex.ConsistentTwoFactorState.N_EXCLUDED);
        EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_COVERED, null);
        EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_EXCLUDED, null);
    }
    protected static final EnumMap<Vertex.ConsistentTwoFactorState, Vertex.ConsistentTwoFactorState> UNDO_EXCLUDE_EDGE_MAP = new EnumMap<>(Vertex.ConsistentTwoFactorState.class);
    static {
        UNDO_EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_INCLUDE_REST, Vertex.ConsistentTwoFactorState.P_UNDISCOVERED);
        UNDO_EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_INCLUDE_REMAINING, Vertex.ConsistentTwoFactorState.P_CHOICE);
        UNDO_EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.P_COVERED, Vertex.ConsistentTwoFactorState.P_DETERMINISTIC_EXCLUDE_REMAINING);

        UNDO_EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_IN_EX_CHOICE, Vertex.ConsistentTwoFactorState.N_UNDISCOVERED);
        UNDO_EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_INCLUDE, Vertex.ConsistentTwoFactorState.N_END_PATH_CHOICE);
        UNDO_EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_EXCLUDE, Vertex.ConsistentTwoFactorState.N_IN_EX_CHOICE);
        UNDO_EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_COVERED, Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_COVER_EXCLUDE);
        UNDO_EXCLUDE_EDGE_MAP.put(Vertex.ConsistentTwoFactorState.N_EXCLUDED, Vertex.ConsistentTwoFactorState.N_DETERMINISTIC_EXCLUDE);
    }


    private final Set<Vertex> deterministicVertices = new HashSet<>();
    private final Set<Vertex> choiceVertices = new HashSet<>();

    private final AbstractUnionFindSet unionFindSet = new CountingUnionFindSet();

    protected final Graph graph;
    protected int numberOfPositiveVertices;
    private int currentWeight = 0;

    public AbstractTwoFactorBuilder(Graph graph) {
        this.graph = graph;
    }

    abstract public boolean canBuild(Edge potenatialEdge);

    protected boolean canBuildConsistentTwoFactor() {
        if (currentWeight == numberOfPositiveVertices) return true;

        final Vertex vertex;
        if (deterministicVertices.size() > 0) vertex = deterministicVertices.stream().findAny().get();
        else if (choiceVertices.size() > 0) vertex = choiceVertices.stream().findAny().get();
        else throw new RuntimeException("No operand.");

        final Edge edge = vertex.getAnyUndiscoveredEdge();
        final Vertex otherVertex = edge.getOppositeVertex(vertex);

        final ConsistentTwoFactorState vertexState = vertex.getConsistentTwoFactorState();

        final ConsistentTwoFactorState otherVertexState = otherVertex.getConsistentTwoFactorState();
        final ConsistentTwoFactorState otherVertexPostIncludeState = INCLUDE_EDGE_MAP.get(otherVertexState);
        final ConsistentTwoFactorState otherVertexPostExcludeState = EXCLUDE_EDGE_MAP.get(otherVertexState);

        switch (vertexState) {
            case P_DETERMINISTIC_EXCLUDE_REMAINING:
            case N_DETERMINISTIC_COVER_EXCLUDE:
            case N_DETERMINISTIC_EXCLUDE:
                if (otherVertexPostExcludeState != null) {
                    excludeEdge(edge);
                    final boolean result = canBuildConsistentTwoFactor();
                    undoExcludeEdge(edge);
                    return result;
                }
                break;
            case P_DETERMINISTIC_INCLUDE_REMAINING:
            case P_DETERMINISTIC_INCLUDE_REST:
            case N_DETERMINISTIC_INCLUDE:
                if (otherVertexPostIncludeState != null) {
                    if (unionFindSet.find(vertex) == unionFindSet.find(otherVertex)) {
                        final int weightOfCycle = unionFindSet.find(vertex).getWeight();
                        if (weightOfCycle % 2 == 0) {
                            includeEdge(edge);
                            currentWeight += weightOfCycle;
                            final boolean result = canBuildConsistentTwoFactor();
                            currentWeight -= weightOfCycle;
                            undoIncludeEdge(edge);
                            return result;
                        }
                    }
                    else {
                        unionFindSet.union(unionFindSet.find(vertex), unionFindSet.find(otherVertex));
                        includeEdge(edge);
                        final boolean result = canBuildConsistentTwoFactor();
                        undoIncludeEdge(edge);
                        unionFindSet.disunion();
                        return result;
                    }
                }
                break;
            case P_CHOICE:
            case N_IN_EX_CHOICE:
            case N_END_PATH_CHOICE:
                boolean result = false;
                if (otherVertexPostExcludeState != null) {
                    excludeEdge(edge);
                    result = canBuildConsistentTwoFactor();
                    undoExcludeEdge(edge);
                    if (result) return true;
                }
                if (otherVertexPostIncludeState != null) {
                    if (unionFindSet.find(vertex) == unionFindSet.find(otherVertex)) {
                        final int weightOfCycle = unionFindSet.find(vertex).getWeight();
                        if (weightOfCycle % 2 == 0) {
                            includeEdge(edge);
                            currentWeight += weightOfCycle;
                            result = canBuildConsistentTwoFactor();
                            currentWeight -= weightOfCycle;
                            undoIncludeEdge(edge);
                        }
                    }
                    else {
                        unionFindSet.union(unionFindSet.find(vertex), unionFindSet.find(otherVertex));
                        includeEdge(edge);
                        result = canBuildConsistentTwoFactor();
                        undoIncludeEdge(edge);
                        unionFindSet.disunion();
                    }
                }
                return result;
            default: return false;
        }
        return false;
    }

    protected void includeEdge(Edge e) {
        // System.out.println("INCLUDE " + e.getId());
        e.setHamCycleState(Edge.hamCycleEdgeState.INCLUDED);
        edgeOperation(e, INCLUDE_EDGE_MAP);
    }
    protected void undoIncludeEdge(Edge e) {
        // System.out.println("UNDO INCLUDE " + e.getId());
        e.setHamCycleState(Edge.hamCycleEdgeState.UNDISCOVERED);
        edgeOperation(e, UNDO_INCLUDE_EDGE_MAP);
    }
    protected void excludeEdge(Edge e) {
        // System.out.println("EXCLUDE " + e.getId());
        e.setHamCycleState(Edge.hamCycleEdgeState.EXCLUDED);
        edgeOperation(e, EXCLUDE_EDGE_MAP);
    }
    protected void undoExcludeEdge(Edge e) {
        // System.out.println("UNDO EXCLUDE " + e.getId());
        e.setHamCycleState(Edge.hamCycleEdgeState.UNDISCOVERED);
        edgeOperation(e, UNDO_EXCLUDE_EDGE_MAP);
    }

    private void edgeOperation(Edge e, EnumMap<ConsistentTwoFactorState, ConsistentTwoFactorState> map) {
        final Vertex vFrom = e.getVFrom();
        final Vertex vTo = e.getVTo();
        final ConsistentTwoFactorState vFromState = vFrom.getConsistentTwoFactorState();
        final ConsistentTwoFactorState vToState = vTo.getConsistentTwoFactorState();
        final ConsistentTwoFactorState vFromNewState = map.get(vFromState);
        final ConsistentTwoFactorState vToNewState = map.get(vToState);
        unsetState(vFrom, vFromState);
        setState(vFrom, vFromNewState);
        unsetState(vTo, vToState);
        setState(vTo, vToNewState);
    }

    private void setState(Vertex v, @NonNull ConsistentTwoFactorState state) {
        switch (state) {
            case P_DETERMINISTIC_EXCLUDE_REMAINING:
            case P_DETERMINISTIC_INCLUDE_REMAINING:
            case P_DETERMINISTIC_INCLUDE_REST:
            case N_DETERMINISTIC_COVER_EXCLUDE:
            case N_DETERMINISTIC_EXCLUDE:
            case N_DETERMINISTIC_INCLUDE:
                deterministicVertices.add(v);
                break;
            case P_CHOICE:
            case N_END_PATH_CHOICE:
            case N_IN_EX_CHOICE:
                choiceVertices.add(v);
                break;
        }
        v.setConsistentTwoFactorState(state);
    }

    private void unsetState(Vertex v, @NonNull ConsistentTwoFactorState state) {
        switch (state) {
            case P_DETERMINISTIC_EXCLUDE_REMAINING:
            case P_DETERMINISTIC_INCLUDE_REMAINING:
            case P_DETERMINISTIC_INCLUDE_REST:
            case N_DETERMINISTIC_COVER_EXCLUDE:
            case N_DETERMINISTIC_EXCLUDE:
            case N_DETERMINISTIC_INCLUDE:
                deterministicVertices.remove(v);
                break;
            case P_CHOICE:
            case N_END_PATH_CHOICE:
            case N_IN_EX_CHOICE:
                choiceVertices.remove(v);
                break;
        }
    }
}
