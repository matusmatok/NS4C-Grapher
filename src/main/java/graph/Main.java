/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import graph.builders.UntouchableBuilder;
import graph.components.NoLadderC4ECGraph;
import graph.generators.noladderc4ec.NoLadderC4ECGenerator;
import graph.preprocessors.HamUntouchableEdgeGetter;
import graph.preprocessors.ambiguous.UntouchableAmbiguousVertexGetter;
import graph.preprocessors.distributors.NoLadderC4ECPolarityDistributor;
import graph.processors.Processor;
import graph.processors.ham.HamEdgeProcessor;
import graph.utils.GlobalProgressBar;

/**
 *
 * @author ASUS
 */


public class Main {
    private static int MAX_DEPTH = 48;
    
    public static void main(String[] args){
        runNoLadderGeneratorWithProcessing(34);
    }

    /**
     * This method runs the {@link NoLadderC4ECGenerator}
     * and verifies whether the graphs contain a hamiltonian-untouchable edge.
     *
     * @param depth is the depth of the generator. At 26, we get the first positive result
     * */
    private static void runNoLadderC4ECGenerator1(final int depth) {
        final NoLadderC4ECGraph graph = new NoLadderC4ECGraph();
        graph.init();

        final NoLadderC4ECGenerator generator = new NoLadderC4ECGenerator(graph, new HamEdgeProcessor(graph, new HamUntouchableEdgeGetter(graph)), depth);
        generator.generate();
    }

    /**
     * This method uses {@link NoLadderC4ECGenerator} and a custom {@link Processor} setup
     * to find the smallest gadget containing an untouchable edge.
     *
     * @param depth is the depth of the generator
     * */
    private static void runNoLadderGeneratorWithProcessing(final int depth) {
        GlobalProgressBar.setEnabled(true);
        final NoLadderC4ECGraph graph = new NoLadderC4ECGraph();
        graph.init();

        final NoLadderC4ECGenerator generator = new NoLadderC4ECGenerator(graph,
                new Processor(graph,
                        new HamUntouchableEdgeGetter(graph),
                        new UntouchableAmbiguousVertexGetter(graph),
                        new NoLadderC4ECPolarityDistributor.NoLadderDistributorFactory(MAX_DEPTH),
                        new UntouchableBuilder(graph)),
                depth);
        generator.generate();
    }
}
