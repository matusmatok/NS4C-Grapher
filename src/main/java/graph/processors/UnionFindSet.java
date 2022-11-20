package graph.processors;

import graph.components.Vertex;
import lombok.AllArgsConstructor;

import java.util.Stack;

public class UnionFindSet {
    @AllArgsConstructor
    private class LogEntry {
        private final Vertex root;
        private final Vertex subordinate;
        private final int rootFormerSubtreeDepth;
    }
    private final Stack<LogEntry> unionLog = new Stack<>();

    public void union(Vertex element1, Vertex element2) {
        if (element1.getParent() != null || element2.getParent() != null) throw new RuntimeException("Trying to unify two non-root elements.");
        if (element1.getSubtreeDepth() > element2.getSubtreeDepth()) {
            element2.setParent(element1);
            unionLog.push(new LogEntry(element1, element2, element1.getSubtreeDepth()));
            element1.setSubtreeSize(element1.getSubtreeSize() + element2.getSubtreeSize());
        }
        else if (element2.getSubtreeDepth() > element1.getSubtreeDepth()) {
            element1.setParent(element2);
            unionLog.push(new LogEntry(element2, element1, element2.getSubtreeDepth()));
            element2.setSubtreeSize(element1.getSubtreeSize() + element2.getSubtreeSize());
        }
        else {
            element2.setParent(element1);
            unionLog.push(new LogEntry(element1, element2, element1.getSubtreeDepth()));
            element1.setSubtreeDepth(element1.getSubtreeDepth() + 1);
            element1.setSubtreeSize(element1.getSubtreeSize() + element2.getSubtreeSize());
        }
    }

    public void disunion() {
        final LogEntry logEntry = unionLog.pop();
        logEntry.root.setSubtreeDepth(logEntry.rootFormerSubtreeDepth);
        logEntry.subordinate.setParent(null);
        logEntry.root.setSubtreeSize(logEntry.root.getSubtreeSize() - logEntry.subordinate.getSubtreeSize());
    }

    public Vertex find(Vertex element) {
        Vertex parent = element.getParent();
        while (parent != null) {
            element = parent;
            parent = element.getParent();
        }
        return element;
    }
}
