package br.udesc.mca.compression.time;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import br.udesc.mca.compression.entities.TrackPoint;
import br.udesc.mca.compression.entities.Vector3D;

public class ErrorMetrics {

	public static final int SED = 1;
	public static final int TIME_RATIO = 2;
	public static final int PERPENDICULAR_DISTANCE = 3;
	public static final int ENCLOSED_AREA = 4;

	public static final int EARTH_RADIUS = 6378137;
	public static final double FOCAL_LENGTH = 500;

	/**
	 * sed(A,B,C) = square root of ((x'B - xB)*(x'B - xB) + ((y'B - yB)*(y'B -
	 * yB)) x'B = xA + vXAC * (tb - ta) y'B = yA + vYAC * (tb - ta) vXAC = (xc -
	 * xa) / (tc - ta) vYAC = (yc - ya) / (tc - ta)
	 * 
	 * @param point
	 *            (B)
	 * @param beginingPoint
	 *            (A)
	 * @param endPoint
	 *            (C)
	 * @return
	 */
	public static double getSynchronizedEuclideanDistance(TrackPoint point,
			TrackPoint beginingPoint, TrackPoint endPoint) {

		double timeRatioA = endPoint.getTime().getTime()
				- beginingPoint.getTime().getTime();

		if (timeRatioA == 0)
			return 0;

		double velocityVectorX = (endPoint.getLon() - beginingPoint.getLon())
				/ (timeRatioA);

		double velocityVectorY = (endPoint.getLat() - beginingPoint.getLat())
				/ (timeRatioA);

		double predictedX = beginingPoint.getLon()
				+ velocityVectorX
				* (point.getTime().getTime() - beginingPoint.getTime()
						.getTime());

		double predictedY = beginingPoint.getLat()
				+ velocityVectorY
				* (point.getTime().getTime() - beginingPoint.getTime()
						.getTime());

		TrackPoint predicted = new TrackPoint(predictedY, predictedX, 0,
				new Date(), 0, 0);

		double sed = ErrorMetrics.getDistanceOnEarth(point, predicted);

		return sed;
	}

	/**
	 * 
	 * @param point
	 * @param beginingPoint
	 * @param endPoint
	 * @return distance in meters
	 */
	public static double getTimeRatioDistance(TrackPoint point,
			TrackPoint beginingPoint, TrackPoint endPoint) {

		double timeIntervalE = endPoint.getTime().getTime()
				- beginingPoint.getTime().getTime();

		if (timeIntervalE == 0)
			return 0;
		else {
			double timeIntervalI = point.getTime().getTime()
					- beginingPoint.getTime().getTime();
			double timeRatio = timeIntervalI / timeIntervalE;

			double xI = beginingPoint.getLon()
					+ (timeRatio * (endPoint.getLon() - beginingPoint.getLon()));
			double yI = beginingPoint.getLat()
					+ (timeRatio * (endPoint.getLat() - beginingPoint.getLat()));

			TrackPoint predicted = new TrackPoint(yI, xI, 0, new Date(), 0, 0);

			double distance = ErrorMetrics.getDistanceOnEarth(point, predicted);

			return distance;
		}
	}

