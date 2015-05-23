package br.udesc.mca.trajectory.similarity.dtw;

public class DynamicTimeWarping2 {

	private float[] seq1;
	private float[] seq2;
	private int[][] warpingPath;

	private int n;
	private int m;
	private int K;

	private double warpingDistance;

	public DynamicTimeWarping2(float[] sample, float[] template) {
		this.seq1 = sample;
		this.seq2 = template;

		this.n = this.seq1.length;
		this.m = this.seq2.length;
		this.K = 1;

		this.warpingPath = new int[n + m][2];
		; // max(n, m) <= K < n + m
		this.warpingDistance = 0.0;

		this.compute();
	}

	public void compute() {
		double accumulatedDistance = 0.0;

		double[][] d = new double[n][m]; // local distances
		double[][] D = new double[n][m]; // global distances

		// Calculate local matrix
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.m; j++) {
				d[i][j] = distanceBetween(this.seq1[i], this.seq2[j]);
			}
		}

		// Calculate global matrix

		D[0][0] = d[0][0];

		for (int i = 1; i < n; i++) {
			D[i][0] = d[i][0] + D[i - 1][0];
		}

		for (int j = 1; j < m; j++) {
			D[0][j] = d[0][j] + D[0][j - 1];
		}

		for (int i = 1; i < n; i++) {
			for (int j = 1; j < m; j++) {
				accumulatedDistance = Math.min(
						Math.min(D[i - 1][j], D[i - 1][j - 1]), D[i][j - 1]);
				accumulatedDistance += d[i][j];
				D[i][j] = accumulatedDistance;
			}
		}
		accumulatedDistance = D[n - 1][m - 1];

		int i = n - 1;
		int j = m - 1;
		int minIndex = 1;

		warpingPath[K - 1][0] = i;
		warpingPath[K - 1][1] = j;

		while ((i + j) != 0) {
			if (i == 0) {
				j -= 1;
			} else if (j == 0) {
				i -= 1;
			} else { // i != 0 && j != 0
				double[] array = { D[i - 1][j], D[i][j - 1], D[i - 1][j - 1] };
				minIndex = this.getIndexOfMinimum(array);

				if (minIndex == 0) {
					i -= 1;
				} else if (minIndex == 1) {
					j -= 1;
				} else if (minIndex == 2) {
					i -= 1;
					j -= 1;
				}
			} // end else
			K++;
			warpingPath[K - 1][0] = i;
			warpingPath[K - 1][1] = j;
		} // end while
		warpingDistance = accumulatedDistance / K;

		this.reversePath(warpingPath);
	}

	/**
	 * Changes the order of the warping path (increasing order)
	 *
	 * @param path
	 *            the warping path in reverse order
	 */
	private void reversePath(int[][] path) {
		int[][] newPath = new int[K][2];
		for (int i = 0; i < K; i++) {
			for (int j = 0; j < 2; j++) {
				newPath[i][j] = path[K - i - 1][j];
			}
		}
		warpingPath = newPath;
	}

	/**
	 * Returns the warping distance
	 *
	 * @return
	 */
	public double getDistance() {
		return warpingDistance;
	}

	/**
	 * Computes a distance between two points
	 *
	 * @param p1
	 *            the point 1
	 * @param p2
	 *            the point 2
	 * @return the distance between two points
	 */
	private double distanceBetween(double p1, double p2) {
		return (p1 - p2) * (p1 - p2);
	}

	/**
	 * Finds the index of the minimum element from the given array
	 *
	 * @param array
	 *            the array containing numeric values
	 * @return the min value among elements
	 */
	protected int getIndexOfMinimum(double[] array) {
		int index = 0;
		double val = array[0];

		for (int i = 1; i < array.length; i++) {
			if (array[i] < val) {
				val = array[i];
				index = i;
			}
		}
		return index;
	}

	public String toString() {
		String retVal = "Warping Distance: " + warpingDistance + "\n";
		retVal += "Warping Path: {";
		for (int i = 0; i < K; i++) {
			retVal += "(" + warpingPath[i][0] + ", " + warpingPath[i][1] + ")";
			retVal += (i == K - 1) ? "}" : ", ";

		}
		return retVal;
	}

	public static void main(String[] args) {
		float[] n1 = { 1, 2, 3, 4 };
		float[] n2 = { 4, 5, 6 };
		// float[] n1 = { 1, 1, 2, 3, 3, 4, 4 };
		// float[] n2 = { 1, 2, 3, 3, 4, 5 };

		DynamicTimeWarping2 dtw = new DynamicTimeWarping2(n1, n2);
		System.out.println(dtw);
	}

}
