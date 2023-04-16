// A Java program for a Server
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Server implements Runnable{
	//initialize socket and input stream
    private final int MAX_PLAYERS = 4;
	private final int NUM_QUESTIONS = 15;
	private ServerSocket server = null;
	private ClientConnection[] clients = new ClientConnection[MAX_PLAYERS];

	private ArrayList<Question>  questions = new ArrayList<Question>();
	private int questionNumber = 0;
	private int port = 5000;
	// constructor with port
	public void start(){
		Thread t = new Thread(this, "Server");
		t.start();
	}

	public void run(){
		// starts server and waits for a connection
		try{
			loadQuestions();


			server = new ServerSocket(port);
			System.out.println("Server started");

            for(int x=0;x<MAX_PLAYERS;x++){
                System.out.println("Waiting for a client "+(x+1)+" ...");
                clients[x] = new ClientConnection(server.accept());
				clients[x].send(""+(x+1));
                System.out.println("Client "+(x+1)+" accepted");
            }

            boolean run = true;

			while (run){
				Question curr = questions.get(questionNumber);
				sendAll(clients,curr.stringify());
				for(int x=0; x<MAX_PLAYERS;x++){
					clients[x].receive();			
				}
				for(int x=0; x<MAX_PLAYERS;x++){
					String in = clients[x].getBuffer();
					System.out.println("User "+(x+1)+" Response: "+in);
					if(in.compareTo(curr.getAnswer()) == 0){
						clients[x].incrementScore();
						clients[x].send("CORRECT, Score is: "+clients[x].getScore());
						
					}else{
						clients[x].send("WRONG\n The Correct Answer Is: " + curr.getAnswer()
						+", Score is: "+clients[x].getScore());
					}				
				}
				for(int x=0; x<MAX_PLAYERS;x++){
					String in = clients[x].receive().getBuffer();
					if(in.compareTo("NEXT") != 0){
						System.out.println("ERR: Incorrect Client Response on "+x+": "+in);
					};				
				}
				questionNumber ++;
				if(questionNumber >= NUM_QUESTIONS){
					run = false;
				}
			}

			sendAll(clients, compositeScores());

			
		}
		catch(Exception i){
			System.out.println(i);
		}
	}
	public Server(int p){
		this.port = p;
	}

	public void closeAll(){
		System.out.println("Closing connection");
			for(int x=0;x<MAX_PLAYERS;x++){
                clients[x].close();
            }
	}

	public String compositeScores(){
		String out = "Scores:";
		for(int x=0;x<clients.length;x++){
			out = out + "~" + "Player "+(x+1)+" Score: "+clients[x].getScore();
		}
		return out;
	}

	public void loadQuestions(){
		try{
			File f = new File("questions.txt");
			Scanner reader = new Scanner(f);
			while (reader.hasNextLine()){
				questions.add(new Question(reader.nextLine()));
			}
			reader.close();
		}catch (Exception e){
		    System.out.println(e);
		}
		Collections.shuffle(questions);
	}

	public void sendAll(ClientConnection[] c, String str){
		for(int x=0; x<MAX_PLAYERS;x++){
			clients[x].send(str);
		}		
	}

	public static void main(String args[]){
		int port = args.length>0? Integer.parseInt(args[0]) : 5000;
		Server server = new Server(port);
		server.run();
		System.out.println(server);
	}
}
