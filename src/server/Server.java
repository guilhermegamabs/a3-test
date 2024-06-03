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
		            	String waitOpo = "Esperando Oponente se conectar!";
		            	player1Writer.println(waitOpo);
		            	player1Writer.flush();
		            	
		            	Socket player2 = serverSocket.accept();
		            	BufferedReader player2Reader = new BufferedReader(new InputStreamReader(player2.getInputStream()));
			            PrintWriter player2Writer = new PrintWriter(player2.getOutputStream(), true);  // Enable auto-flush
			            
			            String playerName2 = player2Reader.readLine();
			            System.out.println("Jogador " + playerName2 + " conectado!");
			            
			            int opcaoPlay2 = Integer.parseInt(player2Reader.readLine());

			            if(opcaoPlay2 == 2) {
			            	player2Writer.println(waitOpo);
			            	player2Writer.flush();
			            	
				            String foundOpo = "Oponente Encontrado!";
			            	player1Writer.println(foundOpo);
			            	player1Writer.flush();
			            	
			            	player2Writer.println(foundOpo);
			            	player2Writer.flush();
			            	
			            	playMultiplayer(player1Reader, player1Writer, player2Reader, player2Writer);
			            }
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
    
    private static void playMultiplayer(BufferedReader player1Reader, PrintWriter player1Writer, BufferedReader player2Reader, PrintWriter player2Writer) throws NumberFormatException, IOException {
    	Player player1 = new Player();
    	Player player2 = new Player();
    	
        int choosedOption1 = 0;
        int choosedOption2 = 0;
		
        while(choosedOption1 !=4 || choosedOption2 != 4) {
        	String chooseOption1 = "Escolha uma opção: (1) Papel (2) Pedra (3) Tesoura (4) Sair";
	    	player1Writer.println(chooseOption1);
	    	player1Writer.flush();
	    	
	        choosedOption1 = Integer.parseInt(player1Reader.readLine());
	        
	        String chooseOption2 = "Escolha uma opção: (1) Papel (2) Pedra (3) Tesoura (4) Sair";
	    	player2Writer.println(chooseOption2);
	    	player2Writer.flush();

	        choosedOption2 = Integer.parseInt(player2Reader.readLine());
	        
	        if (choosedOption1 == choosedOption2) {
	        	player1.increaseDraws();
	        	player2.increaseDraws();
            } else if (choosedOption1 == 1 && choosedOption2 == 2 || choosedOption1 == 2 && choosedOption2 == 3 || choosedOption1 == 3 && choosedOption2 == 1) {
            	player1.increaseVictories();
	        	player2.increaseDefeats();
            } else if(choosedOption1 == 2 && choosedOption2 == 1 || choosedOption1 == 3 && choosedOption2 == 2 || choosedOption1 == 1 && choosedOption2 == 3){
            	player1.increaseDefeats();
            	player2.increaseVictories();
            } else if(choosedOption1 == 4 || choosedOption2 == 4) {
            	System.out.println("Jogo Encerrado!");
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