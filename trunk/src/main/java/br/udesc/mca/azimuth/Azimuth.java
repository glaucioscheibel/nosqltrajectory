package br.udesc.mca.azimuth;

import java.text.DecimalFormat;

// http://www.movable-type.co.uk/scripts/latlong.html
public final class Azimuth {

	// Raio de curvatura da terra usado para achar distâncias
	// fonte http://en.wikipedia.org/wiki/Earth_radius
	public static int EARTH_RADIUS_KM = 6371;

	/**
	 * Implementação do cálculo de distância de Haversine entre dois pontos
	 */
	// http://www.movable-type.co.uk/scripts/latlong.html
	// Azimuth.EARTH_RADIUS_KM = raio de curvatura da terra
	// Δlat = lat2− lat1
	// Δlong = long2− long1
	// a = sin²(Δlat/2) + cos(lat1).cos(lat2).sin²(Δlong/2)
	// c = 2.atan2(√a, √(1−a))
	// d = R.c
	public static double calculateLengthInKM(double lat1, double lon1,
			double lat2, double lon2) {

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2)
				* Math.sin(lonDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = Azimuth.EARTH_RADIUS_KM * c;

		return distance;
	}

	/**
	 * Implementação de azimute a frente
	 */
	public static double azimuth(double lat1, double lon1, double lat2,
			double lon2) {

		DecimalFormat df = new DecimalFormat("0.0000000000000000000");

		double phiLat1 = Math.toRadians(lat1);
		double phiLat2 = Math.toRadians(lat2);
		double deltaLon = new Double(df.format(Math.toRadians(lon2 - lon1)))
				.doubleValue();

		double y = Math.sin(deltaLon) * Math.cos(phiLat2);
		double x = Math.cos(phiLat1) * Math.cos(phiLat2) - Math.sin(phiLat1)
				* Math.cos(phiLat2) * Math.cos(deltaLon);
		double azimuth = Math.atan2(y, x);

		return (Math.toDegrees(azimuth) + 360) % 360;

	}
}
