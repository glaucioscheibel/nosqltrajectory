package br.udesc.mca.trajectory.service.document;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.udesc.mca.discovery.EurekaServiceRegister;
import io.dropwizard.Application;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Environment;

public class DocumentPersistenceApplication extends Application<DocumentPersistenceConfiguration> {

    @Override
    public void run(DocumentPersistenceConfiguration config, Environment env) throws Exception {
        DocumentPersistenceResource resource = new DocumentPersistenceResource();
        DocumentPersistenceHealthCheck healthCheck = new DocumentPersistenceHealthCheck();
        env.getObjectMapper().configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        env.getObjectMapper().setSerializationInclusion(Include.NON_NULL);
        env.getObjectMapper().setSerializationInclusion(Include.NON_EMPTY);
        env.healthChecks().register("document", healthCheck);
        env.jersey().register(resource);

        if(config.getDiscoveryURL() != null) {
            int port = ((HttpConnectorFactory)((DefaultServerFactory)config.getServerFactory()).getApplicationConnectors().get(0)).getPort();
            EurekaServiceRegister discovery = new EurekaServiceRegister("Document", port, "/trajectorydocument");
            discovery.register(config.getDiscoveryURL());
        }
    }

    public static void main(String[] args) throws Exception {
        new DocumentPersistenceApplication().run(args);
    }
}
