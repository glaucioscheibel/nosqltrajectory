package br.udesc.mca.geolife.postgresql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

@Deprecated
public class GeoLifePostgreSQLModel {
    private static Map<Integer, List<TranspMode>> transModes;

    public static void main(String[] args) throws Exception {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/trajectories", "postgres", "admin");
        createTable(con);
        importaTransportationMode();
        importaGeolife(con);
        con.close();
        System.out.println("ok");
    }

    private static void importaTransportationMode() throws Exception {
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        transModes = new TreeMap<>();
        File data = new File("C:/data/db/geolife/data");
        String[] ext = { "txt" };
        Iterator<File> ifs = FileUtils.iterateFiles(data, ext, true);
        while (ifs.hasNext()) {
            File f = ifs.next();
            String dir = f.getParentFile().getName();
            List<TranspMode> ltm = new ArrayList<>();
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            String linha = null;
            while ((linha = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(linha, "\t");
                TranspMode tm = new TranspMode();
                tm.dt1 = dateParser.parse(st.nextToken());
                tm.dt2 = dateParser.parse(st.nextToken());
                tm.type = st.nextToken();
                ltm.add(tm);
            }
            transModes.put(Integer.parseInt(dir), ltm);
            br.close();
            fr.close();
        }
        System.out.println(transModes);
    }

    private static void importaGeolife(Connection con) throws Exception {
        con.setAutoCommit(false);
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        PreparedStatement ps = con.prepareStatement("INSERT INTO geolife VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
        File data = new File("C:/data/db/geolife/data");
        String[] ext = { "plt" };
        Iterator<File> ifs = FileUtils.iterateFiles(data, ext, true);
        boolean type = false;
        int commitCount = 0;
        long contador = 0;
        while (ifs.hasNext()) {
            File f = ifs.next();
            String dir = f.getParentFile().getParentFile().getName();
            int userId = Integer.parseInt(dir);
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
                double dalt = Double.parseDouble(alt);
                // Field 5: Date - number of days (with fractional part) that
                // have passed since 12/30/1899.
                st.nextToken();
                // Field 6: Date as a string.
                String date = st.nextToken();
                // Field 7: Time as a string.
                String time = st.nextToken();

                Date ddate = dateParser.parse(date + ' ' + time);

                ps.clearParameters();
                ps.setInt(1, userId);
                ps.setLong(2, trajId);
                ps.setInt(3, seq++);
                ps.setDouble(4, dlat);
                ps.setDouble(5, dlng);
                if (dalt != -777) {
                    ps.setDouble(6, dalt);
                } else {
                    ps.setNull(6, Types.NUMERIC);
                }
                ps.setTimestamp(7, new java.sql.Timestamp(ddate.getTime()));
                type = false;
                if (transModes.containsKey(userId)) {
                    List<TranspMode> ltm = transModes.get(userId);
                    for (TranspMode tm : ltm) {
                        if (tm.isBetween(ddate)) {
                            ps.setString(8, tm.type);
                            type = true;
                            break;
                        }
                    }
                }
                if (!type) {
                    ps.setNull(8, Types.VARCHAR);
                }
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
        ddl.append("CREATE TABLE geolife (");
        ddl.append("userid integer,");
        ddl.append("trajid bigint,");
        ddl.append("seq int,");
        ddl.append("latitude numeric(10,6),");
        ddl.append("longitude numeric(10,6),");
        ddl.append("altitude numeric(8,2),");
        ddl.append("date timestamp,");
        ddl.append("transpmode varchar,");
        ddl.append("PRIMARY KEY(userid, trajid, seq)");
        ddl.append(")");
        Statement st = con.createStatement();
        st.executeUpdate("drop table if exists geolife");
        st.executeUpdate(ddl.toString());
        st.close();
    }
}
