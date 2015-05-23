package br.udesc.mca.azimuth;

import java.io.Serializable;

/**
 * Bean para manipulacao de coordenadas geograficas
 */
public class GeoCoordinate implements Serializable, Cloneable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9092913101770900910L;

    /**
     * Latitude em graus
     */
    private double latitude;
    /**
     * Longitude em graus
     */
    private double longitude;

    /**
     * Construtor com latitude e longitude em graus
     * 
     * @param latitude
     *            Latitude em graus
     * @param longitude
     *            Longitude em graus
     */
    public GeoCoordinate(double latitude, double longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Recupera a latitude
     * 
     * @return Latitude em graus
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Atribui uma latitude
     * 
     * @param latitude
     *            Latitude em graus
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Recupera uma longitude
     * 
     * @return Longitude em graus
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Atribui uma longitude
     * 
     * @param longitude
     *            Longitude em graus
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}