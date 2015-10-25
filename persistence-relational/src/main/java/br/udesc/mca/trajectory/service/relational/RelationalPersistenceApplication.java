package br.udesc.mca.trajectory.service.relational;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;
import br.udesc.mca.discovery.EurekaServiceRegister;
import io.dropwizard.Application;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Environment;

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

        if(config.getDiscoveryURL() != null) {
            int port = ((HttpConnectorFactory)((DefaultServerFactory)config.getServerFactory()).getApplicationConnectors().get(0)).getPort();
            EurekaServiceRegister discovery = new EurekaServiceRegister("Relational", port, "/trajectoryrelational");
            discovery.register(config.getDiscoveryURL());
        }
    }

    public static void main(String[] args) throws Exception {
        new RelationalPersistenceApplication().run(args);
    }
}
