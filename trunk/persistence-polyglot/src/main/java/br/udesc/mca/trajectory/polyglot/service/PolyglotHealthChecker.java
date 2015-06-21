package br.udesc.mca.trajectory.polyglot.service;

import com.codahale.metrics.health.HealthCheck;

public class PolyglotHealthChecker extends HealthCheck {

    @Override
    public Result check() throws Exception {
        return Result.healthy();
    }
}
