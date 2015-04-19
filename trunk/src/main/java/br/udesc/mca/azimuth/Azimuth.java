package br.udesc.mca.azimuth;

import java.text.NumberFormat;
import java.util.Locale;
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

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
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

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        // DecimalFormat df = new DecimalFormat("0.0000000000000000000");

        double phiLat1 = Math.toRadians(lat1);
        double phiLat2 = Math.toRadians(lat2);
        double deltaLon = new Double(nf.format(Math.toRadians(lon2 - lon1))).doubleValue();

        double y = Math.sin(deltaLon) * Math.cos(phiLat2);
        double x = Math.cos(phiLat1) * Math.cos(phiLat2) - Math.sin(phiLat1) * Math.cos(phiLat2) * Math.cos(deltaLon);
        double azimuth = Math.atan2(y, x);

        double ret = (Math.toDegrees(azimuth) + 360) % 360;
        log.info(ret);
        return ret;

    }
}
