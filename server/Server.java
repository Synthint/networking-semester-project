// A Java program for a Server

import java.net.*;
import java.io.*;
//import 

public class Server{
	//initialize socket and input stream
    private final int MAX_PLAYERS = 2;
	private ServerSocket server = null;
	private ClientConnection[] clients = new ClientConnection[MAX_PLAYERS];

	// constructor with port
	public Server(int port){
		// starts server and waits for a connection
		try{
			server = new ServerSocket(port);
			System.out.println("Server started");

            for(int x=0;x<MAX_PLAYERS;x++){
                System.out.println("Waiting for a client "+(x+1)+" ...");
                clients[x] = new ClientConnection(server.accept());
                System.out.println("Client "+(x+1)+" accepted");
            }

            boolean run = true;
			// reads message from client until "Over" is sent
			while (run){
				

            	for(int x=0; x<MAX_PLAYERS;x++){
					System.out.println("Client "+x+": "+
										clients[x].receive()
											.getBuffer());
					clients[x].send("Hello "+x+"! how are you?");
                }
					

			}





			System.out.println("Closing connection");
			for(int x=0;x<MAX_PLAYERS;x++){
                clients[x].close();
            }
		}
		catch(IOException i){
			System.out.println(i);
		}
	}

	





	public static void main(String args[]){
		Server server = new Server(5000);
	}
}
