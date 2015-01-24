package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Trajectory implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Date lastModified;
    private List<TrajectoryVersion> versions;

    public Trajectory() {}

    public Trajectory(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        return this.id.hashCode();
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
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
