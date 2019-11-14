package main.java.com.accakyra.neighborsFinder;

import main.java.com.accakyra.neighborsFinder.network.PortScanner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        int firstPort = 2000;
        int portAmount = 46;

        if (args.length > 0 && args[0].equals("-p")) {
            int userFirstPort = Integer.parseInt(args[1]);
            if (userFirstPort < 1024 || userFirstPort > 65535) {
                System.out.println("Please input port in range from 1024 to 65535");
                return;
            } else {
                firstPort = userFirstPort;
            }
            if (args.length > 3) {
                int userPortAmount = Integer.parseInt(args[2]);
                if (firstPort + userPortAmount > 65535) {
                    System.out.println("Please input port in range from 1024 to 65535");
                    return;
                } else {
                    portAmount = userPortAmount;
                }
            }
        }

        PortScanner portScanner = new PortScanner(firstPort, portAmount);
        portScanner.start();

        checkHosts("192.168.1");

    }
    public static void checkHosts(String subnet) throws IOException {
        int timeout = 1000;
        for (int i = 1; i < 255; i++)
        {
            String host = subnet + "." + i;
            if (InetAddress.getByName(host).isReachable(timeout))
            {
                System.out.println(host + " is reachable");
            }
        }
        System.out.println("end");
    }
}
