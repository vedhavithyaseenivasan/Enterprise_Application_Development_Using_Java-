import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

public class UDPServer {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        Set<InetAddress> clientAddresses = new HashSet<>();
        Set<Integer> clientPorts = new HashSet<>();

        try {
            socket = new DatagramSocket(12345);
            byte[] buffer = new byte[1024];
            System.out.println("Server is listening on port 12345...");

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                clientAddresses.add(clientAddress);
                clientPorts.add(clientPort);

                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received message from " + clientAddress + ":" + clientPort + " - " + message);

                for (InetAddress address : clientAddresses) {
                    for (int port : clientPorts) {
                        if (!address.equals(clientAddress) || port != clientPort) {
                            DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.length(), address, port);
                            socket.send(sendPacket);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
