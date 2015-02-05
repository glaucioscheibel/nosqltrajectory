package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TrajectoryVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajectoryid")
    private Trajectory trajectory;
    private int version;
    private TrajectoryType type;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @OneToMany()
    private List<TrajectoryPoint> points;
    private TrajectoryData data;
    @OneToMany
    private List<TrajectoryProcessHistory> history;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Trajectory getTrajectory() {
        return this.trajectory;
    }

    public void setTrajectory(Trajectory trajectory) {
        this.trajectory = trajectory;
    }

    public TrajectoryType getType() {
        return this.type;
    }

    public void setType(TrajectoryType type) {
        this.type = type;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
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
