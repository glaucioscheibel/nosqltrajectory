package br.udesc.mca.trajectory.service.keyvalue;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class KeyValuePersistenceApplication extends Application<KeyValuePersistenceConfiguration> {

    @Override
    public void run(KeyValuePersistenceConfiguration config, Environment env) throws Exception {
        KeyValuePersistenceResource resource = new KeyValuePersistenceResource();
        KeyValuePersistenceHealthCheck healthCheck = new KeyValuePersistenceHealthCheck();
        env.getObjectMapper().configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        env.getObjectMapper().setSerializationInclusion(Include.NON_NULL);
        env.getObjectMapper().setSerializationInclusion(Include.NON_EMPTY);
        env.healthChecks().register("keyvalue", healthCheck);
        env.jersey().register(resource);
    }

    public static void main(String[] args) throws Exception {
        new KeyValuePersistenceApplication().run(args);
    }
}
