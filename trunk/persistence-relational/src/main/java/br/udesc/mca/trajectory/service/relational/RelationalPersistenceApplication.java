package br.udesc.mca.trajectory.service.relational;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;

public class RelationalPersistenceApplication extends Application<RelationalPersistenceConfiguration> {

    @Override
    public void run(RelationalPersistenceConfiguration config, Environment env) throws Exception {
        RelationalPersistenceResource resource = new RelationalPersistenceResource();
        RelationalPersistenceHealthCheck healthCheck = new RelationalPersistenceHealthCheck();
        env.getObjectMapper().configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        env.getObjectMapper().setSerializationInclusion(Include.NON_NULL);
        env.getObjectMapper().setSerializationInclusion(Include.NON_EMPTY);
        env.healthChecks().register("relational", healthCheck);
        env.jersey().register(resource);
    }

    public static void main(String[] args) throws Exception {
        new RelationalPersistenceApplication().run(args);
    }
}
