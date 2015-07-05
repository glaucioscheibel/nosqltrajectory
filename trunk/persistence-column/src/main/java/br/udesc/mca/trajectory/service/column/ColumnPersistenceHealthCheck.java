package br.udesc.mca.trajectory.service.column;

import com.codahale.metrics.health.HealthCheck;

public class ColumnPersistenceHealthCheck extends HealthCheck {

    @Override
    public Result check() throws Exception {
        return Result.healthy();
    }
}
