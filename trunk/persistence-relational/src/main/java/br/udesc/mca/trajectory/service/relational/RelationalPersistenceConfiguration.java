package br.udesc.mca.trajectory.service.relational;

import io.dropwizard.Configuration;

public class RelationalPersistenceConfiguration extends Configuration {
    String discoveryURL;

    public String getDiscoveryURL() {
        return discoveryURL;
    }

    public void setDiscoveryURL(String discoveryURL) {
        this.discoveryURL = discoveryURL;
    }
}
