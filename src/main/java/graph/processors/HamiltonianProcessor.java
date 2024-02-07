package graph.processors;

import graph.components.AbstractGraph;
import graph.preprocessors.HamiltonianPreProcessor;
import graph.utils.SimpleUnionFindSet;

public class HamiltonianProcessor extends AbstractProcessor{
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
