package br.udesc.mca.trajectory.service.column;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.udesc.mca.discovery.EurekaServiceRegister;
import io.dropwizard.Application;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Environment;

public class ColumnPersistenceApplication extends Application<ColumnPersistenceConfiguration> {

    @Override
    public void run(ColumnPersistenceConfiguration config, Environment env) throws Exception {
        ColumnPersistenceResource resource = new ColumnPersistenceResource();
        ColumnPersistenceHealthCheck healthCheck = new ColumnPersistenceHealthCheck();
        env.getObjectMapper().configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        env.getObjectMapper().setSerializationInclusion(Include.NON_NULL);
        env.getObjectMapper().setSerializationInclusion(Include.NON_EMPTY);
        env.healthChecks().register("column", healthCheck);
        env.jersey().register(resource);

        if(config.getDiscoveryURL() != null) {
            int port = ((HttpConnectorFactory)((DefaultServerFactory)config.getServerFactory()).getApplicationConnectors().get(0)).getPort();
            EurekaServiceRegister discovery = new EurekaServiceRegister("Column", port, "/trajectorycolumn");
            discovery.register(config.getDiscoveryURL());
        }
    }

    public static void main(String[] args) throws Exception {
        new ColumnPersistenceApplication().run(args);
    }
}
