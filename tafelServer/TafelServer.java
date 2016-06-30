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
public class TafelServer extends Thread{
	private  HashMap<Integer, LinkedBlockingQueue<Message>> queueMap = new HashMap<Integer, LinkedBlockingQueue<Message>>();
	private  HashMap<Integer, SocketAddress> tafelAdressen = new HashMap<Integer, SocketAddress>();
	private  HashMap<Integer, OutboxThread> outboxThreads = new HashMap<Integer, OutboxThread>();
	private  HashMap<Integer, HeartbeatThread> heartbeatThreads = new HashMap<Integer, HeartbeatThread>();
	public static final int SERVER_PORT = 10001;
	private  Anzeigetafel anzeigetafel;
	private  int abteilungsID;

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		TafelServer tafelServer = new TafelServer();
		if (args.length >=1){
			try{
			tafelServer.abteilungsID = Integer.parseInt(args[0]);
			} catch (NumberFormatException nfe){
				System.out.println(args[0]+ "ist keine Integerzahl");
			}
		} else {
			tafelServer.abteilungsID = 1;
		}
		try {
		if(tafelServer.abteilungsID!=1){
			tafelServer.registerTafel(1, new InetSocketAddress("192.168.178.2", SERVER_PORT));
		}
		if(tafelServer.abteilungsID!=2){
			tafelServer.registerTafel(2, new InetSocketAddress("192.168.178.100", SERVER_PORT));
		}
		} catch (TafelException e) {
			tafelServer.print("Idiot");
		}
		
		tafelServer.start();
	}
	@Override
	public void run() {
		init();
		printMessages();
		ServerSocket socket;
		try {
			socket = new ServerSocket(SERVER_PORT);
			while (true) {
				// print("accept...");
				Socket client = socket.accept();
				// print("Starte LocalThread...");
				new LocalThread(client,this).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private  void init() {
		anzeigetafel = Anzeigetafel.loadStateFromFile(abteilungsID);
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


	public synchronized void publishMessage(int messageID, int userID) throws InterruptedException, TafelException {
		anzeigetafel.publishMessage(messageID, userID);
		for (LinkedBlockingQueue<Message> q : queueMap.values()) {
			q.put(anzeigetafel.getMessages().get(messageID));
		}
		anzeigetafel.saveStateToFile();
	}

	public  synchronized void modifyMessage(int messageID, String inhalt, int userID) throws TafelException {
		anzeigetafel.modifyMessage(messageID, inhalt, userID);
		print("User mit ID=" + userID + " hatNachricht mit ID=" + messageID + " geändert!");
		anzeigetafel.saveStateToFile();
	}
	
	public  synchronized LinkedList<Message> getMessagesByUserID(int userID) throws TafelException {
		print("Showing Messages to user " + userID);
		return anzeigetafel.getMessagesByUserID(userID);
	}
	public  synchronized void registerTafel(int abteilungsID, SocketAddress address) throws TafelException{
		if(abteilungsID==abteilungsID){
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
	public synchronized void activateQueue(int abteilungsID) {
		if (!outboxThreads.containsKey(abteilungsID) || !outboxThreads.get(abteilungsID).isAlive()) {
			OutboxThread obt = new OutboxThread(abteilungsID, tafelAdressen.get(abteilungsID),
					queueMap.get(abteilungsID), this);
			outboxThreads.put(abteilungsID, obt);
			obt.start();
		} // else Queue already active
	}
	
	public  synchronized void activateHeartbeat(int abteilungsID){
		if (!heartbeatThreads.containsKey(abteilungsID) || !heartbeatThreads.get(abteilungsID).isAlive()) {
			HeartbeatThread hbt = new HeartbeatThread(abteilungsID, tafelAdressen.get(abteilungsID), this);
			heartbeatThreads.put(abteilungsID, hbt);
			hbt.start();
		}
	}
	
	public synchronized void saveQueueMapToFile() {
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
	private  HashMap<Integer, LinkedBlockingQueue<Message>> loadQueueMapFromFile() {
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
	
	public synchronized void saveTafelAdressenToFile(){
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
	private  HashMap<Integer, SocketAddress> loadTafelAdressenFromFile() {
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


	public synchronized void print(String nachricht) {
		System.out.println(nachricht);
	}

	public  synchronized void printMessages() {
		System.out.println(anzeigetafel.toString());
	}
	
	public Anzeigetafel getAnzeigetafel(){
		return anzeigetafel;
	}
	public HashMap<Integer, SocketAddress> getTafelAdressen() {
		return tafelAdressen;
	}
	public HashMap<Integer, OutboxThread> getOutboxThreads() {
		return outboxThreads;
	}
	public HashMap<Integer, LinkedBlockingQueue<Message>> getQueueMap() {
		return queueMap;
	}
	

}
