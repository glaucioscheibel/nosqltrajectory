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
public class TrajectoryVersionData implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajectoryid")
    private TrajectoryVersion trajectoryVersion;

    private String key;
    private String value;

    public TrajectoryVersionData() {}

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TrajectoryVersion getTrajectoryVersion() {
        return this.trajectoryVersion;
    }

    public void setTrajectoryVersion(TrajectoryVersion trajectoryVersion) {
        this.trajectoryVersion = trajectoryVersion;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
