/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.canoniccoders;

import java.util.ArrayList;
import java.util.List;

import graph.components.*;

/**
 *
 * @author ASUS
 */
public abstract class AbstractCanonicCoder {
    
    public List<Integer> getLexCodeForFace(Face f, Graph g){
        return getLexCodeForFace(f,g,null);
    }
    public abstract List<Integer> getLexCodeForFace(Face f, Graph g, List<Integer> lexCode);
    public boolean isMinimal(Graph g, List<Integer> codeInQuestion){
        int minimalFaceSize = g.minimalFaceSize();
        if (codeInQuestion.get(0) > minimalFaceSize) return false;
        List<Integer> codeToBeat = new ArrayList<>(codeInQuestion);

        for (Face f: g.getFaces()){
            if (f.size() == minimalFaceSize){
                List<Integer> competitor = getLexCodeForFace(f, g, codeToBeat);
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