	/**
	 * Calculates the distance from a point P to the great circle that passes by
	 * two other points A and B.
	 * 
	 * Taken from the Perpendicular Distance Calculator Project
	 * 
	 * @see <a
	 *      href="http://biodiversityinformatics.amnh.org/open_source/pdc/documentation.php">http://biodiversityinformatics.amnh.org/open_source/pdc/documentation.php</a>
	 * 
	 * @param p
	 *            , point P
	 * @param a
	 *            , first point
	 * @param b
	 *            , second point
	 * 
	 * @return the distance, in meters
	 */
	public static double getPerpendicularDistance2(TrackPoint p, TrackPoint a,
			TrackPoint b) {

		/*
		 * Convert Point 1 (Great Circle Reference)
		 */
		double x1 = ErrorMetrics.EARTH_RADIUS
				* Math.cos(longitudeTo2pi(a.getLon()))
				* Math.sin(getColatitude(a.getLat()));
		double y1 = ErrorMetrics.EARTH_RADIUS
				* Math.sin(longitudeTo2pi(a.getLon()))
				* Math.sin(getColatitude(a.getLat()));
		double z1 = ErrorMetrics.EARTH_RADIUS
				* Math.cos(getColatitude(a.getLat()));

		/*
		 * Convert Point 2 (Great Circle Reference)
		 */
		double x2 = ErrorMetrics.EARTH_RADIUS
				* Math.cos(longitudeTo2pi(b.getLon()))
				* Math.sin(getColatitude(b.getLat()));
		double y2 = ErrorMetrics.EARTH_RADIUS
				* Math.sin(longitudeTo2pi(b.getLon()))
				* Math.sin(getColatitude(b.getLat()));
		double z2 = ErrorMetrics.EARTH_RADIUS
				* Math.cos(getColatitude(b.getLat()));

		/*
		 * Convert Point 3 ( The sighting )
		 */
		double x3 = ErrorMetrics.EARTH_RADIUS
				* Math.cos(longitudeTo2pi(p.getLon()))
				* Math.sin(getColatitude(p.getLat()));
		double y3 = ErrorMetrics.EARTH_RADIUS
				* Math.sin(longitudeTo2pi(p.getLon()))
				* Math.sin(getColatitude(p.getLat()));
		double z3 = ErrorMetrics.EARTH_RADIUS
				* Math.cos(getColatitude(p.getLat()));

		/*
		 * Cross normalize Point 1 and Point 2 = N
		 */
		double Nx = y1 * z2 - z1 * y2;
		double Ny = z1 * x2 - x1 * z2;
		double Nz = x1 * y2 - y1 * x2;
		double length = Math.sqrt(Nx * Nx + Ny * Ny + Nz * Nz);
		Nx = Nx / length;
		Ny = Ny / length;
		Nz = Nz / length;

		/*
		 * dot product N with Point 3
		 */
		double angleNOP3 = Nx * x3 + Ny * y3 + Nz * z3;

		/*
		 * Normalize N.P3
		 */
		length = Math.sqrt(x3 * x3 + y3 * y3 + z3 * z3);
		angleNOP3 = angleNOP3 / length;

		/*
		 * Calculate final distance
		 */
		double pDistance = Math.abs((Math.PI / 2.0) - Math.acos(angleNOP3));
		return pDistance * EARTH_RADIUS;
	}

	/**
	 * 
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static double getPerpendicularDistance(TrackPoint p, TrackPoint a,
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

			double perpendicularD = Math.acos(costhp)
					* (ErrorMetrics.EARTH_RADIUS + p.getH());

			if (Double.isNaN(perpendicularD))
				perpendicularD = 0;

			return perpendicularD;
		}
		double num = sinlata
				* (coslatb * coslatp * coslngb * sinlngp - coslatb * coslatp
						* sinlngb * coslngp) + coslata * coslnga
				* (coslatb * sinlatp * sinlngb - sinlatb * coslatp * sinlngp)
				+ coslata * sinlnga
				* (sinlatb * coslatp * coslngp - coslatb * sinlatp * coslngb);
		double sinr = Math.abs(num) / Math.sqrt(sin2th);

		double perpendicularD = (ErrorMetrics.EARTH_RADIUS + p.getH())
				* Math.asin(sinr);

		if (Double.isNaN(perpendicularD))
			perpendicularD = 0;

		return perpendicularD;
	}

	/**
	 * 
	 * @param compressed
	 * @param original
	 * @return velocity avg diff, the absolute value.
	 */
	public static double getDiffVelocityAvg(TrackPoint[] compressed,
			TrackPoint[] original) {

		double velocity = 0;
		double count = 0;
		for (int i = 0; i < original.length; i++) {
			if (i < original.length - 1) {
				count++;
				velocity += ErrorMetrics.getVelocity(original[i],
						original[i + 1]);
			}
		}

		double originalVelocityAvg = velocity / count;

		velocity = 0;
		count = 0;
		for (int i = 0; i < compressed.length; i++) {
			if (i < compressed.length - 1) {
				count++;
				velocity += ErrorMetrics.getVelocity(compressed[i],
						compressed[i + 1]);
			}
		}

		double compressedVelocityAvg = velocity / count;

		return Math.abs(originalVelocityAvg - compressedVelocityAvg);
	}

