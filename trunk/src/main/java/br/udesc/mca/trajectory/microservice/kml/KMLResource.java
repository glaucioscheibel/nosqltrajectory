package br.udesc.mca.trajectory.microservice.kml;

import br.udesc.mca.trajectory.model.*;
import br.udesc.mca.trajectory.segmenter.SegmenterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.micromata.opengis.kml.v_2_2_0.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/trajectory-to-kml")
public class KMLResource {
    private final boolean desegment;
    private final String colori;
    private final String colorp;

    public KMLResource(String desegment, String colori, String colorp){
        this.desegment = "true".equals(desegment);
        this.colori = colori;
        this.colorp = colorp;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_XML)
    public String post(String json){
        ObjectMapper mapper = new ObjectMapper();
        Trajectory t = null;
        try {
            t = mapper.readValue(json, Trajectory.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR:" + e.getMessage();
        }

        return noNameSpaceSimpleMarshall(lastVersionToKml(t));
    }


    public Kml lastVersionToKml(Trajectory trajectory){
        Kml kml = new Kml();
        Document doc = kml.createAndSetDocument();
        doc.setName(Long.toString(trajectory.getId()));
        doc.setDescription(trajectory.getDescription());

        TrajectoryVersion last = trajectory.getVersions().get(trajectory.getVersions().size() -1);

        if(desegment){
            Placemark placemark = doc.createAndAddPlacemark();
            placemark.setName("Version " + last.getVersion());
            placemark.setDescription("");

            LineString ls = placemark.createAndSetLineString();
            ls.setExtrude(true);
            ls.setTessellate(true);
            ls.setAltitudeMode(AltitudeMode.ABSOLUTE);

            List<Coordinate> lc = ls.createAndSetCoordinates();

            TrajectorySegment seg = new SegmenterService().desegment(last.getSegments());
            for(TrajectoryPoint pt:seg.getPoints()) {
                lc.add(new Coordinate(pt.getLng(), pt.getLat(), pt.getH()));
            }
        } else {
            Style st = doc.createAndAddStyle();
            st.setId("colori");
            st.createAndSetLineStyle();
            st.getLineStyle().setWidth(4);
            st.getLineStyle().setColor(colori);

            st = doc.createAndAddStyle();
            st.setId("colorp");
            st.createAndSetLineStyle();
            st.getLineStyle().setWidth(4);
            st.getLineStyle().setColor(colorp);

            int ct = 0;

            for(TrajectorySegment seg: last.getSegments()) {
                Placemark placemark = doc.createAndAddPlacemark();
                placemark.setName("Segment " + (++ct));
                placemark.setDescription("");
                placemark.setStyleUrl("#" + (ct % 2 == 0 ? "colorp" : "colori"));

                LineString ls = placemark.createAndSetLineString();
                ls.setExtrude(true);
                ls.setTessellate(true);
                ls.setAltitudeMode(AltitudeMode.ABSOLUTE);

                List<Coordinate> lc = ls.createAndSetCoordinates();

                for (TrajectoryPoint pt : seg.getPoints()) {
                    lc.add(new Coordinate(pt.getLng(), pt.getLat(), pt.getH()));
                }
            }
        }

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
        List<StyleSelector> styles =  doc.getStyleSelector();
        if(styles != null){
            for (StyleSelector st: styles){
                sb.append("\t\t<Style id=\"").append(st.getId()).append("\">\n");
                sb.append("\t\t\t<LineStyle>\n");
                sb.append("\t\t\t\t<color>").append(((Style)st).getLineStyle().getColor()).append("</color>\n");
                sb.append("\t\t\t\t<width>").append(((Style)st).getLineStyle().getWidth()).append("</width>\n");
                sb.append("\t\t\t</LineStyle>\n");
                sb.append("\t\t</Style>\n");
            }
        }

        for(Feature f:doc.getFeature()){
            if(f instanceof Placemark) {
                Placemark mark = (Placemark) f;
                sb.append("\t\t<Placemark>\n");
                sb.append("\t\t\t<name>").append(mark.getName()).append("</name>\n");
                sb.append("\t\t\t<description>").append(mark.getDescription()).append("</description>\n");
                if(mark.getStyleUrl() != null){
                    sb.append("\t\t\t<styleUrl>").append(mark.getStyleUrl()).append("</styleUrl>\n");
                }

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


    ///Para teste local somente
    public  static void main(String... a){
        System.out.print(new KMLResource("false", "7f00ffff", "7f00ff00").post("{     \"_id\":10,     \"description\":\"Trajectory Desc\",     \"originalTrajectory\":1,     \"lastModified\":1429559053818,     \"versions\":[         {             \"version\":1,             \"user\":null,             \"previousVersion\":null,             \"type\":\"RAW\",             \"lastModified\":1429559053819,             \"segments\":[{                 \"points\":[                     {\"lat\":36.079548,\"lng\":-112.25508,\"h\":0.0,\"timestamp\":1429559053820},                     {\"lat\":36.08117,\"lng\":-112.25493,\"h\":0.0,\"timestamp\":1429559053820},                     {\"lat\":36.086494,\"lng\":-112.26569,\"h\":0.0,\"timestamp\":1429559053820}                 ],                 \"data\":null,                 \"transportationMode\":\"RUN\"}             ],             \"data\":null,             \"history\":null         }     ] }"));
    }
}
