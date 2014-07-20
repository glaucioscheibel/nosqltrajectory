package br.udesc.mca.compression;

import br.udesc.mca.compression.geo.EarthGeometry;
import br.udesc.mca.compression.support.TrackPoint;

public class DouglasPeuckerNlogN {
	private static double maxError;

	public DouglasPeuckerNlogN() {
		throw new UnsupportedOperationException("Instanciation not allowed");
	}

	/**
	 * Compress a track by removing points, using the Douglas-Peucker algorithm.
	 * 
	 * @param track
	 *            points of the track
	 * @param epsilon
	 *            tolerance, in meters
	 * @return the points of the compressed track
	 */
	public static TrackPoint[] compress(TrackPoint[] track, double epsilon) {
		if (track.length > 2) {
			int i;
			maxError = epsilon;
			int end = track.length - 1;
			boolean[] keep = new boolean[end + 1];
			keep[0] = true;
			keep[end] = true;
			douglasPeuckerNlogN(track, 0, end, keep);
			int count = 0;
			for (i = 0; i <= end; ++i) {
				if (keep[i]) {
					++count;
				}
			}

			TrackPoint[] result = new TrackPoint[count];
			int k = 0;
			for (i = 0; i <= end; ++i) {
				if (keep[i]) {
					result[k] = track[i];
					k++;
				}
			}
			return result;
		}
		return track;
	}

	static Integer[] stack; /* recursion stack */
	static int topo; /* recursion stack pointer */

	public static void douglasPeuckerNlogN(TrackPoint[] V, int i, int j,
			boolean[] R) { // main
		int splitPos = 0;
		double tmp, dist;
		int k;

		R[i] = true; // "i" de V[i]

		stack = new Integer[V.length / 2];
		topo = -1;
		Stack_Push(j);
		do {
			dist = -1;
			j = stack[topo];

			if (i + 1 < j) {
				for (k = i + 1; k < j; k++) {
					tmp = EarthGeometry.perpendicularDistance(V[k], V[i], V[j]);
					if (tmp > dist) {
						dist = tmp; /* record the maximum */
						splitPos = k;
					}
				}
			}
			if (dist > maxError) {
				Stack_Push(splitPos);
			} else {
				R[j] = true; // "j" de V[j] /* output segment Vi to Vtop */
				i = Stack_Pop();
			}
		} while (!Stack_EmptyQ());
	}

	private static void Stack_Push(int e) { /* push element onto stack */
		stack[++topo] = e;
	}

	private static int Stack_Pop() { /* pop element from stack (zero if none) */
		return stack[topo--];
	}

	private static boolean Stack_EmptyQ() { /* Is stack empty? */
		return topo < 0;
	}
}
