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

@Entity
public class TrajectoryVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Trajectory trajectory;
    private int version;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    private Integer previousVersion;
    private TrajectoryType type;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @OneToMany(cascade = CascadeType.ALL)
    private List<TrajectorySegment> segments;
    @OneToMany(cascade = CascadeType.ALL)
    private List<TrajectoryVersionData> data;
    @OneToOne
    private TrajectoryProcess process;

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

    public List<TrajectoryVersionData> getData() {
        return this.data;
    }

    public void addData(TrajectoryVersionData data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(data);
    }

    public TrajectoryProcess getProcess() {
        return this.process;
    }

    public void setProcess(TrajectoryProcess process) {
        this.process = process;
    }
}
