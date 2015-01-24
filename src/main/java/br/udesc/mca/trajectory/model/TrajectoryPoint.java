package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TrajectoryPoint implements Serializable {
    private static final long serialVersionUID = 1L;
    private float x;
    private float y;
    private long timestamp;
    private Map<String, String> data;

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

    public String get(Object key) {
        if (this.data != null) {
            return this.data.get(key);
        }
        return null;
    }

    public void put(String key, String value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
    }
}
