package br.udesc.mca.trajectory.service.statistics;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class RStatisticsApplication extends Application<RStatisticsConfiguration> {

    @Override
    public void run(RStatisticsConfiguration config, Environment env) throws Exception {
        RStatisticsHealthCheck healthCheck = new RStatisticsHealthCheck();
        RStatisticsResource resource = new RStatisticsResource();
        env.healthChecks().register("rstatistics", healthCheck);
        env.jersey().register(resource);
        
    }

    public static void main(String[] args) throws Exception {
        new RStatisticsApplication().run(args);
    }
}
