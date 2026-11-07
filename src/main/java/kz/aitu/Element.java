package kz.aitu;

public interface Element {
    String getName();

    void accept(Visitor visitor);
}