	/**
	 * Get the average velocity of an object between two points;
	 * 
	 * @param pointA
	 * @param pointB
	 * @return Velocity in meters/seconds
	 */
	public static double getVelocity(TrackPoint pointA, TrackPoint pointB) {

		double timeInterval = pointB.getTime().getTime()
				- pointA.getTime().getTime();

		if (timeInterval == 0)
			return 0;

		timeInterval = timeInterval / 1000; // Milliseconds to Seconds
		double distance = ErrorMetrics.getDistanceOnEarth(pointA, pointB);

		return distance / timeInterval;
	}

	/**
	 * 
	 * @param trajectory
	 * @param errorType
	 * @return
	 */
	public static double getAppliedError(TrackPoint[] original,
			TrackPoint[] compressed, int errorType) {

		double error = 0;
		List<TrackPoint> missed = new ArrayList<TrackPoint>();

		TrackPoint start = null;
		TrackPoint end = null;

		int startIndex = 0;

		for (int i = 0; i < original.length; i++) {

			if (ErrorMetrics.isOnTrack(compressed, original[i], startIndex)) {

				if (missed.size() > 0) {

					end = original[i];

					if (errorType == ErrorMetrics.ENCLOSED_AREA) {

						missed.add(0, start);
						missed.add(end);

						error += ErrorMetrics.getEnclosedArea(missed
								.toArray(new TrackPoint[missed.size()]));

					} else {

						for (Iterator<TrackPoint> iterator = missed.iterator(); iterator
								.hasNext();) {

							TrackPoint missedPoint = (TrackPoint) iterator
									.next();

							switch (errorType) {
							case ErrorMetrics.SED:
								error += ErrorMetrics
										.getSynchronizedEuclideanDistance(
												missedPoint, start, end);
								break;

							case ErrorMetrics.TIME_RATIO:
								error += ErrorMetrics.getTimeRatioDistance(
										missedPoint, start, end);
								break;

							case ErrorMetrics.PERPENDICULAR_DISTANCE:
								error += ErrorMetrics.getPerpendicularDistance(
										missedPoint, start, end);
								break;

							default:
								break;
							}
						}
					}

					start = null;
					end = null;
					missed = new ArrayList<TrackPoint>();
				}

			} else {
				if (start == null)
					start = original[i - 1];
				missed.add(original[i]);
			}
		}

		return error;
	}

	private static boolean isOnTrack(TrackPoint[] track, TrackPoint p,
			int startIndex) {
		for (int i = startIndex; i < track.length; i++) {
			if (track[i].getSequence() == p.getSequence()) {
				return true;
			}

			if (track[i].getSequence() > p.getSequence())
				break;
		}

		return false;
	}

