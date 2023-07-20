# Graphenprogramm

This project was part of a programming assignment at my school.


It can read an adjacency matrix from a CSV file and calculates the distance matrix, path matrix, eccentricities, centre, radius, bridges, articulations and components.

## Installation

```
git clone https://github.com/Thub543/GraphProgram.git
```

Then run the shell script or cmd script.

```
sh start.sh
```
or

```
start.cmd
```

There are 6 predefined graphs available, if you want to add your own just drop a CSV with a square matrix into the graphsCsv folder.

## Learning Outcome

I have learned and understood the theory behind graphs and created an implementation with this knowledge.

Furthermore, I have taken on the SOLID principle in order to implement a clear structure between the classes and their tasks. In this case the SquareMatrix and Graph class.
In the SquareMatrix class, all tasks are connected to a matrix, such as multiplication, potency or reading in the matrix.
The Graph class is responsible for calculating the graph elements.

I have used dependency injection so that the graph is passed a matrix and so the dependent matrix is injected externally in the constructor.

Additionally, I created test cases to check if my implementations are implemented correctly.
