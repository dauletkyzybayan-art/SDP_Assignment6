package kz.aitu;

public interface Visitor {
    void visit(FileNode file);

    void visit(DirectoryNode directory);
}