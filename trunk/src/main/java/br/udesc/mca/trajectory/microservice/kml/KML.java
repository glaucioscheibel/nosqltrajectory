package br.udesc.mca.trajectory.microservice.kml;


import br.udesc.mca.trajectory.model.Trajectory;
import br.udesc.mca.trajectory.model.TrajectoryPoint;
import br.udesc.mca.trajectory.model.TrajectorySegment;
import br.udesc.mca.trajectory.model.TrajectoryVersion;
import de.micromata.opengis.kml.v_2_2_0.*;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.List;

public class KML extends Application<KMLConfiguration> {

    @Override
    public void run(KMLConfiguration kmlConfiguration, Environment environment) throws Exception {
        KMLResource r = new KMLResource();
        environment.jersey().register(r);
    }

    @Override
    public String getName() {
        return "KML";
    }

    @Override
    public void initialize(Bootstrap<KMLConfiguration> bootstrap) {
    }

    //public Trajectory kmlToTrajectory(Kml kml){
    //}



    public static void main(String... a){
/*        Kml kml = new Kml();
        Document doc = kml.createAndSetDocument();
        doc.setName("Trajectories");
        doc.setDescription("");
        Style st = doc.createAndAddStyle();
        st.setId("yellowLineGreenPoly");
        st.createAndSetLineStyle();
        st.getLineStyle().setWidth(4);
        st.getLineStyle().setColor("7f00ffff");

        st.createAndSetPolyStyle();
        st.getPolyStyle().setColor("7f00ff00");

        Placemark p = doc.createAndAddPlacemark();
        p.setName("Trajectories placemark");
        p.setDescription("");
        p.setStyleUrl("#yellowLineGreenPoly");

        LineString ls = p.createAndSetLineString();
        ls.setExtrude(true);
        ls.setTessellate(true);
        ls.setAltitudeMode(AltitudeMode.ABSOLUTE);
        List<Coordinate> lc = ls.createAndSetCoordinates();

        lc.add(new Coordinate(-112.2550785337791,36.07954952145647));
        lc.add(new Coordinate(-112.2549277039738,36.08117083492122));
        lc.add(new Coordinate(-112.2552505069063,36.08260761307279));
        lc.add(new Coordinate(-112.2564540158376,36.08395660588506));
        lc.add(new Coordinate(-112.2580238976449,36.08511401044813));
        lc.add(new Coordinate(-112.2595218489022,36.08584355239394));
        lc.add(new Coordinate(-112.2608216347552,36.08612634548589));
        lc.add(new Coordinate(-112.262073428656,36.08626019085147));
        lc.add(new Coordinate(-112.2633204928495,36.08621519860091));
        lc.add(new Coordinate(-112.2644963846444,36.08627897945274));
        lc.add(new Coordinate(-112.2656969554589,36.08649599090644));

        System.out.println(noNameSpaceSimpleMarshall(kml));
                     */
        try {
            new KML().run(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
