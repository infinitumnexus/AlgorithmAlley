import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CircleClient {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Server address
        int port = 12345; // Server port

        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter the radius of the circle: ");
            double radius = scanner.nextDouble();

            // Send radius to the server
            out.println(radius);

            // Read response from the server
            String area = in.readLine();
            String circumference = in.readLine();

            // Print the results
            System.out.println(area);
            System.out.println(circumference);
        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}
