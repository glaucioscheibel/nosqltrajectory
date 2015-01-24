package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrajectoryVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    private TrajectoryType type;
    private Date modified;
    private List<TrajectoryPoint> points;

    public TrajectoryType getType() {
        return this.type;
    }

    public void setType(TrajectoryType type) {
        this.type = type;
    }

    public Date getModified() {
        return this.modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public List<TrajectoryPoint> getPoints() {
        return this.points;
    }
    
    public void addPoint(TrajectoryPoint point) {
        if (this.points == null) {
            this.points = new ArrayList<>();
        }
        this.points.add(point);
    }
}
