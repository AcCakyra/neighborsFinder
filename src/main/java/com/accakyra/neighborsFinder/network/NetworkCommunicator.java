package main.java.com.accakyra.neighborsFinder.network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class NetworkCommunicator {

    public List<NetworkNeighbor> tryCommunicate(List<String> ips, int firstPort, int portAmount) {
        System.out.println("Start finding all neighbors");
        List<NetworkNeighbor> neighbors = new ArrayList<>();

        ips.stream()
                .parallel()
                .forEach(ip -> IntStream.range(firstPort, firstPort + portAmount + 1)
                        .filter(port -> isAlive(ip, port))
                        .forEach(port -> neighbors.add(convertNetworkNeighbor(ip, port))));

        System.out.println("Stop finding");
        System.out.println("Result is - " + Arrays.toString(neighbors.toArray()));
        return neighbors;
    }

    public void sendMessage(List<NetworkNeighbor> neighbors, JokeGenerator jokeGenerator) {
        neighbors.stream()
                .parallel()
                .forEach(neighbor -> {
                    System.out.println("Send message to " + neighbor);
                    sendMessage(neighbor.getIp(), neighbor.getPort(), jokeGenerator.getJoke());
                });
    }

    private boolean isAlive(String ip, int port) {
        try (Socket socket = new Socket(ip, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.write("Do you understand me?\n");
            out.flush();
            String answer = in.readLine();
            if (answer.equals("Yes, I do")) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    private NetworkNeighbor convertNetworkNeighbor(String ip, int port) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            String hostName = addr.getHostName();
            return new NetworkNeighbor(port, ip, hostName);
        } catch (UnknownHostException e) {
            System.out.println("Incorrect ip address");
        }
        return null;
    }

    private void sendMessage(String ip, int port, String message) {
        try (Socket socket = new Socket(ip, port);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            out.write(message);
        } catch (IOException e) {
            System.out.println("Some problems in communication with neighbor");
        }
    }
}
