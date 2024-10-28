import java.io.*;
import java.net.*;

public class CircleServer {
    public static void main(String[] args) {
        int port = 12345; // Port number
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running and waiting for connections...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("Client connected.");
                    
                    // Read radius from client
                    String input = in.readLine();
                    double radius = Double.parseDouble(input);
                    
                    // Calculate area and circumference
                    double area = Math.PI * radius * radius;
                    double circumference = 2 * Math.PI * radius;

                    // Send results back to the client
                    out.println("Area: " + area);
                    out.println("Circumference: " + circumference);
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not listen on port " + port);
            System.exit(-1);
        }
    }
}
