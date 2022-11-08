/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.canoniccoders;

import graph.components.Edge;
import graph.components.Face;
import graph.components.Graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author matusmatok
 */

@Deprecated
public class LexCoder extends AbstractCanonicCoder{
    
    @Override
    public List<Integer> getLexCodeForFace(Face f, Graph g, List<Integer> lexCode){
        ArrayList<Edge> edges = f.getAdjacentEdgesCOPY();
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
                    
                    lexCode = getSimpleLexCode(f, g);
                    assert lexCode.size() == g.getFaces().size();
                }
                else{
                    lexCode = toBeat(f, lexCode, g);
                    assert lexCode.size() == g.getFaces().size();
                }
                g.untagFaces();
                mutualNeigh1.setPriority(0);
            }
            if (mutualNeigh2.size() <= mutualNeigh1.size()){
                mutualNeigh2.setPriority(1);
           
                if (lexCode == null){
                    lexCode = getSimpleLexCode(f, g);
                    assert lexCode.size() == g.getFaces().size();
                }
                else{
                    lexCode = toBeat(f, lexCode, g);
                    assert lexCode.size() == g.getFaces().size();
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

    private List<Integer> getSimpleLexCode(Face f, Graph g) {
        
        List<Integer> lexCode = new ArrayList<>();
        ItemPriorityQueue ipq = new ItemPriorityQueue();
        
        int faceId = 0;
        lexCode.add(f.size());
        ipq.addFace(faceId++, f);
        
        while(ipq.hasNext()){
            Face next = ipq.peakNext();
            if (next == null) {
                if (g.getFaces().size() != lexCode.size()) {
                    ipq.printQueue();
                    g.print();
                    throw new RuntimeException("Lexcode length does not match the number of faces.");
                }
                break;
            }
            lexCode.add(next.size());
            ipq.addFace(faceId++, next);
        }
        
        //System.out.println("Simple " + lexCode);
        return lexCode;
    }

    private List<Integer> toBeat(Face f, List<Integer> codeToBeat, Graph g){

        List<Integer> lexCode = new ArrayList<>();
        ItemPriorityQueue ipq = new ItemPriorityQueue();
        if (f.size() > codeToBeat.get(0)) return codeToBeat;        
        
        int faceId = 0;
        lexCode.add(f.size());
        ipq.addFace(faceId++, f);
        boolean beaten = false;
        
        while(ipq.hasNext()){
            Face next = ipq.peakNext();
            if (next == null) {
                if (g.getFaces().size() != lexCode.size()) {
                    ipq.printQueue();
                    throw new RuntimeException("Lexcode length does not match the number of faces.");
                }
                break;
            }
            if (next.size() < codeToBeat.get(faceId)) beaten = true;
            if (!beaten && next.size() > codeToBeat.get(faceId)) return codeToBeat;
            lexCode.add(next.size());
            ipq.addFace(faceId++, next);
        }
        
        //System.out.println("toBeat " + lexCode);
        return lexCode;
    }
    
}
