package br.udesc.mca.trajectory.service.statistics;

import com.codahale.metrics.health.HealthCheck;

public class RStatisticsHealthCheck extends HealthCheck {

    @Override
    public Result check() throws Exception {
        return Result.healthy();
    }
}
