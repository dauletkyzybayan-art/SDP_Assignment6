package kz.aitu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchVisitor implements Visitor {
    private final String suffix;
    private final long minSize;
    private final List<String> matches = new ArrayList<>();

    private Element searchRoot;
    private String path;

    public SearchVisitor(String suffix, long minSize) {
        if (suffix == null || minSize < 0) {
            throw new IllegalArgumentException("Invalid search settings");
        }

        this.suffix = suffix;
        this.minSize = minSize;
    }

    public List<String> search(Element root) {
        matches.clear();
        path = "";
        searchRoot = root;

        root.accept(this);

        Collections.sort(matches);
        return List.copyOf(matches);
    }

    @Override
    public void visit(FileNode file) {
        if (file.getName().endsWith(suffix)
                && file.getSize() >= minSize) {
            matches.add(path + file.getName());
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
        String previousPath = path;

        if (directory != searchRoot) {
            path += directory.getName() + "/";
        }

        for (Node child : directory.getChildren()) {
            child.accept(this);
        }

        path = previousPath;
    }
}