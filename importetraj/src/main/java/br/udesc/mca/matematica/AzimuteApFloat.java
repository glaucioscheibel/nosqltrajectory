package br.udesc.mca.matematica;

import static org.apfloat.ApfloatMath.atan2;
import static org.apfloat.ApfloatMath.cos;
import static org.apfloat.ApfloatMath.sin;
import static org.apfloat.ApfloatMath.sqrt;
import static org.apfloat.ApfloatMath.toDegrees;
import static org.apfloat.ApfloatMath.toRadians;

import org.apfloat.Apfloat;

/**
 * Classe que trabalha com a questão do cálculo de Azimute e Distância em KM.
 * Com base no trabalho em: http://www.movable-type.co.uk/scripts/latlong.html.
 * Usa a biblioteca ApFloat em vez da biblioteca padrão do Java.
 * 
 * @since 25/12/2014
 */
public class AzimuteApFloat {

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
	public static Apfloat calculaDistanciaKM(Apfloat lat1, Apfloat lon1, Apfloat lat2, Apfloat lon2) {
		Apfloat latDistance = toRadians(lat2.subtract(lat1));
		Apfloat lonDistance = toRadians(lon2.subtract(lon1));

		Apfloat apf2 = new Apfloat(2);

		Apfloat a = sin(latDistance.divide(apf2))
				.multiply(sin(latDistance.divide(apf2)).add(cos(toRadians(lat1)).multiply(cos(toRadians(lat2))
						.multiply(sin(lonDistance.divide(apf2)).multiply(sin(lonDistance.divide(apf2)))))));

		Apfloat apf1 = new Apfloat(1);
		Apfloat c = apf2.multiply(atan2(sqrt(a), sqrt(apf1.subtract(a))));
		Apfloat armk = new Apfloat(6371);
		Apfloat distance = armk.multiply(c);

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
	public static Apfloat azimute(Apfloat lat1, Apfloat lon1, Apfloat lat2, Apfloat lon2) {
		Apfloat phi1 = toRadians(lat1);
		Apfloat phi2 = toRadians(lat2);
		Apfloat delta = toRadians(lon2.subtract(lon1));
		Apfloat y = sin(delta).multiply(cos(phi2));
		Apfloat x = cos(phi1).multiply(sin(phi2)).subtract((sin(phi1).multiply(cos(phi2)).multiply(cos(delta))));
		Apfloat teta = atan2(y, x);
		Apfloat ap360 = new Apfloat(360);
		return (toDegrees(teta).add(ap360)).mod(ap360);
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
	public Apfloat diferencaAzimute(Apfloat azimute1, Apfloat azimute2) {
		return azimute1.subtract(azimute2);
	}
}
