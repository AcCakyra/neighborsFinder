package com.accakyra.neighborsFinder.network.handlers;

import java.io.*;
import java.net.Socket;

public class OneMessageSocketHandler implements Runnable {

    private Socket socket;

    public OneMessageSocketHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Got connection from: "
                + socket.getInetAddress().getHostAddress()
                + ":" + socket.getPort());
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            String message = in.readLine();
            System.out.println("Message is - " + message);
            if (message.equals("Do you understand me?")) {
                System.out.println("Write answer");
                out.write("Yes, I do");
                out.newLine();
            }

        } catch (IOException e) {
            System.out.println("Smth went wrong in communications between server and client");
        }
    }
}
