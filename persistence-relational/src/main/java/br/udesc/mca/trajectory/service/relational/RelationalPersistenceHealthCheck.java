package br.udesc.mca.trajectory.service.relational;

import com.codahale.metrics.health.HealthCheck;

public class RelationalPersistenceHealthCheck extends HealthCheck {

    @Override
    public Result check() throws Exception {
        return Result.healthy();
    }
}
