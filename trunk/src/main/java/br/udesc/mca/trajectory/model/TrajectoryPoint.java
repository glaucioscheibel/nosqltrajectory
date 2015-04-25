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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TrajectoryPoint implements Serializable {

    private static final long serialVersionUID = -4325481950110704141L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segmentid")
    private TrajectorySegment segment;
    @OneToMany(cascade = CascadeType.ALL)
    private List<TrajectoryPointData> data;
    private float lat;
    private float lng;
    private float h;
    private long timestamp;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TrajectorySegment getSegment() {
        return this.segment;
    }

    public void setSegment(TrajectorySegment segment) {
        this.segment = segment;
    }

    public void addData(TrajectoryPointData data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(data);
    }

    public List<TrajectoryPointData> getData() {
        return this.data;
    }

    public float getLat() {
        return this.lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return this.lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}