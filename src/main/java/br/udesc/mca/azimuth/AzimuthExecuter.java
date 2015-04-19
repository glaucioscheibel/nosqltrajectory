package br.udesc.mca.azimuth;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;
import br.udesc.mca.trajectory.model.TrajectorySegment;
import br.udesc.mca.trajectory.model.TrajectorySegmentData;
import br.udesc.mca.trajectory.model.TrajectoryVersion;

public class AzimuthExecuter {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        PersistenceDAO<Trajectory> dao = (PersistenceDAO<Trajectory>) PersistenceDAO
                .getInstance(PersistenceModel.RELATIONAL);
        Trajectory t = dao.findById(20150418093055L);
        TrajectoryVersion tv = t.getVersions().get(1);
        for (TrajectorySegment ts : tv.getSegments()) {
            float lat1 = ts.getPoints().get(0).getLat();
            float lgn1 = ts.getPoints().get(0).getLng();
            float lat2 = ts.getPoints().get(1).getLat();
            float lgn2 = ts.getPoints().get(1).getLng();
            double azi = Azimuth.azimuth(lat1, lgn1, lat2, lgn2);
            TrajectorySegmentData tsd = new TrajectorySegmentData();
            tsd.setKey("azimuth");
            tsd.setValue(String.valueOf(azi));
            ts.addData(tsd);
        }
        dao.store(t);
    }
}
