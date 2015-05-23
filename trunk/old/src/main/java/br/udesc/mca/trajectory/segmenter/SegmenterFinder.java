package br.udesc.mca.trajectory.segmenter;

import java.util.List;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;
import br.udesc.mca.trajectory.model.TrajectorySegment;
import br.udesc.mca.trajectory.model.TrajectorySegmentData;
import br.udesc.mca.trajectory.model.TrajectoryVersion;

public class SegmenterFinder {

    public static void main(String[] args) {
        PersistenceDAO dao = PersistenceDAO.getInstance(PersistenceModel.RELATIONAL);
        List<Trajectory> listaTrajetorias = dao.findAll();

        for (Trajectory trajectory : listaTrajetorias) {
            List<TrajectoryVersion> versoes = trajectory.getVersions();
            for (TrajectoryVersion trajectoryVersion : versoes) {
                if (trajectoryVersion.getVersion() == 2) {
                    List<TrajectorySegment> segments = trajectoryVersion.getSegments();
                    for (TrajectorySegment trajectorySegment : segments) {
                        List<TrajectorySegmentData> informacoes = trajectorySegment.getData();

                        for (TrajectorySegmentData trajectorySegmentData : informacoes) {
                            if (trajectorySegmentData.getKey().trim().toLowerCase().equals("azimuthdiff")) {
                                System.out.println("Diferen�a de azimute: " + trajectorySegmentData.getValue());
                            }
                        }
                    }
                }
            }
        }
    }
}
