
# Assignment 6 — Visitor Pattern

**Course:** Software Design Patterns  
**Student:** Dauletkyzy Bayan  
**Group:** SE-2523

## Description
This project implements the Visitor design pattern using a simple in-memory file system.

The program includes three visitors:
- TotalSizeVisitor — calculates total file size.
- SearchVisitor — searches files by name and minimum size.
- StatisticsVisitor — counts files, directories, and maximum depth.

## Technologies
- Java 17
- Maven
- JUnit 5


## Run

```bash
mvn package
java -cp target/classes kz.aitu.Main
```

## Tests

```bash
mvn test
```


## Example Output
```text
Total size: 800 bytes
Found files: [Homework/report.txt]
Files: 3
Directories: 2
Max depth: 2
```

## Tests
FileSystemTest contains 10 automated tests covering double dispatch, file sizes, search, statistics, empty directories, repeated executions, and tree integrity.

## Design
Visitors handle traversal of the file system. Each visitor visits every reachable node once per operation. Visitor instances can be reused because their results are reset before a new operation.

New visitors can be added without modifying existing element classes. Adding a new element type requires updating the Visitor interface and its implementations.
