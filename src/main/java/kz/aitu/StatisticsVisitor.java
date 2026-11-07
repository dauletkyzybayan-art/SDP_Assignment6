package kz.aitu;

public class StatisticsVisitor implements Visitor {
    private int fileCount;
    private int directoryCount;
    private int depth;
    private int maxDepth;

    public void calculate(Element root) {
        fileCount = 0;
        directoryCount = 0;
        depth = 0;
        maxDepth = 0;

        root.accept(this);
    }

    @Override
    public void visit(FileNode file) {
        fileCount++;
        maxDepth = Math.max(maxDepth, depth);
    }

    @Override
    public void visit(DirectoryNode directory) {
        directoryCount++;
        maxDepth = Math.max(maxDepth, depth);

        depth++;
        for (Node child : directory.getChildren()) {
            child.accept(this);
        }
        depth--;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getDirectoryCount() {
        return directoryCount;
    }

    public int getMaxDepth() {
        return maxDepth;
    }
}