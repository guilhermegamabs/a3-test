package server;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import util.Player;

public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
			try {
				ServerSocket serverSocket = new ServerSocket(PORT);
				System.out.println("Server is Running!");
			
				while(true) {
					Socket player1 = serverSocket.accept();
					BufferedReader player1Reader = new BufferedReader(new InputStreamReader(player1.getInputStream()));
		            PrintWriter player1Writer = new PrintWriter(player1.getOutputStream(), true);  // Enable auto-flush
					
		            // Recebe o nome do jogador
		            String playerName = player1Reader.readLine();
		            System.out.println("Jogador " + playerName + " conectado!");
		            
		            int opcaoPlay = Integer.parseInt(player1Reader.readLine());

		            if(opcaoPlay == 1) {
		            	playSingle(player1Reader, player1Writer);
		            } else {
		            	System.out.println("Vai jogar com outro jogador!");
		             }
				}
			}catch(Exception e) {
				e.printStackTrace();
			} finally {
				
			}
    }
    
    private static void playSingle(BufferedReader player1Reader, PrintWriter player1Writer) throws NumberFormatException, IOException {
		Random random = new Random();
		Player player = new Player();
		
		int opcao = 0;
        
		while(opcao !=4 ) {
        	
        	int serverOption = random.nextInt(3) + 1;
            opcao = Integer.parseInt(player1Reader.readLine());
            
            if (opcao == serverOption) {
                player.increaseDraws();
            } else if (opcao == 1 && serverOption == 2 || opcao == 2 && serverOption == 3 || opcao == 3 && serverOption == 1) {
                player.increaseVictories();
            } else if(opcao == 2 && serverOption == 1 || opcao == 3 && serverOption == 2 || opcao == 1 && serverOption == 3){
                player.increaseDefeats();
            } else if(opcao == 4) {
            	System.out.println("Jogo Encerrado!");
            }
            
            // Enviar para o client
            String stats = "Vitórias: " + player.getVictories() + " | Empates: " + player.getDraws() + " | Derrotas: " + player.getDefeats();
            player1Writer.println(stats);
            player1Writer.flush();
        }	
	}
}