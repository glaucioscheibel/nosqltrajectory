package br.udesc.mca.geolife.mongodb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class GeoLifeMongoDBModel {

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private GeoLifeMongoDBModel() {
	}

	public static void main(String[] args) throws Exception {
		MongoClient mc = new MongoClient();
		DB db = mc.getDB("geolife");
		DBCollection dbc = db.createCollection("trajectories", null);
		File data = new File("C:/data/db/geolife/data");
		String[] ext = { "plt" };
		Iterator<File> ifs = FileUtils.iterateFiles(data, ext, true);
		while (ifs.hasNext()) {
			File f = ifs.next();
			String dir = f.getParentFile().getParentFile().getName();
			String name = f.getName();
			System.out.println(name);
			name = name.substring(0, f.getName().indexOf('.'));
			DBObject dbo = new BasicDBObject();
			BasicDBList traj = new BasicDBList();
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
				DBObject point = new BasicDBObject();
				point.put("lat", Double.valueOf(lat));
				point.put("lng", Double.valueOf(lng));
				if (dalt.doubleValue() != -777) {
					point.put("alt", dalt);
				}
				try {
					point.put("date", sdf.parse(date + ' ' + time));
				} catch (ParseException pe) {
					System.out.println("Erro: " + pe.getMessage());
				}
				traj.add(point);
			}
			dbo.put("user", Integer.valueOf(dir));
			dbo.put("trajId", Long.valueOf(name));
			dbo.put("points", traj);
			dbc.insert(dbo);
			br.close();
			fr.close();
		}
		mc.close();
	}
}
