package br.udesc.mca.trajectory.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class TrajectoryPoint implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajectoryid")
    private TrajectoryVersion trajectoryVersion;
    private float x;
    private float y;
    private long timestamp;

    public int getId() {
        return this.id;
    }

    public TrajectoryVersion getTrajectoryVersion() {
        return this.trajectoryVersion;
    }

    public void setTrajectoryVersion(TrajectoryVersion trajectoryVersion) {
        this.trajectoryVersion = trajectoryVersion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
