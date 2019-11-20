package main.java.com.accakyra.neighborsFinder.network;

import main.java.com.accakyra.neighborsFinder.network.handlers.JokeSocketHandler;
import main.java.com.accakyra.neighborsFinder.network.handlers.OneMessageSocketHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkListener {

    private int firstPort;
    private int portAmount;
    private JokeGenerator jokeGenerator;
    private List<NetworkNeighbor> neighbors;

    public NetworkListener(int firstPort, int portAmount, JokeGenerator jokeGenerator) {
        this.firstPort = firstPort;
        this.portAmount = portAmount;
        this.jokeGenerator = jokeGenerator;
    }

    public void start() {
        ServerSocket serverSocket = getServerSocket(firstPort, portAmount);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new PortListener(serverSocket));
    }

    public void setNeighbors(List<NetworkNeighbor> neighbors) {
        this.neighbors = neighbors;
    }

    private class PortListener implements Runnable {
        private ServerSocket serverSocket;
        ExecutorService pool;

        PortListener(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
            pool = Executors.newFixedThreadPool(3);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    if (isNeighbor(socket.getInetAddress().getHostAddress())) {
                        pool.execute(new JokeSocketHandler(socket, jokeGenerator.getJoke()));
                    } else {
                        pool.execute(new OneMessageSocketHandler(socket));
                    }
                }
                catch(IOException e){
                    System.out.println("Smth went while server tries accept connection");
                }
            }
        }

        private boolean isNeighbor(String ip) {
            if (neighbors == null) return false;
            for (NetworkNeighbor neighbor : neighbors) {
                if (ip.equals(neighbor.getIp())) {
                    return true;
                }
            }
            return false;
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
