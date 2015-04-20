package br.udesc.mca.azimuth;

import static org.apache.commons.math3.util.FastMath.atan2;
import static org.apache.commons.math3.util.FastMath.cos;
import static org.apache.commons.math3.util.FastMath.sin;
import static org.apache.commons.math3.util.FastMath.sqrt;
import static org.apache.commons.math3.util.FastMath.toDegrees;
import static org.apache.commons.math3.util.FastMath.toRadians;
import org.apache.log4j.Logger;

/**
 * Classe que trabalha com a questão do cálculo de Azimute e Distância em KM.
 * Com base no trabalho em: http://www.movable-type.co.uk/scripts/latlong.html
 * 
 * @since 25/12/2014
 */
public final class Azimuth {
    private static final Logger log = Logger.getLogger(Azimuth.class);

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
    public static double calculateLengthInKM(double lat1, double lon1, double lat2, double lon2) {

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = sin(latDistance / 2) * sin(latDistance / 2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
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
    public static double azimuth(double lat1, double lon1, double lat2, double lon2) {
        log.info("azimuth(" + lat1 + ", " + lon1 + ", " + lat2 + ", " + lon2 + ")");

        double phiLat1 = toRadians(lat1);
        double phiLat2 = toRadians(lat2);
        double aux = lon2 - lon1;
        double deltaLon = toRadians(aux);

        double y = sin(deltaLon) * cos(phiLat2);
        double x = cos(phiLat1) * cos(phiLat2) - sin(phiLat1) * cos(phiLat2) * cos(deltaLon);
        double azimuth = atan2(y, x);

        double ret = (toDegrees(azimuth) + 360.0) % 360.0;
        log.info(ret);
        return ret;
    }
}
