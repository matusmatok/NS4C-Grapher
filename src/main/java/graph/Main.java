/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import graph.components.Graph;
import graph.generators.SimpleGenerator;

/**
 *
 * @author ASUS
 */


public class Main {
    
    
    public static void main(String[] args){
        
        Graph g = new Graph();
        g.init();
        //ArrayList<Integer> res = lc.getLexCodeForFace(g.faces.get(1), g);
        //System.out.println(res);
        /*g.op3(g.vertices.get(0));

        ArrayList<Integer> res2 = LexCoder.getLexCodeForFace(g.faces.get(3), g);
        System.out.println(res2);
        g.op3(g.vertices.get(0));
        ArrayList<Integer> res3 = LexCoder.getLexCodeForFace(g.faces.get(0), g);
        System.out.println(res3);
        g.op3(g.vertices.get(4));
        ArrayList<Integer> res4 = LexCoder.getLexCodeForFace(g.faces.get(0), g);
        System.out.println(res4);
        g.undoOp3(g.faces.get(g.faces.size()-1));
        res4 = LexCoder.getLexCodeForFace(g.faces.get(0), g);
        System.out.println(res4);
        g.undoOp3(g.faces.get(g.faces.size()-1));
        res4 = LexCoder.getLexCodeForFace(g.faces.get(3), g);
        System.out.println(res4);
        g.op3(g.vertices.get(0));
        res3 = LexCoder.getLexCodeForFace(g.faces.get(0), g);
        System.out.println(res3);*/
        assert 0 == 1 : "YEBEE";
        SimpleGenerator generator = new SimpleGenerator();
        generator.generate(g);
        generator.printMap();
        
        
      
    }
    
}
