package br.udesc.mca.trajectory.microservice.trajectory;


import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class Trajectory extends Application<TrajectoryConfiguration> {

    @Override
    public void run(TrajectoryConfiguration configuration, Environment environment) throws Exception {
        TrajectoryResource r = new TrajectoryResource(configuration.getPersistenceModel());
        TrajectoryHealthCheck hc = new TrajectoryHealthCheck();
        environment.jersey().register(r);
        environment.healthChecks().register("simple", hc);
    }

    @Override
    public String getName() {
        return "Trajectory";
    }

    @Override
    public void initialize(Bootstrap<TrajectoryConfiguration> bootstrap) {
    }


    public static void main(String... a){
        try {
            new Trajectory().run(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
