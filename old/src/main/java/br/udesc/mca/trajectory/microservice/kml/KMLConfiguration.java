package br.udesc.mca.trajectory.microservice.kml;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;


public class KMLConfiguration extends Configuration{
    @NotEmpty
    private String desegment;
    private String segmetcolorp;
    private String segmetcolori;

    @JsonProperty
    public String getDesegment() {
        return desegment;
    }

    @JsonProperty
    public void setDesegment(String desegment) {
        this.desegment = desegment;
    }

    @JsonProperty
    public String getSegmetcolorp() {
        return segmetcolorp;
    }

    @JsonProperty
    public void setSegmetcolorp(String segmetcolorp) {
        this.segmetcolorp = segmetcolorp;
    }

    @JsonProperty
    public String getSegmetcolori() {
        return segmetcolori;
    }

    @JsonProperty
    public void setSegmetcolori(String segmetcolori) {
        this.segmetcolori = segmetcolori;
    }
}
