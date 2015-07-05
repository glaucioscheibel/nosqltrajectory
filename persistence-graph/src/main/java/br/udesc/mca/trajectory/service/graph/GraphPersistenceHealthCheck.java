package br.udesc.mca.trajectory.service.graph;

import com.codahale.metrics.health.HealthCheck;

public class GraphPersistenceHealthCheck extends HealthCheck {

    @Override
    public Result check() throws Exception {
        return Result.healthy();
    }
}
