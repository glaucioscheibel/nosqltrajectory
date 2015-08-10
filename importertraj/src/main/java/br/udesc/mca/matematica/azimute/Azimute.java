package br.udesc.mca.matematica.azimute;

/**
 * Classe que trabalha com a questão do cálculo de Azimute e Distância em KM.
 * Com base no trabalho em: http://www.movable-type.co.uk/scripts/latlong.html.
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
	public static double calculaDistanciaKM(double lat1, double lon1,
			double lat2, double lon2) {

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
				* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = Azimute.RAIO_TERRA_KM * c;

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
	public static double azimute(double lat1, double lon1, double lat2,
			double lon2) {
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
