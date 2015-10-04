package br.udesc.mca.importabusriodejaneiro;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BusData {
    private List<String> columns;
    private List<List<String>> data;

    public List<String> getColumns() {
        return this.columns;
    }

    @JsonProperty("COLUMNS")
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<List<String>> getData() {
        return this.data;
    }

    @JsonProperty("DATA")
    public void setData(List<List<String>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BusData [columns=" + this.columns + ", data=" + this.data + "]";
    }
}
