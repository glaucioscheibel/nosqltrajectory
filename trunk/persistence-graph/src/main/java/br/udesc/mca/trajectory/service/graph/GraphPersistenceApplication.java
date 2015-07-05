package br.udesc.mca.trajectory.service.graph;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class GraphPersistenceApplication extends Application<GraphPersistenceConfiguration> {

    @Override
    public void run(GraphPersistenceConfiguration config, Environment env) throws Exception {
        GraphPersistenceResource resource = new GraphPersistenceResource();
        GraphPersistenceHealthCheck healthCheck = new GraphPersistenceHealthCheck();
        env.getObjectMapper().configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        env.getObjectMapper().setSerializationInclusion(Include.NON_NULL);
        env.getObjectMapper().setSerializationInclusion(Include.NON_EMPTY);
        env.healthChecks().register("graph", healthCheck);
        env.jersey().register(resource);
    }

    public static void main(String[] args) throws Exception {
        new GraphPersistenceApplication().run(args);
    }
}
