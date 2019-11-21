package com.accakyra.neighborsFinder.network;

public class NetworkNeighbor {

    private int port;
    private String ip;
    private String hostName;

    public NetworkNeighbor(int port, String ip, String hostName) {
        this.port = port;
        this.ip = ip;
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public String getHostName() {
        return hostName;
    }

    @Override
    public String toString() {
        return ip + ":" + port + ":" + hostName;
    }
}
