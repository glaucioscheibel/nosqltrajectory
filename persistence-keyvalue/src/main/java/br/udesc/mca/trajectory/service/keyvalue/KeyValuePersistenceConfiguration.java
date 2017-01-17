package br.udesc.mca.trajectory.service.keyvalue;

import io.dropwizard.Configuration;

public class KeyValuePersistenceConfiguration extends Configuration {
    String discoveryURL;

    public String getDiscoveryURL() {
        return this.discoveryURL;
    }

    public void setDiscoveryURL(String discoveryURL) {
        this.discoveryURL = discoveryURL;
    }

}
