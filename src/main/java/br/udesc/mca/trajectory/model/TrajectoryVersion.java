package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class TrajectoryVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Trajectory trajectory;
    private int version;
    @JsonProperty
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @JsonProperty
    private Integer previousVersion;
    @JsonProperty
    private TrajectoryType type;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty
    private Date lastModified;
    @OneToMany(cascade = CascadeType.ALL)
    private List<TrajectorySegment> segments;
    @JsonProperty
    private TrajectoryVersionData data;
    @OneToOne
    @JsonProperty
    private TrajectoryProcess history; // TODO: ou apropriedade precisa mudar
                                       // para process ou o get para history

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Integer getPreviousVersion() {
        return this.previousVersion;
    }

    public void setPreviousVersion(Integer previousVersion) {
        this.previousVersion = previousVersion;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public List<TrajectorySegment> getSegments() {
        return this.segments;
    }

    public void addSegment(TrajectorySegment segment) {
        if (this.segments == null) {
            this.segments = new ArrayList<>();
        }
        this.segments.add(segment);
    }

    public TrajectoryVersionData getData() {
        return this.data;
    }

    public void setData(TrajectoryVersionData data) {
        this.data = data;
    }

    public TrajectoryProcess getProcess() {
        return this.history;
    }

    public void setHistory(TrajectoryProcess history) {
        this.history = history;
    }
}
