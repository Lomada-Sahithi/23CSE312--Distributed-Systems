import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        final int PORT = 5000;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            System.out.println("Waiting for client connection...");

            try (Socket socket = serverSocket.accept();
                 BufferedReader input = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(
                     socket.getOutputStream(), true);
                 Scanner keyboard = new Scanner(System.in)) {

                System.out.println("Client connected: "
                    + socket.getInetAddress());

                // Receive messages from the client in a separate thread.
                Thread receiver = new Thread(() -> {
                    try {
                        String message;

                        while ((message = input.readLine()) != null) {
                            if (message.equalsIgnoreCase("/exit")) {
                                System.out.println(
                                    "\nClient requested to end the chat.");
                                break;
                            }

                            System.out.println("\nClient: " + message);
                            System.out.print("Server: ");
                        }
                    } catch (IOException e) {
                        System.out.println("\nConnection closed.");
                    }
                });

                receiver.setDaemon(true);
                receiver.start();

                // Read keyboard input and send messages to the client.
                while (true) {
                    System.out.print("Server: ");
                    String message = keyboard.nextLine();
                    output.println(message);

                    if (message.equalsIgnoreCase("/exit")) {
                        break;
                    }
                }
            }

            System.out.println("Server stopped.");

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
