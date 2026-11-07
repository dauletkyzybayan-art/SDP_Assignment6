package kz.aitu;

public class Main {
    public static void main(String[] args) {
        DirectoryNode root = new DirectoryNode("Study");
        DirectoryNode homework = new DirectoryNode("Homework");

        root.add(new FileNode("notes.txt", 100));
        root.add(new FileNode("photo.jpg", 500));

        homework.add(new FileNode("report.txt", 200));
        root.add(homework);

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();

        System.out.println(
                "Total size: " + sizeVisitor.calculate(root) + " bytes"
        );

        SearchVisitor searchVisitor = new SearchVisitor(".txt", 150);

        System.out.println("Found files: " + searchVisitor.search(root));
    }
}