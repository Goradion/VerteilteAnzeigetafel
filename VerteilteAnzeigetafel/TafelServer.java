/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VerteilteAnzeigetafel;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Simon Bastian
 */
public class TafelServer {
	private static HashMap<Integer, LinkedBlockingQueue<Message>> queueMap = new HashMap<Integer, LinkedBlockingQueue<Message>>();
	private static HashMap<Integer, SocketAddress> tafelAdressen = new HashMap<Integer, SocketAddress>();
	private static HashMap<Integer, OutboxThread> outboxThreads = new HashMap<Integer, OutboxThread>();
	public static final int SERVER_PORT = 10001;
	private static Anzeigetafel anzeigetafel;

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
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
		try {
			anzeigetafel = Anzeigetafel.loadStateFromFile();
			print("Zustand aus Datei geladen!");
		} catch (TafelException e) {
			print("Neue Anzeigetafel erstellt!");
			anzeigetafel = new Anzeigetafel();
		}
//		LinkedBlockingQueue<Message> q = new LinkedBlockingQueue<Message>();
//		q.add(new Message("AAAAAAAAAAAAAAAAAAAA", 1, 2, true, 4711));
//		q.add(new Message("BBBBBBBBBBBBBBBBBBBB", 1, 2, true, 4711));
//		queueMap.put(1, q);
//		tafelAdressen.put(1, new InetSocketAddress("134.96.216.15", 10001));
//		activateQueue(1);
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

	public static synchronized void activateQueue(int abteilungsID) {
		if (!outboxThreads.containsKey(abteilungsID) || !outboxThreads.get(abteilungsID).isAlive()) {
			OutboxThread obt = new OutboxThread(abteilungsID, tafelAdressen.get(abteilungsID), queueMap.get(abteilungsID)); 
			outboxThreads.put(abteilungsID, obt);
			obt.start();
		}//else Queue already active
	}

	public static synchronized void print(String nachricht) {
		System.out.println(nachricht);
	}

	public static synchronized void printMessages() {
		System.out.println(anzeigetafel.toString());
	}

	public static synchronized LinkedList<Message> getMessagesByUserID(int userID) throws TafelException {
		print("Showing Messages to user " + userID);
		return anzeigetafel.getMessagesByUserID(userID);
	}
}
