package graph.processors;

import graph.builders.AbstractTwoFactorBuilder;
import graph.components.Edge;
import graph.components.AbstractGraph;
import graph.components.Vertex;
import graph.preprocessors.ambiguous.AmbiguousVertexGetter;
import graph.preprocessors.HamEdgeGetter;
import graph.preprocessors.reducible.ReducibleConfigurationFinder;
import graph.preprocessors.distributors.VertexPolarityDistributor;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractProcessorV2 {
    protected final AbstractGraph graph;

    private final HamEdgeGetter<Edge> potentialEdgeGetter;

    private final AmbiguousVertexGetter ambiguousVertexGetter;
    private final AbstractTwoFactorBuilder builder;
    private final ReducibleConfigurationFinder finder;

    public AbstractProcessorV2(AbstractGraph graph, HamEdgeGetter<Edge> potentialEdgeGetter,
                               AmbiguousVertexGetter ambiguousVertexGetter, AbstractTwoFactorBuilder builder) {
        this.graph = graph;
        this.potentialEdgeGetter = potentialEdgeGetter;
        this.ambiguousVertexGetter = ambiguousVertexGetter;
        this.builder = builder;
        finder = new ReducibleConfigurationFinder(graph);
    }
    public boolean findGoodEdges() {
        if (finder.containsPolarIndependentReducibleConfig()) return false;
        final List<Edge> potentiallyGoodEdges = potentialEdgeGetter.getResult();
        System.out.println(potentiallyGoodEdges);
        final int[][] potentialEdgePolarities = {{0,0},{0,1},{1,0},{1,1}};

        boolean finalResult = false;

        for (Edge potentialEdge : potentiallyGoodEdges){
            /*resultMatrix[0][x] == true means that for potentialEdge.getVFrom() we already found
             * a polarity distribution such that by removing the vertex the tri-pole is of type:
             * x == 0: D
             * x == 1: E
             * x == 2: F
             * */
            final boolean[][] resultMatrix = {{false,false,false},{false,false,false}};
            final List<Vertex> ambiguousVertices = ambiguousVertexGetter.getResult(potentialEdge);
            for (int[] potentialEdgePolarity : potentialEdgePolarities) {
                final ArrayList<Vertex> edgeVertices = new ArrayList<>();
                edgeVertices.add(potentialEdge.getVFrom());
                edgeVertices.add(potentialEdge.getVTo());
                final VertexPolarityDistributor distributor = new VertexPolarityDistributor(graph,
                        ambiguousVertices,
                        edgeVertices,
                        potentialEdgePolarity);

                for (AbstractGraph polarisedGraph : distributor) {
                    if ((potentialEdgePolarity[0] == 1 ? resultMatrix[0][0] : resultMatrix[0][1] && resultMatrix[0][2])
                        && (potentialEdgePolarity[1] == 1 ? resultMatrix[1][0] : resultMatrix[1][1] && resultMatrix[1][2])) {
                        break;
                    }
                    verifyForPotentialVertex(potentialEdgePolarity[0], potentialEdge.getVFrom(), potentialEdge, resultMatrix[0]);
                    verifyForPotentialVertex(potentialEdgePolarity[1], potentialEdge.getVTo(), potentialEdge, resultMatrix[1]);
                }
            }
            finalResult |= resultMatrix[0][0] || resultMatrix[0][1] || resultMatrix[0][2] || resultMatrix[1][0] || resultMatrix[1][1] || resultMatrix[1][2];
        }
        return finalResult;
    }

    private void verifyForPotentialVertex(final int polarity, final Vertex vertex, final Edge potentialEdge,
            final boolean[] resultMatrix) {
        if (polarity == 1) {
            if (!resultMatrix[0]) {
                if (!builder.canBuild(potentialEdge)) {
                    System.out.println("---- Type D tri-pole ----");
                    System.out.println("Vertex: " + vertex.getId() + ", Potential edge: "
                            + potentialEdge.getId());
                    System.out.println(graph);
                    resultMatrix[0] = true;
                }
            }
        }  else if (!resultMatrix[1] || !resultMatrix[2]){
            vertex.setSelfPseudoNegative();
            if (!builder.canBuild(potentialEdge)) {
                vertex.setSelfNegative();
                if (builder.canBuild(potentialEdge)) {
                    if (!resultMatrix[1]) {
                        System.out.println("---- Type E tri-pole ----");
                        System.out.println("Vertex: " + vertex.getId() + ", Potential edge: " + potentialEdge.getId());
                        System.out.println(graph);
                    }
                    resultMatrix[1] = true;
                } else {
                    if (!resultMatrix[2]) {
                        System.out.println("---- Type F tri-pole ----");
                        System.out.println("Vertex: " + vertex.getId() + ", Potential edge: " + potentialEdge.getId());
                        System.out.println(graph);
                    }
                    resultMatrix[2] = true;
                }
            }
            vertex.setSelfNegative();
        }
    }
}
