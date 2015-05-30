package br.udesc.mca.segmenter.service;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SegmenterApplication extends Application<SegmenterConfiguration> {

    @Override
    public void run(SegmenterConfiguration config, Environment env) throws Exception {
        SegmenterResource resource = new SegmenterResource();
        SegmenterHealthCheck healthCheck = new SegmenterHealthCheck();
        env.getObjectMapper().configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        env.getObjectMapper().setSerializationInclusion(Include.NON_NULL);
        env.getObjectMapper().setSerializationInclusion(Include.NON_EMPTY);
        env.healthChecks().register("segmenter", healthCheck);
        env.jersey().register(resource);
    }

    public static void main(String[] args) throws Exception {
        new SegmenterApplication().run(args);
    }
}
