package br.udesc.mca.azimuth;

import br.udesc.mca.trajectory.string.match.NGramDistance;

public class AzimuthSchulze {

    public static void main(String[] args) {
        GeoCoordinate t1p1Schulze = new GeoCoordinate(-26.2564, -48.9057);
        GeoCoordinate t1p2Schulze = new GeoCoordinate(-26.2563, -48.9059);
        GeoCoordinate t1p3Schulze = new GeoCoordinate(-26.2561, -48.9060);

        GeoCoordinate t2p1Schulze = new GeoCoordinate(-26.2566, -48.9039);
        GeoCoordinate t2p2Schulze = new GeoCoordinate(-26.2568, -48.9036);
        GeoCoordinate t2p3Schulze = new GeoCoordinate(-26.2570, -48.9034);

        String azimute1 = new String();
        String azimute2 = new String();

        azimute1 = ((Double) Azimuth.azimuth(t1p1Schulze.getLatitude(), t1p1Schulze.getLongitude(),
                t1p2Schulze.getLatitude(), t1p2Schulze.getLongitude())).toString();
        azimute1 = azimute1
                + " "
                + ((Double) Azimuth.azimuth(t1p2Schulze.getLatitude(), t1p2Schulze.getLongitude(),
                        t1p3Schulze.getLatitude(), t1p3Schulze.getLongitude())).toString();

        azimute2 = ((Double) Azimuth.azimuth(t2p1Schulze.getLatitude(), t2p1Schulze.getLongitude(),
                t2p2Schulze.getLatitude(), t2p2Schulze.getLongitude())).toString();

        azimute2 = azimute2
                + " "
                + ((Double) Azimuth.azimuth(t2p2Schulze.getLatitude(), t2p2Schulze.getLongitude(),
                        t2p3Schulze.getLatitude(), t2p3Schulze.getLongitude())).toString();

        System.out.println(azimute1);
        System.out.println(azimute2);

        NGramDistance gramDistance = new NGramDistance();
        System.out.println(gramDistance.getDistance(azimute1, azimute2));

        String sequencia1 = "10 -20 -20 -30 -10";
        String sequencia2 = "10.5 -20 -20.1 -30 -10";
        System.out.println(gramDistance.getDistance(sequencia1, sequencia2));

        String sequencia3 = "10";
        String sequencia4 = "10.5";
        System.out.println(gramDistance.getDistance(sequencia3, sequencia4));

        /*
         * System.out.println(PointConverter
         * .converterToDecimalDegree("26o15'23.35\"S"));
         * System.out.println(PointConverter
         * .converterToDecimalDegree("48o54'20.59\"O")); System.out.println();
         * System.out.println(PointConverter
         * .converterToDecimalDegree("26o15'22.88\"S"));
         * System.out.println(PointConverter
         * .converterToDecimalDegree("48o54'21.41\"O")); System.out.println();
         * System.out.println(PointConverter
         * .converterToDecimalDegree("26o15'22.06\"S"));
         * System.out.println(PointConverter
         * .converterToDecimalDegree("48o54'21.81\"O")); System.out.println();
         * System.out.println(PointConverter
         * .converterToDecimalDegree("26o15'21.07\"S"));
         * System.out.println(PointConverter
         * .converterToDecimalDegree("48o54'21.76\"O")); System.out.println();
         * System.out.println(PointConverter
         * .converterToDecimalDegree("26o15'20.21\"S"));
         * System.out.println(PointConverter
         * .converterToDecimalDegree("48o54'20.96\"O")); System.out.println();
         * System.out.println(PointConverter
         * .converterToDecimalDegree("26o15'20.06\"S"));
         * System.out.println(PointConverter
         * .converterToDecimalDegree("48o54'19.61\"O")); System.out.println();
         * System.out.println(PointConverter
         * .converterToDecimalDegree("26o15'21.44\"S"));
         * System.out.println(PointConverter
         * .converterToDecimalDegree("48o54'18.11\"O"));
         * 
         * System.out.println("================================================="
         * ); System.out.println(PointConverter
         * .converterToDecimalDegree("26o15'24.00\"S"));
         * System.out.println(PointConverter
         * .converterToDecimalDegree("48o54'14.17\"O")); System.out.println();
         * System.out.println(PointConverter
         * .converterToDecimalDegree("26o15'24.51\"S"));
         * System.out.println(PointConverter
         * .converterToDecimalDegree("48o54'13.05\"O")); System.out.println();
         * System.out.println(PointConverter
         * .converterToDecimalDegree("26o15'25.33\"S"));
         * System.out.println(PointConverter
         * .converterToDecimalDegree("48o54'12.56\"O")); System.out.println();
         * System.out.println(PointConverter
         * .converterToDecimalDegree("26o15'26.32\"S"));
         * System.out.println(PointConverter
         * .converterToDecimalDegree("48o54'12.71\"O")); System.out.println();
         * System.out.println(PointConverter
         * .converterToDecimalDegree("26o15'26.92\"S"));
         * System.out.println(PointConverter
         * .converterToDecimalDegree("48o54'13.34\"O")); System.out.println();
         * System.out.println(PointConverter
         * .converterToDecimalDegree("26o15'26.91\"S"));
         * System.out.println(PointConverter
         * .converterToDecimalDegree("48o54'14.87\"O")); System.out.println();
         * System.out.println(PointConverter
         * .converterToDecimalDegree("26o15'25.63\"S"));
         * System.out.println(PointConverter
         * .converterToDecimalDegree("48o54'15.93\"O")); System.out.println();
         */
    }
}