	/**
	 * 
	 * @param trajectory
	 * @param errorType
	 * @return
	 */
	public static double getMaxAppliedError(TrackPoint[] original,
			TrackPoint[] compressed, int errorType) {

		double maxError = 0;
		double localError = 0;

		List<TrackPoint> missed = new ArrayList<TrackPoint>();

		TrackPoint start = null;
		TrackPoint end = null;

		int startIndex = 0;
		int contError = 0;

		for (int i = 0; i < original.length; i++) {

			if (ErrorMetrics.isOnTrack(compressed, original[i], startIndex)) {

				if (missed.size() > 0) {

					end = original[i];

					if (errorType == ErrorMetrics.ENCLOSED_AREA) {

						missed.add(0, start);
						missed.add(end);

						localError = ErrorMetrics.getEnclosedArea(missed
								.toArray(new TrackPoint[missed.size()]));
						if (localError > maxError)
							maxError = localError;

					} else {

						for (Iterator<TrackPoint> iterator = missed.iterator(); iterator
								.hasNext();) {

							TrackPoint missedPoint = (TrackPoint) iterator
									.next();

							switch (errorType) {
							case ErrorMetrics.SED:

								localError = ErrorMetrics
										.getSynchronizedEuclideanDistance(
												missedPoint, start, end);

								if (localError > maxError)
									maxError = localError;
								break;

							case ErrorMetrics.TIME_RATIO:
								localError = ErrorMetrics.getTimeRatioDistance(
										missedPoint, start, end);
								if (localError > maxError)
									maxError = localError;
								break;

							case ErrorMetrics.PERPENDICULAR_DISTANCE:
								localError = ErrorMetrics
										.getPerpendicularDistance(missedPoint,
												start, end);

								if (localError > maxError)
									maxError = localError;
								break;

							default:
								break;
							}
						}
					}

					start = null;
					end = null;
					missed = new ArrayList<TrackPoint>();
				}

			} else {
				if (start == null)
					start = original[i - 1];
				missed.add(original[i]);
			}
		}

		return maxError;
	}

