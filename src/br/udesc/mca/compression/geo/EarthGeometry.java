package br.udesc.mca.compression.geo;

import br.udesc.mca.compression.support.TrackPoint;

/**
 * Created by Maurice Perry: http://code.google.com/p/hiketools/
 * http://stackoverflow
 * .com/questions/3761786/douglas-peucker-shortest-arc-from-a
 * -point-to-a-circle-on-the-surface-of-a-sph
 */
public class EarthGeometry {
	private static final double R = 6371030.0;
	private static final double A = 6378137.0;
	private static final double A2 = A * A;
	private static final double B = 6356752.3142;
	private static final double B2 = B * B;

	private EarthGeometry() {
		throw new UnsupportedOperationException("Instanciation not allowed");
	}

	/**
	 * Returns the distance between two points, on the great circle.
	 * 
	 * @param p1
	 *            first point
	 * @param p2
	 *            second point
	 * @return the distance, in meters
	 */
	public static double distance(TrackPoint p1, TrackPoint p2) {
		double lat1 = Math.toRadians(p1.getLat());
		double lat2 = Math.toRadians(p2.getLat());
		double lon1 = Math.toRadians(p1.getLon());
		double lon2 = Math.toRadians(p2.getLon());
		double cosP = Math.cos(lat1);
		double sinP = Math.sin(lat1);
		double s2 = A2 * cosP * cosP + B2 * sinP * sinP;
		double s = Math.sqrt(s2);
		double sq1 = (lat1 - lat2) * (p1.getH() + A2 * B2 / (s * s2));
		double sq2 = (lon1 - lon2) * (p1.getH() + A2 / s) * cosP;
		return Math.sqrt(sq1 * sq1 + sq2 * sq2);
	}

	/**
	 * Calculates the distance from a point P to the great circle that passes by
	 * two other points A and B.
	 * 
	 * @param p
	 *            the point
	 * @param a
	 *            first point
	 * @param b
	 *            second point
	 * @return the distance, in meters
	 */
	public static double perpendicularDistance(TrackPoint p, TrackPoint a,
			TrackPoint b) {
		double lata = Math.toRadians(a.getLat());
		double lnga = Math.toRadians(a.getLon());
		double latb = Math.toRadians(b.getLat());
		double lngb = Math.toRadians(b.getLon());
		double latp = Math.toRadians(p.getLat());
		double lngp = Math.toRadians(p.getLon());
		double sinlata = Math.sin(lata);
		double coslata = Math.cos(lata);
		double sinlnga = Math.sin(lnga);
		double coslnga = Math.cos(lnga);
		double sinlatb = Math.sin(latb);
		double coslatb = Math.cos(latb);
		double sinlngb = Math.sin(lngb);
		double coslngb = Math.cos(lngb);
		double sinlatp = Math.sin(latp);
		double coslatp = Math.cos(latp);
		double sinlngp = Math.sin(lngp);
		double coslngp = Math.cos(lngp);
		double costh = sinlata * sinlatb + coslata * coslatb
				* (coslnga * coslngb + sinlnga * sinlngb);
		double sin2th = 1 - costh * costh;
		if (sin2th < 1.0E-20) {
			// a and b are very close; return distance from a to p
			double costhp = sinlata * sinlatp + coslata * coslatp
					* (coslnga * coslngp + sinlnga * sinlngp);
			return Math.acos(costhp) * (R + p.getH());
		}
		double num = sinlata
				* (coslatb * coslatp * coslngb * sinlngp - coslatb * coslatp
						* sinlngb * coslngp) + coslata * coslnga
				* (coslatb * sinlatp * sinlngb - sinlatb * coslatp * sinlngp)
				+ coslata * sinlnga
				* (sinlatb * coslatp * coslngp - coslatb * sinlatp * coslngb);
		double sinr = Math.abs(num) / Math.sqrt(sin2th);
		return (R + p.getH()) * Math.asin(sinr);
	}
}
