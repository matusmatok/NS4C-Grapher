package graph.preprocessors;

import graph.components.Edge;
import graph.components.AbstractGraph;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HamUntouchableEdgeGetter extends HamEdgeGetter<Edge> {

    private final Set<Edge> badEdges = new HashSet<>();
    public HamUntouchableEdgeGetter(AbstractGraph g) {
        super(g);
    }

    @Override
    public List<Edge> getResult() {
        badEdges.clear();
        List<Edge> goodEdges = new ArrayList<>();
        for (Edge e: graph.getEdges()) {
            if (!badEdges.contains(e)) {
                includeEdge(e);
                unionFindSet.union(e.getVFrom(), e.getVTo());
                if (!cycleConstructable()) {
                    goodEdges.add(e);
                }
                unionFindSet.disunion();
                undoIncludeEdge(e);
            }
        }
        return goodEdges;
    }

    @Override
    protected void finalTask() {
        for (Edge e : graph.getEdges()) {
            if (e.getHamCycleState() == Edge.hamCycleEdgeState.INCLUDED) {
                badEdges.add(e);
            }
        }
    }
}
