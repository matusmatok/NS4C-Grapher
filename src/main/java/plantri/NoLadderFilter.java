package plantri;

import graph.components.NoLadderC4ECGraph;
import graph.preprocessors.reducible.ReducibleConfigurationFinder;

import java.io.InputStreamReader;

public class NoLadderFilter extends AbstractFilter{

    public NoLadderFilter(InputStreamReader reader) {
        super(reader);
    }

    @Override
    public boolean filter(NoLadderC4ECGraph graph) {
        return !ReducibleConfigurationFinder.containsLadder(graph);
    }
}
