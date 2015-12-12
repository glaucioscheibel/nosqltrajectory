package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TrajectoryVersionData implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Integer id;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private TrajectoryVersion trajectoryVersion;
    private String dataKey;
    private String dataValue;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TrajectoryVersion getTrajectoryVersion() {
        return this.trajectoryVersion;
    }

    public void setTrajectoryVersion(TrajectoryVersion trajectoryVersion) {
        this.trajectoryVersion = trajectoryVersion;
    }

    public String getDataKey() {
        return this.dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getDataValue() {
        return this.dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }   
}
