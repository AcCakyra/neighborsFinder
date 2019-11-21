package com.accakyra.neighborsFinder.network.handlers;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class JokeSocketHandler implements Runnable {

    private Socket socket;
    private String message;
    final static Logger logger = Logger.getLogger(JokeSocketHandler.class);

    public JokeSocketHandler(Socket socket, String message) {
        this.socket = socket;
        this.message = message;
    }

    @Override
    public void run() {
        logger.info("Got connection from: "
                + socket.getInetAddress().getHostAddress()
                + ":" + socket.getPort());
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            String inputMessage = in.readLine();
            logger.info("Message is - " + inputMessage);
            out.write(message);
            out.newLine();
        } catch (IOException e) {
            logger.error("Smth went wrong in communications between server and client");
        }
    }
}
