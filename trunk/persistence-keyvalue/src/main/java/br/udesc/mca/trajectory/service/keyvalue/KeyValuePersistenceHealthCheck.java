package br.udesc.mca.trajectory.service.keyvalue;

import com.codahale.metrics.health.HealthCheck;

public class KeyValuePersistenceHealthCheck extends HealthCheck {

    @Override
    public Result check() throws Exception {
        return Result.healthy();
    }
}
