package br.udesc.mca.trajectory;

import org.postgis.Point;

public class Trajectory {

	private int id;
	private Point location;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
}
