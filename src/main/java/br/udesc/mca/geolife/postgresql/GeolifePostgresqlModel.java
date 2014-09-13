package br.udesc.mca.geolife.postgresql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.postgresql.geometric.PGpoint;

public class GeolifePostgresqlModel {

	public static void main(String[] args) throws Exception {
		Connection con = DriverManager
				.getConnection("jdbc:postgresql://localhost/trajectories",
						"postgres", "admin");
		createTable(con);
		importaGeolife(con);
		con.close();
		System.out.println("ok");
	}

	private static void importaGeolife(Connection con) throws Exception {
		con.setAutoCommit(false);
		SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeParser = new SimpleDateFormat("hh:mm:ss");
		PreparedStatement ps = con
				.prepareStatement("INSERT INTO geolife VALUES(?, ?, ?, ?, ?, ?)");
		File data = new File("C:/data/db/geolife/data");
		String[] ext = { "plt" };
		Iterator<File> ifs = FileUtils.iterateFiles(data, ext, true);
		int commitCount = 0;
		long contador = 0;
		while (ifs.hasNext()) {
			File f = ifs.next();
			String dir = f.getParentFile().getParentFile().getName();
			int dirId = Integer.parseInt(dir);
			String name = f.getName();
			System.out.println(name + " - " + String.valueOf(contador++));
			name = name.substring(0, f.getName().indexOf('.'));
			long trajId = Long.parseLong(name);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			// Line 1-6 are useless in this dataset, and can be ignored.
			for (int i = 1; i <= 6; i++) {
				br.readLine();
			}
			String linha = null;
			int seq = 1;
			while ((linha = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(linha, ",");
				// Field 1: Latitude in decimal degrees.
				String lat = st.nextToken();
				double dlat = Double.parseDouble(lat);
				// Field 2: Longitude in decimal degrees.
				String lng = st.nextToken();
				double dlng = Double.parseDouble(lng);
				// Field 3: All set to 0 for this dataset.
				st.nextToken();
				// Field 4: Altitude in feet (-777 if not valid).
				String alt = st.nextToken();
				// Field 5: Date - number of days (with fractional part) that
				// have passed since 12/30/1899.
				double days = Double.parseDouble(st.nextToken());
				// Field 6: Date as a string.
				String date = st.nextToken();
				Date ddate = dateParser.parse(date);
				// Field 7: Time as a string.
				String time = st.nextToken();
				Date dtime = timeParser.parse(time);
				ps.clearParameters();
				ps.setInt(1, dirId);
				ps.setLong(2, trajId);
				ps.setInt(3, seq++);
				PGpoint p = new PGpoint(dlng, dlat);
				ps.setObject(4, p);
				ps.setDate(5, new java.sql.Date(ddate.getTime()));
				ps.setTime(6, new Time(dtime.getTime()));
				try {
					ps.executeUpdate();
				} catch (Exception e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
				if (commitCount++ >= 10000) {
					con.commit();
					commitCount = 0;
				}
			}
			br.close();
			fr.close();
		}
		ps.close();
		con.commit();
		con.setAutoCommit(true);
	}

	private static void createTable(Connection con) throws Exception {
		StringBuilder ddl = new StringBuilder();
		ddl.append("CREATE TABLE IF NOT EXISTS geolife (");
		ddl.append("id integer,");
		ddl.append("trajid bigint,");
		ddl.append("seq int,");
		ddl.append("latlon point,");
		ddl.append("date date,");
		ddl.append("time time,");
		ddl.append("PRIMARY KEY(id, trajid, seq)");
		ddl.append(")");
		Statement st = con.createStatement();
		st.executeUpdate(ddl.toString());
		st.close();
	}
}
