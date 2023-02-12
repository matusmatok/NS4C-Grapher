/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import graph.components.Graph;
import graph.generators.AbstractGenerator;
import graph.generators.TopVerifyingGenerator;
import graph.processors.UnavoidableEdgeProcessor;

/**
 *
 * @author ASUS
 */


public class Main {
    
    
    public static void main(String[] args){
        
        Graph g = new Graph();
        g.init();
        g.op3(0);
        g.getFaces().get(0).toNormalForm();
        AbstractGenerator generator = new TopVerifyingGenerator(g, new UnavoidableEdgeProcessor(g), 28 );
        generator.generate();
        System.out.println("DONE");
      
    }
    
}
