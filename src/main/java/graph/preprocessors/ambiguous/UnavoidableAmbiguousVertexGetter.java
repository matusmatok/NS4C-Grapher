package graph.preprocessors.ambiguous;

import graph.components.Edge;
import graph.components.AbstractGraph;
import graph.components.Vertex;
import graph.preprocessors.ambiguous.AmbiguousVertexGetter;

import java.util.ArrayList;
import java.util.List;

/*AmbiguousVertexGetter returns a list of ambiguous vertices for a given "good" edge.
 *The definition of an ambiguous vertex is as follows:
 * If for a fixed good edge 'e' there exists no cycle covering all vertices but 'v',
 * not containing the edge 'e', then 'v' is an ambiguous vertex.
 * */
public class UnavoidableAmbiguousVertexGetter extends AmbiguousVertexGetter {

    public UnavoidableAmbiguousVertexGetter(AbstractGraph g) {
        super(g);
    }

    @Override
    public List<Vertex> getResult(Edge goodEdge) {
        List<Vertex> ambiguousVertices = new ArrayList<>();
        for (Vertex v : graph.getVertices()) {
            for (Edge e : v.getAdjacentEdges()) excludeOmittedEdge(e, v);
            if (v.getAdjacentEdges().contains(goodEdge)) {
                if (!cycleConstructable(v.getAdjacentEdges().get(0).getOppositeVertex(v))) ambiguousVertices.add(v);
            }
            else if (excludeEdge(goodEdge)) {
                if (!cycleConstructable(v.getAdjacentEdges().get(0).getOppositeVertex(v))) ambiguousVertices.add(v);
                undoExcludeEdge(goodEdge);
            }
            else {
                ambiguousVertices.add(v);
            }
            for (Edge e : v.getAdjacentEdges()) undoExcludeOmittedEdge(e, v);
        }
        return ambiguousVertices;
    }
}
