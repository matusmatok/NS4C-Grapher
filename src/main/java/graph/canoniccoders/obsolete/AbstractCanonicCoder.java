/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.canoniccoders.obsolete;

import java.util.ArrayList;
import java.util.List;

import graph.canoniccoders.LexCode;
import graph.components.*;

/**
 *
 * @author ASUS
 */
public abstract class AbstractCanonicCoder {
    
    public LexCode getLexCodeForFace(Face f, Graph g){
        return getLexCodeForFace(f,g,null);
    }
    public abstract LexCode getLexCodeForFace(Face f, Graph g, LexCode lexCode);
    public boolean isMinimal(Graph g, LexCode codeInQuestion){
        int minimalFaceSize = g.minimalFaceSize();
        if (codeInQuestion.shortCodeAt(0) > minimalFaceSize) return false;
        LexCode codeToBeat = codeInQuestion;

        for (Face f: g.getFaces()){
            if (f.size() == minimalFaceSize){
                LexCode competitor = getLexCodeForFace(f, g, codeToBeat);
                if (competitor.compareTo(codeToBeat) < 0) return false;
            }
        }
        return true;
    }
}
