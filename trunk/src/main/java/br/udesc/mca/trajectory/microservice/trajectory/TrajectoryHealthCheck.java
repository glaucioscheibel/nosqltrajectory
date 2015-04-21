package br.udesc.mca.trajectory.microservice.trajectory;

import com.codahale.metrics.health.HealthCheck;

public class TrajectoryHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
