/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import java.util.ArrayList;
import java.util.*;

/**
 *
 * @author ASUS
 */


public class main {
    
    
    public static void main(String[] args){
        
        Graph g = new Graph();
        g.init();
        LexCoder lc = new LexCoder();
        //ArrayList<Integer> res = lc.getLexCodeForFace(g.faces.get(1), g);
        //System.out.println(res);
        g.op3(g.vertices.get(0));

        ArrayList<Integer> res2 = lc.getLexCodeForFace(g.faces.get(3), g);
        System.out.println(res2);
        g.op3(g.vertices.get(0));
        ArrayList<Integer> res3 = lc.getLexCodeForFace(g.faces.get(0), g);
        System.out.println(res3);
        g.op3(g.vertices.get(4));
        ArrayList<Integer> res4 = lc.getLexCodeForFace(g.faces.get(0), g);
        System.out.println(res4);
        g.undoOp3(g.faces.get(g.faces.size()-1));
        res4 = lc.getLexCodeForFace(g.faces.get(0), g);
        System.out.println(res4);
        g.undoOp3(g.faces.get(g.faces.size()-1));
        res4 = lc.getLexCodeForFace(g.faces.get(0), g);
        System.out.println(res4);
        g.op3(g.vertices.get(0));
        res3 = lc.getLexCodeForFace(g.faces.get(0), g);
        System.out.println(res3);
        
        
      
    }
    
}
