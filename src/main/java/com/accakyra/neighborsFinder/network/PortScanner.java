package main.java.com.accakyra.neighborsFinder.network;

import main.java.com.accakyra.neighborsFinder.network.handlers.OneMessageSocketHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PortScanner {

    private int firstPort;
    private int portAmount;

    public PortScanner(int firstPort, int portAmount) {
        this.firstPort = firstPort;
        this.portAmount = portAmount;
    }

    public void start() {
        ServerSocket serverSocket = getServerSocket(firstPort, portAmount);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new PortListener(serverSocket));
    }

    private static class PortListener implements Runnable {
        private ServerSocket serverSocket;

        PortListener(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            ExecutorService pool = Executors.newFixedThreadPool(3);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    pool.execute(new OneMessageSocketHandler(socket));
                }
                catch(IOException e){
                    System.out.println("Smth went while server tries accept connection");
                }
            }
        }
    }

    private ServerSocket getServerSocket(int firstPort, int portAmount) {
        int lastPort = firstPort + portAmount;
        for (int i = firstPort; i < lastPort; i++) {
            ServerSocket socket = tryStartConnection(i);
            if (socket != null) {
                System.out.println("Server start listening on " + socket.getLocalPort() + " port");
                return socket;
            }
        }
        return null;
    }

    private ServerSocket tryStartConnection(int port) {
        try {
            return new ServerSocket(port);
        } catch (IOException e) {
            return null;
        }
    }

}
