package graph.preprocessors;

import graph.components.AbstractGraph;
import graph.components.Edge;
import graph.components.EdgePair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HamUntouchablePairGetter extends HamEdgeGetter<EdgePair> {
    private final Set<EdgePair> badPairs = new HashSet<>();

    public HamUntouchablePairGetter(AbstractGraph g) {
        super(g);
    }

    @Override
    public List<EdgePair> getResult() {
        badPairs.clear();
        final List<EdgePair> goodPairs = new ArrayList<>();


        for (int i = 0; i < graph.getEdges().size(); i++) {
            for (int j = i + 1; j < graph.getEdges().size(); j++) {
                final Edge e1 = graph.getEdge(i);
                final Edge e2 = graph.getEdge(j);
                final EdgePair ep = new EdgePair(e1, e2);

                if (!badPairs.contains(ep)) {
                    includeEdge(e1);
                    unionFindSet.union(e1.getVFrom(), e1.getVTo());

                    includeEdge(e2);
                    unionFindSet.union(e2.getVFrom(), e2.getVTo());

                    if (!cycleConstructable(e1.getVTo())) {
                        goodPairs.add(ep);
                    }

                    undoIncludeEdge(e2);
                    unionFindSet.disunion();

                    undoIncludeEdge(e1);
                    unionFindSet.disunion();
                }
            }
        }
        return goodPairs;
    }

    @Override
    protected void finalTask() {
        for (int i = 0; i < graph.getEdges().size(); i++) {
            for (int j = i + 1; j < graph.getEdges().size(); j++) {
                final Edge e1 = graph.getEdge(i);
                final Edge e2 = graph.getEdge(j);
                if (e1.getHamCycleState() == Edge.hamCycleEdgeState.INCLUDED &&
                        e2.getHamCycleState() == Edge.hamCycleEdgeState.INCLUDED) {
                    badPairs.add(new EdgePair(e1, e2));
                }
            }
        }
    }
}
