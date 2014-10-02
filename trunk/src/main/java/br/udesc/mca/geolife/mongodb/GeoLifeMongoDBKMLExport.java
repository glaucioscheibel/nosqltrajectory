package br.udesc.mca.geolife.mongodb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark;

public class GeoLifeMongoDBKMLExport {
    
    public void export(int userId, List<Long> trajIds, File kmlFile) throws Exception {
        MongoClient mc = new MongoClient();
        DB db = mc.getDB("geolife");
        DBCollection trajCol = db.getCollection("trajectories");
        DBObject ref = new BasicDBObject();
        ref.put("user", userId);
        DBObject tra = new BasicDBObject();
        BasicDBList tralist = new BasicDBList();
        tralist.addAll(trajIds);
        tra.put("$in", tralist);
        ref.put("trajId", tra);
        Kml kml = new Kml();

        Placemark p = kml.createAndSetPlacemark();
        p.setDescription("Trajectories");
        MultiGeometry mg = p.createAndSetMultiGeometry();
        
        DBCursor dbc = trajCol.find(ref);
        while (dbc.hasNext()) {
            DBObject to = dbc.next();
            LineString ls = mg.createAndAddLineString();
            List<Coordinate> lc = ls.createAndSetCoordinates();
            BasicDBList traj = (BasicDBList) to.get("points");
            for (Object o : traj) {
                DBObject dbo = (DBObject) o;
                lc.add(new Coordinate((Double) dbo.get("lng"), (Double) dbo.get("lat")));
            }
        }
        dbc.close();
        mc.close();
        kml.marshal(kmlFile);
    }

    public static void main(String[] args) throws Exception {
        GeoLifeMongoDBKMLExport exp = new GeoLifeMongoDBKMLExport();
        List<Long> ll = new ArrayList<>();
        ll.add(20081023025304L);
        ll.add(20081026134407L);
        exp.export(0, ll, new File("teste.kml"));
    }
}
