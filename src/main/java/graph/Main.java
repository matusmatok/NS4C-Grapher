/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import graph.canoniccoders.CanonicalCoder;
import graph.components.NoLadderC4ECGraph;
import graph.components.SimpleGraph;
import graph.generators.cubicplanar.TopVerifyingGenerator;
import graph.generators.noladderc4ec.NoLadderC4ECGenerator;
import graph.preprocessors.HamUntouchableEdgeGetter;
import graph.processors.AbstractProcessor;
import graph.processors.UnavoidableEdgeProcessor;
import graph.processors.ham.HamEdgeProcessor;

/**
 *
 * @author ASUS
 */


public class Main {
    
    
    public static void main(String[] args){
        System.out.println(2^(0));
    }

    /**
     * This method runs the
     * @link NoLadderC4ECGenerator.java
     *
     * and verifies whether the graphs contain an untouchable edge.
     *
     * @param depth is the depth of the generator. At 26, we get the first positive result
     * */
    private static void runNoLadderC4ECGenerator1(final int depth) {
        final NoLadderC4ECGraph graph = new NoLadderC4ECGraph();
        graph.init();

        final NoLadderC4ECGenerator generator = new NoLadderC4ECGenerator(graph, new HamEdgeProcessor(graph, new HamUntouchableEdgeGetter(graph)), depth);
        generator.generate();
    }
}
