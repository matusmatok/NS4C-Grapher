package graph.canoniccoders;

import graph.components.Face;
import graph.components.Graph;

import java.awt.image.ImageProducer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BFSCoder extends AbstractCanonicCoder{

    private List<Face> bfsQueue;
    private List<Face> parentFace;
    private Map<Face,Boolean> covered;

    @Override
    public LexCode getLexCodeForFace(Face f, Graph g, LexCode lexCode) {
        final List<Face> incidentFaces = f.getIncidentFaces();
        final int minimalIncidentFaceDegree = incidentFaces.stream().map((face) -> face.size()).min(Integer::compareTo).get();

        if (lexCode == null) {
            lexCode = LexCode.builder().shortAdd(Integer.MAX_VALUE).build();
        }

        for (int i=0; i<incidentFaces.size(); i++) {
            final Face oppositeFace = incidentFaces.get(i);
            if (oppositeFace.size() == minimalIncidentFaceDegree) {
                final int direction[] = {-1,1};
                for (int d = 0; d<2; d++){
                    final Face thirdFace = incidentFaces.get((incidentFaces.size() + i + direction[d])%incidentFaces.size());
                    if (thirdFace.size() <= incidentFaces.get((incidentFaces.size() + i + direction[d]*(-1))%incidentFaces.size()).size()) {
                        bfsQueue = new ArrayList<>();
                        parentFace = new ArrayList<>();
                        covered = new HashMap<>();

                        bfsQueue.add(f);
                        bfsQueue.add(oppositeFace);
                        bfsQueue.add(thirdFace);

                        parentFace.add(oppositeFace);
                        parentFace.add(f);
                        parentFace.add(f);

                        covered.put(f,true);
                        covered.put(oppositeFace,true);
                        covered.put(thirdFace,true);

                        lexCode = getLexCodeWithInitialConfig(lexCode, direction[d]);

                    }
                }
            }
        }
        return lexCode;
    }

    private LexCode getLexCodeWithInitialConfig(LexCode codeToBeat, int direction){
        int faceBfsIndex = 0;
        final List<Integer> result = new ArrayList<>();
        boolean beaten = false;

        while (bfsQueue.size() > faceBfsIndex) {

            final Face currentFace = bfsQueue.get(faceBfsIndex);
            result.add(currentFace.size());

            if (!beaten){
                if (currentFace.size() < codeToBeat.shortCodeAt(faceBfsIndex)) beaten = true;
                if (currentFace.size() > codeToBeat.shortCodeAt(faceBfsIndex)) return codeToBeat;
            }

            final List<Face> currentFaceNeighbours = currentFace.getIncidentFaces();
            final int indexOfParentFace = currentFace.getIndexOfIncidentFace(parentFace.get(faceBfsIndex));

            for (int currentIncidentFaceIndex = (currentFace.size() + indexOfParentFace + direction) % currentFace.size();
                 currentIncidentFaceIndex != indexOfParentFace;
                 currentIncidentFaceIndex = (currentIncidentFaceIndex + direction + currentFace.size()) % currentFace.size()) {
                    final Face currentlyProcessedNeighbour = currentFaceNeighbours.get(currentIncidentFaceIndex);
                    if (!covered.containsKey(currentlyProcessedNeighbour)){
                        bfsQueue.add(currentlyProcessedNeighbour);
                        parentFace.add(currentFace);
                        covered.put(currentlyProcessedNeighbour, true);
                    }
            }
            faceBfsIndex++;
        }
        return LexCode.builder().shortAddAll(result).build();
    }

}
