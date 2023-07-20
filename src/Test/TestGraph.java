package Test;

import Model.Graph;
import Model.GraphException;
import Model.SquareMatrix;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Model.Graph.*;

import static org.junit.Assert.*;

public class TestGraph {

	String pathToConnectedGraph = 		Paths.get("").toAbsolutePath().resolve("src/Test/TestGraphs/Test_graph_5Nodes_connected.csv").toString();
	String pathToNotConnectedGraph = 	Paths.get("").toAbsolutePath().resolve("src/Test/TestGraphs/Test_graph_4Nodes_not_connected.csv").toString();
	String pathToTestPathMatrixGraph = 	Paths.get("").toAbsolutePath().resolve("src/Test/TestGraphs/Test_graph_pathmatrix.csv").toString();


	@Test
	public void distanceMatrixConnected() throws GraphException {

		var graph = new Graph(SquareMatrix.fromCSV(pathToConnectedGraph));
		int[][] distanceMatrix = {
				{0,1,2,1,3},
				{1,0,1,2,2},
				{2,1,0,1,1},
				{1,2,1,0,2},
				{3,2,1,2,0}
		};
		assertArrayEquals(distanceMatrix, graph.getDistanceMatrix());
	}

	//Test: DM2
	@Test
	public void distanceMatrixNotConnected() throws GraphException {
		var graph = new Graph(SquareMatrix.fromCSV(pathToNotConnectedGraph));
		int[][] distanceMatrix = {
				{0, 1, 1, Integer.MAX_VALUE},
				{1, 0, 2, Integer.MAX_VALUE},
				{1, 2, 0, Integer.MAX_VALUE},
				{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0}
		};
		assertArrayEquals(distanceMatrix, graph.getDistanceMatrix());
	}

	@Test
	public void pathMatrixConnected() throws GraphException {
		var graph = new Graph(SquareMatrix.fromCSV(pathToConnectedGraph));
		int[][] pathMatrix = {
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
		};
		assertArrayEquals(pathMatrix, graph.getPathMatrix());
	}

	@Test
	public void pathMatrixNotConnected() throws GraphException {
		var graph = new Graph(SquareMatrix.fromCSV(pathToTestPathMatrixGraph));
		int[][] pathMatrix = {
				{1,0,1,1,0},
				{0,1,0,0,1},
				{1,0,1,1,0},
				{1,0,1,1,0},
				{0,1,0,0,1},
		};
		assertArrayEquals(pathMatrix, graph.getPathMatrix());
	}



	@Test
	public void excentricitiesOK() throws GraphException {
		var graph = new Graph(SquareMatrix.fromCSV(pathToConnectedGraph));
		int[] result =  {3, 2, 2, 2, 3};
		assertArrayEquals(result, graph.eccentricity());
	}

	@Test(expected = GraphException.class)
	public void excentricitiesNotConnected() throws GraphException {
		var graph = new Graph(SquareMatrix.fromCSV(pathToNotConnectedGraph));
		graph.eccentricity();
	}

	@Test
	public void radiusOK() throws GraphException {
		var graph = new Graph(SquareMatrix.fromCSV(pathToConnectedGraph));
		assertEquals(2,graph.radius());
	}

	@Test(expected = GraphException.class)
	public void radiusNotConnected() throws GraphException {
		var graph = new Graph(SquareMatrix.fromCSV(pathToNotConnectedGraph));
		graph.radius();
	}

	@Test
	public void diameterOK() throws GraphException {
		var graph = new Graph(SquareMatrix.fromCSV(pathToConnectedGraph));
		assertEquals(3, graph.diameter());
	}
	@Test(expected = GraphException.class)
	public void diameterNotConnected() throws GraphException {
		var graph = new Graph(SquareMatrix.fromCSV(pathToNotConnectedGraph));
		graph.diameter();
	}


	@Test
	public void centerOK() throws GraphException {
		var graph = new Graph(SquareMatrix.fromCSV(pathToConnectedGraph));
		List<Integer> result = Arrays.asList(2,3,4);
		Assert.assertEquals(result, graph.center());
	}

	@Test(expected = GraphException.class)
	public void centerNotConnected() throws GraphException {
		var graph = new Graph(SquareMatrix.fromCSV(pathToNotConnectedGraph));
		graph.center();
	}


	@Test
	public void articulationsOK() throws GraphException {
		var graph = new Graph(SquareMatrix.fromCSV(pathToConnectedGraph));
		List<Integer> ergebnis = List.of(3);
		Assert.assertEquals(ergebnis, graph.articulations());
	}


	@Test
	public void bridgesOK() throws GraphException {
		var graph = new Graph(SquareMatrix.fromCSV(Paths.get("").toAbsolutePath().resolve("src/Test/TestGraphs/TestGraphBridges.csv").toString()));

		ArrayList<ArrayList<Integer>> results = new ArrayList<>();

		ArrayList<Integer> bridge1 = new ArrayList<>();
		bridge1.add(1);
		bridge1.add(2);

		ArrayList<Integer> bridge2 = new ArrayList<>();
		bridge2.add(5);
		bridge2.add(6);

		ArrayList<Integer> bridge3 = new ArrayList<>();
		bridge3.add(8);
		bridge3.add(9);

		ArrayList<Integer> bridge4 = new ArrayList<>();
		bridge4.add(8);
		bridge4.add(10);

		ArrayList<Integer> bridge5 = new ArrayList<>();
		bridge5.add(12);
		bridge5.add(13);

		results.add(bridge1);
		results.add(bridge2);
		results.add(bridge3);
		results.add(bridge4);
		results.add(bridge5);

		Assert.assertEquals(results, graph.bridges());
	}

	@Test
	public void componentsOK() throws GraphException {
		var graph = new Graph(SquareMatrix.fromCSV(pathToNotConnectedGraph));
		ArrayList<ArrayList<Integer>> results = new ArrayList<>();
		ArrayList<Integer> k1 = new ArrayList<>();
		k1.add(1);
		k1.add(2);
		k1.add(3);

		ArrayList<Integer> k2 = new ArrayList<>();
		k2.add(4);

		results.add(k1);
		results.add(k2);

		Assert.assertEquals(results, components(graph.getAdjacency()));
	}
}




