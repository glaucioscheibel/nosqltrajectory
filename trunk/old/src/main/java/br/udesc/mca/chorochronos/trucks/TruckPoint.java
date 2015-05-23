package br.udesc.mca.chorochronos.trucks;

import java.util.Date;

public class TruckPoint {
    private Date date;
    private double lat;
    private double lon;
    private double x;
    private double y;

    public TruckPoint(Date date, double lat, double lon, double x, double y) {
        this.date = date;
        this.lat = lat;
        this.lon = lon;
        this.x = x;
        this.y = y;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "TruckPoint [date=" + date + ", lat=" + lat + ", lon=" + lon + ", x=" + x + ", y=" + y + "]";
    }
}
