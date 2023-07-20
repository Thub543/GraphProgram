

import Model.Graph;
import Model.GraphException;
import Model.SquareMatrix;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		try {
			// *************************************************************************************
			// Datei auswählen
			// *************************************************************************************
			var csvDir = Paths.get("").toAbsolutePath().resolve("graphsCsv");
			List<Path> files = Files.find(csvDir, 1,
							(path, attributes) -> path.toString().endsWith(".csv"))
					.sorted()
					.toList();
			System.out.printf("Found files in  %s:%n", csvDir);
			int i = 0;
			for (var file : files)
				System.out.printf("[%d]: %s%n", i++, file);

			int filenr = -1;
			var scanner = new Scanner(System.in);
			do {
				try {
					System.out.print("Choose a file... (Abort with CTRL+C): ");
					filenr = scanner.nextInt();
				} catch (NoSuchElementException e) {
					System.out.println(e.getMessage());
				}
			} while (filenr < 0 || filenr >= files.size());
			scanner.close();
			var file = files.get(filenr).toString();
			var graph = new Graph(SquareMatrix.fromCSV(file));


			System.out.println("ANALYSIS OF THE GRAPH:");
			System.out.println("Distancematrix(N = infinite):");
			printMatrix(graph.getDistanceMatrix());
			System.out.println("-----------------------------------------");
			System.out.println("Pathmatrix:");
			printMatrix(graph.getPathMatrix());
			System.out.println("-----------------------------------------");

			System.out.printf("Nodes count: %d%n", graph.nodeCount);


			var components = Graph.components(graph.getAdjacency());
			System.out.printf("The graph has %d component/s:%n", components.size());
			System.out.println(components);

			System.out.println("Eccentricities:");
			try {
				var eccentricities = graph.eccentricity();
				System.out.println(Arrays.toString(eccentricities));

				var diameter = graph.diameter();
				System.out.printf("diameter of the Graph: %s%n",
						diameter == Graph.INF ? "inf" : diameter);
				var radius = graph.radius();
				System.out.printf("Radius of the Graph:      %s%n",
						radius == Graph.INF ? "inf" : radius);

				System.out.print("Center of the Graph:             ");
				System.out.println(Arrays.toString(graph.center().toArray()));
			} catch (GraphException e){
				System.out.println(e.getMessage());
			}

			System.out.print("Articulations of the Graph: ");
			System.out.println(Arrays.toString(graph.articulations().toArray()));
			System.out.print("Bridges of the Graph:            ");
			System.out.println(Arrays.deepToString(graph.bridges().toArray()));


		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (GraphException e) {
			System.err.println(e.getMessage());
		}
	}


	public static void printMatrix(int[][] matrix){
		for (int[] ints : matrix) {
			for (int j = 0; j < matrix.length; j++) {
				if (ints[j] == Integer.MAX_VALUE) {
					System.out.print(" N ");
				} else
					System.out.print(" "+ ints[j] + " ");
			}
			System.out.println();
		}
	}
}
