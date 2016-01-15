package br.udesc.mca.geolife.column;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import br.udesc.mca.trajectory.model.Trajectory;
import br.udesc.mca.trajectory.model.TrajectoryPoint;
import br.udesc.mca.trajectory.model.TrajectorySegment;
import br.udesc.mca.trajectory.model.TrajectoryType;
import br.udesc.mca.trajectory.model.TrajectoryVersion;
import br.udesc.mca.trajectory.model.User;

public class GeoLifeColumnImport {

    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
        long ini = System.currentTimeMillis();
        int cont = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        File data = new File("C:/geolife/data");
        String[] ext = {"plt"};
        Iterator<File> ifs = FileUtils.iterateFiles(data, ext, true);
        User user = null;

        ObjectMapper om = new ObjectMapper();
        om.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        om.setSerializationInclusion(Include.NON_NULL);
        om.setSerializationInclusion(Include.NON_EMPTY);

        while (ifs.hasNext()) {
            cont++;
            File f = ifs.next();
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

                seg.addPoint(tp);
            }
            br.close();
            fr.close();

            String json = om.writeValueAsString(tr);
            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json");
            CloseableHttpClient hc = HttpClients.createDefault();
            HttpPost post = new HttpPost("http://127.0.0.1:8080/trajectorycolumn/");
            post.addHeader("accept", "application/json");
            post.setEntity(entity);
            CloseableHttpResponse response = hc.execute(post);
            System.out.println(response.getStatusLine());
        }
        System.out.println("Trajetórias: " + cont);
        System.out.println("Tempo:       " + (System.currentTimeMillis() - ini));
    }
}
