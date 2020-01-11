package org.rainbow.learning.htcw.client;

class ProgramArguments {
    private String remoteHost;
    private int remotePort;
    private String uri;

    String getRemoteHost() {
        return remoteHost;
    }

    void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    int getRemotePort() {
        return remotePort;
    }

    void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    String getUri() {
        return uri;
    }

    void setUri(String uri) {
        this.uri = uri;
    }
}
