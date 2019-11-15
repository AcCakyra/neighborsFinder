package main.java.com.accakyra.neighborsFinder.network;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class NetworkScanner {

    
    public List<String> getAliveHosts() throws IOException {
        List<String> ips = new ArrayList<>();

        int timeout = 100;
        String myIp = getMyLocalNetworkAddress();
        int maskLength = getSubnetMask(myIp);
        String ipCandidate = getFirstIpInRange(myIp, maskLength);
        int rangeLength = (int) Math.pow(2, 32 - maskLength);

        for (int i = 0; i < rangeLength; i++) {
            if (InetAddress.getByName(ipCandidate).isReachable(timeout)) {
                ips.add(InetAddress.getByName(ipCandidate).getHostAddress());
            }
            ipCandidate = nextIpAddress(ipCandidate);
        }

        return ips;
    }

    private String getFirstIpInRange(String ip, int maskLength) {
        String[] myIpBytes = ip.split("\\.");
        int byteForSubtraction = Integer.parseInt(myIpBytes[maskLength / 8]);
        byteForSubtraction = Math.max(0, (int) (byteForSubtraction - Math.pow(2, 8 - (maskLength % 8))));
        StringBuilder ipBuilder = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            if (maskLength / 8 != i) {
                ipBuilder.append(myIpBytes[i]);
            } else {
                ipBuilder.append(byteForSubtraction);
            }
            if (i != 3) {
                ipBuilder.append(".");
            }
        }
        return ipBuilder.toString();
    }

    private String nextIpAddress(String input) {
        String[] tokens = input.split("\\.");
        if (tokens.length != 4)
            throw new IllegalArgumentException();
        for (int i = tokens.length - 1; i >= 0; i--) {
            int item = Integer.parseInt(tokens[i]);
            if (item < 255) {
                tokens[i] = String.valueOf(item + 1);
                for (int j = i + 1; j < 4; j++) {
                    tokens[j] = "0";
                }
                break;
            }
        }

        return new StringBuilder()
                .append(tokens[0]).append('.')
                .append(tokens[1]).append('.')
                .append(tokens[2]).append('.')
                .append(tokens[3])
                .toString();
    }

    private String getMyLocalNetworkAddress() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getSubnetMask(String subnet) {
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName(subnet));
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        return networkInterface.getInterfaceAddresses().get(1).getNetworkPrefixLength();
    }

}
