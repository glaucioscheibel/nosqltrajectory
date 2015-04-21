package br.udesc.mca.trajectory.microservice.kml;

import com.codahale.metrics.health.HealthCheck;

public class KMLHealthCheck  extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
