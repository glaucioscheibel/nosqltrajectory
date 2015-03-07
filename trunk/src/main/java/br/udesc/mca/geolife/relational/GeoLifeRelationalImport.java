package br.udesc.mca.geolife.relational;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.dao.user.UserDAO;
import br.udesc.mca.trajectory.model.Trajectory;
import br.udesc.mca.trajectory.model.TrajectoryPoint;
import br.udesc.mca.trajectory.model.TrajectoryType;
import br.udesc.mca.trajectory.model.TrajectoryVersion;
import br.udesc.mca.trajectory.model.User;

public class GeoLifeRelationalImport {

    private static Map<Integer, List<TransportationMode>> transModes;

    @SuppressWarnings({ "unchecked", "unused" })
    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PersistenceDAO<Trajectory> dao = (PersistenceDAO<Trajectory>) PersistenceDAO
                .getInstance(PersistenceModel.RELATIONAL);

        GeoLifeRelationalImport.importTransportationMode();

        File data = new File("C:/geolife/data");
        String[] ext = { "plt" };
        Iterator<File> ifs = FileUtils.iterateFiles(data, ext, true);
        boolean type = false;

        UserDAO udao = new UserDAO();
        int prevUser = -1;
        User user = null;

        while (ifs.hasNext()) {
            File f = ifs.next();
            String dir = f.getParentFile().getParentFile().getName();
            int userId = Integer.parseInt(dir);
            if (userId != prevUser) {
                user = new User(userId, "User " + userId);
                udao.create(user);
                prevUser = userId;
            }
            String trajDesc = f.getName();
            trajDesc = trajDesc.substring(0, trajDesc.indexOf('.'));
            long trajId = Long.parseLong(trajDesc);
            Trajectory tr = new Trajectory(trajId, trajDesc);
            System.out.println(trajDesc);
            TrajectoryVersion tv = new TrajectoryVersion();
            tv.setVersion(1);
            tv.setUser(user);
            tv.setType(TrajectoryType.RAW);
            tv.setLastModified(new Date());
            tr.addVersion(tv);
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
                TrajectoryPoint tp = new TrajectoryPoint();
                tp.setLng(Float.parseFloat(lng)); // axis x longitude
                tp.setLat(Float.parseFloat(lat)); // axis y latitude
                tp.setH(Float.parseFloat(alt)); // height of the point
                tp.setTimestamp(sdf.parse(date + ' ' + time).getTime());

                Date ddate = sdf.parse(date + ' ' + time);

                type = false;
                if (transModes.containsKey(userId)) {
                    List<TransportationMode> ltm = transModes.get(userId);
                    for (TransportationMode tm : ltm) {
                        if (tm.isBetween(ddate)) {
                            tp.setTransportationMode(tm.getType());
                            type = true;
                            break;
                        }
                    }
                }
                if (!type) {
                    tp.setTransportationMode(null);
                }
                tv.addPoint(tp);
            }
            br.close();
            fr.close();
            dao.store(tr);
        }
    }

    private static void importTransportationMode() throws Exception {
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        transModes = new TreeMap<>();
        File data = new File("C:/geolife/data");
        String[] ext = { "txt" };
        Iterator<File> ifs = FileUtils.iterateFiles(data, ext, true);
        while (ifs.hasNext()) {
            File f = ifs.next();
            String dir = f.getParentFile().getName();
            List<TransportationMode> ltm = new ArrayList<>();
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            String linha = null;
            while ((linha = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(linha, "\t");
                TransportationMode tm = new TransportationMode();
                tm.setDt1(dateParser.parse(st.nextToken()));
                tm.setDt2(dateParser.parse(st.nextToken()));
                tm.setType(st.nextToken());
                ltm.add(tm);
            }
            transModes.put(Integer.parseInt(dir), ltm);
            br.close();
            fr.close();
        }
        System.out.println(transModes);
    }
}
