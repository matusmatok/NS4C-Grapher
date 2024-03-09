package graph.processors.ham;

import graph.components.AbstractGraph;
import graph.preprocessors.HamiltonianPreProcessor;
import graph.processors.Processor;
import graph.utils.SimpleUnionFindSet;

public class HamiltonianProcessor extends Processor {
    private HamiltonianPreProcessor hamPreProcess;

    public HamiltonianProcessor(AbstractGraph graph) {
        super(graph, null, null, null);
        hamPreProcess = new HamiltonianPreProcessor(graph, new SimpleUnionFindSet());
    }

    @Override
    public boolean hasProperty(){
        return hamPreProcess.isHamiltonian();
    }
}
