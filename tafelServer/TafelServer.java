/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tafelServer;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import verteilteAnzeigetafel.Anzeigetafel;
import verteilteAnzeigetafel.Message;
import verteilteAnzeigetafel.TafelException;

/**
 *
 * @author Simon Bastian
 */
public class TafelServer {
	private static HashMap<Integer, LinkedBlockingQueue<Message>> queueMap = new HashMap<Integer, LinkedBlockingQueue<Message>>();
	private static HashMap<Integer, SocketAddress> tafelAdressen = new HashMap<Integer, SocketAddress>();
	private static HashMap<Integer, OutboxThread> outboxThreads = new HashMap<Integer, OutboxThread>();
	private static HashMap<Integer, HeartbeatThread> heartbeatThreads = new HashMap<Integer, HeartbeatThread>();
	public static final int SERVER_PORT = 10001;
	private static Anzeigetafel anzeigetafel;
	private static int abteilungsID;

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		if (args.length >=1){
			try{
			abteilungsID = Integer.parseInt(args[0]);
			print("AbteilungsID="+abteilungsID);
			} catch (NumberFormatException nfe){
				print(args[0]+ "ist keine Integerzahl");
			}
		} else {
			abteilungsID = 1;
		}
		init();
		ServerSocket socket;
		try {
			socket = new ServerSocket(SERVER_PORT);
			while (true) {
				// print("accept...");
				Socket client = socket.accept();
				// print("Starte LocalThread...");
				new LocalThread(client).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void init() {
		anzeigetafel = Anzeigetafel.loadStateFromFile();
		if(anzeigetafel != null){
			print("Anzeigetafel aus Datei geladen!");
		} else {
			anzeigetafel = new Anzeigetafel(abteilungsID);
			anzeigetafel.saveStateToFile();
			print("Neue Anzeigetafel erstellt!");
		}
		queueMap = loadQueueMapFromFile();
		tafelAdressen = loadTafelAdressenFromFile();
		
		//tafelAdressen.put(1, new InetSocketAddress("localhost", SERVER_PORT));
		// LinkedBlockingQueue<Message> q = new LinkedBlockingQueue<Message>();
		// q.add(new Message("AAAAAAAAAAAAAAAAAAAA", 1, 2, true, 4711));
		// q.add(new Message("BBBBBBBBBBBBBBBBBBBB", 1, 2, true, 4711));
		// queueMap.put(1, q);
		// activateQueue(1);
		// File f = new File("tafel");
		// if(f.exists() && !f.isDirectory()){
		// try {
		// anzeigetafel = Anzeigetafel.loadStateFromFile();
		// print("Anzeigetafel geladen!");
		// //anzeigetafel.saveStateToFile();
		// printMessages();
		// } catch (TafelException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// } else {
		// anzeigetafel = new Anzeigetafel();
		// try {
		// f.createNewFile();
		// print("Anzeigetafel erstellt!");
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
	}

	public static synchronized int createMessage(String msg, int userID, int abtNr, boolean oeffentlich)
			throws TafelException, InterruptedException {
		int msgID = anzeigetafel.createMessage(msg, userID, abtNr, oeffentlich);
		print("Nachricht erstellt:\n" + anzeigetafel.getMessages().get(msgID));
		anzeigetafel.saveStateToFile();
		return msgID;
	}
	
	public static synchronized void receiveMessage(Message msg) throws TafelException{
		anzeigetafel.receiveMessage(msg);
		print("Nachricht von Abteilung "+msg.getAbtNr()+" erhalten!");
		anzeigetafel.saveStateToFile();
	}
	
	public static synchronized void deleteMessage(int messageID, int userID) throws TafelException {
		anzeigetafel.deleteMessage(messageID, userID);
		print("User mit ID=" + userID + " hat Nachricht mit ID=" + messageID + " gelöscht!");
		anzeigetafel.saveStateToFile();
	}

	public static synchronized void publishMessage(int messageID, int userID)
			throws InterruptedException, TafelException {
		anzeigetafel.publishMessage(messageID, userID);
		for (LinkedBlockingQueue<Message> q : queueMap.values()) {
			q.put(anzeigetafel.getMessages().get(messageID));
		}
		anzeigetafel.saveStateToFile();

	}

	

	public static synchronized void modifyMessage(int messageID, String inhalt, int userID) throws TafelException {
		anzeigetafel.modifyMessage(messageID, inhalt, userID);
		print("User mit ID=" + userID + " hatNachricht mit ID=" + messageID + " geändert!");
		anzeigetafel.saveStateToFile();
	}
	
	public static synchronized LinkedList<Message> getMessagesByUserID(int userID) throws TafelException {
		print("Showing Messages to user " + userID);
		return anzeigetafel.getMessagesByUserID(userID);
	}
	public static synchronized void registerTafel(int abteilungsID, SocketAddress address) throws TafelException{
		if(abteilungsID==anzeigetafel.getAbteilungsID()){
			throw new TafelException("Die eigene Abteilung wird nicht registriert");
		}
		
		if(tafelAdressen.containsKey(abteilungsID)){
			if(!tafelAdressen.get(abteilungsID).equals(address)){
				tafelAdressen.replace(abteilungsID, address);
			}
						
		}else {
			tafelAdressen.put(abteilungsID, address);
			queueMap.put(abteilungsID, new LinkedBlockingQueue<Message>());
		}
		activateHeartbeat(abteilungsID);
		activateQueue(abteilungsID);
		saveTafelAdressenToFile();
	}
	public static synchronized void activateQueue(int abteilungsID) {
		if (!outboxThreads.containsKey(abteilungsID) || !outboxThreads.get(abteilungsID).isAlive()) {
			OutboxThread obt = new OutboxThread(abteilungsID, tafelAdressen.get(abteilungsID),
					queueMap.get(abteilungsID));
			outboxThreads.put(abteilungsID, obt);
			obt.start();
		} // else Queue already active
	}
	
	public static synchronized void activateHeartbeat(int abteilungsID){
		if (!heartbeatThreads.containsKey(abteilungsID) || !heartbeatThreads.get(abteilungsID).isAlive()) {
			HeartbeatThread hbt = new HeartbeatThread(abteilungsID, tafelAdressen.get(abteilungsID));
			heartbeatThreads.put(abteilungsID, hbt);
			hbt.start();
		}
	}
	
	public static synchronized void saveQueueMapToFile() {
		FileOutputStream fileoutput = null;
		ObjectOutputStream objoutput = null;
		try {
			fileoutput = new FileOutputStream("./QueueMap");
			objoutput = new ObjectOutputStream(fileoutput);
			objoutput.writeObject(queueMap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				objoutput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static HashMap<Integer, LinkedBlockingQueue<Message>> loadQueueMapFromFile() {
		HashMap<Integer, LinkedBlockingQueue<Message>> qMap = new HashMap<Integer, LinkedBlockingQueue<Message>>();
		FileInputStream fileInput = null;
		ObjectInputStream objinput = null;
		try {
			fileInput = new FileInputStream("./queueMap");
			objinput = new ObjectInputStream(fileInput);
			Object obj = objinput.readObject();
			qMap = (HashMap<Integer, LinkedBlockingQueue<Message>>) obj;
		} catch (FileNotFoundException e) {
			print("Kein Queue-Backup gefunden");
		} catch (IOException | ClassNotFoundException e) {
			print("Fehler beim lesen des Queue-Backups");
			e.printStackTrace();
		}
		return qMap;
	}
	
	public static synchronized void saveTafelAdressenToFile(){
		FileOutputStream fileoutput = null;
		ObjectOutputStream objoutput = null;
		try {
			fileoutput = new FileOutputStream("./TafelAdressen");
			objoutput = new ObjectOutputStream(fileoutput);
			objoutput.writeObject(tafelAdressen);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				objoutput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@SuppressWarnings("unchecked")
	private static HashMap<Integer, SocketAddress> loadTafelAdressenFromFile() {
		HashMap<Integer, SocketAddress> adressen = new HashMap<Integer, SocketAddress>();
		FileInputStream fileInput = null;
		ObjectInputStream objinput = null;
		try {
			fileInput = new FileInputStream("./tafelAdressen");
			objinput = new ObjectInputStream(fileInput);
			Object obj = objinput.readObject();
			adressen = (HashMap<Integer, SocketAddress>) obj;
		} catch (FileNotFoundException e) {
			print("Kein Adressen-Backup gefunden");
		} catch (IOException | ClassNotFoundException e) {
			print("Fehler beim lesen des Adressen-Backups");
			e.printStackTrace();
		}
		return adressen;
	}


	public static synchronized void print(String nachricht) {
		System.out.println(nachricht);
	}

	public static synchronized void printMessages() {
		System.out.println(anzeigetafel.toString());
	}

}
