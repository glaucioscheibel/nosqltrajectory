package br.udesc.mca.azimuth;


public class TesteAzimuth {

	public static void main(String[] args) {
		GeoCoordinate udesc = new GeoCoordinate(-26.1549893, -48.8807443);
		GeoCoordinate udesc2 = new GeoCoordinate(-26.1509445, -48.8814309);

		double distancia = Azimuth.calculateLengthInKM(udesc.getLatitude(),
				udesc.getLongitude(), udesc2.getLatitude(),
				udesc2.getLongitude());
		double azimute = Azimuth.azimuth(udesc.getLatitude(),
				udesc.getLongitude(), udesc2.getLatitude(),
				udesc2.getLongitude());

		System.out.println(distancia + " e " + azimute);

		// Trajetória um
		/*
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("26°15'23.35\"S"));
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("48°54'20.59\"O")); System.out.println();
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("26°15'22.88\"S"));
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("48°54'21.41\"O")); System.out.println();
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("26°15'22.06\"S"));
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("48°54'21.81\"O")); System.out.println();
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("26°15'21.07\"S"));
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("48°54'21.76\"O")); System.out.println();
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("26°15'20.21\"S"));
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("48°54'20.96\"O")); System.out.println();
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("26°15'20.06\"S"));
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("48°54'19.61\"O")); System.out.println();
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("26°15'21.44\"S"));
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("48°54'18.11\"O"));
		 * 
		 * 
		 * System.out.println("================================================="
		 * ); System.out.println(PointConverter
		 * .converterDecimalDegress("26°15'24.00\"S"));
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("48°54'14.17\"O")); System.out.println();
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("26°15'24.51\"S"));
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("48°54'13.05\"O")); System.out.println();
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("26°15'25.33\"S"));
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("48°54'12.56\"O")); System.out.println();
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("26°15'26.32\"S"));
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("48°54'12.71\"O")); System.out.println();
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("26°15'26.92\"S"));
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("48°54'13.34\"O")); System.out.println();
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("26°15'26.91\"S"));
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("48°54'14.87\"O")); System.out.println();
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("26°15'25.63\"S"));
		 * System.out.println(PointConverter
		 * .converterDecimalDegress("48°54'15.93\"O")); System.out.println();
		 */

	}
}
