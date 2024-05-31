package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

import util.Player;

public class PlayMatch implements Runnable {
    private final Socket player1Socket;
    private final Socket player2Socket;

    public PlayMatch(Socket player1) {
        this.player1Socket = player1;
        this.player2Socket = null;
    }

    @Override
    public void run() {
        if (player2Socket == null) {
            playSingle();
        } else {
            playDuo();
        }
    }

    private void playSingle() {
        Random random = new Random();
        Player player = new Player();

        try {
            BufferedReader player1Reader = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            PrintWriter player1Writer = new PrintWriter(player1Socket.getOutputStream(), true);

            while (true) {
                String optionStr = player1Reader.readLine();
                int option = Integer.parseInt(optionStr);

                if (option == 4) {
                    break; // Sai do loop se a opção for 4 (Sair)
                }

                int cpuOption = random.nextInt(3) + 1;

                // Lógica do jogo aqui...
                if (option == cpuOption) {
                    player.increaseDraws();
                } else if (option == 1 && cpuOption == 2 || option == 2 && cpuOption == 3 || option == 3 && cpuOption == 1) {
                    player.increaseVictories();
                } else {
                    player.increaseDefeats();
                }

                // Envie as estatísticas atualizadas de volta para o cliente
                player1Writer.println("\nVitórias: " + player.getVictories() + "\nEmpates: " + player.getDraws() + "\nDerrotas: " + player.getDefeats());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void playDuo() {

    }
}