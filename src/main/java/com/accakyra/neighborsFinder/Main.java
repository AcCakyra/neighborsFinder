package main.java.com.accakyra.neighborsFinder;

import main.java.com.accakyra.neighborsFinder.network.JokeGenerator;
import main.java.com.accakyra.neighborsFinder.network.NetworkCommunicator;
import main.java.com.accakyra.neighborsFinder.network.NetworkScanner;
import main.java.com.accakyra.neighborsFinder.network.PortScanner;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        int firstPort = 2000;
        int portAmount = 46;

        PortScanner portScanner = new PortScanner(firstPort, portAmount);
        NetworkScanner networkScanner = new NetworkScanner();
        NetworkCommunicator networkCommunicator = new NetworkCommunicator();
        JokeGenerator jokeGenerator = new JokeGenerator();

        portScanner.start();
        List<String> networkIps = networkScanner.getAliveHosts();
        List<String> neighbors = networkCommunicator.tryCommunicate(networkIps, firstPort, portAmount);

        networkCommunicator.sendMessage(neighbors, jokeGenerator);
    }
}
