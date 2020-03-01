package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.*;

public class Server {

    // zmienne globalne
    private ArrayList clients = new ArrayList();
    private PrintWriter out;

    // start programu
    public static void main(String[] args) {
        Server s = new Server();

        try {
            s.serverStart();
        } catch (Exception e) {
            // handle exception
        }
    }

    // włączenie serwera
    private void serverStart() throws IOException {

        ServerSocket ss = new ServerSocket(9806);

        // obsługiwanie połączeń
        while (true) {
            Socket socket = ss.accept();
            System.out.println("Połączono z " + ss);
            out = new PrintWriter(socket.getOutputStream());
            clients.add(out);

            Thread t = new Thread(new Client(socket));
            t.start();
        }

    }

    // klasa klienta obsługująca połączenie
    class Client implements Runnable {

        Socket socket;
        BufferedReader in;

        Client(Socket socket) {

            // dodanie socketu
            this.socket = socket;

            // obsługa bufora wejścia
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // wątek zarządząjący
        @Override
        public void run() {
            String str;
            PrintWriter out = null;

            try {

                while ((str = in.readLine()) != null) {

                    System.out.println("Odebrano => " + str);
                    Iterator it = clients.iterator();

                    // rozsyłanie wiadomości do każdego klienta
                    while (it.hasNext()) {
                        out = (PrintWriter) it.next();
                        out.println(str);
                        out.flush();
                    }
                }
            } catch (Exception e) {
                // handle exception
            }

        }

    }
}
