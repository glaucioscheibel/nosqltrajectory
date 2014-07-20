package br.udesc.mca.sec1.projeto.model;

import java.io.Serializable;

public class CustomerData implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String dataKey;
    private String dataValue;

    public CustomerData() {}

    public CustomerData(String dataKey, String dataValue) {
        this.dataKey = dataKey;
        this.dataValue = dataValue;
    }

    public int getId() {
        return this.id;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataKey == null) ? 0 : dataKey.hashCode());
        result = prime * result + id;
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
        CustomerData other = (CustomerData) obj;
        if (dataKey == null) {
            if (other.dataKey != null) {
                return false;
            }
        } else if (!dataKey.equals(other.dataKey)) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.dataKey + "=" + this.dataValue;
    }
}
