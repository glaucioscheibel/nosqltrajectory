package br.udesc.mca.geolife.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.io.FileUtils;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.model.Trajectory;
import br.udesc.mca.trajectory.model.TrajectoryPoint;
import br.udesc.mca.trajectory.model.TrajectorySegment;
import br.udesc.mca.trajectory.model.TrajectoryType;
import br.udesc.mca.trajectory.model.TrajectoryVersion;
import br.udesc.mca.trajectory.model.TransportationMode;
import br.udesc.mca.trajectory.model.User;

public abstract class GeolifeImporter {
    private List<TranspAux> transps;
    protected PersistenceDAO dao;
    protected UserDAO udao;

    @SuppressWarnings("unused")
    public void importData() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.transps = new ArrayList<>();
        File data = new File("C:/geolife/data");
        String[] ext = {"plt"};
        Iterator<File> ifs = FileUtils.iterateFiles(data, ext, true);
        boolean type = false;
        int prevUser = -1;
        User user = null;

        while (ifs.hasNext()) {
            File f = ifs.next();
            String dir = f.getParentFile().getParentFile().getName();
            int userId = Integer.parseInt(dir);
            if (userId != prevUser) {
                user = new User(userId, "User " + userId);
                if (this.udao != null) {
                    this.udao.create(user);
                }
                prevUser = userId;
                File transp = new File("c:/geolife/data/" + dir + "/labels.txt");
                importTransportationMode(transp);
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
            TrajectorySegment seg = new TrajectorySegment();
            tv.addSegment(seg);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            // Line 1-6 are useless in this dataset, and can be ignored.
            for (int i = 1; i <= 6; i++) {
                br.readLine();
            }
            String linha = null;
            while ((linha = br.readLine()) != null) {
                // pular linhas em branco
                if (linha.trim().length() == 0) {
                    continue;
                }
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
                for (TranspAux t : this.transps) {
                    if (ddate.after(t.dt1) && ddate.before(t.dt2)) {
                        seg.setTransportationMode(t.mode);
                        break;
                    }
                }
                seg.addPoint(tp);
            }
            br.close();
            fr.close();
            dao.store(tr);
        }
    }

    private void importTransportationMode(File f) throws Exception {
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        this.transps.clear();
        if (f.exists()) {
            System.out.println(f.getAbsolutePath());
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            String linha = null;
            while ((linha = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(linha, "\t");
                Date dt1 = dateParser.parse(st.nextToken());
                Date dt2 = dateParser.parse(st.nextToken());
                TransportationMode tmode = TransportationMode.valueOf(st.nextToken().toUpperCase());
                this.transps.add(new TranspAux(dt1, dt2, tmode));
            }
            br.close();
            fr.close();
        }
    }

    public void setDao(PersistenceDAO dao) {
        this.dao = dao;
    }

    public void setUserDao(UserDAO dao) {
        this.udao = dao;
    }

    private static class TranspAux {
        Date dt1;
        Date dt2;
        TransportationMode mode;

        public TranspAux(Date dt1, Date dt2, TransportationMode mode) {
            this.dt1 = dt1;
            this.dt2 = dt2;
            this.mode = mode;
        }
    }
}
