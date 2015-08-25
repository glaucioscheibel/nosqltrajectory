package br.udesc.mca.matematica;

import br.udesc.mca.modelo.ponto.Ponto;

/**
 * Classe que trabalha com a questão do cálculo de Azimute e Distância em KM.
 * Com base no trabalho em: http://www.movable-type.co.uk/scripts/latlong.html e
 * http://robbyn.github.io/hiketools/
 * 
 * @since 25/12/2014
 */
public final class Azimute {

	/**
	 * Valor do raio de curvatura de terra em KM
	 */
	public static int RAIO_TERRA_KM = 6371;
	
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
	public static double calculaDistanciaKM(double lat1, double lon1, double lat2, double lon2) {

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = Azimute.RAIO_TERRA_KM * c;

		return distance;
	}

	/**
	 * Calcula a distância a partir de um ponto P passando pelo grande círculo
	 * que passa por outros dois pontos A e B.
	 * 
	 * @param p
	 *            ponto
	 * @param a
	 *            primeiro ponto
	 * @param b
	 *            segundo ponto
	 * @return distância, em metros
	 */
	public static double distanciaPerpendicular(Ponto p, Ponto a, Ponto b) {
		double lata = Math.toRadians(a.getLatitude());
		double lnga = Math.toRadians(a.getLongitude());
		double latb = Math.toRadians(b.getLatitude());
		double lngb = Math.toRadians(b.getLongitude());
		double latp = Math.toRadians(p.getLatitude());
		double lngp = Math.toRadians(p.getLongitude());
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
		double costh = sinlata * sinlatb + coslata * coslatb * (coslnga * coslngb + sinlnga * sinlngb);
		double sin2th = 1 - costh * costh;
		if (sin2th < 1.0E-8) {
			// a e b são muito próximos; return a distância de a para p
			double costhp = sinlata * sinlatp + coslata * coslatp * (coslnga * coslngp + sinlnga * sinlngp);
			return Math.acos(costhp) * (RAIO_TERRA_KM + p.getAltitude());
		}
		double num = sinlata * (coslatb * coslatp * coslngb * sinlngp - coslatb * coslatp * sinlngb * coslngp)
				+ coslata * coslnga * (coslatb * sinlatp * sinlngb - sinlatb * coslatp * sinlngp)
				+ coslata * sinlnga * (sinlatb * coslatp * coslngp - coslatb * sinlatp * coslngb);
		double sinr = Math.abs(num) / Math.sqrt(sin2th);
		return (RAIO_TERRA_KM + p.getAltitude()) * Math.asin(sinr);
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
	public static double azimute(double lat1, double lon1, double lat2, double lon2) {
		double phi1 = Math.toRadians(lat1);
		double phi2 = Math.toRadians(lat2);
		double delta = Math.toRadians(lon2 - lon1);
		double y = Math.sin(delta) * Math.cos(phi2);
		double x = Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(delta);
		double teta = Math.atan2(y, x);
		return (Math.toDegrees(teta) + 360D) % 360D;
	}

	/**
	 * Retorna a diferença de azimute em graus decimais do primeiro subtraindo
	 * do segundo.
	 * 
	 * @param azimute1
	 *            recebe o azimute em graus decimais.
	 * @param azimute2
	 *            recebe o azimute em graus decimais.
	 * @returns direferença de azimute em graus decimais.
	 */
	public static double diferencaAzimute(double azimute1, double azimute2) {
		return azimute1 - azimute2;
	}
}