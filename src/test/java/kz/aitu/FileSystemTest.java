package kz.aitu;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemTest {

    private DirectoryNode createTree() {
        DirectoryNode root = new DirectoryNode("Root");
        DirectoryNode docs = new DirectoryNode("docs");
        DirectoryNode archive = new DirectoryNode("archive");

        root.add(new FileNode("z.txt", 150));
        root.add(new FileNode("photo.jpg", 500));
        docs.add(new FileNode("small.txt", 50));
        archive.add(new FileNode("a.txt", 200));

        docs.add(archive);
        root.add(docs);
        root.add(new DirectoryNode("empty"));

        return root;
    }

    @Test
    void callsCorrectVisitMethods() {
        List<String> calls = new ArrayList<>();

        Visitor visitor = new Visitor() {
            @Override
            public void visit(FileNode file) {
                calls.add("file:" + file.getName());
            }

            @Override
            public void visit(DirectoryNode directory) {
                calls.add("directory:" + directory.getName());
            }
        };

        Element file = new FileNode("test.txt", 10);
        Element directory = new DirectoryNode("docs");

        file.accept(visitor);
        directory.accept(visitor);

        assertEquals(List.of("file:test.txt", "directory:docs"), calls);
    }

    @Test
    void calculatesSizeAcrossNestedDirectories() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();

        assertEquals(900, visitor.calculate(createTree()));
    }

    @Test
    void combinesNameAndMinimumSize() {
        DirectoryNode root = createTree();

        SearchVisitor textSearch = new SearchVisitor(".txt", 150);
        assertEquals(
                List.of("docs/archive/a.txt", "z.txt"),
                textSearch.search(root)
        );

        SearchVisitor imageSearch = new SearchVisitor(".jpg", 500);
        assertEquals(List.of("photo.jpg"), imageSearch.search(root));
    }

    @Test
    void returnsSortedPathsRelativeToChosenRoot() {
        DirectoryNode outer = new DirectoryNode("Outer");
        DirectoryNode chosen = new DirectoryNode("Chosen");

        chosen.add(new FileNode("z.txt", 10));
        chosen.add(new FileNode("a.txt", 20));
        outer.add(chosen);

        SearchVisitor visitor = new SearchVisitor(".txt", 0);

        assertEquals(List.of("a.txt", "z.txt"), visitor.search(chosen));
        assertEquals(List.of("a.txt", "z.txt"), visitor.search(chosen));
    }

    @Test
    void calculatesStatisticsIncludingEmptyDirectory() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        visitor.calculate(createTree());

        assertEquals(4, visitor.getFileCount());
        assertEquals(4, visitor.getDirectoryCount());
        assertEquals(3, visitor.getMaxDepth());
    }

    @Test
    void handlesEmptyDirectoryAndNoMatches() {
        DirectoryNode empty = new DirectoryNode("Empty");

        assertEquals(0, new TotalSizeVisitor().calculate(empty));
        assertTrue(new SearchVisitor(".txt", 0).search(empty).isEmpty());
        assertTrue(
                new SearchVisitor(".pdf", 0).search(createTree()).isEmpty()
        );

        StatisticsVisitor visitor = new StatisticsVisitor();
        visitor.calculate(empty);

        assertEquals(0, visitor.getFileCount());
        assertEquals(1, visitor.getDirectoryCount());
        assertEquals(0, visitor.getMaxDepth());
    }

    @Test
    void resetsAllVisitorsBetweenRuns() {
        DirectoryNode root = createTree();
        DirectoryNode empty = new DirectoryNode("Empty");

        TotalSizeVisitor size = new TotalSizeVisitor();
        SearchVisitor search = new SearchVisitor(".txt", 150);
        StatisticsVisitor statistics = new StatisticsVisitor();

        for (int i = 0; i < 2; i++) {
            assertEquals(900, size.calculate(root));
            assertEquals(
                    List.of("docs/archive/a.txt", "z.txt"),
                    search.search(root)
            );

            statistics.calculate(root);
            assertEquals(4, statistics.getFileCount());
            assertEquals(4, statistics.getDirectoryCount());
            assertEquals(3, statistics.getMaxDepth());
        }

        assertEquals(0, size.calculate(empty));
        assertTrue(search.search(empty).isEmpty());

        statistics.calculate(empty);
        assertEquals(0, statistics.getFileCount());
        assertEquals(1, statistics.getDirectoryCount());
        assertEquals(0, statistics.getMaxDepth());
    }

    @Test
    void rejectsInvalidTreeChanges() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FileNode("bad.txt", -1)
        );

        DirectoryNode root = new DirectoryNode("Root");
        DirectoryNode child = new DirectoryNode("Child");
        DirectoryNode other = new DirectoryNode("Other");
        FileNode file = new FileNode("test.txt", 10);

        root.add(child);
        root.add(file);

        assertThrows(
                IllegalArgumentException.class,
                () -> root.add(new FileNode("test.txt", 20))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> other.add(file)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> child.add(root)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> root.add(root)
        );

        child.add(new FileNode("valid.txt", 30));

        assertEquals(2, root.getChildren().size());
        assertTrue(other.getChildren().isEmpty());
        assertEquals(40, new TotalSizeVisitor().calculate(root));
    }

    @Test
    void protectsChildrenCollection() {
        DirectoryNode root = new DirectoryNode("Root");
        root.add(new FileNode("test.txt", 10));

        assertThrows(
                UnsupportedOperationException.class,
                () -> root.getChildren().clear()
        );

        assertEquals(1, root.getChildren().size());
    }

    @Test
    void handlesSingleFileAsRoot() {
        FileNode file = new FileNode("single.txt", 75);

        assertEquals(75, new TotalSizeVisitor().calculate(file));
        assertEquals(
                List.of("single.txt"),
                new SearchVisitor(".txt", 75).search(file)
        );

        StatisticsVisitor visitor = new StatisticsVisitor();
        visitor.calculate(file);

        assertEquals(1, visitor.getFileCount());
        assertEquals(0, visitor.getDirectoryCount());
        assertEquals(0, visitor.getMaxDepth());
    }
}