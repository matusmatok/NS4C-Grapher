package graph.preprocessors.distributors.negset;

import graph.components.AbstractGraph;
import graph.components.Edge;
import graph.components.Vertex;
import graph.preprocessors.distributors.negset.provider.AbstractNegSetsProvider;
import graph.preprocessors.distributors.negset.provider.AllFacesProvider;
import lombok.Getter;
import java.util.*;
import java.util.stream.Collectors;

public abstract class NegativeSetsDistributor {

    private static class NegativeSet implements Iterable<Void> {
        @Getter
        private final List<Vertex> setVertices;
        @Getter
        private List<Vertex> ambiguousVertices;
        private List<Boolean> globalMap;
        private List<Vertex> allButLastVertex;
        private Vertex lastVertex;
        private int maxDistributionNumber;

        @Getter
        private Iterator<Void> currentIterator;

        private NegativeSet(final List<Vertex> setVertices) {
            this.setVertices = setVertices;
        }

        private void setAmbiguousVertices(final List<Vertex> ambiguousVertices, final List<Boolean> globalMap) {
            this.ambiguousVertices = ambiguousVertices;
            this.globalMap = globalMap;
            maxDistributionNumber = (int)Math.pow(2, ambiguousVertices.size() - 1);
            if (size() > 0) {
                lastVertex = ambiguousVertices.get(size() - 1);
                allButLastVertex = setVertices.stream().filter(vertex -> vertex != lastVertex)
                        .collect(Collectors.toList());
                iterator();
            } else {
                allButLastVertex = new ArrayList<>(setVertices);
            }
        }

        private int size() {
            return ambiguousVertices.size();
        }

        private void setInGlobalMap(final Vertex vertex, final boolean polarity) {
            globalMap.set(vertex.getId(), polarity);
        }

        private boolean getFromGlobalMap(final Vertex vertex) {
            return globalMap.get(vertex.getId());
        }

        private boolean isNegative() {
            return setVertices.stream().map(NegativeSet.this::getFromGlobalMap)
                    .filter(bool -> bool).count() % 2 == 1;
        }

        private void setPolarity() {
            for (final Vertex vertex : ambiguousVertices) {
                if (getFromGlobalMap(vertex)) {
                    vertex.setSelfNegative();
                } else {
                    vertex.setSelfPositive();
                }
            }
        }

        @Override
        public Iterator<Void> iterator() {

            this.currentIterator = new Iterator<>() {
                private int currentDistributionNumber = 0;

                @Override
                public boolean hasNext() {
                    return currentDistributionNumber < maxDistributionNumber;
                }

                @Override
                public Void next() {
                    for (int i = 0; i <= size() - 2; i++) {
                        setInGlobalMap(ambiguousVertices.get(i), (currentDistributionNumber & (1 << i)) > 0);
                    }

                    setInGlobalMap(lastVertex, allButLastVertex.stream().map(NegativeSet.this::getFromGlobalMap)
                            .filter(bool -> bool).count() % 2 != 1);

                    currentDistributionNumber++;
                    return null;
                }
            };
            currentIterator.next();
            return currentIterator;
        }
    }

    @Getter
    private final AbstractGraph graph;
    private final List<NegativeSet> negativeSets;
    private final List<Vertex> cubes;

    public NegativeSetsDistributor(final AbstractGraph graph, final Edge potentialEdge, final List<Vertex> cubes) {
        this(graph, potentialEdge, new AllFacesProvider(graph), cubes);
    }

    public NegativeSetsDistributor(final AbstractGraph graph, final Edge potentialEdge,
                                   final AbstractNegSetsProvider potentialSetProvider, final List<Vertex> cubes) {
        this.graph = graph;
        this.negativeSets = new ArrayList<>();
        this.cubes = cubes;

        for (final List<Vertex> potentialSet : potentialSetProvider.providePotentialSets()) {
            if (action(potentialEdge, potentialSet)) {
                negativeSets.add(new NegativeSet(potentialSet));
            }
        }
    }

    protected abstract boolean action(final Edge potentialEdge, final List<Vertex> potentialSet);

    private class NegSetIterator implements  Iterator<AbstractGraph> {
        private boolean hasNext;

        public NegSetIterator(final List<Vertex> ambiguousVertices){
            super();
            final Set<Vertex> ambiguousSet = new HashSet<>(ambiguousVertices);
            negativeSets.sort(Comparator.comparing((negSet) ->
                    negSet.getSetVertices().stream().filter(ambiguousSet::contains).count()));

            List<Boolean> nextMap = new ArrayList<>(graph.getVertices().size());
            for (int i = 0; i < graph.getVertices().size(); i++) {
                if (cubes.contains(graph.getVertex(i))) {
                    nextMap.add(false);
                } else {
                    nextMap.add(true);
                }
            }

            for (final NegativeSet negSet : negativeSets) {
                final List<Vertex> negSetsAmbiguousVertices = ambiguousVertices.stream()
                        .filter(vertex -> negSet.getSetVertices().contains(vertex)).collect(Collectors.toList());
                negSet.setAmbiguousVertices(negSetsAmbiguousVertices, nextMap);
                ambiguousVertices.removeAll(negSetsAmbiguousVertices);
            }

            hasNext = true;
            for (int i = 0; i < negativeSets.size(); i++) {
                final NegativeSet negativeSet = negativeSets.get(i);
                if (negativeSet.size() == 0) {
                    if (!negativeSet.isNegative()) {
                        hasNext = calculateNextSignature(i);
                        break;
                    }
                }
            }
        }

        private boolean calculateNextSignature(final int negSetIndex) {
            if (negSetIndex < 0) {
                return false;
            }

            final NegativeSet negSet = negativeSets.get(negSetIndex);

            boolean result;
            if (negSet.size() ==  0) {
                do {
                    result = calculateNextSignature(negSetIndex - 1);
                    if (!result) return false;
                } while (!negSet.isNegative());
            } else {
                if (negSet.getCurrentIterator().hasNext()) {
                    negSet.getCurrentIterator().next();
                    return true;
                } else {
                    result = calculateNextSignature(negSetIndex - 1);
                    if (!result) return false;
                    negSet.iterator();
                }
            }
            return true;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public AbstractGraph next() {
            for (final NegativeSet negativeSet : negativeSets) {
                negativeSet.setPolarity();
            }
            hasNext = calculateNextSignature(negativeSets.size() - 1);
            return graph;
        }
    }

    public Iterator<AbstractGraph> iterator(final List<Vertex> ambiguousVertices) {
        return new NegSetIterator(ambiguousVertices);
    }
}
