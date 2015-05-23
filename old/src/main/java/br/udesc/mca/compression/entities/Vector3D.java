package br.udesc.mca.compression.entities;

import br.udesc.mca.compression.time.ErrorMetrics;

public class Vector3D {
	
	private double X;
	private double Y;
	private double Z;
	
	public Vector3D(double x, double y, double z){
		this.X = x;
		this.Y = y;
		this.Z = z;
	}
	
	public double getX() {
		return X;
	}
	public void setX(double x) {
		X = x;
	}
	public double getY() {
		return Y;
	}
	public void setY(double y) {
		Y = y;
	}
	public double getZ() {
		return Z;
	}
	public void setZ(double z) {
		Z = z;
	}
	
	public Vector3D cross(Vector3D v){
		double x = this.Y*v.getZ() - this.getZ()*v.getY();
		double y = this.Z*v.getX() - this.getX()*v.getZ();
		double z = this.X*v.getY() - this.getY()*v.getX();

	    return new Vector3D(x, y, z);
	}
	
	public TrackPoint toLatLonS(){
		double varphi = Math.atan2(this.Z, Math.sqrt(this.X*this.X + this.Y*this.Y));
	    double lambda = Math.atan2(this.Y, this.X);

	    return new TrackPoint(ErrorMetrics.rad2deg(varphi), ErrorMetrics.rad2deg(lambda), 0, null, 0, 0);
	}
}
