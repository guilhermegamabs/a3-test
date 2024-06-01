package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        final int PORT = 12345;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado...");

            while (true) {
                Socket player1 = serverSocket.accept();
                System.out.println("Cliente conectado!");

                Thread thread = new Thread(() -> {
                    try {
                        BufferedReader player1Reader = new BufferedReader(new InputStreamReader(player1.getInputStream()));
                        PrintWriter player1Writer = new PrintWriter(player1.getOutputStream(), true);

                        // Recebe o nome do jogador
                        String playerName = player1Reader.readLine();
                        System.out.println("Jogador conectado: " + playerName);

                        // Recebe a opção de jogo (true para jogar sozinho, false para jogar em dupla)
                        boolean isPlayAlone = Boolean.parseBoolean(player1Reader.readLine());
                        System.out.println("Jogar sozinho: " + isPlayAlone);

                        if (isPlayAlone) {
                            Thread gameThread = new Thread(new PlayMatch(player1));
                            gameThread.start();
                        } else {
                            System.out.println("EM DESENVOLVIMENTO!");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
