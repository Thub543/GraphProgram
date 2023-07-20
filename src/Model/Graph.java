package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Graph {
	private final SquareMatrix adjacency;
	private final SquareMatrix distanceMatrix;
	public final SquareMatrix pathMatrix;
	public final int nodeCount; // Die Anzahl der Knoten des eingelesenen Graphen.
	public static final int INF = Integer.MAX_VALUE;


	public Graph(SquareMatrix adj)  {
		this.adjacency = adj;
		nodeCount = adjacency.getDimension();
		distanceMatrix = calculateDistanceMatrix();
		pathMatrix = calculatePathMatrix();
	}

	public int[][] getAdjacency(){
		return adjacency.getArray();
	}
	public int[][] getDistanceMatrix(){
		return distanceMatrix.getArray();
	}
	public int[][] getPathMatrix(){
		return pathMatrix.getArray();
	}



	/**
	 * Calculates the shortest path distances between all pairs of nodes in a graph.
	 * This method uses the Floyd-Warshall algorithm to determine the shortest paths
	 * between all pairs of nodes based on the adjacency matrix of the graph.
	 * Steps:
	 * 1. Initialize the distance matrix:
	 *    - If the nodes are the same, the distance is 0.
	 *    - If there is a direct edge between nodes, the distance is the weight of that edge (from the adjacency matrix).
	 *    - Otherwise, the distance is set to infinity (represented by the constant INF).
	 * 2. Update the distance matrix using the Floyd-Warshall algorithm:
	 *    - For each node 'k', consider it as an intermediate point between every pair of nodes (i, j).
	 *    - Update the shortest distance between i and j if a shorter path is found via node k.
	 */
	private SquareMatrix calculateDistanceMatrix(){
		int[][] result = adjacency.getArray();
		for (int row = 0; row < nodeCount; row++) {
			for (int col = 0; col < nodeCount; col++) {
				if (row == col) {
					result[row][col] = 0;
				} else if (adjacency.getValue(row,col) == 0) {
					result[row][col] = INF;
				} else {
					result[row][col] = adjacency.getValue(row,col);
				}
			}
		}
		for (int k = 0; k < nodeCount; k++) {
			for (int i = 0; i < nodeCount; i++) {
				for (int j = 0; j < nodeCount; j++) {
					if (result[i][k] != INF && result[k][j] != INF
							&& result[i][j] > result[i][k] + result[k][j]) {
						result[i][j] = result[i][k] + result[k][j];
					}
				}
			}
		}
		return new SquareMatrix(result);
	}

	/**
	 * Calculates the path matrix to determine the existence of paths between all pairs of nodes in the graph.
	 * Steps:
	 * 1. Create a copy of the adjacency matrix to ensure the original matrix remains unmodified.
	 * 2. Initialize the pathMatrix:
	 *    - For each node, set pathMatrix[i][i] to 1 (indicating a node can reach itself).
	 *    - For all direct connections from the adjacency matrix, copy the value to the pathMatrix.
	 * 3. For each increasing power (number of steps) up to nodeCount:
	 *    - Calculate the matrix raised to that power.
	 *    - Check if new paths are discovered between pairs of nodes. If so, update pathMatrix.
	 *    - If no updates were made during an iteration, break out early.
	 */
	private SquareMatrix calculatePathMatrix() {
		int[][] result = adjacency.getArray();
		for (int i = 0; i < nodeCount; i++) {
			result[i][i]=1;
		}
		for (int potenz = 2; potenz < nodeCount; potenz++) {
			boolean updated = false;
			SquareMatrix potenzmatrix = new SquareMatrix(result).pow(potenz);
			for (int row = 0; row < nodeCount; row++) {
				for (int col = 0; col < nodeCount; col++) {
					if ( row != col &&  result[row][col] == 0 && potenzmatrix.getValue(row,col) != 0) {
						result[row][col] = 1;
						updated = true;
					}
				}
			}
			if (!updated){
				break;
			}
		}
		return new SquareMatrix(result);
	}

	/**
	 * Calculates the excentricity values for each node in the graph.
	 * The excentricity of a node is the greatest distance from the node to
	 * any other node in the graph. It is used to measure how "externally
	 * central" a node is, the lower the excentricity, the more central the node is.
	 * Steps:
	 * 1. Check if the graph is connected using the distanceMatrixIsNotConnected() method.
	 *    - If the graph is not connected, throw a GraphException.
	 * 2. For each node in the graph:
	 *    - Iterate through all the other nodes.
	 *    - Update the eccentricity value for the node based on the maximum
	 *      distance found from the distanceMatrix.
	 * @return An array of integers representing the excentricity values for each node.
	 * @throws GraphException If the graph is not connected.
	 */
	public int[] eccentricity() throws GraphException {
		if (distanceMatrixIsNotConnected()) {
			throw new GraphException("Graph not connected");
		}
		int[] exzen = new int[nodeCount];
		for (int row = 0; row < nodeCount; row++) {
			for (int col = 0; col < nodeCount; col++) {
				exzen[row] = Math.max(distanceMatrix.getValue(row,col), exzen[row]);
			}
		}
		return exzen;
	}

	/**
	 * Calculates the radius of the graph.
	 * The radius of a graph is the smallest excentricity among all nodes in the graph.
	 * The excentricity of a node is the greatest distance from the node to any other
	 * node in the graph.
	 * Steps:
	 * 1. Check if the graph is connected using the distanceMatrixIsNotConnected() method.
	 *    - If the graph is not connected, throw a GraphException.
	 * 2. Get the eccentricity values for all nodes using the excentricity() method.
	 * 3. Find the smallest excentricity value among all nodes to determine the radius.
	 *
	 * @return The radius value of the graph.
	 * @throws GraphException If the graph is not connected.
	 */
	public int radius() throws GraphException { // min Wert bei exentri.
		if (distanceMatrixIsNotConnected()) throw new GraphException("Graph not connected");

		int [] exzentrizitaeten = eccentricity();
		int radius = exzentrizitaeten[0];
		for (int j : exzentrizitaeten)
			radius = Math.min(j, radius);

		return radius;
	}

	/**
	 * Calculates the diameter of the graph.
	 * The diameter of a graph is the largest excentricity among all nodes in the graph.
	 * The diameter indicates the greatest distance between any two nodes in the graph,
	 * considering the shortest paths.
	 * Steps:
	 * 1. Check if the graph is connected using the distanceMatrixIsNotConnected() method.
	 *    - If the graph is not connected, throw a GraphException.
	 * 2. Retrieve the eccentricity values for all nodes using the excentricity() method.
	 * 3. Identify the largest eccentricity value among all nodes to determine the diameter.
	 * @return the value of the diameter
	 */
	public int diameter() throws GraphException {// Max Wert bei exentri.
		if (distanceMatrixIsNotConnected()) throw new GraphException("Graph not connected");

		int[] exzentrizitaeten = eccentricity();
		int dm = exzentrizitaeten[0];
		for (int j : exzentrizitaeten)
			dm = Math.max(j, dm);

		return dm;
	}

	/**
	 * Identifies and returns the central nodes of the graph.
	 * The center of a graph consists of all nodes whose
	 * eccentricity equals the graph's radius. The radius is the smallest eccentricity among
	 * all nodes.
	 * Steps:
	 * 1. Check if the graph is connected using the distanceMatrixIsNotConnected() method.
	 * - If the graph is not connected, throw a GraphException.
	 * 2. Retrieve the eccentricity values for all nodes using the eccentricity() method.
	 * 3. Determine the radius of the graph using the radius() method.
	 * 4. Iterate through all nodes and identify those whose eccentricity matches the radius.
	 *
	 * @return A list of integers representing the central nodes of the graph.
	 * @throws GraphException If the graph is not connected.
	 */
	public List<Integer> center() throws GraphException { //  exzen == radius = zentrum
		if (distanceMatrixIsNotConnected()){
			throw new GraphException("Graph not connected");
		}

		int[] eccentricities = eccentricity();
		int radius = radius();
		ArrayList<Integer> centers = new ArrayList<>();

		for(int i = 0; i < eccentricities.length; i++) {
			if(eccentricities[i] == radius)
				centers.add(i + 1);
		}
		return centers;
	}

	/**
	 * Determines and returns the connected components of a given graph.
	 * The method identifies separate connected components in the graph
	 * represented by the input adjacency matrix. It uses Depth-First Search (DFS)
	 * to explore the graph from each unvisited node, marking nodes as visited
	 * as they are encountered.
	 * Steps:
	 * 1. Initialize an array to keep track of visited nodes.
	 * 2. For each node in the graph:
	 *    - If the node has not been visited:
	 *      - Start a DFS from this node, identifying all nodes in the same component.
	 *      - Add this component to the list of all components.
	 * @param matrix The adjacency matrix of the graph
	 * @return A list containing the connected components of the graph
	 */
	public static List<List<Integer>> components(int[][] matrix) {
		int nodeCount = matrix.length;
		boolean[] visited = new boolean[nodeCount];
		List<List<Integer>> allKomponents = new ArrayList<>();

		for (int node = 0; node < nodeCount; node++) {
			if (!visited[node]) {
				List<Integer> component = new ArrayList<>();
				dfs(matrix, node, visited, component);
				allKomponents.add(component);
			}
		}
		return allKomponents;
	}

	/**
	 * Performs a Depth-First Search (DFS) from the specified node to explore
	 * its connected component in the graph.
	 * The method starts from the given node and traverses the graph deeply,
	 * visiting all connected nodes and marking them as visited. The nodes
	 * belonging to the current connected component are added to the `component` list.
	 * Steps:
	 * 1. Mark the current node as visited.
	 * 2. Add the current node to the component list.
	 * 3. For each neighboring node:
	 *    - If the neighbor has an edge with the current node (non-zero value in the matrix)
	 *      and has not been visited:
	 *      - Recursively perform DFS on the neighbor.
	 * @param matrix    The adjacency matrix representing the graph.
	 * @param node      The current node from which the DFS starts.
	 * @param visited   An array indicating whether each node has been visited.
	 * @param component A list to store nodes that belong to the current connected component.
	 */
	private static void dfs(int[][] matrix, int node, boolean[] visited, List<Integer> component) {
		visited[node] = true;
		component.add(node + 1);

		for (int neighbor = 0; neighbor < matrix.length; neighbor++) {
			if (matrix[node][neighbor] != 0 && !visited[neighbor]) {
				dfs(matrix, neighbor, visited, component);
			}
		}
	}

	/**
	 * Identifies and returns the bridges in the graph.
	 * A bridge in a graph is an edge whose removal increases the number of
	 * connected components. This method explores each edge in the graph
	 * to determine if it is a bridge by temporarily removing it and checking
	 * the number of connected components.
	 * Steps:
	 * 1. Create a working copy of the adjacency matrix.
	 * 2. Determine the initial number of connected components in the graph.
	 * 3. For each edge in the graph:
	 *    - Temporarily remove the edge from the working matrix.
	 *    - Determine the number of connected components without the edge.
	 *    - If the number of components increased, identify the edge as a bridge.
	 *    - Restore the edge to the working matrix.
	 *
	 *   @return A list containing the bridges of the graph.
	 *  */
	public List<List<Integer>> bridges() {
		List<List<Integer>> bridges = new ArrayList<>();
		int[][] workMatrix = adjacency.getArray();
		int componentsSize = components(workMatrix).size();
		for (int row = 0; row < nodeCount; row++) {
			for (int col = 0; col < nodeCount; col++) {
				if (workMatrix[row][col] == 1) {
					workMatrix[row][col] = 0;
					if (components(workMatrix).size() > componentsSize) {
						List<Integer> bruecke = new ArrayList<>();
						bruecke.add(Math.min(col + 1, row + 1));
						bruecke.add(Math.max(col + 1, row + 1));
						if (!bridges.contains(bruecke))
							bridges.add(bruecke);
					}
					workMatrix[row][col] = 1;
				}
			}
		}
		return bridges;
	}

	/**
	 * Identifies and returns the articulation points (or "cut vertices") of the graph.
	 * An articulation point in a graph is a node whose removal (along with its associated edges)
	 * increases the number of connected components. The method works by temporarily removing each
	 * nodex and its edges, and then checking if the number of connected components has increased.
	 *
	 *  @return A list of indices representing the articulation nodes in the graph.
	 *  */
	public List<Integer> articulations() {
		int componentsSize = components(adjacency.getArray()).size();
		ArrayList<Integer> artikulationen = new ArrayList<>();
		for(int aktknoten = 0; aktknoten < nodeCount; aktknoten++)
		{
			int[][] workMatrix = adjacency.getArray();
			for(int i = 0; i < nodeCount; i++)
			{
				workMatrix[aktknoten][i] = 0; // removes all edges from the node
				workMatrix[i][aktknoten] = 0;
			}
			//  stores the components of the new matrix and then compares if the new components are more than the original ones
			// if there are more components after than before the Node is an articulation
			List<List<Integer>> aktuelleKomponenten = components(workMatrix);
			if (aktuelleKomponenten.size() - 1 > componentsSize)
			{
				artikulationen.add(aktknoten + 1);
			}
		}
		return artikulationen;
	}

	/***
	 * Help Method to check if the graph is connected
	 */
	private boolean distanceMatrixIsNotConnected(){
		for (int row = 0; row < nodeCount; row++) {
			for (int col = 0; col < nodeCount; col++) {
				if (distanceMatrix.getValue(row,col) == INF) {
					return true;  // Der Graph ist nicht verbunden
				}
			}
		}
		return false;  // Der Graph ist verbunden
	}


}
