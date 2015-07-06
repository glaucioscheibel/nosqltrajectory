package br.udesc.mca.azimuth;

import br.udesc.mca.point.converter.PointConverter;

public class TesteAzimuthQuinze {

	//KML da Schulz
    public static void main(String[] args) {

        GeoCoordinate udesc = new GeoCoordinate(-26.2980, -48.8551);
        GeoCoordinate udesc2 = new GeoCoordinate(-26.2985, -48.8552);

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

        System.out.println(PointConverter.converterToDecimalDegree("26°17'51.38\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°51'17.68\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°17'52.87\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°51'18.68\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°17'54.89\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°51'18.96\"W"));
        System.out.println();
        // Trajetória dois
        System.out.println("=================================================");
        System.out.println(PointConverter.converterToDecimalDegree("26°17'52.85\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°51'34.81\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°17'53.81\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°51'32.90\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°17'54.99\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°51'31.08\"W"));
        System.out.println();
        System.out.println(PointConverter.converterToDecimalDegree("26°17'55.25\"S"));
        System.out.println(PointConverter.converterToDecimalDegree("48°51'28.64\"W"));
        System.out.println();
    }
}
