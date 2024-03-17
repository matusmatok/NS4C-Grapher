package graph.preprocessors.distributors.negset.provider;

import graph.components.AbstractGraph;
import graph.components.Face;
import graph.components.Vertex;

import java.util.ArrayList;
import java.util.List;

public class AllFacesProvider extends AbstractNegSetsProvider {

    public AllFacesProvider(AbstractGraph graph) {
        super(graph);
    }

    @Override
    public List<List<Vertex>> providePotentialSets() {
        final List<List<Vertex>> result = new ArrayList<>();
        for (final Face face : graph.getFaces()) {
            result.add(face.getAdjacentVertices());
        }
        return result;
    }
}
