package br.udesc.mca.trajectory.microservice.kml;

import br.udesc.mca.trajectory.model.*;
import de.micromata.opengis.kml.v_2_2_0.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Path("/trajectory-to-kml")
public class KMLResource {

    public KMLResource(){
    }

    //@POST
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.APPLICATION_JSON)
    //public String test(@QueryParam("trajectory") Trajectory trajectory){
    public String test(){
        //TESTANDO retorno... com POST eh complicado :D

        Trajectory t = new Trajectory();
        t.setId(10l);
        t.setDescription("Trajectory Desc");
        t.setLastModified(new Date());
        t.setOriginalTrajectory(1);

        TrajectoryVersion version = new TrajectoryVersion();
        version.setVersion(1);
        version.setType(TrajectoryType.RAW);
        version.setLastModified(new Date());
        t.addVersion(version);

        TrajectorySegment seg = new TrajectorySegment();
        seg.setTransportationMode(TransportationMode.RUN);

        TrajectoryPoint p = new TrajectoryPoint();
        p.setTimestamp(System.currentTimeMillis());
        p.setLng(-112.2550785337791f);
        p.setLat(36.07954952145647f);
        seg.addPoint(p);

        p = new TrajectoryPoint();
        p.setTimestamp(System.currentTimeMillis());
        p.setLng(-112.2549277039738f);
        p.setLat(36.08117083492122f);
        seg.addPoint(p);

        p = new TrajectoryPoint();
        p.setTimestamp(System.currentTimeMillis());
        p.setLng(-112.2656969554589f);
        p.setLat(36.08649599090644f);
        seg.addPoint(p);
        version.addSegment(seg);

        return noNameSpaceSimpleMarshall(lastVersionToKml(t));
    }


    public Kml lastVersionToKml(Trajectory trajectory){
        Kml kml = new Kml();
        Document doc = kml.createAndSetDocument();
        doc.setName(Long.toString(trajectory.getId()));
        doc.setDescription(trajectory.getDescription());

        TrajectoryVersion last = trajectory.getVersions().get(trajectory.getVersions().size() -1);
        Placemark placemark = doc.createAndAddPlacemark();
        placemark.setName("Version " + last.getVersion());
        placemark.setDescription("");

        LineString ls = placemark.createAndSetLineString();
        ls.setExtrude(true);
        ls.setTessellate(true);
        ls.setAltitudeMode(AltitudeMode.ABSOLUTE);

        List<Coordinate> lc = ls.createAndSetCoordinates();
        for(TrajectorySegment seg:last.getSegments()){  ///DUVIDA.... cada segmento seria um placemark?????  NAO... arrumar
            for(TrajectoryPoint pt:seg.getPoints()) {
                lc.add(new Coordinate(pt.getLng(), pt.getLat()));
            }
        }

        return kml;
    }

    public Kml allVersionsToKml(Trajectory trajectory){
        Kml kml = new Kml();
        Document doc = kml.createAndSetDocument();
        doc.setName(Long.toString(trajectory.getId()));
        doc.setDescription(trajectory.getDescription());

        //trajectory.getVersions().get(1).getSegments().get(0).get


        return kml;
    }

    private static String noNameSpaceSimpleMarshall (Kml kml){
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
        Document doc = (Document)kml.getFeature();
        sb.append("\t<Document>\n");
        sb.append("\t\t<name>").append(doc.getName()).append("</name>\n");
        sb.append("\t\t<description>").append(doc.getDescription()).append("</description>\n");

        for(Feature f:doc.getFeature()){
            if(f instanceof Placemark) {
                Placemark mark = (Placemark) f;
                sb.append("\t\t<Placemark>\n");
                sb.append("\t\t\t<name>").append(mark.getName()).append("</name>\n");
                sb.append("\t\t\t<description>").append(mark.getDescription()).append("</description>\n");

                LineString ls = (LineString) mark.getGeometry();
                sb.append("\t\t\t<LineString>\n");
                sb.append("\t\t\t\t<coordinates>\n");
                for (Coordinate c : ls.getCoordinates()){
                    sb.append(" ").append(c.getLongitude()).append(",").append(c.getLatitude());
                }
                sb.append("\n\t\t\t\t</coordinates>\n");
                sb.append("\t\t\t</LineString>\n");
                sb.append("\t\t</Placemark>\n");
            }
        }
        sb.append("\t</Document>\n");
        sb.append("</kml>\n");

        return sb.toString();
    }
}
