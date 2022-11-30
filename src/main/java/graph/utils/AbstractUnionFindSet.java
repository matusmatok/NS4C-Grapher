package graph.utils;

import graph.components.Vertex;

public interface AbstractUnionFindSet {
    public abstract void union(Vertex element1, Vertex element2);

    public abstract void disunion();

    default public Vertex find(Vertex element) {
        Vertex parent = element.getParent();
        while (parent != null) {
            element = parent;
            parent = element.getParent();
        }
        return element;
    }
}
