package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Trajectory implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("_id")
    @Id
    private long id;
    private String description;
    private int originalTrajectory;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @OneToMany(cascade = CascadeType.ALL)
    private List<TrajectoryVersion> versions;

    public Trajectory() {}

    public Trajectory(long id) {
        this.id = id;
    }

    public Trajectory(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public List<TrajectoryVersion> getVersions() {
        return this.versions;
    }

    public void addVersion(TrajectoryVersion version) {
        if (this.versions == null) {
            this.versions = new ArrayList<>();
        }
        this.versions.add(version);
    }

    public void removeVersion(TrajectoryVersion version) {
        if (this.versions != null) {
            this.versions.remove(version);
        }
    }

    public int getOriginalTrajectory() {
        return this.originalTrajectory;
    }

    public void setOriginalTrajectory(int originalTrajectory) {
        this.originalTrajectory = originalTrajectory;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.id ^ (this.id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Trajectory other = (Trajectory) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}