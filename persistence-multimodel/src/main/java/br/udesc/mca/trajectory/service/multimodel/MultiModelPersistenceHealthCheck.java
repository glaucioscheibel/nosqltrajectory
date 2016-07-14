package br.udesc.mca.trajectory.service.multimodel;

import com.codahale.metrics.health.HealthCheck;

public class MultiModelPersistenceHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
