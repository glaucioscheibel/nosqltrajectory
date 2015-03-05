package br.udesc.mca.geolife.relational;

import java.util.Date;

public class TransportationMode {

	private Date dt1;
	private Date dt2;
	private String type;
	private int typeMode;

	public enum TransportationType {
		AIRPLANE(1), BIKE(2), BOAT(3), BUS(4), CAR(5), MOTORCYCLE(6), RUN(7), SUBWAY(
				8), TAXI(9), TRAIN(10), WALK(11);

		private int value;

		private TransportationType(int value) {
			this.value = value;
		}
	}

	boolean isBetween(Date dt) {
		return dt.after(dt1) && dt.before(dt2);
	}

	@Override
	public String toString() {
		return "TranspMode [dt1=" + dt1 + ", dt2=" + dt2 + ", type=" + type
				+ "]";
	}

	public Date getDt1() {
		return dt1;
	}

	public void setDt1(Date dt1) {
		this.dt1 = dt1;
	}

	public Date getDt2() {
		return dt2;
	}

	public void setDt2(Date dt2) {
		this.dt2 = dt2;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type.trim().toLowerCase();
	}

	public int getTypeMode() {
		return typeMode;
	}

	public void setTypeMode(int typeMode) {
		this.typeMode =  typeMode;
	}
}
