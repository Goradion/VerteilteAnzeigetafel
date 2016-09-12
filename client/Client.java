/**
 *Projekt Anzeigetafel
 *
 * @author: Michael Moser
 * @author: Andrea Caruana
 * @author: Diego Rodriguez Castellanos
 * @author: Viktor Semenitsch
 * @author: Simon Bastian
 * @author: Alexander Mueller 
 * Datei: Client Client zur Kommunikation zwischen
 * Benutzer und Anzeigetafel
 */
package client;

import java.net.*;
import java.util.Scanner;

import serverRequests.*;
import tafelServer.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Serializable {

	private final long serialVersionUID = -1466790708777017802L;
	private final int ENDE = 0;
	private final int SERVER_PORT = 10001;
	private String SERVER_HOSTNAME = "localhost";
	private final String ABTEILUNG_1 = "192.168.178.10";
	private final String ABTEILUNG_2 = "192.168.178.11";
	private final String ABTEILUNG_3 = "192.168.178.12";
	private final String ABTEILUNG_4 = "192.168.178.13";
	private ClientWindow mainWindow;
	private LoggingWindow logWin;
	private int userID;
	private int abtNr;// Abteilungsnummer

	/**
	 * Konstruktor zum Erstellung des Benutzers
	 *
	 * @param benutzerName
	 * @param abtNr
	 * @param administrator
	 * @param userID
	 */
	public Client() {
		this.userID = 0;
		this.abtNr = 0;
		this.mainWindow = new ClientWindow("Client", this);
		this.logWin = new LoggingWindow(this);
		mainWindow.setResizable(false);
	}

	public void startClient() {
		mainWindow.run();
		logWin.run();
	}

	private void setAbteilung(int abt) {
		switch (abt) {
		case 1:
			SERVER_HOSTNAME = ABTEILUNG_1;
			break;
		case 2:
			SERVER_HOSTNAME = ABTEILUNG_2;
			break;
		case 3:
			SERVER_HOSTNAME = ABTEILUNG_3;
			break;
		case 4:
			SERVER_HOSTNAME = ABTEILUNG_4;
			break;
		default:
			break;
		}
	}

	public int getabtNr() {
		return abtNr;
	}

	public void setAbtNr(int abtNr) {
		this.abtNr = abtNr;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	/*
	 * Methode zum senden der Nachricht Die Methode ist nur fuer das senden der
	 * Nachricht und das abfangen der damit verbundenen Fehlerfuelle zustaendig
	 */

	public void sendeMessageWithGui(int abt, String message, int userID) {
		setAbteilung(abt);
		try {
			
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 1000);
			if (!socket.isConnected()) {
				logWin.addEntry("Keine Verbindung zum Server " + SERVER_HOSTNAME + " moeglich! \n");
			} else {
				ServerRequest sr = ServerRequest.buildCreateRequest(message, userID, abt);
				ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
				oout.writeObject(sr);
				log(socket);
				oout.close();
			}
			
			socket.close();
		} catch (UnknownHostException e) {
			// Wenn Rechnername nicht bekannt ist.
			System.out.println("Rechnername unbekannt:\n" + e.getMessage());
		} catch (IOException e) {
			// Wenn die Kommunikation fehlschlaegt
			System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
		}
	}

	public void removeMessageWithGui(int abt, int userID, int msgID) {
		setAbteilung(abt);
		try {

			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 1000);
			if (!socket.isConnected()) {
				logWin.addEntry("Keine Verbindung zum Server " + SERVER_HOSTNAME + " moeglich! \n");
			} else {
				ServerRequest serverR = ServerRequest.buildDeleteRequest(msgID, userID);
				ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
				oout.writeObject(serverR);
				log(socket);
				oout.close();
			}
			socket.close();

		} catch (UnknownHostException e) {
			// Wenn Rechnername nicht bekannt ist.
			System.out.println("Rechnername unbekannt:\n" + e.getMessage());
		} catch (IOException e) {
			// Wenn die Kommunikation fehlschlaegt
			System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
		}
	}

	public String changeMessageWithGui(int abt, int userID, int msgID, String neueNachricht) {
		setAbteilung(abt);
		String rueckmeldung = "Aenderung erfolgreich abgeschickt";

		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 1000);
			if (!socket.isConnected()) {
				logWin.addEntry("Keine Verbindung zum Server " + SERVER_HOSTNAME + " moeglich! \n");
			} else {
				ServerRequest serverR = ServerRequest.buildModifyRequest(msgID, neueNachricht, userID);
				ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());

				oout.writeObject(serverR);
				log(socket);
				oout.close();
			}
			socket.close();
		} catch (UnknownHostException e) {
			// Wenn Rechnername nicht bekannt ist.
			System.out.println("Rechnername unbekannt:\n" + e.getMessage());
			rueckmeldung = "Rechnername unbekannt:\n" + e.getMessage();
		} catch (IOException e) {
			// Wenn die Kommunikation fehlschlaegt
			System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
			rueckmeldung = "Fehler waehrend der Kommunikation:\n" + e.getMessage();
		}
		return rueckmeldung;
	}

	public String showMessagesWithGui(int abt, int userID) {
		setAbteilung(abt);
		String str = null;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 1000);
			if (!socket.isConnected()) {
				logWin.addEntry("Keine Verbindung zum Server " + SERVER_HOSTNAME + " moeglich! \n");
			} else {
				ServerRequest serverR = ServerRequest.buildShowMyMessagesRequest(userID);
				ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
				oout.writeObject(serverR);
				ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
				str = input.readObject().toString();
				System.out.println(str);
	
				oout.close();
			}
			socket.close();

		} catch (UnknownHostException e) {
			// Wenn Rechnername nicht bekannt ist.
			str = "Rechnername unbekannt:\n";
			System.out.println("Rechnername unbekannt:\n" + e.getMessage());
		} catch (IOException e) {
			// Wenn die Kommunikation fehlschlaegt
			str = "Fehler waehrend der Kommunikation:\n";
			System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (str == null) {
			str = "";
		}
		return str;
	}

	public void publishMessageWithGui(int messageId, int userId) {

		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 1000);
			if (!socket.isConnected()) {
				logWin.addEntry("Keine Verbindung zum Server " + SERVER_HOSTNAME + " moeglich! \n");
			} else {
			ServerRequest serverRequest = ServerRequest.buildPublishRequest(messageId, userId);
			ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
			oout.writeObject(serverRequest);
			log(socket);
			}
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void log(Socket socket) throws IOException {
		ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
		try {
			logWin.addEntry(input.readObject().toString());
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		input.close();
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.startClient();

	}
}
