/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import graph.components.Edge;
import graph.components.Face;
import graph.components.Graph;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author ASUS
 */
public class LexCoder {
    
    public static ArrayList<Integer> getLexCodeForFace(Face f, Graph g){
        ArrayList<Edge> edges = f.getAdjacentEdgesCOPY();
        ArrayList<Integer> lexCode = null;
        Collections.sort(edges, (Edge e1, Edge e2) -> e1.getOppositeFace(f).size() - e2.getOppositeFace(f).size());
        
        int smallestSize = edges.get(0).getOppositeFace(f).size();
        //System.out.println("Smallest : " + smallestSize);
        int i = 0;
        while(edges.get(i).getOppositeFace(f).size() == smallestSize){
            //System.out.println("iter");
            Edge edge = edges.get(i);
            Face mutualNeigh1 = edge.getVFrom().getThirdFace(edge.getLeftFace(), edge.getRightFace());
            Face mutualNeigh2 = edge.getVTo().getThirdFace(edge.getLeftFace(), edge.getRightFace());
            
            Face primaryFace = edge.getOppositeFace(f);
            primaryFace.setPriority(2);
            
            if (mutualNeigh1.size() <= mutualNeigh2.size()){
                mutualNeigh1.setPriority(1);

                if (lexCode == null){
                    
                    lexCode = getSimpleLexCode(f);
                }
                else{
                    lexCode = toBeat(f, lexCode);
                }
                g.untagFaces();
                mutualNeigh1.setPriority(0);
            }
            if (mutualNeigh2.size() <= mutualNeigh1.size()){
                mutualNeigh2.setPriority(1);
           
                if (lexCode == null){
                    lexCode = getSimpleLexCode(f);
                }
                else{
                    lexCode = toBeat(f, lexCode);
                }
                g.untagFaces();
                mutualNeigh2.setPriority(0);
            }
            
            primaryFace.setPriority(0);
            i++;
            if (i >= edges.size()) break;
            
        }
        return lexCode;
    }
    
    public static ArrayList<Integer> getSimpleLexCode(Face f){
        
        ArrayList<Integer> lexCode = new ArrayList<>();
        ItemPriorityQueue ipq = new ItemPriorityQueue();
        
        int faceId = 0;
        lexCode.add(f.size());
        ipq.addFace(faceId++, f);
        
        while(ipq.hasNext()){
            Face next = ipq.peakNext();
            lexCode.add(next.size());
            ipq.addFace(faceId++, next);
        }
        
        //System.out.println("Simple " + lexCode);
        return lexCode;
    }
    
    public static ArrayList<Integer> toBeat(Face f, ArrayList<Integer> codeToBeat){

        ArrayList<Integer> lexCode = new ArrayList<>();
        ItemPriorityQueue ipq = new ItemPriorityQueue();
        if (f.size() > codeToBeat.get(0)) return codeToBeat;        
        
        int faceId = 0;
        lexCode.add(f.size());
        ipq.addFace(faceId++, f);
        boolean beaten = false;
        
        while(ipq.hasNext()){
            Face next = ipq.peakNext();
            if (next.size() < codeToBeat.get(faceId)) beaten = true;
            if (!beaten && next.size() > codeToBeat.get(faceId)) return codeToBeat;
            lexCode.add(next.size());
            ipq.addFace(faceId++, next);
        }
        
        //System.out.println("toBeat " + lexCode);
        return lexCode;
    }
    
    public static boolean isMinimal(Graph g, ArrayList<Integer> codeInQuestion){
        int minimalFaceSize = g.minimalFaceSize();
        if (codeInQuestion.get(0) > minimalFaceSize) return false;

        for (Face f: g.getFaces()){
            if (f.size() == minimalFaceSize){
                ArrayList<Integer> competitor = LexCoder.getLexCodeForFace(f, g);
                assert competitor.size() == codeInQuestion.size();
                for (int i = 0; i<competitor.size(); i++){
                    if (competitor.get(i) < codeInQuestion.get(i)) return false;
                    if (competitor.get(i) > codeInQuestion.get(i)) break;
                }
            }
        }
        return true;
    }
}
