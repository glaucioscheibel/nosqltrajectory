package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TrajectoryProcessHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajectoryversionid")
    private TrajectoryVersion trajectoryVersion;
    private int componentId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date executionTime;
    private long executionDuration;

    public int getComponentId() {
        return this.componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public Date getExecutionTime() {
        return this.executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    public long getExecutionDuration() {
        return this.executionDuration;
    }

    public void setExecutionDuration(long executionDuration) {
        this.executionDuration = executionDuration;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
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
        TrajectoryProcessHistory other = (TrajectoryProcessHistory) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
