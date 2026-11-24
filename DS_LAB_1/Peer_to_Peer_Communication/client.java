import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {

        int port = 12346;

        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(System.in)
            );

            System.out.print("Enter Peer A IP Address: ");
            String host = input.readLine();

            Socket socket = new Socket(host, port);

            System.out.println("\nConnected to Peer A successfully!");

            System.out.println("\nFiles available in the shared folder:");
            System.out.println("--------------------------------------");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            String file;

            while ((file = in.readLine()) != null) {
                System.out.println(file);
            }

            System.out.println("--------------------------------------");
            System.out.println("File listing received successfully!");

            socket.close();

        } catch (ConnectException e) {
            System.out.println(
                    "Connection failed! Check whether Peer A is running."
            );

        } catch (IOException e) {
            System.out.println("Client Error: " + e.getMessage());
        }
    }
}
