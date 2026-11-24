import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.stream.Stream;

public class Server {

    public static void main(String[] args) {

        int port = 12346;

        try {
            ServerSocket server = new ServerSocket(port);

            System.out.println("Peer A is waiting for connection...");

            Socket socket = server.accept();

            System.out.println("Connected to Peer B: "
                    + socket.getInetAddress());

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(System.in)
            );

            System.out.print("Enter folder path to share: ");
            String folderPath = input.readLine();

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true
            );

            Path path = Paths.get(folderPath);

            if (Files.isDirectory(path)) {

                try (Stream<Path> files = Files.list(path)) {

                    StringBuilder fileList = new StringBuilder();

                    files.filter(Files::isRegularFile)
                         .forEach(file ->
                             fileList.append(file.getFileName())
                                     .append("\n")
                         );

                    if (fileList.length() == 0) {
                        out.println("No files found in the folder.");
                    } else {
                        out.print(fileList.toString());
                        out.flush();
                    }
                }

            } else {
                out.println("ERROR: Invalid folder path.");
            }

            System.out.println("File listing sent successfully.");

            socket.close();
            server.close();

            System.out.println("Peer A disconnected.");

        } catch (IOException e) {
            System.out.println("Server Error: " + e.getMessage());
        }
    }
}
