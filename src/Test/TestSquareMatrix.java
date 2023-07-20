package Test;


import Model.GraphException;
import Model.SquareMatrix;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class TestSquareMatrix {

	String pathToConnectedGraph = Paths.get("").toAbsolutePath().resolve("src/Test/TestGraphs/Test_graph_5Nodes_connected.csv").toString();

	@Test
	public void power2() throws GraphException {
		var matrix = new SquareMatrix(SquareMatrix.fromCSV(pathToConnectedGraph).getArray());
		int[][] A2 = {
				{2,0,2,0,0},
				{0,2,0,2,1},
				{2,0,3,0,0},
				{0,2,0,2,1},
				{0,1,0,1,1}
		};
		assertArrayEquals(A2, matrix.pow(2).getArray());
	}


	//Potenz3
	@Test
	public void power3() throws GraphException {
		var matrix = new SquareMatrix(SquareMatrix.fromCSV(pathToConnectedGraph).getArray());
		int[][] A3 = {
				{0, 4, 0, 4, 2},
				{4, 0, 5, 0, 0},
				{0, 5, 0, 5, 3},
				{4, 0, 5, 0, 0},
				{2, 0, 3, 0, 0}
		};
		assertArrayEquals(A3, matrix.pow(3).getArray());
	}

	//Test: Potenz4
	@Test
	public void power4() throws GraphException {
		var matrix = new SquareMatrix(SquareMatrix.fromCSV(pathToConnectedGraph).getArray());
		int[][] A4 = {
				{8, 0, 10, 0, 0},
				{0, 9, 0, 9, 5},
				{10, 0, 13, 0, 0},
				{0, 9, 0, 9, 5},
				{0, 5, 0, 5, 3}
		};
		assertArrayEquals(A4, matrix.pow(4).getArray());
	}




	@Test
	public void multi() throws GraphException {

		int[][] factor1 = {
				{0, 1, 0, 0},
				{1, 0, 1, 1},
				{0, 1, 0, 0},
				{0, 1, 0, 0}
		};
		var matrix = new SquareMatrix(factor1);

		int[][] factor2 = {
				{0, 1, 1, 0},
				{1, 0, 1, 0},
				{1, 1, 0, 1},
				{0, 0, 1, 0}
		};
		var f2 = new SquareMatrix(factor2);


		int [][] result = {
				{1, 0, 1, 0},
				{1, 2, 2, 1},
				{1, 0, 1, 0},
				{1, 0, 1, 0}
		};

		assertArrayEquals(result, matrix.multiplication(f2).getArray());
	}

}
