package br.udesc.mca.trajectory.service.relational;

import org.eclipse.jetty.servlet.ServletHandler;

import br.udesc.mca.discovery.EurekaServiceRegister;
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

        int port = 9090; //TODO: Descobrir a porta

        EurekaServiceRegister discovery = new EurekaServiceRegister("Relational", port, "/trajectoryrelational");
        discovery.register("http://localhost:8080/persistence-discovery/v2");

    }

    public static void main(String[] args) throws Exception {
        new RelationalPersistenceApplication().run(args);
    }
}
