import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        final int PORT = 5000;

        try (Scanner keyboard = new Scanner(System.in)) {
            System.out.print("Enter Server IP Address: ");
            String host = keyboard.nextLine().trim();

            try (Socket socket = new Socket(host, PORT);
                 BufferedReader input = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(
                     socket.getOutputStream(), true)) {

                System.out.println("Connected to server: "
                    + socket.getInetAddress());

                // Receive messages from the server in a separate thread.
                Thread receiver = new Thread(() -> {
                    try {
                        String message;

                        while ((message = input.readLine()) != null) {
                            if (message.equalsIgnoreCase("/exit")) {
                                System.out.println(
                                    "\nServer requested to end the chat.");
                                break;
                            }

                            System.out.println("\nServer: " + message);
                            System.out.print("Client: ");
                        }
                    } catch (IOException e) {
                        System.out.println("\nConnection closed.");
                    }
                });

                receiver.setDaemon(true);
                receiver.start();

                // Read keyboard input and send messages to the server.
                while (true) {
                    System.out.print("Client: ");
                    String message = keyboard.nextLine();
                    output.println(message);

                    if (message.equalsIgnoreCase("/exit")) {
                        break;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}
