package kz.aitu;

public abstract class Node implements Element {
    private final String name;
    DirectoryNode parent;

    protected Node(String name) {
        if (name == null || name.isBlank()
                || name.contains("/") || name.contains("\\")) {
            throw new IllegalArgumentException("Invalid name");
        }

        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}