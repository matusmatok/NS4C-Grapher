package graph.utils;

import graph.components.Vertex;
import graph.utils.AbstractUnionFindSet;
import lombok.AllArgsConstructor;

import java.util.Stack;

public class SimpleUnionFindSet implements AbstractUnionFindSet {
    @AllArgsConstructor
    protected class LogEntry {
        protected final Vertex root;
        protected final Vertex subordinate;
        protected final int rootFormerSubtreeDepth;
    }
    protected final Stack<LogEntry> unionLog = new Stack<>();
    @Override
    public void union(Vertex element1, Vertex element2) {
        if (element1.getParent() != element1 && element2.getParent() != element2) throw new RuntimeException("Trying to unify two non-root elements.");

        final Vertex root1 = element1.getParent();
        final Vertex root2 = element2.getParent();

        if (root1.getSubtreeDepth() > root2.getSubtreeDepth()) {
            root2.setParent(root1);
            unionLog.push(new LogEntry(root1, root2, root1.getSubtreeDepth()));
            root1.setSubtreeSize(root2.getSubtreeSize() + root1.getSubtreeSize());
        }
        else if (root2.getSubtreeDepth() > root1.getSubtreeDepth()) {
            root1.setParent(root2);
            unionLog.push(new LogEntry(root2, root1, root2.getSubtreeDepth()));
            root2.setSubtreeSize(root1.getSubtreeSize() + root2.getSubtreeSize());
        }
        else {
            root2.setParent(root1);
            unionLog.push(new LogEntry(root1, root2, root1.getSubtreeDepth()));
            root1.setSubtreeDepth(root1.getSubtreeDepth() + 1);
            root1.setSubtreeSize(root1.getSubtreeSize() + root2.getSubtreeSize());
        }
    }

    @Override
    public void disunion() {
        final LogEntry logEntry = unionLog.pop();
        logEntry.root.setSubtreeDepth(logEntry.rootFormerSubtreeDepth);
        logEntry.subordinate.setParent(logEntry.subordinate);
        logEntry.root.setSubtreeSize(logEntry.root.getSubtreeSize() - logEntry.subordinate.getSubtreeSize());
    }
}
