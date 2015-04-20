package br.udesc.mca.azimuth;

import static org.apache.commons.math3.util.FastMath.atan2;
import static org.apache.commons.math3.util.FastMath.cos;
import static org.apache.commons.math3.util.FastMath.sin;
import static org.apache.commons.math3.util.FastMath.sqrt;
import static org.apache.commons.math3.util.FastMath.toDegrees;
import static org.apache.commons.math3.util.FastMath.toRadians;

/**
 * Classe que trabalha com a questão do cálculo de Azimute e Distância em KM.
 * Com base no trabalho em: http://www.movable-type.co.uk/scripts/latlong.html
 * 
 * @since 25/12/2014
 */
public final class Azimuth {
	/**
	 * Valor do raio de curvatura de terra em KM
	 */
	public static int EARTH_RADIUS_KM = 6371;

	/**
	 * Retorna a distância entre dois pontos usando a fórmula de haversine.
	 * 
	 * @param lat1
	 *            latitude do primeiro ponto
	 * @param lon2
	 *            logintude do primeiro ponto
	 * @param lat2
	 *            latitude do segundo ponto
	 * @param lon2
	 *            logintude do segundo ponto
	 * 
	 * @returns Distância em KM entre os dois pontos.
	 */
	public static double calculateLengthInKM(double lat1, double lon1,
			double lat2, double lon2) {

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);

		double a = sin(latDistance / 2) * sin(latDistance / 2)
				+ cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
				* sin(lonDistance / 2) * sin(lonDistance / 2);

		double c = 2 * atan2(sqrt(a), sqrt(1 - a));
		double distance = Azimuth.EARTH_RADIUS_KM * c;

		return distance;
	}

	/**
	 * Retorna o azimute a frente entre 2 pontos.
	 * 
	 * @param lat1
	 *            latitude do primeiro ponto
	 * @param lon2
	 *            logintude do primeiro ponto
	 * @param lat2
	 *            latitude do segundo ponto
	 * @param lon2
	 *            logintude do segundo ponto
	 * @returns azimute em graus.
	 */
	public static double azimuth(double lat1, double lon1, double lat2,
			double lon2) {
		double phi1 = toRadians(lat1);
		double phi2 = toRadians(lat2);
		double delta = toRadians(lon2 - lon1);
		double y = sin(delta) * cos(phi2);
		double x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(delta);
		double teta = atan2(y, x);
		return (toDegrees(teta) + 360D) % 360D;
	}
}
