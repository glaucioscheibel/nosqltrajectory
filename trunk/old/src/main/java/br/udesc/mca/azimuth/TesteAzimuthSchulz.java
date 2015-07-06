package br.udesc.mca.azimuth;

import org.apfloat.Apfloat;
import br.udesc.mca.point.converter.PointConverter;

public class TesteAzimuthSchulz {

	//KML da Schulz
    public static void main(String[] args) {

        GeoCoordinate udesc = new GeoCoordinate(-26.2574, -48.9037);
        GeoCoordinate udesc2 = new GeoCoordinate(-26.2574, -48.9041);

        double distancia = Azimuth.calculateLengthInKM(udesc.getLatitude(), udesc.getLongitude(), udesc2.getLatitude(),
                udesc2.getLongitude());
        double azimute = Azimuth.azimuth(udesc.getLatitude(), udesc.getLongitude(), udesc2.getLatitude(),
                udesc2.getLongitude());

        System.out.println(distancia + " e " + azimute);
        System.out.println("=========================");
        /*distancia = AzimuthJS.calculateLengthInKM(udesc.getLatitude(), udesc.getLongitude(), udesc2.getLatitude(),
                udesc2.getLongitude());
        azimute = AzimuthJS.azimuth(udesc.getLatitude(), udesc.getLongitude(), udesc2.getLatitude(),
                udesc2.getLongitude());
        System.out.println(distancia + " e " + azimute);
        System.out.println("=========================");*/
        /*Apfloat apdistancia = AzimuthApFloat.calculateLengthInKM(new Apfloat("-26.2564"), new Apfloat("-48.9057"),
                new Apfloat("-26.2563"), new Apfloat("-48.9059"));
        Apfloat apazimute = AzimuthApFloat.azimuth(new Apfloat("-26.2564"), new Apfloat("-48.9057"), new Apfloat(
                "-26.2563"), new Apfloat("-48.9059"));
        System.out.println(apdistancia.toString(true) + " e " + apazimute.toString(true));

        System.out.println();*/

        // Trajetória um

        System.out.println(PointConverter.converterToDecimalDegree("26°15'23.35\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°54'20.59\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°15'22.88\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°54'21.41\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°15'22.06\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°54'21.81\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°15'21.07\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°54'21.76\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°15'20.21\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°54'20.96\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°15'20.06\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°54'19.61\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°15'21.44\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°54'18.11\"W"));

        // Trajetória dois
        System.out.println("=================================================");
        System.out.println(PointConverter.converterToDecimalDegree("26°15'24.00\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°54'14.17\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°15'24.51\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°54'13.05\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°15'25.33\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°54'12.56\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°15'26.32\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°54'12.71\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°15'26.92\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°54'13.34\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°15'26.91\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°54'14.87\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°15'25.63\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°54'15.93\"O"));
        System.out.println();

    }
}
