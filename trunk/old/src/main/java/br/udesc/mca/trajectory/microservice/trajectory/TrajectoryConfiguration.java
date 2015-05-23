package br.udesc.mca.trajectory.microservice.trajectory;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;


public class TrajectoryConfiguration extends Configuration{
    @NotEmpty
    private String persistenceModel;

    @JsonProperty
    public String getPersistenceModel() {
        return persistenceModel;
    }

    @JsonProperty
    public void setPersistenceModel(String persistenceModel) {
        this.persistenceModel = persistenceModel;
    }

}
