package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Socket socket = null;

        final String SERVER_IP = "localhost";
        final int SERVER_PORT = 12345;

        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Qual é o seu nome?");
            String playerName = scanner.nextLine();
            writer.println(playerName);

            System.out.println("Você vai jogar sozinho? \n(1) - Sim \n(2) - Não");
            int opcaoPlay = scanner.nextInt();

            boolean isPlayAlone;

            if (opcaoPlay == 1) {
                isPlayAlone = true;
            } else if (opcaoPlay == 2) {
                isPlayAlone = false;
            } else {
                System.out.println("Opção Inválida!");
                isPlayAlone = true;
            }

            writer.println(isPlayAlone);

            int opcao = 0;
            while (opcao != 4) {
                // Aguarda a opção escolhida pelo jogador
                System.out.println("\nEscolha uma opção: \n(1) Papel \n(2) Pedra \n(3) Tesoura \n(4) Sair");
                opcao = scanner.nextInt();

                // Envie a opção para o servidor
                writer.println(Integer.toString(opcao));

                // Exiba a opção escolhida pelo jogador
                System.out.println("Você escolheu a opção: " + opcao);

                // Receba e exiba as estatísticas atualizadas do jogador
                String resposta = reader.readLine();
                if (resposta.equals("ATUALIZAR_ESTATISTICAS")) {
                    // Ler e exibir as estatísticas atualizadas
                    String estatisticasAtualizadas = reader.readLine();
                    System.out.println(estatisticasAtualizadas);
                    
                    // Envie uma confirmação de que as estatísticas foram recebidas
                    writer.println("STATS_RECEIVED");
                }
            }

            System.out.println("Jogo Encerrado!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
