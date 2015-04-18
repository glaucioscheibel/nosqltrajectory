package br.udesc.mca.trajectory.segmenter;

import java.util.ArrayList;
import java.util.List;
import br.udesc.mca.trajectory.model.TrajectoryPoint;
import br.udesc.mca.trajectory.model.TrajectorySegment;

public class SegmenterService {
    public List<TrajectorySegment> segmenter(TrajectorySegment segment, int points) {
        long timestamp = System.currentTimeMillis();
        List<TrajectorySegment> ret = new ArrayList<>();
        int aux = 0;
        TrajectorySegment ts = new TrajectorySegment();
        ts.setTransportationMode(segment.getTransportationMode());
        List<TrajectoryPoint> ltp = segment.getPoints();
        for (int i = 0; i < ltp.size(); i++) {
            TrajectoryPoint tp = ltp.get(i);
            aux++;
            TrajectoryPoint taux = new TrajectoryPoint();
            taux.setH(tp.getH());
            taux.setLat(tp.getLat());
            taux.setLng(tp.getLng());
            taux.setTimestamp(timestamp);
            ts.addPoint(taux);
            if (aux == points) {
                ret.add(ts);
                ts = new TrajectorySegment();
                ts.setTransportationMode(segment.getTransportationMode());
                aux = 0;
                i--;
            }
        }
        return ret;
    }
}
