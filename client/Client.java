// A Java program for a Client
import java.io.*;
import java.net.*;

public class Client {
	// initialize socket and input output streams
	ServerConnection server = null;

	// constructor to put ip address and port
	public Client(String address, int port){
		// establish a connection
		try {
			server = new ServerConnection(new Socket(address, port));
			System.out.println("Connected");
		}
		catch (UnknownHostException u) {
			System.out.println(u);
			return;
		}
		catch (IOException i) {
			System.out.println(i);
			return;
		}

		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        /*/////////////////////////////
        
        
        
        
        
        */////////////////////////////



		String line = "";
		// keep reading until "Over" is input
		while (!line.equals("Over")) {
			try {
				line = userInput.readLine();
				server.send(line);
				System.out.println("Server: "+server.receive().getBuffer());






				
			}
			catch (IOException i) {
				System.out.println(i);
			}
		}



        /*/////////////////////////////
        
        
        
        
        
        */////////////////////////////
		// close the connection
		
		server.close();
		
	}

	public static void main(String args[]){
		Client client = new Client("127.0.0.1", 5000);
	}
}
