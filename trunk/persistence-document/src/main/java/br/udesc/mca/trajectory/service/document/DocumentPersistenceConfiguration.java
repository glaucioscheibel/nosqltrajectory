package br.udesc.mca.trajectory.service.document;

import io.dropwizard.Configuration;

public class DocumentPersistenceConfiguration extends Configuration {
    String discoveryURL;

    public String getDiscoveryURL() {
        return discoveryURL;
    }

    public void setDiscoveryURL(String discoveryURL) {
        this.discoveryURL = discoveryURL;
    }

}