	/**
	 * Calculate the distance on Earth between two lag/long.
	 * 
	 * This uses the haversine formula to calculate the distance between two
	 * lat/long points.
	 * 
	 * @see <a
	 *      href="http://www.movable-type.co.uk/scripts/latlong.html">http://www.movable-type.co.uk/scripts/latlong.html</a>
	 * 
	 * @param ta
	 * @param tb
	 * @return Distance in meters
	 */
	public static double getDistanceOnEarth(TrackPoint ta, TrackPoint tb) {

		double R = ErrorMetrics.EARTH_RADIUS;
		double latA = ErrorMetrics.deg2rad(ta.getLat()), lonA = ErrorMetrics
				.deg2rad(ta.getLon());
		double latB = ErrorMetrics.deg2rad(tb.getLat()), lonB = ErrorMetrics
				.deg2rad(tb.getLon());
		double latDiff = latB - latA;
		double lonDiff = lonB - lonA;

		double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
				+ Math.cos(latA) * Math.cos(latB) * Math.sin(lonDiff / 2)
				* Math.sin(lonDiff / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;

		return d;
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static TrackPoint getMidPoint(TrackPoint a, TrackPoint b) {

		double latA = ErrorMetrics.deg2rad(a.getLat());
		double lonA = ErrorMetrics.deg2rad(a.getLon());
		double latB = ErrorMetrics.deg2rad(b.getLat());
		double lonB = ErrorMetrics.deg2rad(b.getLon());

		double Bx = Math.cos(latB) * Math.cos(lonB - lonA);
		double By = Math.cos(latB) * Math.sin(lonB - lonA);

		double latC = Math.atan2(
				Math.sin(latA) + Math.sin(latB),
				Math.sqrt((Math.cos(latA) + Bx) * (Math.cos(latA) + Bx) + By
						* By));
		double lonC = lonA + Math.atan2(By, Math.cos(latA) + Bx);

		TrackPoint t = new TrackPoint(latC, lonC, 0, null, 0, 0);

		return t;
	}

	/**
	 * Translates a longitude, expressed in decimal degrees, into radians
	 * between the value of 0 and 2PI
	 * 
	 * @param longitude
	 *            Longitude to be translated into 2PI
	 * @return Longitude expressed in radians, relative to 2PI
	 */
	public static double longitudeTo2pi(double longitude) {
		if (longitude < 0)
			return Math.toRadians(360 + longitude);
		return Math.toRadians(longitude);
	}

	/**
	 * Calculates the co-latitude for a given Latitude, expressed in decimal
	 * degrees.
	 * 
	 * @param latitude
	 *            Latitude to be converted
	 * @return (90 - latitude) expressed in radians
	 */
	public static double getColatitude(double latitude) {
		return Math.toRadians(90.0 - latitude);
	}

	/**
	 * Convert degrees in radians
	 * 
	 * @param deg
	 * @return rad
	 */
	public static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/**
	 * Convert radians in degrees
	 * 
	 * @param rad
	 * @return deg
	 */
	public static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	/**
	 * Get the enclosed area of a sequence of track points.
	 * 
	 * @param points
	 * @return Enclosed Area in m2
	 * @see http://www.icmu.org/icmu2012/papers/FP-6.pdf
	 */
	public static double getEnclosedArea(TrackPoint[] points) {

		double enclosedArea = 0;

		List<TrackPoint> list = new ArrayList<TrackPoint>();
		List<TrackPoint> original = new ArrayList<TrackPoint>();
		List<TrackPoint[]> intersectionSegments = new ArrayList<TrackPoint[]>();

		for (int i = 0; i < points.length; i++) {

			list.add(points[i]); // Original point.
			original.add(points[i]); // Original point.

			for (int j = 0; j < i - 1 && i > 1; j++) {
				if (i + 1 >= points.length)
					continue;
				TrackPoint p = ErrorMetrics.getTheIntersection(points[i],
						points[i + 1], points[j], points[j + 1]);
				if (p != null) {
					p.setIntersection(true);
					if (!ErrorMetrics.alreadyHasIntersectionOnSegment(
							points[i], points[i + 1], intersectionSegments)) {
						list.add(i + 1, p);
						TrackPoint[] segment = { points[i], points[i + 1] };
						intersectionSegments.add(segment);
					}
					if (!ErrorMetrics.alreadyHasIntersectionOnSegment(
							points[j], points[j + 1], intersectionSegments)) {
						TrackPoint[] segment = { points[j], points[j + 1] };
						intersectionSegments.add(segment);
						list.add(j + 1, p);
					}
				}
			}
		}

		int anchor = 0;
		int count = 0;
		for (Iterator<TrackPoint> iterator = list.iterator(); iterator
				.hasNext();) {
			TrackPoint trackPoint = (TrackPoint) iterator.next();

			if (trackPoint.isIntersection()) {
				enclosedArea += ErrorMetrics.getAreaOfPolygon(list.subList(
						anchor, count));
				anchor = count;
			}

			count++;
		}
		enclosedArea += ErrorMetrics.getAreaOfPolygon(list.subList(anchor,
				count));

		if (enclosedArea > ErrorMetrics.getAreaOfPolygon(original)) {
			System.out.println(enclosedArea);
			System.out.println(ErrorMetrics.getAreaOfPolygon(original));
			System.out.println("=======");
		}

		return enclosedArea;
	}

	public static boolean alreadyHasIntersectionOnSegment(TrackPoint a,
			TrackPoint b, List<TrackPoint[]> listIntersections) {

		for (Iterator<TrackPoint[]> iterator = listIntersections.iterator(); iterator
				.hasNext();) {
			TrackPoint[] trackPoints = (TrackPoint[]) iterator.next();

			if ((trackPoints[0].equals(a) && trackPoints[1].equals(b))
					|| (trackPoints[1].equals(a) && trackPoints[0].equals(b))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Calculate the area of a polygon formed by a sequence of track points.
	 * 
	 * @param points
	 * @return area in km2.
	 * @see: http://www.mathopenref.com/coordpolygonarea2.html
	 */
	private static double getAreaOfPolygon(List<TrackPoint> points) {

		double area = 0;

		TrackPoint[] pointsArray = points
				.toArray(new TrackPoint[points.size()]);

		// Accumulates area in the loop
		int j = points.size() - 1; // The last vertex is the 'previous' one to
									// the first

		for (int i = 0; i < points.size(); i++) {
			// Km
			double projectedXI = (pointsArray[i].getProjectedX());
			double projectedYI = (pointsArray[i].getProjectedY());

			// Km
			double projectedXJ = (pointsArray[j].getProjectedX());
			double projectedYJ = (pointsArray[j].getProjectedY());

			area += (projectedXI * projectedYJ) - (projectedYI * projectedXJ);
			j = i; // j is previous vertex to i
		}

		return Math.abs(area / 2); // Return could be negative if polygon is
									// formed in a counterclock direction.
	}

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @param q1
	 * @param q2
	 * @return boolean
	 */
	public static TrackPoint getIntersectionPoint(TrackPoint p1, TrackPoint p2,
			TrackPoint q1, TrackPoint q2) {

		double A1 = p2.getProjectedY() - p1.getProjectedY();
		double B1 = p1.getProjectedX() - p2.getProjectedX();
		double C1 = A1 * p1.getProjectedX() + B1 * p1.getProjectedY();

		double A2 = q2.getProjectedY() - q1.getProjectedY();
		double B2 = q1.getProjectedX() - q2.getProjectedX();
		double C2 = A2 * q1.getProjectedX() + B2 * q1.getProjectedY();

		double det = A1 * B2 - A2 * B1;

		if (det == 0) {
			// Lines are parallel
			return null;
		} else {
			double x = (B2 * C1 - B1 * C2) / det;
			double y = (A1 * C2 - A2 * C1) / det;

			System.out.println(p1);
			System.out.println(p1.getProjectedX() + "," + p1.getProjectedY());
			System.out.println("=========");
			System.out.println(p2);
			System.out.println(p2.getProjectedX() + "," + p2.getProjectedY());

			double minX = (p1.getProjectedX() < p2.getProjectedX()) ? p1
					.getProjectedX() : p2.getProjectedX();
			double maxX = (p1.getProjectedX() > p2.getProjectedX()) ? p1
					.getProjectedX() : p2.getProjectedX();
			double minY = (p1.getProjectedY() < p2.getProjectedY()) ? p1
					.getProjectedY() : p2.getProjectedY();
			double maxY = (p1.getProjectedY() > p2.getProjectedY()) ? p1
					.getProjectedY() : p2.getProjectedY();

			if (minX < x && x < maxX && minY < y && y < maxY) {
				return getTheIntersection(p1, p2, q1, q2);
			}
		}

		return null;
	}

	/**
	 * Returns the point of intersection of two paths each defined by point
	 * pairs or start point and bearing.
	 *
	 * @see http
	 *      ://www.movable-type.co.uk/scripts/latlong-vectors.html#intersection
	 * 
	 * @param {LatLon} path1start - Start point of first path.
	 * @param {LatLon|number} path1brngEnd - End point of first path or initial
	 *        bearing from first start point.
	 * @param {LatLon} path2start - Start point of second path.
	 * @param {LatLon|number} path2brngEnd - End point of second path or initial
	 *        bearing from second start point.
	 * @returns {LatLon} Destination point (null if no unique intersection
	 *          defined)
	 *
	 * @example var p1 = LatLon(51.8853, 0.2545), brng1 = 108.55; var p2 =
	 *          LatLon(49.0034, 2.5735), brng2 = 32.44; var pInt =
	 *          LatLon.intersection(p1, brng1, p2, brng2); // pInt.toString():
	 *          50.9076°N, 004.5086°E
	 */
	public static TrackPoint getTheIntersection(TrackPoint p1, TrackPoint p2,
			TrackPoint q1, TrackPoint q2) {

		Vector3D c1, c2;

		c1 = p1.toVector().cross(p2.toVector());
		c2 = q1.toVector().cross(q2.toVector());

		Vector3D intersection = c1.cross(c2);

		// TODO: Will not work for some lat/long. Greater distances, specially.
		TrackPoint pIntersection = intersection.toLatLonS();
		double minorLatP = (p1.getLat() < p2.getLat()) ? p1.getLat() : p2
				.getLat();
		double maxLatP = (p1.getLat() > p2.getLat()) ? p1.getLat() : p2
				.getLat();
		double minorLngP = (q1.getLat() < q2.getLat()) ? q1.getLat() : q2
				.getLat();
		double maxLngP = (q1.getLat() > q2.getLat()) ? q1.getLat() : q2
				.getLat();

		if (minorLatP < pIntersection.getLat()
				&& pIntersection.getLat() < maxLatP
				&& minorLngP < pIntersection.getLon()
				&& pIntersection.getLon() < maxLngP) {
			return pIntersection;
		}

		return null;
	};

}
