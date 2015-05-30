package br.udesc.mca.segmenter.service;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import br.udesc.mca.segmenter.SegmenterByPoints;
import br.udesc.mca.trajectory.model.Trajectory;
import br.udesc.mca.trajectory.model.TrajectorySegment;
import br.udesc.mca.trajectory.model.TrajectoryType;
import br.udesc.mca.trajectory.model.TrajectoryVersion;

@Path("/segmenter")
@Produces(MediaType.APPLICATION_JSON)
public class SegmenterResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/segmentByPoints/{points}")
    public Trajectory segmentByPoints(@PathParam("points") int points, Trajectory trajectory) {
        List<TrajectoryVersion> ltv = trajectory.getVersions();
        TrajectoryVersion tv = ltv.get(ltv.size() - 1);
        List<TrajectorySegment> lts = tv.getSegments();
        TrajectorySegment ts = lts.get(lts.size() - 1);
        lts = SegmenterByPoints.segmenter(ts, points);
        TrajectoryVersion tv2 = new TrajectoryVersion();
        tv2.setVersion(2);
        tv2.setType(TrajectoryType.SEGMENTED);
        for (TrajectorySegment s : lts) {
            tv2.addSegment(s);
        }
        trajectory.addVersion(tv2);
        return trajectory;
    }
}
