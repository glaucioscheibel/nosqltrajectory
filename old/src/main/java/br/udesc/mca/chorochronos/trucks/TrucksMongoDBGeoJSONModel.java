package br.udesc.mca.chorochronos.trucks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/*
 {obj-id, traj-id, date(dd/mm/yyyy), time(hh:mm:ss), lat, lon, x, y}
 where (lat, lon) is in WGS84 reference system and (x, y) is in GGRS87 reference system.
 */

public class TrucksMongoDBGeoJSONModel {

    public static void main(String[] args) throws Exception {
        Map<Integer, Map<Integer, List<TruckPoint>>> trucks = new TreeMap<>();
        SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        File f = new File("/data/db/trucks/Trucks.txt");
        if (!f.exists()) {
            throw new FileNotFoundException();
        }
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String linha;
        while ((linha = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(linha, ";");
            int truck = Integer.parseInt(st.nextToken());

            Map<Integer, List<TruckPoint>> trajMap = trucks.get(truck);
            if (trajMap == null) {
                trajMap = new TreeMap<>();
                trucks.put(truck, trajMap);
            }

            int trajid = Integer.parseInt(st.nextToken());
            List<TruckPoint> ltp = trajMap.get(trajid);
            if (ltp == null) {
                ltp = new ArrayList<>();
                trajMap.put(trajid, ltp);
            }

            Date date = data.parse(st.nextToken() + ' ' + st.nextToken());
            // WGS84
            double lat = Double.parseDouble(st.nextToken());
            double lon = Double.parseDouble(st.nextToken());
            // GGRS87
            double x = Double.parseDouble(st.nextToken());
            double y = Double.parseDouble(st.nextToken());
            ltp.add(new TruckPoint(date, lat, lon, x, y));
        }
        br.close();
        fr.close();

        MongoClient mc = new MongoClient();
        DB db = mc.getDB("trucks");
        DBCollection dbc = db.createCollection("Trucks", null);
        for (Integer truckId : trucks.keySet()) {
            BasicDBObject truck = new BasicDBObject();
            truck.put("truckId", truckId);
            Map<Integer, List<TruckPoint>> trajMap = trucks.get(truckId);
            BasicDBList trajs = new BasicDBList();
            for (Integer trackId : trajMap.keySet()) {
                BasicDBObject traj = new BasicDBObject();
                traj.put("trajId", trackId);
                traj.put("type", "LineString");
                BasicDBList coords = new BasicDBList();
                for (TruckPoint tp : trajMap.get(trackId)) {
                    BasicDBList point = new BasicDBList();
                    point.add(tp.getLat());
                    point.add(tp.getLon());
                    coords.add(point);
                }
                traj.put("coordinates", coords);
                trajs.add(traj);
            }
            truck.put("trajectories", trajs);
            dbc.insert(truck);
        }
        mc.close();
    }
}
