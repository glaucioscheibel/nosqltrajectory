package br.udesc.mca.segmenter.service;

import com.codahale.metrics.health.HealthCheck;

public class SegmenterHealthCheck extends HealthCheck {

    @Override
    public Result check() throws Exception {
        return Result.healthy();
    }
}
