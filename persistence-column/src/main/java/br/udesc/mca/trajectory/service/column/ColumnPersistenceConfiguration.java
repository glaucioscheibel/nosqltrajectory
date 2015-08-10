package br.udesc.mca.trajectory.service.column;

import io.dropwizard.Configuration;

public class ColumnPersistenceConfiguration extends Configuration {
    String discoveryURL;

    public String getDiscoveryURL() {
        return discoveryURL;
    }

    public void setDiscoveryURL(String discoveryURL) {
        this.discoveryURL = discoveryURL;
    }

}
