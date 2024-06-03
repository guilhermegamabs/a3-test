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
					Thread thread = new Thread(new PlayMatch(player1));
					thread.run();
				}
			}catch(Exception e) {
				e.printStackTrace();
			} finally {
				
			}
    }
}

class PlayMatch implements Runnable {
	private Socket player1 = null;
	private Socket player2 = null;

	public PlayMatch(Socket player1) {
		this.player1 = player1;
	}
	
	public PlayMatch(Socket player1, Socket player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

	@Override
	public void run() {
		try {
            BufferedReader player1Reader = new BufferedReader(new InputStreamReader(player1.getInputStream()));
            PrintWriter player1Writer = new PrintWriter(player1.getOutputStream(), true);  // Enable auto-flush
            
            // Recebe o nome do jogador
            String playerName = player1Reader.readLine();
            System.out.println("Jogador " + playerName + " conectado!");
            
            int opcaoPlay = Integer.parseInt(player1Reader.readLine());
            System.out.println("JOGAR SOZINHO? " + opcaoPlay);

            if(opcaoPlay == 1) {
            	if(player2 == null) {
            		playSingle(player1Reader, player1Writer);
            	}
            } else {
            	System.out.println("Vai jogar com outro jogador!");
             }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void playSingle(BufferedReader player1Reader, PrintWriter player1Writer) throws NumberFormatException, IOException {
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
            } else {
                player.increaseDefeats();
            }
            
            // Enviar para o client
            String stats = "Vitórias: " + player.getVictories() + " | Empates: " + player.getDraws() + " | Derrotas: " + player.getDefeats();
            player1Writer.println(stats);
            player1Writer.flush();
        }	
	}
}