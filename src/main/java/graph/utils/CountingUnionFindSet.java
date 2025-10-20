package graph.utils;

import graph.components.Vertex;
import lombok.AllArgsConstructor;

import java.util.Stack;

public class CountingUnionFindSet implements AbstractUnionFindSet {

    @AllArgsConstructor
    protected class LogEntry {
        protected final Vertex root;
        protected final Vertex subordinate;
        protected final int rootFormerSubtreeDepth;
        protected final int rootFormerWeight;
    }

    private final Stack<LogEntry> unionLog = new Stack<>();

    @Override
    public void union(Vertex element1, Vertex element2) {
        if (element1.getParent() != element1 && element2.getParent() != element2) throw new RuntimeException("Trying to unify two non-root elements.");

        final Vertex root1 = element1.getParent();
        final Vertex root2 = element2.getParent();

        if (root1.getSubtreeDepth() > root2.getSubtreeDepth()) {
            root2.setParent(root1);
            unionLog.push(new CountingUnionFindSet.LogEntry(root1, root2, root1.getSubtreeDepth(), root1.getWeight()));
            root1.setSubtreeSize(root2.getSubtreeSize() + root1.getSubtreeSize());
            root1.setWeight(root1.getWeight() + root2.getWeight());
        }
        else if (root2.getSubtreeDepth() > root1.getSubtreeDepth()) {
            root1.setParent(root2);
            unionLog.push(new CountingUnionFindSet.LogEntry(root2, root1, root2.getSubtreeDepth(), root2.getWeight()));
            root2.setSubtreeSize(root1.getSubtreeSize() + root2.getSubtreeSize());
            root2.setWeight(root2.getWeight() + root1.getWeight());
        }
        else {
            root2.setParent(root1);
            unionLog.push(new CountingUnionFindSet.LogEntry(root1, root2, root1.getSubtreeDepth(), root1.getWeight()));
            root1.setSubtreeDepth(root1.getSubtreeDepth() + 1);
            root1.setSubtreeSize(root1.getSubtreeSize() + root2.getSubtreeSize());
            root1.setWeight(root1.getWeight() + root2.getWeight());
        }
    }

    @Override
    public void disunion() {
        final CountingUnionFindSet.LogEntry logEntry = unionLog.pop();
        logEntry.root.setSubtreeDepth(logEntry.rootFormerSubtreeDepth);
        logEntry.subordinate.setParent(logEntry.subordinate);
        logEntry.root.setSubtreeSize(logEntry.root.getSubtreeSize() - logEntry.subordinate.getSubtreeSize());
        logEntry.root.setWeight(logEntry.rootFormerWeight);
    }
}
