/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import graph.components.SimpleGraph;
import graph.generators.cubicplanar.TopVerifyingGenerator;
import graph.processors.UnavoidableEdgeProcessor;

/**
 *
 * @author ASUS
 */


public class Main {
    
    
    public static void main(String[] args){
        
        SimpleGraph g = new SimpleGraph();
        g.init();
        g.op3(0);
        g.getFaces().get(0).toNormalForm();
        TopVerifyingGenerator generator = new TopVerifyingGenerator(g, new UnavoidableEdgeProcessor(g), 24 );
        generator.generate();
        System.out.println(generator.getTopLevelGraphs());
      
    }
    
}
