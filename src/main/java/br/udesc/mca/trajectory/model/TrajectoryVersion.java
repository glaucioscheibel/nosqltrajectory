package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrajectoryVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    private int version;
    private TrajectoryType type;
    private Date modified;
    private List<TrajectoryPoint> points;
    private TrajectoryData data;
    private List<TrajectoryProcessHistory> history;

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

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

    public TrajectoryData getData() {
        return this.data;
    }

    public void setData(TrajectoryData data) {
        this.data = data;
    }

    public List<TrajectoryProcessHistory> getHistory() {
        return this.history;
    }

    public void addHistory(TrajectoryProcessHistory history) {
        if (this.history == null) {
            this.history = new ArrayList<>();
        }
        this.history.add(history);
    }
}
