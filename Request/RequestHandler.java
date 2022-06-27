package Request;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Scanner;

import Database.Database;


public class RequestHandler extends Thread {
    Socket socket;
    DataOutputStream dos;
    DataInputStream dis;

    public RequestHandler(Socket socket) {
        this.socket = socket;
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.err.println("Handling request");
        try {
            String request = listen();
            System.err.println("Request: " + request);

            Scanner sc = new Scanner(request);
            String command = sc.nextLine();
            System.err.println(command);
            Map<String, String> data = Convertor.stringToMap(sc.nextLine());
            sc.close();

            if (command.equals("sendMessage")) {
                System.err.println("Sending message");
                Database.getDatabase().getTable("Messages").insert(data);
                System.err.println("Message sent");
            }

            send("OK\n");

        


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dos.close();
                dis.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String listen() throws IOException {

        StringBuilder sb = new StringBuilder();
        int c = dis.read();
        while (c != 0) {
            sb.append((char) c);
            c = dis.read();
        }

        return sb.toString();
    }

    void send(String s) throws IOException {
        dos.writeUTF(s);
    }


    
}
