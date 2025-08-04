package th.mfu;

import java.io.*;
import java.net.*;

public class MockWebClient {
    public static void main(String[] args) {
        try {
            //  Create a socket to connect to the web server on port 8080
            Socket clientSocket = new Socket("localhost", 8080);

            //  Create output and input streams for the socket
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //  Send an HTTP GET request to the web server
            String request = "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n";
            out.print(request);
            out.flush();  // Important to flush the stream

            //  Read and print the full response from the server
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            //  Close resources
            in.close();
            out.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
