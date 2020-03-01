package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    // zmienne globalne
    private BufferedReader in;
    private String name;

    // start programu
    public static void main(String[] args) {

        Client client = new Client();
        client.startClient();

    }

    // uruchomienie klienta
    private void startClient() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj nazwę: ");
        name = scanner.nextLine();

        try {
            Socket socket = new Socket("localhost",9806);
            System.out.println("Połączono z " + socket);

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread t = new Thread(new Receiver());
            t.start();

            while (true) {
                System.out.println(" >> ");
                String str = scanner.nextLine();
                if (!str.equalsIgnoreCase("q")) {
                    out.println(name + ": " + str);
                    out.flush();
                } else {
                    out.println(name + " opuścił czat");
                    out.flush();
                    out.close();
                    scanner.close();
                    socket.close();
                }
            }

        } catch (Exception e) {
            System.out.println("Connection error.");
        }

    }

    class Receiver implements Runnable {



        @Override
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    String[] tab = message.split(":");
                    if (!tab[0].equals(name)) System.out.println(message);
                }
            } catch (Exception e) {
                // handle exception
            }

        }
    }

}

