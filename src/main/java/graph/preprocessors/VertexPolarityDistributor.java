package graph.preprocessors;

import graph.components.CubeTripole;
import graph.components.Graph;
import graph.components.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class VertexPolarityDistributor implements Iterable<Graph> {

    private final Graph graph;
    private final List<Vertex> ambiguousVertices;
    private final List<Vertex> forcedVertices;
    private final int[] prescribedPolarity;
    private final List<CubeTripole> cubes;

    private final Set<Vertex> cubeNestedVertices;

    private final int numberOfForcedNegativeVertices;

    private int currentDistributionNumber = 0;
    private final int maxDistributionNumber;

    public VertexPolarityDistributor(Graph g, List<Vertex> ambiguousVertices) {
        this(g, ambiguousVertices, new ArrayList<>(), new int[] {});
    }

    public VertexPolarityDistributor(Graph g, List<Vertex> ambiguousVertices, List<Vertex> forcedVertices, int[] prescribedPolarity) {
        graph = g;
        cubes = graph.getCubes();
        this.ambiguousVertices = ambiguousVertices;
        this.cubeNestedVertices = new HashSet<>();

        for (CubeTripole cube : cubes) {
            this.cubeNestedVertices.addAll(cube.getAllVertices());
        }
        ambiguousVertices.removeAll(cubeNestedVertices);
        ambiguousVertices.removeAll(forcedVertices);
        this.forcedVertices = forcedVertices;
        this.prescribedPolarity = prescribedPolarity;

        int numberOfForcedNegativeVertices = cubes.size();
        for (int j : prescribedPolarity) {
            if (j == 0) numberOfForcedNegativeVertices++;
        }

        this.numberOfForcedNegativeVertices = numberOfForcedNegativeVertices;
        maxDistributionNumber = (int)Math.pow(2, ambiguousVertices.size() - 1);
    }

    @Override
    public Iterator<Graph> iterator() {

        currentDistributionNumber = 0;
        for (Vertex v : graph.getVertices()) {
            v.setSelfPositive();
        }
        for (CubeTripole cube : cubes) {
            cube.distributePolarity();
        }
        if (prescribedPolarity.length == 2) {
            if ((prescribedPolarity[0] == 0 && cubeNestedVertices.contains(forcedVertices.get(0))) ||
                    (prescribedPolarity[1] == 0 && cubeNestedVertices.contains(forcedVertices.get(1)))) {
                currentDistributionNumber = maxDistributionNumber;
            } else {
                for (int j = 0; j < 2; j++) {
                    if (prescribedPolarity[j] == 1) {
                        forcedVertices.get(j).setSelfPositive();
                    } else {
                        forcedVertices.get(j).setSelfNegative();
                    }
                }
            }
        }

        Iterator<Graph> it = new Iterator<>() {

            @Override
            public boolean hasNext() {
                return currentDistributionNumber < maxDistributionNumber;
            }

            @Override
            public Graph next() {
                int counter = numberOfForcedNegativeVertices;
                for (int i = ambiguousVertices.size() - 2; i >=0; i--) {
                    final Vertex vertex = ambiguousVertices.get(i);
                    if ((currentDistributionNumber&(1 << i)) > 0) {
                        vertex.setSelfPositive();
                    }
                    else {
                        vertex.setSelfNegative();
                        counter++;
                    }
                }
                final Vertex lastVertex = ambiguousVertices.get(ambiguousVertices.size() - 1);
                if (counter % 2 == 1) {
                    lastVertex.setSelfNegative();
                }
                else {
                    lastVertex.setSelfPositive();
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
