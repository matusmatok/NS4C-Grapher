package graph.preprocessors;

import graph.components.Edge;
import graph.components.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Edge E is considered bad if there exists a hamiltonian cycle in G not containing edge E.
 * E is considered good otherwise.
 */
public class HamUnavoidableEdgeGetter extends HamEdgeGetter{

    private final Set<Edge> badEdges = new HashSet<>();
    public HamUnavoidableEdgeGetter(Graph g) {
        super(g);
    }

    @Override
    public List<Edge> getResult() {
        badEdges.clear();
        List<Edge> goodEdges = new ArrayList<>();
        for (Edge e: graph.getEdges()) {
            if (!badEdges.contains(e)) {
                excludeEdge(e);
                if (!cycleConstructible())  goodEdges.add(e);
                undoExcludeEdge(e);
            }
        }
        return goodEdges;
    }

    @Override
    protected void finalTask() {
        for (Edge e : graph.getEdges()) {
            if (e.getHamCycleState() != Edge.hamCycleEdgeState.INCLUDED) {
                badEdges.add(e);
            }
        }
    }

}
