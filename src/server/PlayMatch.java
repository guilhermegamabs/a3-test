package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import util.Player;

public class PlayMatch implements Runnable {
    private final Socket player1Socket;

    public PlayMatch(Socket player1) {
        this.player1Socket = player1;
    }

    @Override
    public void run() {
        playSingle();
    }

    private void playSingle() {
        Random random = new Random();
        Player player = new Player();

        try {
            BufferedReader player1Reader = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            PrintWriter player1Writer = new PrintWriter(player1Socket.getOutputStream(), true);

            while (true) {
                // Recebe a opção do jogador
                String optionStr = player1Reader.readLine();
                if (optionStr == null) {
                    break; // Sai do loop se a leitura retornar null (conexão encerrada)
                }

                int option = Integer.parseInt(optionStr);

                if (option == 4) {
                    player1Writer.println("Jogo Encerrado!");
                    break; // Sai do loop se a opção for 4 (Sair)
                }

                int cpuOption = random.nextInt(3) + 1;

                // Lógica do jogo
                String result;
                if (option == cpuOption) {
                    player.increaseDraws();
                    result = "Empate!";
                } else if (option == 1 && cpuOption == 2 || option == 2 && cpuOption == 3 || option == 3 && cpuOption == 1) {
                    player.increaseVictories();
                    result = "Você venceu!";
                } else {
                    player.increaseDefeats();
                    result = "Você perdeu!";
                }

                // Envia o resultado da rodada
                player1Writer.println(result);

                // Envia as estatísticas atualizadas
                String stats = "Vitórias: " + player.getVictories() + " Empates: " + player.getDraws() + " Derrotas: " + player.getDefeats();
                player1Writer.println(stats);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                player1Socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
