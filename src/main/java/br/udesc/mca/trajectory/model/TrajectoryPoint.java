package br.udesc.mca.trajectory.model;

import java.io.Serializable;

public class TrajectoryPoint implements Serializable {
    private static final long serialVersionUID = 1L;
    private float x;
    private float y;
    private long timestamp;

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
