package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TrajectorySegment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL)
    private List<TrajectoryPoint> points;
    @OneToMany(cascade = CascadeType.ALL)
    private List<TrajectorySegmentData> data;
    private TransportationMode transportationMode;

    public List<TrajectoryPoint> getPoints() {
        return this.points;
    }

    public void addPoint(TrajectoryPoint point) {
        if (this.points == null) {
            this.points = new ArrayList<>();
        }
        this.points.add(point);
    }

    public List<TrajectorySegmentData> getData() {
        return this.data;
    }

    public void addData(TrajectorySegmentData data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(data);
    }

    public TransportationMode getTransportationMode() {
        return this.transportationMode;
    }

    public void setTransportationMode(TransportationMode transportationMode) {
        this.transportationMode = transportationMode;
    }
}
