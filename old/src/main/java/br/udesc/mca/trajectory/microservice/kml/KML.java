package br.udesc.mca.trajectory.microservice.kml;


import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class KML extends Application<KMLConfiguration> {

    @Override
    public void run(KMLConfiguration kmlConfiguration, Environment environment) throws Exception {
        KMLResource r = new KMLResource(kmlConfiguration.getDesegment(), kmlConfiguration.getSegmetcolori(), kmlConfiguration.getSegmetcolorp());
        KMLHealthCheck hc = new KMLHealthCheck();
        environment.jersey().register(r);
        environment.healthChecks().register("simple", hc);
    }

    @Override
    public String getName() {
        return "KML";
    }

    @Override
    public void initialize(Bootstrap<KMLConfiguration> bootstrap) {
    }


    public static void main(String... a){
        try {
            new KML().run(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
