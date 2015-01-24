package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class TrajectoryProcessHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID componentId;
    private Date executionTime;
    private long executionDuration;

    public UUID getComponentId() {
        return this.componentId;
    }

    public void setComponentId(UUID componentId) {
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
}
