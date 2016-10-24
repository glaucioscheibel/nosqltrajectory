package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trajectory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    private String description;
    private int originalTrajectory;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL)
    private List<TrajectoryVersion> versions;

    public Trajectory() {}

    public Trajectory(Long id) {
        this.id = id;
    }

    public Trajectory(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    @JsonGetter("_id")
    public Long getId() {
        return this.id;
    }

    @JsonSetter("_id")
    public void setId(Object o) {
        if (o instanceof String) {
            String s = (String) o;
            if (s.contains("/")) {
                StringTokenizer st = new StringTokenizer(s, "/");
                st.nextToken();
                this.id = Long.valueOf(st.nextToken());
            } else {
                this.id = Long.valueOf(s);
            }
        } else {
            System.out.println(o.getClass().getName());
        }
    }

    public void setId(Long id) {
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
        version.setTrajectory(this);
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
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
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