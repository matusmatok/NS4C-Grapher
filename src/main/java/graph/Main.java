/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import graph.builders.UntouchableBuilder;
import graph.components.Edge;
import graph.components.NoLadderC4ECGraph;
import graph.components.Vertex;
import graph.generators.noladderc4ec.NoLadderC4ECGenerator;
import graph.preprocessors.HamUnavoidableEdgeGetter;
import graph.preprocessors.HamUntouchableEdgeGetter;
import graph.preprocessors.ambiguous.UnavoidableAmbiguousVertexGetter;
import graph.preprocessors.ambiguous.UntouchableAmbiguousVertexGetter;
import graph.preprocessors.distributors.NoLadderC4ECPolarityDistributor;
import graph.processors.Processor;
import graph.processors.ham.HamEdgeProcessor;
import graph.utils.GlobalProgressBar;
import plantri.PlantriOutputReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ASUS
 */


public class Main {
    private static final Map<Integer, Integer> GRAPH_COUNTS = new HashMap<>();
    static {
        GRAPH_COUNTS.put(20, 12);
        GRAPH_COUNTS.put(22, 32);
        GRAPH_COUNTS.put(24, 123);
        GRAPH_COUNTS.put(26, 497);
        GRAPH_COUNTS.put(28, 2242);
        GRAPH_COUNTS.put(30, 10488);
        GRAPH_COUNTS.put(32, 51111);
        GRAPH_COUNTS.put(34, 253382);
        GRAPH_COUNTS.put(36, 1276891);
        GRAPH_COUNTS.put(44, 902238391);

    }
    private static int MAX_DEPTH = 48;
    
    public static void main(String[] args) throws FileNotFoundException {
        runPlantriWithProcessing(30, new File("C:\\Users\\ASUS\\Downloads\\output_dual17.txt"));
        //runNoLadderC4ECGenerator1(38);
    }

    /**
     * This method runs the {@link NoLadderC4ECGenerator}
     * and verifies whether the graphs contain a hamiltonian-untouchable edge.
     *
     * @param depth is the depth of the generator. At 26, we get the first positive result
     * */
    private static void runNoLadderC4ECGenerator1(final int depth) {
        GlobalProgressBar.setEnabled(true);
        final NoLadderC4ECGraph graph = new NoLadderC4ECGraph();
        graph.init();

        final NoLadderC4ECGenerator generator = new NoLadderC4ECGenerator(graph, new HamEdgeProcessor(graph, new HamUntouchableEdgeGetter(graph)), depth);
        generator.generate();
    }

    /**
     * This method uses {@link NoLadderC4ECGenerator} and a custom {@link Processor} setup
     * to find the smallest gadget containing an untouchable edge.
     *
     * @param depth is the depth of the generator
     * */
    private static void runNoLadderGeneratorWithProcessing(final int depth) {
        GlobalProgressBar.setEnabled(true);
        final NoLadderC4ECGraph graph = new NoLadderC4ECGraph();
        graph.init();

        final NoLadderC4ECGenerator generator = new NoLadderC4ECGenerator(graph,
                new Processor(graph,
                        new HamUntouchableEdgeGetter(graph),
                        new UntouchableAmbiguousVertexGetter(graph),
                        new NoLadderC4ECPolarityDistributor.NoLadderDistributorFactory(MAX_DEPTH),
                        new UntouchableBuilder(graph)),
                depth);
        generator.generate();
    }

    private static void runPlantriWithProcessing(final int depth, final File file) throws FileNotFoundException {
        final int graphCount = GRAPH_COUNTS.get(depth);
        GlobalProgressBar.setEnabled(true);
        GlobalProgressBar.setMAX(graphCount);
        GlobalProgressBar.setTASK_NAME("PROCESSING");

        final PlantriOutputReader reader = new PlantriOutputReader(new InputStreamReader(new FileInputStream(file)));
        GlobalProgressBar.startProgressBar();

        for (NoLadderC4ECGraph graph : reader) {
            GlobalProgressBar.step();
            final Processor processor = new Processor(graph,
                    new HamUntouchableEdgeGetter(graph),
                    new UntouchableAmbiguousVertexGetter(graph),
                    new NoLadderC4ECPolarityDistributor.NoLadderDistributorFactory(MAX_DEPTH),
                    new UntouchableBuilder(graph));
            if (processor.hasProperty()) graph.print();
        }
    }

    private static void runPlantriStatistics(final int depth) throws FileNotFoundException {
        final File file = null;
        final InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
        final PlantriOutputReader reader = new PlantriOutputReader(null);
        final Iterator<NoLadderC4ECGraph> iterator = reader.iterator();
        int numberOfHamEdges = 0;
        int totalNumberOfAmbig = 0;

        while (iterator.hasNext()) {
            NoLadderC4ECGraph graph = iterator.next();
            HamUnavoidableEdgeGetter hamGetter = new HamUnavoidableEdgeGetter(graph);
            List<Edge> hamEdges = hamGetter.getResult();
            numberOfHamEdges += hamEdges.size();

            for (final Edge e : hamEdges) {
                final UnavoidableAmbiguousVertexGetter ambigGetter = new UnavoidableAmbiguousVertexGetter(graph);
                final List<Vertex> ambigVertices = ambigGetter.getResult(e);
                totalNumberOfAmbig += ambigVertices.size();
            }
        }
    }
}
