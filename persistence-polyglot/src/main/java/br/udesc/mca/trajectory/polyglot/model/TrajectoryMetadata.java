package br.udesc.mca.trajectory.polyglot.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.TrajectoryType;

@Entity
public class TrajectoryMetadata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long trajectoryId;
    private long trajectoryVersion;
    private TrajectoryType type;
    private PersistenceModel persistence;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTrajectoryId() {
        return trajectoryId;
    }

    public void setTrajectoryId(long trajectoryId) {
        this.trajectoryId = trajectoryId;
    }

    public long getTrajectoryVersion() {
        return trajectoryVersion;
    }

    public void setTrajectoryVersion(long trajectoryVersion) {
        this.trajectoryVersion = trajectoryVersion;
    }

    public TrajectoryType getType() {
        return type;
    }

    public void setType(TrajectoryType type) {
        this.type = type;
    }

    public PersistenceModel getPersistence() {
        return persistence;
    }

    public void setPersistence(PersistenceModel persistence) {
        this.persistence = persistence;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TrajectoryMetadata other = (TrajectoryMetadata) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TrajectoryMetadata [id=" + id + ", trajectoryId=" + trajectoryId + ", trajectoryVersion="
                + trajectoryVersion + ", type=" + type + ", persistence=" + persistence + "]";
    }
}
