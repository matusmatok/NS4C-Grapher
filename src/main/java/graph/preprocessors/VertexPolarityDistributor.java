package graph.preprocessors;

import graph.components.Graph;
import graph.components.Vertex;

import java.util.Iterator;
import java.util.List;

public class VertexPolarityDistributor implements Iterable<Graph> {

    private final Graph graph;
    private final List<Vertex> ambiguousVertices;

    private int currentDistributionNumber = 0;
    private int maxDistributionNumber;

    public VertexPolarityDistributor(Graph g, List<Vertex> ambiguousVertices) {
        graph = g;
        this.ambiguousVertices = ambiguousVertices;
        maxDistributionNumber = (int)Math.pow(2, ambiguousVertices.size() - 1);
    }

    @Override
    public Iterator<Graph> iterator() {

        currentDistributionNumber = 0;
        for (Vertex v : graph.getVertices()) {
            v.setWeight(1);
            v.setConsistentTwoFactorState(Vertex.ConsistentTwoFactorState.P_UNDISCOVERED);
        }

        Iterator<Graph> it = new Iterator<>() {

            @Override
            public boolean hasNext() {
                return currentDistributionNumber < maxDistributionNumber;
            }

            @Override
            public Graph next() {
                int counter = 0;
                for (int i = ambiguousVertices.size() - 2; i >=0; i--) {
                    final Vertex vertex = ambiguousVertices.get(i);
                    if ((currentDistributionNumber&(1 << i)) > 0) {
                        vertex.setWeight(1);
                        vertex.setConsistentTwoFactorState(Vertex.ConsistentTwoFactorState.P_UNDISCOVERED);
                    }
                    else {
                        vertex.setWeight(0);
                        vertex.setConsistentTwoFactorState(Vertex.ConsistentTwoFactorState.N_UNDISCOVERED);
                        counter++;
                    }
                }
                final Vertex lastVertex = ambiguousVertices.get(ambiguousVertices.size() - 1);
                if (counter % 2 == 1) {
                    lastVertex.setWeight(0);
                    lastVertex.setConsistentTwoFactorState(Vertex.ConsistentTwoFactorState.N_UNDISCOVERED);
                }
                else {
                    lastVertex.setWeight(1);
                    lastVertex.setConsistentTwoFactorState(Vertex.ConsistentTwoFactorState.P_UNDISCOVERED);
                }
                currentDistributionNumber++;
                return graph;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }
}
