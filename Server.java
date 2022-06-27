

import java.io.IOException;
import java.net.*;

import Request.RequestHandler;

public class Server {

    static int port = 8080;
    
    public static void main(String[] args) {
        ServerSocket ss = null;

        try {
            ss = new ServerSocket(port);

            while (true) {
                Socket socket = ss.accept();
                System.err.println("Accepted connection from " + socket.getInetAddress());

                new RequestHandler(socket).start();
            }
            
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
}