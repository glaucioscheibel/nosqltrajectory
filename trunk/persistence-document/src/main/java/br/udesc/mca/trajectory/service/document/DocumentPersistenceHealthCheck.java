package br.udesc.mca.trajectory.service.document;

import com.codahale.metrics.health.HealthCheck;

public class DocumentPersistenceHealthCheck extends HealthCheck {

    @Override
    public Result check() throws Exception {
        return Result.healthy();
    }
}
