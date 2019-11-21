package com.accakyra.neighborsFinder.network;

import java.io.IOException;
import java.math.BigInteger;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class NetworkScanner {

    public List<String> getAliveHosts() throws IOException {
        System.out.println("Start scanning network for list of available ip");
        List<String> ips = new ArrayList<>();

        int timeout = 100;
        String myIp = getMyLocalNetworkAddress();
        int maskLength = getSubnetMask(myIp);
        String firstIpInRange = getFirstIpInRange(myIp, maskLength);
        int rangeLength = (int) Math.pow(2, 32 - maskLength);

        IntStream.range(0, rangeLength + 1)
                .parallel()
                .mapToObj(delta -> getIpByFirstInNetworkAndDelta(firstIpInRange, delta))
                .filter(ip -> checkIp(ip, timeout))
                .forEach(ips::add);

        System.out.println("Stop scanning");
        return ips;
    }

    private String getFirstIpInRange(String ip, int maskLength) {
        String[] myIpBytes = ip.split("\\.");
        int byteForSubtraction = Integer.parseInt(myIpBytes[maskLength / 8]);
        byteForSubtraction = Math.max(0, (int) (byteForSubtraction - Math.pow(2, 8 - (maskLength % 8))));
        StringBuilder ipBuilder = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            if (maskLength / 8 != i) ipBuilder.append(myIpBytes[i]);
            else ipBuilder.append(byteForSubtraction);
            if (i != 3) ipBuilder.append(".");
        }

        return ipBuilder.toString();
    }

    private String getIpByFirstInNetworkAndDelta(String firstIp, int delta) {
        try {
            long ipNumbers = convertIpToLong(firstIp);
            ipNumbers += delta;
            return convertLongToIp(ipNumbers);
        } catch (UnknownHostException e) {
            System.out.println("Cannot get ip by first ip and delta");
        }
        return null;
    }

    private String getMyLocalNetworkAddress() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            System.out.println("Cannot get my network address");
        }
        return null;
    }

    private int getSubnetMask(String subnet) {
        try {
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName(subnet));
            return networkInterface.getInterfaceAddresses().get(1).getNetworkPrefixLength();
        } catch (SocketException | UnknownHostException e) {
            System.out.println("Cannot get subnet mask");
        }
        return 0;
    }

    private boolean checkIp(String ip, int timeout) {
        try {
            if (InetAddress.getByName(ip).isReachable(timeout)) {
                System.out.println("Find one ip " + ip);
                return true;
            }
        } catch (IOException e) {
            System.out.println("Cannot get ip " + ip + " from string");
        }
        return false;
    }

    private long convertIpToLong(String ip) {
        long ipNumbers = 0;
        String[] parts = ip.split("\\.");
        for (int i = 0; i < 4; i++) {
            ipNumbers += Integer.parseInt(parts[i]) << (24 - (8 * i));
        }
        return ipNumbers;
    }

    private String convertLongToIp(long ip) throws UnknownHostException {
        byte[] bytes = BigInteger.valueOf(ip).toByteArray();
        return InetAddress.getByAddress(bytes).getHostAddress();
    }
}
