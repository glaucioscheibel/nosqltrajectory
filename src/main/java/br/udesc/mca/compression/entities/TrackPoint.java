package br.udesc.mca.compression.entities;

import java.util.Date;

import br.udesc.mca.compression.time.ErrorMetrics;

public class TrackPoint{
    
	private int id;
	private double lat;
    private double lng;
    private double h;
    private Date time;
    private double bearing;
    private double speed;
    private int sequence;
    private boolean intersection = false;

	private double sed;

    public TrackPoint(double lat, double lng, double h, Date time, double bearing, double speed){
        this.lat 		= lat;
        this.lng 		= lng;
        this.h 			= h;
        this.time 		= time;
        this.bearing 	= bearing;
        this.speed 		= speed;
        this.intersection = false;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isIntersection() {
		return intersection;
	}

	public void setIntersection(boolean intersection) {
		this.intersection = intersection;
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
    
    public void setSed(double sed){
    	this.sed = sed;
    }
    
    public double getSed(){
    	return sed;
    }

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public double getProjectedX() {
		return ErrorMetrics.EARTH_RADIUS * Math.cos(ErrorMetrics.longitudeTo2pi(lng)) * Math.sin(ErrorMetrics.getColatitude(lat));
	}

	public double getProjectedY() {
		return ErrorMetrics.EARTH_RADIUS * Math.sin(ErrorMetrics.longitudeTo2pi(lng)) * Math.sin(ErrorMetrics.getColatitude(lat));
	}
	
	public Vector3D toVector(){
		
		double varphi = ErrorMetrics.deg2rad(this.lat);
	    double lambda = ErrorMetrics.deg2rad(this.lng);

	    // right-handed vector: x -> 0°E,0°N; y -> 90°E,0°N, z -> 90°N
	    double x = ErrorMetrics.EARTH_RADIUS * Math.cos(varphi) * Math.cos(lambda);
	    double y = ErrorMetrics.EARTH_RADIUS * Math.cos(varphi) * Math.sin(lambda);
	    double z = ErrorMetrics.EARTH_RADIUS * Math.sin(varphi);

	    return new Vector3D(x, y, z);		
	}

	/**
	 * @param p
	 * @return
	 */
	public boolean equals(TrackPoint p){
		if(p.getLat() == this.getLat() && p.getLon() == this.getLon()) return true;
		return false;
	}
	
	public String toString(){
		return this.getLat()+","+this.getLon();
	}
}
