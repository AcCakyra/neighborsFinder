package main.java.com.accakyra.neighborsFinder.network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class NetworkCommunicator {

    public List<String> tryCommunicate(List<String> ips, int firstPort, int portAmount) {
        List<String> neighbors = new ArrayList<>();

        ips.stream()
                .parallel()
                .forEach(ip -> {
                    IntStream.range(firstPort, firstPort + portAmount + 1)
                            .filter(p -> isAlive(ip, p))
                            .forEach(p -> neighbors.add(ip + ":" + p));
                });

        return neighbors;
    }

    public void sendMessage(List<String> neighbors, JokeGenerator jokeGenerator) {
        neighbors.stream()
                .parallel()
                .forEach(neighbor -> {
                    String[] address = neighbor.split(":");
                    String ip = address[0];
                    int port = Integer.parseInt(address[1]);
                    sendMessage(ip, port, jokeGenerator.getJoke());
                });
    }

    private boolean isAlive(String ip, int port) {
        try (Socket socket = new Socket(ip, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            socket.setSoTimeout(1000);
            out.write("Do you understand me?");
            if (in.readLine().equals("Yes, I do")) {
                InetAddress addr = InetAddress.getByName(ip);
                String host = addr.getHostName();
                System.out.println(ip + ":" + port + ":" + host);
                return true;
            }

        } catch (IOException e) {
            System.out.println("Some problems in communication with neighbor");
        }

        return false;
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
