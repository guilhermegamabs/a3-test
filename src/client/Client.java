package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
    	Scanner scanner = new Scanner(System.in);
        Socket socket = null;

        final String SERVER_IP = "localhost";
        final int SERVER_PORT = 12345;
        
    	try {
    		socket = new Socket(SERVER_IP, SERVER_PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Qual é seu nome? ");
            String playerName = scanner.nextLine();
            writer.println(playerName);
            
            System.out.println("Você vai jogar sozinho? \n(1) - Sim \n(2) - Não");
            int opcaoPlay = scanner.nextInt();
            writer.println(opcaoPlay);
            
            int opcao = 0;
            
            while(opcao != 4) {
                System.out.println("Escolha uma opção: \n(1) Papel \n(2) Pedra \n(3) Tesoura \n(4) Sair");
                opcao = scanner.nextInt();
                writer.println(opcao);
                
                String stats = reader.readLine();
                System.out.println(stats);
            }
            
            
    	} catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		scanner.close();
    		socket.close();
    	}  
    }
}