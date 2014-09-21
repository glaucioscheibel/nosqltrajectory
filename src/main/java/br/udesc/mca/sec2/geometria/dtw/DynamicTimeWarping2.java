package br.udesc.mca.sec2.geometria.dtw;

public class DynamicTimeWarping2 {

	private double[][] dtw;
	private double[] x;
	private double[] y;
	private int n;
	private int m;

	public DynamicTimeWarping2(double x[], double y[]) {
		this.x = x;
		this.y = y;
		this.n = x.length;
		this.m = y.length;
		this.dtw = new double[this.n][this.m];
	}

	public double[][] calculateLocalCostMatrix() {
		double c[][] = new double[this.n][this.m];

		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.m; j++) {
				c[i][j] = distanceBetween(this.x[i], this.y[j]);
			}
		}
		return c;
	}

	public void accumulatedCostMatrix(double x[], double y[], double c[][]) {
		this.dtw[0][0] = 0;

		for (int i = 1; i < n; i++) {
			this.dtw[i][1] = this.dtw[i - 1][1] + c[i][1];
		}

		for (int j = 1; j < m; j++) {
			this.dtw[1][j] = c[1][j - 1] + c[1][j];
		}

		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.m; j++) {
				this.dtw[i][j] = c[i][j]
						+ Math.min(Math.min(this.dtw[i - 1][j],
								this.dtw[i][j - 1]), this.dtw[i - 1][j - 1]);
			}
		}
	}

	public void optimalWarpingPath() {

	}

	protected double distanceBetween(double p1, double p2) {
		return (p1 - p2) * (p1 - p2);
	}

}
