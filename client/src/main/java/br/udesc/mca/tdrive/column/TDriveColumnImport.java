package br.udesc.mca.tdrive.column;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
import br.udesc.mca.trajectory.model.TrajectorySegmentData;
import br.udesc.mca.trajectory.model.TrajectoryType;
import br.udesc.mca.trajectory.model.TrajectoryVersion;
import br.udesc.mca.trajectory.model.TrajectoryVersionData;
import br.udesc.mca.trajectory.model.User;

public final class TDriveColumnImport {
    private static ObjectMapper om;
    
    private TDriveColumnImport() {}
    
    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        File data = new File("C:/tdrive");
        String[] ext = {"txt"};
        Iterator<File> ifs = FileUtils.iterateFiles(data, ext, true);
        User user = null;

        om = new ObjectMapper();
        om.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        om.setSerializationInclusion(Include.NON_NULL);
        om.setSerializationInclusion(Include.NON_EMPTY);

        long trajId = 0;
        int points = 0;

        if (ifs.hasNext()) {
            File f = ifs.next();
            String trajDesc = f.getName();
            trajDesc = trajDesc.substring(0, trajDesc.indexOf('.'));
            user = new User(Integer.parseInt(trajDesc));
            Trajectory tr = null;
            TrajectoryVersion tv = null;
            TrajectorySegment seg = null;
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String linha = null;
            int day = 0;
            while ((linha = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(linha, ",");
                String taxiId = st.nextToken();
                String dateTime = st.nextToken();
                String lng = st.nextToken();
                String lat = st.nextToken();

                Date d = sdf.parse(dateTime);

                if (day != d.getDate()) {
                    if (tr != null) {
                        System.out.println(user.getId() + " " + day + " " + points);
                        post(tr);
                        points = 0;
                    }
                    day = d.getDate();
                    trajId++;
                    tr = new Trajectory(trajId, trajDesc);
                    tv = new TrajectoryVersion();
                    tv.setVersion(1);
                    tv.setUser(user);
                    tv.setType(TrajectoryType.RAW);
                    tv.setLastModified(new Date());
                    TrajectoryVersionData tvd = new TrajectoryVersionData();
                    tvd.setDataKey("chave");
                    tvd.setDataValue("valor");
                    tv.addData(tvd);
                    tr.addVersion(tv);
                    seg = new TrajectorySegment();
                    TrajectorySegmentData tsd = new TrajectorySegmentData();
                    tsd.setDataKey("chave");
                    tsd.setDataValue("valor");
                    seg.addData(tsd);
                    tv.addSegment(seg);
                }

                TrajectoryPoint tp = new TrajectoryPoint();
                tp.setLng(Float.parseFloat(lng)); // axis x longitude
                tp.setLat(Float.parseFloat(lat)); // axis y latitude
                tp.setTimestamp(d.getTime());
                TrajectoryPointData tpd = new TrajectoryPointData();
                tpd.setDataKey("chave");
                tpd.setDataValue("valor");
                tp.addData(tpd);
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
        FileWriter fw = new FileWriter("/jsons/" + tr.getId() + ".json");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(json);
        bw.flush();
        bw.close();
        fw.close();
        StringEntity entity = new StringEntity(json);
        entity.setContentType("application/json");
        CloseableHttpClient hc = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://127.0.0.1:8080/trajectorycolumn/");
        post.addHeader("accept", "application/json");
        post.setEntity(entity);
        CloseableHttpResponse response = hc.execute(post);
        System.out.println(response.getStatusLine());
        response.getEntity();
    }

}
