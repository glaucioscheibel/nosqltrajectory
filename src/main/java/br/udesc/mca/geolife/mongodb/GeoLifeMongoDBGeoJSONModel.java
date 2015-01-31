package br.udesc.mca.geolife.mongodb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class GeoLifeMongoDBGeoJSONModel {

    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
        MongoClient mc = new MongoClient();
        DB db = mc.getDB("geolife");
        DBCollection dbc = db.createCollection("trajectories", null);
        File data = new File("C:/data/db/geolife/data");
        String[] ext = { "plt" };
        Iterator<File> ifs = FileUtils.iterateFiles(data, ext, true);
        while (ifs.hasNext()) {
            File f = ifs.next();
            String trajId = f.getName();
            DBObject dbo = new BasicDBObject();
            dbo.put("file", trajId);
            DBObject traj = new BasicDBObject();
            traj.put("type", "LineString");
            dbo.put("loc", traj);
            BasicDBList coords = new BasicDBList();
            traj.put("coordinates", coords);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            // Line 1-6 are useless in this dataset, and can be ignored.
            for (int i = 1; i <= 6; i++) {
                br.readLine();
            }
            String linha = null;
            while ((linha = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(linha, ",");
                // Field 1: Latitude in decimal degrees.
                String lat = st.nextToken();
                // Field 2: Longitude in decimal degrees.
                String lng = st.nextToken();
                // Field 3: All set to 0 for this dataset.
                st.nextToken();
                // Field 4: Altitude in feet (-777 if not valid).
                String alt = st.nextToken();
                Double dalt = Double.valueOf(alt);
                // Field 5: Date - number of days (with fractional part) that
                // have passed since 12/30/1899.
                st.nextToken();
                // Field 6: Date as a string.
                String date = st.nextToken();
                // Field 7: Time as a string.
                String time = st.nextToken();
                BasicDBList point = new BasicDBList();
                point.add(Double.valueOf(lat));
                point.add(Double.valueOf(lng));
                coords.add(point);
            }
            br.close();
            fr.close();
            dbc.insert(dbo);
        }
        mc.close();
    }
}
