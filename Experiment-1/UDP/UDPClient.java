import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClient {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
            Scanner scanner = new Scanner(System.in);

            // Thread to receive messages
            Thread receiveThread = new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet); // No issue now as 'socket' is effectively final
                        String message = new String(packet.getData(), 0, packet.getLength());
                        System.out.println("Received message: " + message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            receiveThread.start();

            // Sending messages
            while (true) {
                System.out.print("Enter message to send: ");
                String message = scanner.nextLine();
                DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), serverAddress, 12345);
                socket.send(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
