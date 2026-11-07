package kz.aitu;

public class Main {
    public static void main(String[] args) {
        DirectoryNode root = new DirectoryNode("Study");
        FileNode file = new FileNode("notes.txt", 100);

        root.add(file);

        System.out.println("Folder: " + root.getName());
        System.out.println("File: " + file.getName());
        System.out.println("Size: " + file.getSize() + " bytes");
        System.out.println("Children: " + root.getChildren().size());
    }
}