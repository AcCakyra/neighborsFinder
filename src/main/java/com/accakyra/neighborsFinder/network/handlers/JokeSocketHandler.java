package main.java.com.accakyra.neighborsFinder.network.handlers;

import java.io.*;
import java.net.Socket;

public class JokeSocketHandler implements Runnable {

    private Socket socket;
    private String message;

    public JokeSocketHandler(Socket socket, String message) {
        this.socket = socket;
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println("Got connection from: "
                + socket.getInetAddress().getHostAddress()
                + ":" + socket.getPort());
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            String inputMessage = in.readLine();
            System.out.println("Message is - " + inputMessage);
            out.write(message);
            out.newLine();
        } catch (IOException e) {
            System.out.println("Smth went wrong in communications between server and client");
        }
    }
}
