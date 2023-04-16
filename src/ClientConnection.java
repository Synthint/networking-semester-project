import java.net.*;
import java.io.*;



public class ClientConnection {
   private Socket sock = null;
   private DataInputStream cliIn = null;
   private DataOutputStream cliOut = null; 
   private String buffer = "";
    private int score = 0;

    public ClientConnection(Socket s){
        this.sock = s;
        try{
            this.cliIn = new DataInputStream(
            new BufferedInputStream(sock.getInputStream()));
            
            this.cliOut = new DataOutputStream(
				sock.getOutputStream());
        }
        catch(Exception e){
            System.out.println("ERR Creation: "+e+"\n\n");
        }
    }


    public ClientConnection receive(){
        try{
            buffer = cliIn.readUTF();
        }
        catch(Exception e){
            System.out.println("ERR Receive: "+e+"\n\n");
            buffer = "READ ERR";
        }
        return this;
    }

    public void send(String msg){
        try{
            cliOut.writeUTF(msg);
        }
        catch(Exception e){
            System.out.println("ERR Send: "+e+"\n\n");
        }
    }
    
    public String getBuffer(){return buffer;}
    public ClientConnection setBuffer(String b){
        buffer = b;
        return this;
    }

    public void close(){
        try{
            cliIn.close();
            cliOut.close();
            sock.close();
        }
        catch(Exception e){
            System.out.println("ERR Close: "+e+"\n\n");
        }
    }

    public void incrementScore(){
        this.score++;
    }

    public int getScore(){
        return this.score;
    }

}
