package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class TrajectorySegment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private TrajectoryVersion trajectoryVersion;
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL)
    private List<TrajectoryPoint> points;
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL)
    private List<TrajectorySegmentData> data;
    private TransportationMode transportationMode;
    
    public TrajectoryVersion getTrajectoryVersion() {
        return this.trajectoryVersion;
    }

    public void setTrajectoryVersion(TrajectoryVersion trajectoryVersion) {
        this.trajectoryVersion = trajectoryVersion;
    }

    public List<TrajectoryPoint> getPoints() {
        return this.points;
    }

    public void addPoint(TrajectoryPoint point) {
        if (this.points == null) {
            this.points = new ArrayList<>();
        }
        point.setSegment(this);
        this.points.add(point);
    }

    public List<TrajectorySegmentData> getData() {
        return this.data;
    }

    public void addData(TrajectorySegmentData data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        data.setTrajectorySegment(this);
        this.data.add(data);
    }

    public TransportationMode getTransportationMode() {
        return this.transportationMode;
    }

    public void setTransportationMode(TransportationMode transportationMode) {
        this.transportationMode = transportationMode;
    }
}
