/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VerteilteAnzeigetafel;
import java.io.*;
import java.net.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Simon Bastian
 */
public class TafelServer {
	private static LinkedList<Queue<Message>> queueList;
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
//    	File f = new File("tafel");
//    	if(f.exists() && !f.isDirectory()){
//    		try {
//				anzeigetafel = Anzeigetafel.loadStateFromFile();
//				print("Anzeigetafel geladen!");
//				//anzeigetafel.saveStateToFile();
//				printMessages();
//			} catch (TafelException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    	} else {
//    		anzeigetafel = new Anzeigetafel();
//    		try {
//				f.createNewFile();
//				print("Anzeigetafel erstellt!");
//				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    	}
    }
    
    public static synchronized void createMessage(String msg, int userID, int abtNr, boolean oeffentlich) throws TafelException{
    	Message m = new Message(msg, userID, abtNr, oeffentlich);
    	anzeigetafel.createMessage(m, userID);
    	print("Nachricht erstellt:\n"+m);
    }
    
    public static synchronized void deleteMessage(int messageID, int userID) throws TafelException{
    	anzeigetafel.deleteMessage(messageID, userID);
    	print("User mit ID="+userID+" hat Nachricht mit ID="+messageID+" gelöscht!");
    }
    
    public static synchronized void publishMessage(int messageID){
    	for (Queue<Message> q : queueList){
    		q.add(anzeigetafel.getMessages().get(messageID));
    	}
    }
    public static synchronized void modifyMessage(int messageID, String inhalt,int userID) throws TafelException{
    	anzeigetafel.modifyMessage(messageID, inhalt, userID);
    	print("User mit ID="+userID+" hatNachricht mit ID="+messageID+" geändert!");
    }
    
    public static synchronized void activateQueue(int abteilungsID){
    	//TODO implement this method
    }
    public static synchronized void print (String nachricht)
    {
       System.out.println (nachricht);
    }
    public static synchronized void printMessages(){
    		System.out.println(anzeigetafel.getMessages().toString());
    }
    
    public static synchronized LinkedList<Message> getMessagesByUserID(int userID){
    	print("Showing Messages to user "+userID);
    	Collection<Message> allMessages = anzeigetafel.getMessages().values();
    	LinkedList<Message> userMessages = new LinkedList<Message>();
    	for(Message m : allMessages){
    		if (m.getUserID() == userID){
    			userMessages.add(m);
    		}
    	}
    	return userMessages;
    	
    }
}
