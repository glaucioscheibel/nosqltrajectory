package br.udesc.mca.trajectory.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TrajectoryPoint implements Serializable {

	private static final long serialVersionUID = -4325481950110704141L;

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trajectoryid")
	private TrajectoryVersion trajectoryVersion;
	private float lat;
	private float lng;
	private float h;
	private long timestamp;
	private String transportationMode;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TrajectoryVersion getTrajectoryVersion() {
		return this.trajectoryVersion;
	}

	public void setTrajectoryVersion(TrajectoryVersion trajectoryVersion) {
		this.trajectoryVersion = trajectoryVersion;
	}

	public float getLat() {
		return this.lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return this.lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	public float getH() {
		return h;
	}

	public void setH(float h) {
		this.h = h;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getTransportationMode() {
		return transportationMode;
	}

	public void setTransportationMode(String transportationMode) {
		this.transportationMode = transportationMode;
	}
}