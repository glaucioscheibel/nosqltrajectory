package br.udesc.mca.trajectory.service.graph;

import io.dropwizard.Configuration;

public class GraphPersistenceConfiguration extends Configuration {
    String discoveryURL;

    public String getDiscoveryURL() {
        return discoveryURL;
    }

    public void setDiscoveryURL(String discoveryURL) {
        this.discoveryURL = discoveryURL;
    }

}
