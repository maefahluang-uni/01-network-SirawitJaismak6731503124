package th.mfu;

import java.io.*;
import java.net.*;

public class MockWebServer implements Runnable {

    private int port;

    public MockWebServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        //  Start the server socket inside a try block
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Mock Web Server running on port " + port + "...");

            while (true) {
                //  Accept incoming client connection
                try (
                    Socket clientSocket = serverSocket.accept();
                    //  Create input/output streams
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
                ) {
                    //  Read client request (optional: for debug)
                    String line;
                    while ((line = input.readLine()) != null && !line.isEmpty()) {
                        System.out.println("Request: " + line);
                    }

                    //  Prepare and send HTTP response
                    String response = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n"
                            + "<html><body>Hello, Web! on Port " + port + "</body></html>";
                    out.println(response);

                    //  Resources are auto-closed via try-with-resources

                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Failed to start server on port " + port + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        //  Start two server threads
        Thread server1 = new Thread(new MockWebServer(8080));
        Thread server2 = new Thread(new MockWebServer(8081));

        server1.start();
        server2.start();

        System.out.println("Press ENTER to stop the servers...");
        try {
            System.in.read();

            //  Graceful shutdown suggestion (servers will stop when JVM exits)
            System.out.println("Stopping servers...");
            // Note: Threads will keep running as there’s no exit condition in the run() loop.
            // You would need a shutdown flag or socket close to actually stop them.

            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
