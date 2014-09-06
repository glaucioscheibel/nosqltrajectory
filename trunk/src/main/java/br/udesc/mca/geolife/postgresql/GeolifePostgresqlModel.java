package br.udesc.mca.geolife.postgresql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.postgresql.geometric.PGpoint;

public class GeolifePostgresqlModel {

    public static void main(String[] args) throws Exception {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/trajectories", "postgres", "admin");
        createTable(con);
        importaGeolife(con);
        con.close();
        System.out.println("ok");
    }

    private static void importaGeolife(Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement("INSERT INTO geolife VALUES(?, ?, ?)");
        File data = new File("C:/data/db/geolife/data");
        String[] ext = { "plt" };
        Iterator<File> ifs = FileUtils.iterateFiles(data, ext, true);
        while (ifs.hasNext()) {
            File f = ifs.next();
            String name = f.getName();
            System.out.println(name);
            name = name.substring(0, f.getName().indexOf('.'));
            long trajId = Long.parseLong(name);
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
                // Field 7: Time as a string.
                String time = st.nextToken();
                ps.clearParameters();
                ps.setLong(1, trajId);
                ps.setDouble(2, days);
                PGpoint p = new PGpoint(dlat, dlng);
                ps.setObject(3, p);
                try {
                    ps.executeUpdate();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            br.close();
            fr.close();
        }
        ps.close();
    }

    private static void createTable(Connection con) throws Exception {
        StringBuilder ddl = new StringBuilder();
        ddl.append("CREATE TABLE IF NOT EXISTS geolife (");
        ddl.append("trajid bigint,");
        ddl.append("datetime decimal,");
        ddl.append("latlon point,");
        ddl.append("PRIMARY KEY(trajid, datetime)");
        ddl.append(")");
        Statement st = con.createStatement();
        st.executeUpdate(ddl.toString());
        st.close();
    }
}
