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
import graph.processors.AbstractProcessor;
import graph.processors.UnavoidableEdgeProcessor;

/**
 *
 * @author ASUS
 */


public class Main {
    
    
    public static void main(String[] args){


        final NoLadderC4ECGraph graph = new NoLadderC4ECGraph();
        graph.init();
        final NoLadderC4ECGenerator generator = new NoLadderC4ECGenerator(graph, new AbstractProcessor(graph, null, null, null) {
            @Override
            public boolean hasProperty() {
                return true;
            }
        }, 36);
        generator.generate();
      
    }
    
}
