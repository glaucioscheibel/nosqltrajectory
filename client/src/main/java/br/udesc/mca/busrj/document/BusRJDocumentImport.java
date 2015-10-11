package br.udesc.mca.busrj.document;

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
import br.udesc.mca.trajectory.model.TrajectoryPointData;
import br.udesc.mca.trajectory.model.TrajectorySegment;
import br.udesc.mca.trajectory.model.TrajectoryType;
import br.udesc.mca.trajectory.model.TrajectoryVersion;
import br.udesc.mca.trajectory.model.User;

public class BusRJDocumentImport {
    private static ObjectMapper om;

    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        File data = new File("C:/busrj");
        String[] ext = {"csv"};
        Iterator<File> ifs = FileUtils.iterateFiles(data, ext, true);
        User user = null;

        om = new ObjectMapper();
        om.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        om.setSerializationInclusion(Include.NON_NULL);
        om.setSerializationInclusion(Include.NON_EMPTY);

        long trajId = 0;
        int points = 0;
        int userid = 1;

        while (ifs.hasNext()) {
            File f = ifs.next();
            String trajDesc = f.getName();
            trajDesc = trajDesc.substring(0, trajDesc.indexOf('.'));
            System.out.println(trajDesc);
            user = new User(userid++);
            user.setName(trajDesc);
            Trajectory tr = null;
            TrajectoryVersion tv = null;
            TrajectorySegment seg = null;
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String linha = null;
            trajId++;
            tr = new Trajectory(trajId, trajDesc);
            tv = new TrajectoryVersion();
            tv.setVersion(1);
            tv.setUser(user);
            tv.setType(TrajectoryType.RAW);
            tv.setLastModified(new Date());
            tr.addVersion(tv);
            seg = new TrajectorySegment();
            tv.addSegment(seg);
            while ((linha = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(linha, ",");
                String dateTime = st.nextToken();
                String lin = st.nextToken();
                String lat = st.nextToken();
                String lng = st.nextToken();
                String vel = null;
                try {
                    vel = st.nextToken();
                } catch (Exception e) {}
                String dir = null;
                try {
                    dir = st.nextToken();
                } catch (Exception e) {}

                Date d = sdf.parse(dateTime);

                TrajectoryPoint tp = new TrajectoryPoint();
                tp.setLng(Float.parseFloat(lng)); // axis x longitude
                tp.setLat(Float.parseFloat(lat)); // axis y latitude
                tp.setTimestamp(d.getTime());

                TrajectoryPointData tpd = null;
                if (vel != null && !vel.equals("0.0")) {
                    tpd = new TrajectoryPointData();
                    tpd.setKey("velocidade");
                    tpd.setValue(vel);
                    tp.addData(tpd);
                }
                if (dir != null) {
                    tpd = new TrajectoryPointData();
                    tpd.setKey("direcao");
                    tpd.setValue(dir);
                    tp.addData(tpd);
                }
                seg.addPoint(tp);
                points++;
            }
            if (tr != null) {
                post(tr);
            }
            br.close();
            fr.close();
        }
    }

    private static void post(Trajectory tr) throws Exception {
        String json = om.writeValueAsString(tr);
        StringEntity entity = new StringEntity(json);
        entity.setContentType("application/json");
        CloseableHttpClient hc = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://127.0.0.1:8080/trajectorydocument/");
        post.addHeader("accept", "application/json");
        post.setEntity(entity);
        CloseableHttpResponse response = hc.execute(post);
        System.out.println(response.getStatusLine());
        response.getEntity();
    }
}
