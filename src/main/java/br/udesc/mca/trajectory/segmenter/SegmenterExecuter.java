package br.udesc.mca.trajectory.segmenter;

import java.util.List;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;
import br.udesc.mca.trajectory.model.TrajectorySegment;
import br.udesc.mca.trajectory.model.TrajectoryVersion;

public class SegmenterExecuter {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        PersistenceDAO<Trajectory> dao = (PersistenceDAO<Trajectory>) PersistenceDAO
                .getInstance(PersistenceModel.RELATIONAL);
        Trajectory t = dao.findById(20150418093055L);
        TrajectorySegment s = t.getVersions().get(0).getSegments().get(0);
        SegmenterService ss = new SegmenterService();
        List<TrajectorySegment> lts = ss.segmenter(s, 2);
        TrajectoryVersion tv = new TrajectoryVersion();
        tv.setVersion(2);
        for (TrajectorySegment ts : lts) {
            tv.addSegment(ts);
        }
        t.addVersion(tv);
        dao.store(t);
    }
}
