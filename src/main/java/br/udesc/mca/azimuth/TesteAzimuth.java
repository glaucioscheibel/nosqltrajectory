package br.udesc.mca.azimuth;

import br.udesc.mca.point.converter.PointConverter;

public class TesteAzimuth {

	public static void main(String[] args) {
		GeoCoordinate udesc = new GeoCoordinate(-26.1549893, -48.8807443);
		GeoCoordinate udesc2 = new GeoCoordinate(-26.1509445, -48.8814309);

		// Trajetória um
		System.out.println(PointConverter
				.converterDecimalDegress("26°15'23.35\"S"));
		System.out.println(PointConverter
				.converterDecimalDegress("48°54'20.59\"O"));

		System.out.println(PointConverter
				.converterDecimalDegress("26°15'22.88\"S"));
		System.out.println(PointConverter
				.converterDecimalDegress("48°54'21.41\"O"));

		System.out.println(PointConverter
				.converterDecimalDegress("26°15'22.06\"S"));
		System.out.println(PointConverter
				.converterDecimalDegress("48°54'21.81\"O"));

		System.out.println(PointConverter
				.converterDecimalDegress("26°15'21.07\"S"));
		System.out.println(PointConverter
				.converterDecimalDegress("48°54'21.76\"O"));

		System.out.println(PointConverter
				.converterDecimalDegress("26°15'20.21\"S"));
		System.out.println(PointConverter
				.converterDecimalDegress("48°54'20.96\"O"));

		System.out.println(PointConverter
				.converterDecimalDegress("26°15'20.06\"S"));
		System.out.println(PointConverter
				.converterDecimalDegress("48°54'19.61\"O"));

		/*
		 * double distancia = Azimuth.calculateLengthInKM(udesc.getLatitude(),
		 * udesc.getLongitude(), udesc2.getLatitude(), udesc2.getLongitude());
		 * double azimute = Azimuth.azimuth(udesc.getLatitude(),
		 * udesc.getLongitude(), udesc2.getLatitude(), udesc2.getLongitude());
		 * 
		 * System.out.println(distancia + " e " + azimute);
		 */
	}
}
