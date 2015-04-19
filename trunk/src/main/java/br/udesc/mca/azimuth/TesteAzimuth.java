package br.udesc.mca.azimuth;

import br.udesc.mca.point.converter.PointConverter;

public class TesteAzimuth {

	public static void main(String[] args) {
		GeoCoordinate udesc = new GeoCoordinate(-26.1549893, -48.8807443);
		GeoCoordinate udesc2 = new GeoCoordinate(-26.1509445, -48.8814309);
		// GeoCoordinate udesc = new GeoCoordinate(35, 45);
		// GeoCoordinate udesc2 = new GeoCoordinate(35, 135);
		
		//PointConverter.converterDecimalDegress("26°15'23.35\"s");
		PointConverter.converterDecimalDegress(" -26°15'23.35\"S");

		double distancia = Azimuth.calculateLengthInKM(udesc.getLatitude(),
				udesc.getLongitude(), udesc2.getLatitude(),
				udesc2.getLongitude());
		double azimute = Azimuth.azimuth(udesc.getLatitude(),
				udesc.getLongitude(), udesc2.getLatitude(),
				udesc2.getLongitude());

		System.out.println(distancia + " e " + azimute);
	}
}
