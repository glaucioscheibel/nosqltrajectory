package br.udesc.mca.trajectory.service.multimodel;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class MultiModelPersistenceApplication extends Application<MultiModelPersistenceConfiguration> {

    @Override
    public void run(MultiModelPersistenceConfiguration config, Environment env) throws Exception {
        MultiModelPersistenceResource resource = new MultiModelPersistenceResource();
        MultiModelPersistenceHealthCheck healthCheck = new MultiModelPersistenceHealthCheck();
        env.getObjectMapper().configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        env.getObjectMapper().setSerializationInclusion(Include.NON_NULL);
        env.getObjectMapper().setSerializationInclusion(Include.NON_EMPTY);
        env.healthChecks().register("keyvalue", healthCheck);
        env.jersey().register(resource);
    }

    public static void main(String[] args) throws Exception {
        new MultiModelPersistenceApplication().run(args);
    }
}
