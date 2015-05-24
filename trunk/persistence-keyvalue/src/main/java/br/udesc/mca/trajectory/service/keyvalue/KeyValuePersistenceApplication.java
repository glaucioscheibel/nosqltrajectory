package br.udesc.mca.trajectory.service.keyvalue;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class KeyValuePersistenceApplication extends Application<KeyValuePersistenceConfiguration> {

    @Override
    public void run(KeyValuePersistenceConfiguration config, Environment env) throws Exception {
        KeyValuePersistenceResource resource = new KeyValuePersistenceResource();
        KeyValuePersistenceHealthCheck healthCheck = new KeyValuePersistenceHealthCheck();
        env.healthChecks().register("keyvalue", healthCheck);
        env.jersey().register(resource);
    }

    public static void main(String[] args) throws Exception {
        new KeyValuePersistenceApplication().run(args);
    }
}
