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
import java.util.concurrent.LinkedBlockingDeque;
import verteilteAnzeigetafel.Anzeigetafel;
import verteilteAnzeigetafel.Message;
import verteilteAnzeigetafel.TafelException;

/**
 *
 * @author Simon Bastian
 */
public class TafelServer extends Thread {
	private HashMap<Integer, LinkedBlockingDeque<Message>> queueMap = new HashMap<Integer, LinkedBlockingDeque<Message>>();
	private HashMap<Integer, SocketAddress> tafelAdressen = new HashMap<Integer, SocketAddress>();
	private HashMap<Integer, OutboxThread> outboxThreads = new HashMap<Integer, OutboxThread>();
	private HashMap<Integer, HeartbeatThread> heartbeatThreads = new HashMap<Integer, HeartbeatThread>();
	public static final int SERVER_PORT = 10001;
	private Anzeigetafel anzeigetafel;
	private int abteilungsID;

	/**
	 * Processes command line arguments, configures and starts a new
	 * TafelServer.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		TafelServer tafelServer = new TafelServer();
		if (args.length >= 1) {
			try {
				tafelServer.abteilungsID = Integer.parseInt(args[0]);
			} catch (NumberFormatException nfe) {
				System.out.println(args[0] + "ist keine Integerzahl");
			}
		} else {
			tafelServer.abteilungsID = 1;
		}

		tafelServer.start();
		// try {
		// if (tafelServer.abteilungsID != 1) {
		// tafelServer.registerTafel(1, new InetSocketAddress("134.96.216.24",
		// SERVER_PORT));
		// }
		// if (tafelServer.abteilungsID != 2) {
		// tafelServer.registerTafel(2, new InetSocketAddress("10.9.40.214",
		// SERVER_PORT));
		// }
		//
		// // if(tafelServer.abteilungsID!=3){
		// // tafelServer.registerTafel(3, new
		// // InetSocketAddress("134.96.216.15", SERVER_PORT));
		// // }
		// } catch (TafelException e) {
		// tafelServer.print("Idiot");
		// }
	}

	/**
	 * Initializiation and running of the TafelServer.
	 */
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
				new LocalThread(client, this).start();
			}
		} catch (IOException e) {
			printStackTrace(e);
			print("Fahre herunter...");
		}
	}

	/**
	 * Initializes the TafelServer.
	 */
	private void init() {
		anzeigetafel = Anzeigetafel.loadStateFromFile(abteilungsID);
		if (anzeigetafel != null) {
			print("Anzeigetafel aus Datei geladen!");
		} else {
			anzeigetafel = new Anzeigetafel(abteilungsID);
			anzeigetafel.saveStateToFile();
			print("Neue Anzeigetafel erstellt!");
		}
		queueMap = loadQueueMapFromFile();
		loadTafelAdressenFromFile();
	}

	/**
	 * Publishes a message if possible. Set the message to public and try to
	 * deliver it to other TafelServers.
	 * 
	 * @param messageID
	 * @param userID
	 * @throws InterruptedException
	 *             if a Message Queue was interrupted.
	 * @throws TafelException
	 *             if the Anzeigetafel rejects the publication.
	 */
	public synchronized void publishMessage(int messageID, int userID) throws InterruptedException, TafelException {
		anzeigetafel.publishMessage(messageID, userID);
		for (LinkedBlockingDeque<Message> q : queueMap.values()) {
			q.put(anzeigetafel.getMessages().get(messageID));
		}
		anzeigetafel.saveStateToFile();
	}

	/**
	 * Modifies a message if possible.
	 * 
	 * @param messageID
	 * @param inhalt
	 * @param userID
	 * @throws TafelException
	 *             if the Anzeigetafel rejects the modification.
	 */
	public synchronized void modifyMessage(int messageID, String inhalt, int userID) throws TafelException {
		anzeigetafel.modifyMessage(messageID, inhalt, userID);
		print("User mit ID=" + userID + " hatNachricht mit ID=" + messageID + " geändert!");
		anzeigetafel.saveStateToFile();
	}

	/**
	 * Gets the messages of a given userID if possible.
	 * 
	 * @param userID
	 * @return the messages of the given userID
	 * @throws TafelException
	 *             if Anzeigetafel does not recognize the userID.
	 */
	public synchronized LinkedList<Message> getMessagesByUserID(int userID) throws TafelException {
		print("Showing Messages to user " + userID);
		return anzeigetafel.getMessagesByUserID(userID);
	}

	/**
	 * Registers a TafelServer if possible.
	 * 
	 * @param abteilungsID
	 * @param address
	 * @throws TafelException
	 *             if the given abteilungsID is the own abteilungsID
	 */
	public synchronized void registerTafel(int abteilungsID, SocketAddress address) throws TafelException {
		if (this.abteilungsID == abteilungsID) {
			throw new TafelException("Die eigene Abteilung wird nicht registriert");
		}

		if (tafelAdressen.containsKey(abteilungsID)) {
			if (!tafelAdressen.get(abteilungsID).equals(address)) {
				tafelAdressen.replace(abteilungsID, address);
			}

		} else {
			tafelAdressen.put(abteilungsID, address);
			queueMap.put(abteilungsID, new LinkedBlockingDeque<Message>());
		}
		activateHeartbeat(abteilungsID);
		activateQueue(abteilungsID);
		saveTafelAdressenToFile();
	}

	/**
	 * Activated the message queue for the given abteilungID.
	 * 
	 * @param abteilungsID
	 */
	public synchronized void activateQueue(int abteilungsID) {

		if (!outboxThreads.containsKey(abteilungsID) || !outboxThreads.get(abteilungsID).isAlive()) {
			OutboxThread obt = new OutboxThread(abteilungsID, tafelAdressen.get(abteilungsID),
					queueMap.get(abteilungsID), this);
			outboxThreads.put(abteilungsID, obt);
			obt.start();
		} // else Queue already active
	}

	/**
	 * Activates a Heartbeat for the given abteilungsID.
	 * 
	 * @param abteilungsID
	 */
	public synchronized void activateHeartbeat(int abteilungsID) {
		if (!heartbeatThreads.containsKey(abteilungsID) || !heartbeatThreads.get(abteilungsID).isAlive()) {
			HeartbeatThread hbt = new HeartbeatThread(abteilungsID, tafelAdressen.get(abteilungsID), this);
			heartbeatThreads.put(abteilungsID, hbt);
			hbt.start();
		}
	}

	/**
	 * Writes the message queues for each Abteilung to a file.
	 */
	public synchronized void saveQueueMapToFile() {
		FileOutputStream fileoutput = null;
		ObjectOutputStream objoutput = null;
		try {
			fileoutput = new FileOutputStream("./QueueMap" + abteilungsID);
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

	/**
	 * Loads the message queues for each Abteilung from a file.
	 * 
	 * @return queueMap
	 */
	@SuppressWarnings("unchecked")
	private HashMap<Integer, LinkedBlockingDeque<Message>> loadQueueMapFromFile() {
		HashMap<Integer, LinkedBlockingDeque<Message>> qMap = new HashMap<Integer, LinkedBlockingDeque<Message>>();
		FileInputStream fileInput = null;
		ObjectInputStream objinput = null;
		try {
			fileInput = new FileInputStream("./queueMap");
			objinput = new ObjectInputStream(fileInput);
			Object obj = objinput.readObject();
			qMap = (HashMap<Integer, LinkedBlockingDeque<Message>>) obj;
		} catch (FileNotFoundException e) {
			print("Kein Queue-Backup gefunden");
		} catch (IOException | ClassNotFoundException e) {
			print("Fehler beim lesen des Queue-Backups");
			e.printStackTrace();
		}
		return qMap;
	}

	/**
	 * Writes the addresses of the registered TafelServers to a file.
	 */
	public synchronized void saveTafelAdressenToFile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("./TafelAdressen" + abteilungsID))) {
			for (int i : tafelAdressen.keySet()) {
				InetSocketAddress address = (InetSocketAddress) tafelAdressen.get(i);
				writer.write(i + ":" + address.getHostName() + ":" + address.getPort());
			}
		} catch (IOException e) {
			printStackTrace(e);
		}

		// FileOutputStream fileoutput = null;
		// ObjectOutputStream objoutput = null;
		// try {
		// fileoutput = new FileOutputStream("./TafelAdressen");
		// objoutput = new ObjectOutputStream(fileoutput);
		// objoutput.writeObject(tafelAdressen);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// objoutput.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
	}

	// /**
	// * Loads the addresses of registered TafelServers from a file.
	// *
	// * @return tafelAddressen
	// */
	// @SuppressWarnings("unchecked")
	// private HashMap<Integer, SocketAddress> loadTafelAdressenFromFile() {
	// HashMap<Integer, SocketAddress> adressen = new HashMap<Integer,
	// SocketAddress>();
	// FileInputStream fileInput = null;
	// ObjectInputStream objinput = null;
	// try {
	// fileInput = new FileInputStream("./tafelAdressen");
	// objinput = new ObjectInputStream(fileInput);
	// Object obj = objinput.readObject();
	// adressen = (HashMap<Integer, SocketAddress>) obj;
	// } catch (FileNotFoundException e) {
	// print("Kein Adressen-Backup gefunden");
	// } catch (IOException | ClassNotFoundException e) {
	// print("Fehler beim lesen des Adressen-Backups");
	// e.printStackTrace();
	// }
	// return adressen;
	// }

	private void loadTafelAdressenFromFile() {
		int lines = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader("./tafelAdressen" + abteilungsID))) {
			String address = "";
			while ((address = reader.readLine()) != null) {
				lines++;
				String[] addressParts = address.split(":");
				try {
					registerTafel(Integer.parseInt(addressParts[0]),
							new InetSocketAddress(addressParts[1], Integer.parseInt(addressParts[2])));
				} catch (NumberFormatException e) {
					print("NumberFormatException in line " + lines + " " + e.getMessage());
					e.printStackTrace();
				} catch (TafelException e) {
					printStackTrace(e);
				}
			}

		} catch (FileNotFoundException e) {
			printStackTrace(e);
		} catch (IOException e) {
			printStackTrace(e);
		}
	}

	/**
	 * Outputs a message on the TafelSever.
	 * 
	 * @param nachricht
	 *            the message
	 */
	public synchronized void print(String nachricht) {
		System.out.println(nachricht);
	}

	public synchronized void printStackTrace(Exception e) {
		e.printStackTrace();
	}

	/**
	 * Outputs all messages of the Anzeigetafel.
	 */
	public synchronized void printMessages() {
		System.out.println(anzeigetafel.toString());
	}

	/**
	 * Returns the anzeigetafel data of the TafelServer
	 * 
	 * @return anzeigetafel
	 */
	public synchronized Anzeigetafel getAnzeigetafel() {
		return anzeigetafel;
	}

	/**
	 * Returns the abteilungsID of the TafelServer
	 * 
	 * @return abteilungsID
	 */
	public synchronized int getAbteilungsID() {
		return abteilungsID;
	}

	/**
	 * Returns the addresses of the registered TafelServers.
	 * 
	 * @return tafelAdressen
	 */
	public HashMap<Integer, SocketAddress> getTafelAdressen() {
		return tafelAdressen;
	}

	/**
	 * Returns the assigned outboxThread by abteilungsID.
	 * 
	 * @return outboxThreads
	 */
	public HashMap<Integer, OutboxThread> getOutboxThreads() {
		return outboxThreads;
	}

	/**
	 * Retunrs the message queues by abteilungsID.
	 * 
	 * @return queueMap
	 */
	public HashMap<Integer, LinkedBlockingDeque<Message>> getQueueMap() {
		return queueMap;
	}

}
