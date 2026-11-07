package kz.aitu;

import java.util.ArrayList;
import java.util.List;

public class DirectoryNode extends Node {
    private final List<Node> children = new ArrayList<>();

    public DirectoryNode(String name) {
        super(name);
    }

    public void add(Node child) {
        if (child == null) {
            throw new IllegalArgumentException("Child cannot be null");
        }

        if (child.parent != null) {
            throw new IllegalArgumentException("Child already has a parent");
        }

        for (Node current = this; current != null; current = current.parent) {
            if (current == child) {
                throw new IllegalArgumentException("Cycle is not allowed");
            }
        }

        for (Node existing : children) {
            if (existing.getName().equals(child.getName())) {
                throw new IllegalArgumentException("Duplicate name");
            }
        }

        children.add(child);
        child.parent = this;
    }

    public List<Node> getChildren() {
        return List.copyOf(children);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}