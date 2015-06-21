package br.udesc.mca.trajectory.polyglot.service;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class PolyglotApplication extends Application<PolyglotConfiguration> {

    @Override
    public void run(PolyglotConfiguration config, Environment env) throws Exception {
        PolyglotResource resource = new PolyglotResource();
        PolyglotHealthChecker healthCheck = new PolyglotHealthChecker();
        env.healthChecks().register("polyglot", healthCheck);
        env.jersey().register(resource);
    }
    
    public static void main(String[] args) throws Exception {
        new PolyglotApplication().run(args);
    }
}
