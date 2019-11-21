package com.accakyra.neighborsFinder;

import com.accakyra.neighborsFinder.network.*;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        int firstPort = 2000;
        int portAmount = 46;

        JokeGenerator jokeGenerator = new JokeGenerator();
        NetworkListener networkListener = new NetworkListener(firstPort, portAmount, jokeGenerator);
        NetworkScanner networkScanner = new NetworkScanner();
        NetworkCommunicator networkCommunicator = new NetworkCommunicator();

        networkListener.start();
        List<String> networkIps = networkScanner.getAliveHosts();
        List<NetworkNeighbor> neighbors = networkCommunicator
                .tryCommunicate(networkIps, firstPort, portAmount);
        networkCommunicator.sendMessage(neighbors, jokeGenerator);
        networkListener.setNeighbors(neighbors);
    }
}
