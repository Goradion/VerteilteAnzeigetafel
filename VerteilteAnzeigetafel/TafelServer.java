/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VerteilteAnzeigetafel;
import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Simon Bastian
 */
public class TafelServer {
	private LinkedList<Queue<Message>> queueList;
	public static final int SERVER_PORT = 10001;
	private static  Anzeigetafel anzeigetafel;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	init();
    	ServerSocket socket;
		try {
			socket = new ServerSocket(SERVER_PORT);
    	while (true){
    		//print("accept...");
    		Socket client = socket.accept();
    		//print("Starte LocalThread...");
    		new LocalThread(client).start();
    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private static void init(){
    	anzeigetafel = new Anzeigetafel();
    }
    
    public static synchronized void createMessage(){
    	anzeigetafel.createMessage(null);
    }
    
    public static synchronized void deleteMessage(int messageID){
    	anzeigetafel.deleteMessage(messageID);
    }
    
    public static synchronized void modifyMessage(int messageID, String inhalt){
    	anzeigetafel.modifyMessage(messageID, inhalt);;
    }
    
    public static synchronized void activateQueue(int abteilungsID){
    	//TODO implement this method
    }
    public static synchronized void print (String nachricht)
    {
       System.out.println (nachricht);
    }
}
