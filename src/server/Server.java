package server;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

import util.Player;

public class Server {
	private static final int PORT = 12345;
    private static ArrayList<Socket> multiplayerList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server is Running!");

        // Cria uma thread para lidar com a lista de jogadores multiplayer
        Thread multiplayerThread = new Thread(new MultiplayerHandler());
        multiplayerThread.start();

        while (true) {
            Socket playerSocket = serverSocket.accept();
            BufferedReader playerReader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            PrintWriter playerWriter = new PrintWriter(playerSocket.getOutputStream(), true);

            // Recebe o nome do jogador
            String playerName = playerReader.readLine();
            System.out.println("Jogador " + playerName + " conectado!");

            int opcaoPlay = Integer.parseInt(playerReader.readLine());
            if (opcaoPlay == 1) {
                // Jogador escolheu jogar sozinho
                playSingle(playerReader, playerWriter);
            } else {
                // Jogador escolheu jogar multiplayer
                multiplayerList.add(playerSocket);
                playerWriter.println("Esperando Oponente se conectar!");
                playerWriter.flush();
            }
        }
    }

    private static class MultiplayerHandler implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (multiplayerList.size() >= 2) {
                    // Encontra dois jogadores que desejam jogar multiplayer
                    Socket player1Socket = multiplayerList.remove(0);
                    Socket player2Socket = multiplayerList.remove(0);

                    // Cria uma instância de jogo multiplayer para os dois jogadores
                    try {
						playMultiplayer(player1Socket, player2Socket);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                } else {
                    // Aguarda um pouco antes de verificar novamente
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private static void playSingle(BufferedReader player1Reader, PrintWriter player1Writer) throws NumberFormatException, IOException {
        Random random = new Random();
        Player player = new Player();

        int opcao = 0;

        while (opcao != 4) {
            int serverOption = random.nextInt(3) + 1;
            opcao = Integer.parseInt(player1Reader.readLine());

            if (opcao == serverOption) {
                player.increaseDraws();
            } else if (opcao == 1 && serverOption == 2 || opcao == 2 && serverOption == 3 || opcao == 3 && serverOption == 1) {
                player.increaseVictories();
            } else if (opcao == 2 && serverOption == 1 || opcao == 3 && serverOption == 2 || opcao == 1 && serverOption == 3) {
                player.increaseDefeats();
            } else if (opcao == 4) {
                System.out.println("Jogo Encerrado!");
            }

            // Enviar para o client
            String stats = "Vitórias: " + player.getVictories() + " | Empates: " + player.getDraws() + " | Derrotas: " + player.getDefeats();
            player1Writer.println(stats);
            player1Writer.flush();
        }
    }

    private static void playMultiplayer(Socket player1Socket, Socket player2Socket) throws NumberFormatException, IOException {
        BufferedReader player1Reader = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
        PrintWriter player1Writer = new PrintWriter(player1Socket.getOutputStream(), true);
        BufferedReader player2Reader = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
        PrintWriter player2Writer = new PrintWriter(player2Socket.getOutputStream(), true);

        Player player1 = new Player();
        Player player2 = new Player();
        
        String foundOpo = "Oponente Encontrado!";
        player1Writer.println(foundOpo);
        player1Writer.flush();

        player2Writer.println(foundOpo);
        player2Writer.flush();

        int choosedOption1 = 0;
        int choosedOption2 = 0;

        while (choosedOption1 != 4 && choosedOption2 != 4) {
            String chooseOption1 = "Escolha uma opção: (1) Papel (2) Pedra (3) Tesoura (4) Sair";
            player1Writer.println(chooseOption1);
            player1Writer.flush();

            choosedOption1 = Integer.parseInt(player1Reader.readLine());

            if (choosedOption1 == 4) {
                player1Writer.println("Jogo Encerrado!");
                player1Writer.flush();
                player2Writer.println("Oponente saiu do jogo. Jogo Encerrado!");
                player2Writer.flush();
                break;
            }

            String chooseOption2 = "Escolha uma opção: (1) Papel (2) Pedra (3) Tesoura (4) Sair";
            player2Writer.println(chooseOption2);
            player2Writer.flush();

            choosedOption2 = Integer.parseInt(player2Reader.readLine());

            if (choosedOption2 == 4) {
                player2Writer.println("Jogo Encerrado!");
                player2Writer.flush();
                player1Writer.println("Oponente saiu do jogo. Jogo Encerrado!");
                player1Writer.flush();
                break;
            }

            if (choosedOption1 == choosedOption2) {
                player1.increaseDraws();
                player2.increaseDraws();
            } else if (choosedOption1 == 1 && choosedOption2 == 2 || choosedOption1 == 2 && choosedOption2 == 3 || choosedOption1 == 3 && choosedOption2 == 1) {
                player1.increaseVictories();
                player2.increaseDefeats();
            } else if (choosedOption1 == 2 && choosedOption2 == 1 || choosedOption1 == 3 && choosedOption2 == 2 || choosedOption1 == 1 && choosedOption2 == 3) {
                player1.increaseDefeats();
                player2.increaseVictories();
            }

            // Enviar para o client
            String stats1 = "Vitórias: " + player1.getVictories() + " | Empates: " + player1.getDraws() + " | Derrotas: " + player1.getDefeats();
            player1Writer.println(stats1);
            player1Writer.flush();

            // Enviar para o client
            String stats2 = "Vitórias: " + player2.getVictories() + " | Empates: " + player2.getDraws() + " | Derrotas: " + player2.getDefeats();
            player2Writer.println(stats2);
            player2Writer.flush();
        }
    }
}