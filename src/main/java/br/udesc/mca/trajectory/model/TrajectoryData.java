package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class TrajectoryData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, String> data;

    public TrajectoryData() {
        this.data = new LinkedHashMap<>();
    }

    public int size() {
        return this.data.size();
    }

    public boolean containsKey(Object key) {
        return this.data.containsKey(key);
    }

    public String get(Object key) {
        return this.data.get(key);
    }

    public String put(String key, String value) {
        return this.data.put(key, value);
    }

    public String remove(Object key) {
        return this.data.remove(key);
    }

    public void clear() {
        this.data.clear();
    }

    public boolean equals(Object o) {
        return this.data.equals(o);
    }

    public int hashCode() {
        return this.data.hashCode();
    }
}
