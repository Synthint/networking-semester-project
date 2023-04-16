// A Java program for a Client
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Client implements ActionListener{
	// initialize socket and input output streams
	ServerConnection server = null;
	private BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
	
	JFrame frame = new JFrame("Client");

	JPanel cards = new JPanel(new CardLayout());

    JPanel questionPanel = new JPanel();
    JButton buttonA = new JButton("A");
	JButton buttonB = new JButton("B");
	JButton buttonC = new JButton("C");
	JButton buttonD = new JButton("D");
    JLabel questionLabel = new JLabel("Number of clicks: ");

	JPanel answerValidPanel = new JPanel();
	JButton nextButton = new JButton("NEXT");
	JLabel answerLabel = new JLabel();

	JPanel connectionPanel = new JPanel();
	JButton connectButton = new JButton("Connect");
	JButton hostButton = new JButton("Host Game On Port");

	JPanel scorePanel = new JPanel();

	boolean connectedAndPlaying = false;
	// constructor to put ip address and port
	String state = "Disconnected";

	Question curr;

	JLabel playerName = new JLabel("Player ");

	JTextField hostAddr = new JTextField();
	JTextField portNo = new JTextField();
	JLabel addrLabel = new JLabel("Address: ");
	JLabel portLabel = new JLabel("Port: ");

	public void updateState(String s){
		state = s;
		System.out.println(state);
	}

	public Client(){
		buildGUI();
	}

	public String getUserInput(){
		try {
			return userInput.readLine();
		}
		catch (Exception i) {
			System.out.println(i);
		}
		return "INVALID";
	}
	public static void main(String args[]){
		Client client = new Client();
		System.out.println(client);
	}

	public void actionPerformed(ActionEvent e){
		updateState("Input Recieved");
		if(e.getSource().equals(buttonA) ||
			e.getSource().equals(buttonB) ||
			e.getSource().equals(buttonC) ||
			e.getSource().equals(buttonD)){
				updateState("Sending Answer");
				server.send(((JButton)e.getSource()).getText());
				updateState("Awaiting Server For Answer Validation");
				String answerValidation = server.receive().getBuffer();
				updateState("Loading Answer Validation");
				GUILoadAnswer(answerValidation);
				updateState("Loaded Answer Validation");
		}

		if(e.getSource().equals(nextButton)){
			updateState("Requesting Next Question");
			server.send("NEXT");
			updateState("Awaiting Server For Next Question");
			String nextServerResponse = server.receive().getBuffer();
			if(nextServerResponse.charAt(0) == 'Q'){
				curr = new Question(nextServerResponse);
				updateState("Recieved Next Question");
				GUILoadQuestion(curr);
				updateState("Loaded Next Question");
			}else if(nextServerResponse.charAt(0) == 'S'){
				updateState("Recieved Scores");
				GUILoadScores(nextServerResponse);
			}
			
		}

		if(e.getSource().equals(connectButton)){
			try {
				server = new ServerConnection(new Socket(hostAddr.getText(), Integer.parseInt(portNo.getText())));
			}
			catch (Exception u) {
				System.out.println(u);
				return;
			}
			updateState("Connected");
			playerName.setText("Player "+server.receive().getBuffer());
			curr = new Question(server.receive().getBuffer());
			updateState("Recieved First Question");
			GUILoadQuestion(curr);
			updateState("Loaded First Question");
		}

		if(e.getSource().equals(hostButton)){
			Server serv = new Server(Integer.parseInt(portNo.getText()));
			Thread ser = new Thread(serv,"Server");
			ser.start();
			
			try {
				Thread.sleep(50);
				server = new ServerConnection(new Socket("127.0.0.1", Integer.parseInt(portNo.getText())));
			}
			catch (Exception u) {
				System.out.println(u);
				return;
			}
			try {
				JOptionPane.showMessageDialog(frame, "Host for other players to connect: "+InetAddress.getLocalHost());
			}
			catch (Exception u) {
				System.out.println(u);
			}
			updateState("Connected");
			playerName.setText("Player "+server.receive().getBuffer());
			curr = new Question(server.receive().getBuffer());
			updateState("Recieved First Question");
			GUILoadQuestion(curr);
			updateState("Loaded First Question");
			
		}
    }



	private void GUILoadScores(String sc) {
		String[] scores = sc.split("~");
		JLabel[] labels = new JLabel[scores.length];
		Font font1 = new Font("SansSerif", Font.BOLD, 18);
		for(int x=0; x<labels.length; x++){
			labels[x] = new JLabel(scores[x]);
			scorePanel.add(labels[x]);
			labels[x].setFont(font1);
		}

		updateState("UI Load Scores");
		CardLayout ui = (CardLayout)(cards.getLayout());
		ui.show(cards, "Scores");
	}

	public void GUILoadQuestion(Question q){
		questionLabel.setText(q.getQuestion());
		String[] ans = {q.getAnswer(),q.getFake1(),q.getFake2(),q.getFake3()};
		
		Collections.shuffle(Arrays.asList(ans));

		buttonA.setText(ans[0]);
		buttonB.setText(ans[1]);
		buttonC.setText(ans[2]);
		buttonD.setText(ans[3]);

		updateState("UI Load Question Content");

		CardLayout ui = (CardLayout)(cards.getLayout());
		ui.show(cards, "Question");
	}

	public void GUILoadAnswer(String answer){
		answerLabel.setText(answer);
		updateState("UI Load Answer");

		CardLayout ui = (CardLayout)(cards.getLayout());
		ui.show(cards, "Answer");
	}

	public void buildGUI(){
		Font font1 = new Font("SansSerif", Font.BOLD, 20);
		questionLabel.setFont(font1);
		buttonA.setFont(font1);
		buttonB.setFont(font1);
		buttonC.setFont(font1);
		buttonD.setFont(font1);

		buttonA.addActionListener(this);
		buttonB.addActionListener(this);
		buttonC.addActionListener(this);
		buttonD.addActionListener(this);
        questionPanel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        questionPanel.setLayout(new GridLayout(5,5));

		questionPanel.add(questionLabel);
        questionPanel.add(buttonA);
		questionPanel.add(buttonB);
		questionPanel.add(buttonC);
		questionPanel.add(buttonD);

		addrLabel.setFont(font1);
		hostAddr.setFont(font1);
		portNo.setFont(font1);
		portLabel.setFont(font1);
		connectionPanel.add(addrLabel);
		connectionPanel.add(hostAddr);
		connectionPanel.add(portLabel);
		connectionPanel.add(portNo);
		connectButton.addActionListener(this);
		hostButton.addActionListener(this);
		connectionPanel.add(connectButton);
		connectionPanel.add(hostButton);
		connectionPanel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        connectionPanel.setLayout(new GridLayout(5,5));

		answerValidPanel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        answerValidPanel.setLayout(new GridLayout(5,5));
		nextButton.addActionListener(this);
		nextButton.setFont(font1);
		answerLabel.setFont(font1);
		answerValidPanel.add(answerLabel);
		answerValidPanel.add(nextButton);

	
		scorePanel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        scorePanel.setLayout(new GridLayout(5,5));
		
		JPanel waitPanel = new JPanel();
		JLabel waitLabel = new JLabel("WAITING FOR OTHER PLAYERS");
		waitLabel.setFont(font1);
		waitPanel.add(waitLabel);
		

		cards.add(connectionPanel, "Connect");
		cards.add(questionPanel, "Question");
		cards.add(answerValidPanel, "Answer");
		cards.add(scorePanel, "Scores");
		cards.add(waitPanel,"Wait");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(600, 600));
        frame.pack();

		playerName.setFont(font1);
		frame.add(playerName, BorderLayout.NORTH);
		frame.add(cards, BorderLayout.CENTER);
		

        frame.setVisible(true);
	}

}
