package br.udesc.mca.compression.support;

import java.util.Date;

public class TrackPoint{
    private double lat;
    private double lng;
    private double h;
    private Date time;
    private double bearing;
    private double speed;

    public TrackPoint(double lat, double lng, double h, Date time, double bearing, double speed){
        this.lat = lat;
        this.lng = lng;
        this.h = h;
        this.time = time;
        this.bearing = bearing;
        this.speed = speed;
    }

    public double getLat(){
        return lat;
    }
    public double getLon(){
        return lng;
    }
    public double getH(){
        return h;
    }
    public Date getTime(){
        return time;
    }
    public double getBearing(){
    	return bearing;
    }
    public double getSpeed(){
    	return speed;
    }
}
