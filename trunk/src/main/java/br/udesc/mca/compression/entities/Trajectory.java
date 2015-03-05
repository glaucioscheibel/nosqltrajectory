package br.udesc.mca.compression.entities;

import java.util.List;

public class Trajectory {
	
	private long id;
	private List<TrackPoint> trackPoints;
	private List<TrackPoint> compressedTrackPoints;
	
	private double sedErrorAvg 			= 0;
	private double speedErrorAvg 		= 0;
	private double timeRatioErrorAvg 	= 0;
	private double areaError 			= 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<TrackPoint> getTrackPoints() {
		return trackPoints;
	}

	public void setTrackPoints(List<TrackPoint> trajectory) {
		this.trackPoints = trajectory;
	}

	public List<TrackPoint> getCompressedTrackPoins() {
		return compressedTrackPoints;
	}

	public void setCompressedTrackPoints(List<TrackPoint> compressedTrackPoints) {
		this.compressedTrackPoints = compressedTrackPoints;
	}

	public double getSedErrorAvg() {
		return sedErrorAvg;
	}

	public void setSedErrorAvg(double sedErrorAvg) {
		this.sedErrorAvg = sedErrorAvg;
	}

	public double getSpeedErrorAvg() {
		return speedErrorAvg;
	}

	public void setSpeedErrorAvg(double speedErrorAvg) {
		this.speedErrorAvg = speedErrorAvg;
	}

	public double getTimeRatioErrorAvg() {
		return timeRatioErrorAvg;
	}

	public void setTimeRatioErrorAvg(double timeRatioErrorAvg) {
		this.timeRatioErrorAvg = timeRatioErrorAvg;
	}

	public double getAreaError() {
		return areaError;
	}

	public void setAreaError(double areaError) {
		this.areaError = areaError;
	}
	
	public double getCompressionRate(){
		
		double originalSize = this.trackPoints.size();
		double compressedSize = this.compressedTrackPoints.size();
		
		return (originalSize > 0) ? compressedSize / originalSize : 0;
	}
}
