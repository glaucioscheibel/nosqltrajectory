package br.udesc.mca.trajectory.microservice.trajectory;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.*;
import br.udesc.mca.trajectory.segmenter.SegmenterService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/trajectory")
public class TrajectoryResource {
    private final PersistenceModel model;

    public TrajectoryResource(String persistenceModel){
        this.model = PersistenceModel.valueOf(persistenceModel);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public br.udesc.mca.trajectory.model.Trajectory get(@PathParam("id") Long id){
    //TODO: acertar o classloader do DAO para funcionar de dentro do fatjar
        PersistenceDAO<br.udesc.mca.trajectory.model.Trajectory> dao = (PersistenceDAO<br.udesc.mca.trajectory.model.Trajectory>) PersistenceDAO
                .getInstance(model);

        return  dao.findById(id);
    }



    //para teste local somente
    public  static void main(String... a){
/*        br.udesc.mca.trajectory.model.Trajectory t = new br.udesc.mca.trajectory.model.Trajectory();
        t.setId(10l);
        t.setDescription("Trajectory Desc");
        t.setLastModified(new java.util.Date());
        t.setOriginalTrajectory(1);

        TrajectoryVersion version = new TrajectoryVersion();
        version.setVersion(1);
        version.setType(TrajectoryType.RAW);
        version.setLastModified(new java.util.Date());
        t.addVersion(version);

        TrajectorySegment seg = new TrajectorySegment();
        seg.setTransportationMode(TransportationMode.RUN);

        TrajectoryPoint p = new TrajectoryPoint();
        p.setTimestamp(System.currentTimeMillis());
        p.setLng(-112.2550785337791f);
        p.setLat(36.07954952145647f);
        seg.addPoint(p);

        p = new TrajectoryPoint();
        p.setTimestamp(System.currentTimeMillis());
        p.setLng(-112.2549277039738f);
        p.setLat(36.08117083492122f);
        seg.addPoint(p);

        p = new TrajectoryPoint();
        p.setTimestamp(System.currentTimeMillis());
        p.setLng(-112.2656969554589f);
        p.setLat(36.08649599090644f);
        seg.addPoint(p);

        List<TrajectorySegment> segs =  new SegmenterService().segmenter(seg, 2);
        for(TrajectorySegment s: segs){
            version.addSegment(s);
        }

        PersistenceDAO<br.udesc.mca.trajectory.model.Trajectory> dao = (PersistenceDAO<br.udesc.mca.trajectory.model.Trajectory>) PersistenceDAO
                .getInstance(PersistenceModel.RELATIONAL);

        t = dao.store(t);
        System.out.println(t.getId());*/

        System.out.println(new TrajectoryResource("RELATIONAL").get(10l));
    }
}
