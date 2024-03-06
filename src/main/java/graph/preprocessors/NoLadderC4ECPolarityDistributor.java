package graph.preprocessors;

import graph.components.AbstractGraph;
import graph.components.Vertex;
import graph.utils.EasyList;
import graph.utils.SubsetIterator;

import java.util.Iterator;
import java.util.List;

public class NoLadderC4ECPolarityDistributor implements Iterable<AbstractGraph> {

    private final int maxGraphSize;

    private final AbstractGraph graph;
    private final List<Vertex> ambiguousVertices;

    public NoLadderC4ECPolarityDistributor(final AbstractGraph graph, final List<Vertex> ambiguousVertices,
                                           final int maxGraphSize) {
        this.graph = graph;
        this.ambiguousVertices = ambiguousVertices;
        this.maxGraphSize = maxGraphSize;
    }

    @Override
    public Iterator<AbstractGraph> iterator() {
        return new PolarityIterator(graph, ambiguousVertices);
    }

    private class PolarityIterator implements Iterator<AbstractGraph> {
        private final AbstractGraph graph;
        private final List<Vertex> ambiguousVertices;
        private final int maxCubes;
        private int cubeNumber;

        private SubsetIterator cubeDistributor;
        private List<Integer> cubePositions;
        private List<Vertex> effectivelyAmbiguousVertices;

        private int maxDistributionNumber;
        private int currentDistributionNumber;

        private PolarityIterator(final AbstractGraph graph, final List<Vertex> ambiguousVertices) {
            this.graph = graph;
            this.ambiguousVertices = ambiguousVertices;
            this.cubeDistributor = new SubsetIterator(1, 0);
            this.cubePositions = cubeDistributor.next();
            this.maxCubes = (maxGraphSize - graph.getVertices().size()) / 6;
            this.cubeNumber = 0;

            this.effectivelyAmbiguousVertices = List.copyOf(ambiguousVertices);
            this.maxDistributionNumber = (int)Math.pow(2, ambiguousVertices.size() - 1);
            this.currentDistributionNumber = 0;

            for (final Vertex v : graph.getVertices()) {
                v.setSelfPositive();
            }
            for (final Vertex v : ambiguousVertices) {
                v.setSelfNegative();
            }
        }

        @Override
        public boolean hasNext() {
            return !(cubeNumber == maxCubes && !cubeDistributor.hasNext() && currentDistributionNumber >= maxDistributionNumber);
        }

        @Override
        public AbstractGraph next() {
            if (currentDistributionNumber >= maxDistributionNumber) {
                if (!cubeDistributor.hasNext()) {
                    cubeNumber++;
                    cubeDistributor = new SubsetIterator(graph.getVertices().size(), cubeNumber);
                }
                //remove old cubes
                for (final int pos : cubePositions) {
                    graph.getVertex(pos).setSelfPositive();
                }
                //distribute new cubes
                cubePositions = cubeDistributor.next();
                for (final int pos : cubePositions) {
                    graph.getVertex(pos).setSelfPseudoNegative();
                }

                effectivelyAmbiguousVertices = EasyList.copyOf(ambiguousVertices);
                for (final int vertID : cubePositions) {
                    effectivelyAmbiguousVertices.remove(graph.getVertex(vertID));
                }

                maxDistributionNumber = (int)Math.pow(2, effectivelyAmbiguousVertices.size() - 1);
                currentDistributionNumber = 0;
            }

            int counter = cubeNumber;
            for (int i = effectivelyAmbiguousVertices.size() - 2; i >=0; i--) {
                final Vertex vertex = effectivelyAmbiguousVertices.get(i);
                if ((currentDistributionNumber&(1 << i)) > 0) {
                    vertex.setSelfPositive();
                }
                else {
                    vertex.setSelfNegative();
                    counter++;
                }
            }
            final Vertex lastVertex = effectivelyAmbiguousVertices.get(effectivelyAmbiguousVertices.size() - 1);
            if (counter % 2 == 1) {
                lastVertex.setSelfNegative();
            }
            else {
                lastVertex.setSelfPositive();
            }

            currentDistributionNumber++;
            return graph;
        }
    }
}
