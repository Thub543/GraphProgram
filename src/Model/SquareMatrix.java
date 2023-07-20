package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SquareMatrix {

	private final int[][] matrix;

	public SquareMatrix(int[][] matrix) {
		if (matrix.length != matrix[0].length)
			throw new IllegalArgumentException("Matrix must be square");

		this.matrix = matrix;
	}
	private SquareMatrix(int dimension) {
		this.matrix = new int[dimension][dimension];
	}


	public int getValue(int row, int col){
		if (row < 0 || row >= matrix.length || col < 0 || col >= matrix.length)
			throw new IllegalArgumentException("Matrix must be square");

		return this.matrix[row][col];
	}

	public SquareMatrix multiplication(SquareMatrix other) {
		int dimension = getDimension();
		int[][] result = new int[dimension][dimension];
		for (int row = 0; row < dimension; ++row) {
			for (int col = 0; col < dimension; ++col) {
				for (int i = 0; i < dimension; ++i) {
					result[row][col] += matrix[row][i] * other.matrix[i][col];
				}
			}
		}
		return new SquareMatrix(result);
	}

	public SquareMatrix pow(int power) {
		SquareMatrix result = new SquareMatrix(matrix);
		if (power < 1)
			throw new IllegalArgumentException("power must be >= 1");

		for (int i = 1; i < power; i++) {
			result = multiplication(result);
		}
		return result;
	}

	public int getDimension(){
		return this.matrix.length;
	}

	public int[][] getArray(){
		int[][] result = new int[matrix.length][matrix.length];
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix.length; col++) {
				result[row][col] = this.matrix[row][col];
			}
		}
		return result;
	}

	public static SquareMatrix fromCSV(String path) throws GraphException {
		String zeile;
		String[] zeilenTeile;
		int nodeCount = getCsvLength(path);
		int[][] result = new int[nodeCount][nodeCount];

		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			zeile = br.readLine();
			int row = 0;
			while (zeile != null){
				zeilenTeile = zeile.trim().split(";");
				for (int i = 0; i < zeilenTeile.length; i++){
					result[row][i] = Integer.parseInt(zeilenTeile[i]);
				}
				row++;
				zeile = br.readLine();
			}
		}
		catch (IOException e){
			throw new GraphException(e.getMessage());
		}
		return new SquareMatrix(result);
	}
	private static int getCsvLength(String path) throws GraphException {
		String zeile;
		String[] zeilenTeile;

		int count = 0;

		try(BufferedReader br = new BufferedReader(new FileReader(path))){

			zeile = br.readLine();
			zeilenTeile = zeile.trim().split(";");

			count = zeilenTeile.length;

		} catch (IOException e){
			throw new GraphException("Error at getCsvLength(): " + "Input path: "+ path +": "+e.getMessage());
		}
		return count;
	}
}
