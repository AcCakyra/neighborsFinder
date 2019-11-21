package com.accakyra.neighborsFinder.network.handlers;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class OneMessageSocketHandler implements Runnable {

    private Socket socket;
    final static Logger logger = Logger.getLogger(OneMessageSocketHandler.class);

    public OneMessageSocketHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        logger.info("Got connection from: "
                + socket.getInetAddress().getHostAddress()
                + ":" + socket.getPort());
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            String message = in.readLine();
            logger.info("Message is - " + message);
            if (message.equals("Do you understand me?")) {
                logger.info("Write answer");
                out.write("Yes, I do");
                out.newLine();
            }

        } catch (IOException e) {
            logger.error("Smth went wrong in communications between server and client");
        }
    }
}
