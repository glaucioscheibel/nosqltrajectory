package br.udesc.mca.geolife.keyvalue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;
import br.udesc.mca.trajectory.model.TrajectoryPoint;
import br.udesc.mca.trajectory.model.TrajectoryType;
import br.udesc.mca.trajectory.model.TrajectoryVersion;

public class GeoLifeKeyValueJsonImport {

    @SuppressWarnings({ "unchecked", "unused" })
    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PersistenceDAO<Trajectory> dao = (PersistenceDAO<Trajectory>) PersistenceDAO
                .getInstance(PersistenceModel.KEY_VALUE_JSON);
        File data = new File("C:/geolife/data");
        String[] ext = { "plt" };
        Iterator<File> ifs = FileUtils.iterateFiles(data, ext, true);
        while (ifs.hasNext()) {
            File f = ifs.next();
            String trajDesc = f.getName();
            trajDesc = trajDesc.substring(0, trajDesc.indexOf('.'));
            long trajId = Long.parseLong(trajDesc);
            Trajectory tr = new Trajectory(trajId, trajDesc);
            System.out.println(trajDesc);
            TrajectoryVersion tv = new TrajectoryVersion();
            tv.setVersion(1);
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
                tp.setY(Float.parseFloat(lat));
                tp.setX(Float.parseFloat(lng));
                tp.setTimestamp(sdf.parse(date + ' ' + time).getTime());
                tv.addPoint(tp);
            }
            br.close();
            fr.close();
            dao.store(tr);
        }
    }
}
