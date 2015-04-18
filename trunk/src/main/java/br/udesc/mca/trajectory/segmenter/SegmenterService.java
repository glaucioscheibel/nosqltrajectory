package br.udesc.mca.trajectory.segmenter;

import java.util.ArrayList;
import java.util.List;
import br.udesc.mca.trajectory.model.TrajectoryPoint;
import br.udesc.mca.trajectory.model.TrajectorySegment;

public class SegmenterService {
    public List<TrajectorySegment> segmenter(TrajectorySegment segment, int points) {
        List<TrajectorySegment> ret = new ArrayList<>();
        int aux = 0;
        TrajectorySegment ts = new TrajectorySegment();
        for (TrajectoryPoint tp : segment.getPoints()) {
            aux++;
            TrajectoryPoint taux = new TrajectoryPoint();
            taux.setH(tp.getH());
            taux.setLat(tp.getLat());
            taux.setLng(tp.getLng());
            ts.addPoint(taux);
            if (aux == points) {
                ret.add(ts);
                ts = new TrajectorySegment();
                ts.addPoint(taux);
                aux = 1;
            }
        }
        return ret;
    }
}
