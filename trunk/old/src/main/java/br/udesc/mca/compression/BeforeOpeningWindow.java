package br.udesc.mca.compression;

import br.udesc.mca.compression.geo.EarthGeometry;
import br.udesc.mca.compression.support.TrackPoint;

public class BeforeOpeningWindow {
	private static double maxError;

	private BeforeOpeningWindow() {
		throw new UnsupportedOperationException("Instanciation not allowed");
	}

	/**
	 * Compress a track by removing points, using the Before Opening Window
	 * algorithm.
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
			int end = track.length;
			boolean[] keep = new boolean[end];

			beforeOpeningWindow(track, keep);
			int count = 0;
			for (i = 0; i < end; ++i) {
				if (keep[i]) {
					++count;
				}
			}

			TrackPoint[] result = new TrackPoint[count];
			int k = 0;
			for (i = 0; i < end; ++i) {
				if (keep[i]) {
					result[k] = track[i];
					k++;
				}
			}
			return result;
		}
		return track;
	}

	private static void beforeOpeningWindow(TrackPoint[] track, boolean[] keep) {
		double dist;

		keep[0] = true;
		int anchor = 0;
		int floater = 2;

		int end = track.length - 1;
		TrackPoint anchorPt = track[anchor];
		int index;
		for (; floater <= end; floater++) {
			for (index = anchor + 1; index < floater; index++) {
				dist = EarthGeometry.perpendicularDistance(track[index],
						anchorPt, track[floater]);
				if (dist > maxError) {
					anchor = floater - 1;
					keep[anchor] = true;
					anchorPt = track[anchor];
					// floater already is +1. The other +1 comes from the for
					break;
				}
			}
		}
		keep[end] = true;
	}
}
