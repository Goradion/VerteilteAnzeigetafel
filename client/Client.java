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
import serverRequests.*;
import java.io.*;

public class Client implements Serializable {

	private static final long serialVersionUID = -6299053685373379874L;

	private final int SERVER_PORT = 10001;
	private String serverHostname = "localhost";
	private final String ABTEILUNG_1 = "192.168.178.10";
	private final String ABTEILUNG_2 = "192.168.178.11";
	private final String ABTEILUNG_3 = "192.168.178.12";
	private final String ABTEILUNG_4 = "192.168.178.13";
	private ClientWindow mainWindow;
	private LoggingWindow logWin;
	private int userID;
	private int abtNr;

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
		this.logWin = new LoggingWindow(mainWindow);
		mainWindow.setResizable(false);
	}

	public void startClient() {
		mainWindow.run();
		logWin.run();
	}

	private void setAbteilung(int abt) {
		switch (abt) {
		case 0:
			serverHostname = "localhost";
			break;
		case 1:
			serverHostname = ABTEILUNG_1;
			break;
		case 2:
			serverHostname = ABTEILUNG_2;
			break;
		case 3:
			serverHostname = ABTEILUNG_3;
			break;
		case 4:
			serverHostname = ABTEILUNG_4;
			break;
		default:
			serverHostname = "Unknown Host";
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

	public void sendMessage(int abt, String message, int userID) {
		setAbteilung(abt);
		Socket socket = new Socket();
		ObjectOutputStream oout = null;
		try {

			socket.connect(new InetSocketAddress(serverHostname, SERVER_PORT), 1000);

			ServerRequest sr = ServerRequest.buildCreateRequest(message, userID, abt);
			oout = new ObjectOutputStream(socket.getOutputStream());
			oout.writeObject(sr);
			log(socket);

		} catch (UnknownHostException e) {
			System.out.println("Rechnername unbekannt:\n" + e.getMessage());
			log("Sending failed:\n" + "Abteilung " + abt + " unknown or offline.\n");
		} catch (IOException e) {
			System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
			log("Sending failed:" + " I/O error while sending a message.\n" + "Abteilung "+abt + " might be offline.");
		} finally {
			try {
				oout.close();
				socket.close();
			} catch (IOException e) {
				log("I/O error while sending a message.\n");
			}
		}
	}

	public void removeMessage(int abt, int userID, int msgID) {
		setAbteilung(abt);
		Socket socket = new Socket();
		ObjectOutputStream oout = null;
		try {

			socket.connect(new InetSocketAddress(serverHostname, SERVER_PORT), 1000);
			ServerRequest serverR = ServerRequest.buildDeleteRequest(msgID, userID);
			oout = new ObjectOutputStream(socket.getOutputStream());
			oout.writeObject(serverR);
			log(socket);

		} catch (UnknownHostException e) {
			System.out.println("Rechnername unbekannt:\n" + e.getMessage());
			log("Deleting failed:\n" + "Abteilung " + abt + " unknown or offline.\n");
		} catch (IOException e) {
			System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
			log("Deleting failed:" + " I/O error while deleting a message.\n" +  "Abteilung "+abt + " might be offline.");
		} finally {
			try {
				oout.close();
				socket.close();
			} catch (IOException e) {
				log("I/O error while deleting a message.\n");
			}

		}
	}

	public void changeMessage(int abt, int userID, int msgID, String neueNachricht) {
		setAbteilung(abt);
		Socket socket = new Socket();
		ObjectOutputStream oout = null;
		try {

			socket.connect(new InetSocketAddress(serverHostname, SERVER_PORT), 1000);

			ServerRequest serverR = ServerRequest.buildModifyRequest(msgID, neueNachricht, userID);
			oout = new ObjectOutputStream(socket.getOutputStream());

			oout.writeObject(serverR);
			log(socket);
			
		} catch (UnknownHostException e) {
			System.out.println("Rechnername unbekannt:\n" + e.getMessage());
			log("Changing failed:\n" + "Abteilung " + abt + " unknown or offline.\n");
		} catch (IOException e) {
			System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
			log("Changing failed:" + " I/O error while changing a message.\n" +  "Abteilung "+abt + " might be offline.");
		} finally {
			try {
				oout.close();
				socket.close();
			} catch (IOException e) {
				log("I/O error while changing a message.\n");
			}

		}
	}

	public String showMessages(int abt, int userID) {
		setAbteilung(abt);
		Socket socket = new Socket();
		ObjectOutputStream oout = null;
		String str = null;
		try {

			socket.connect(new InetSocketAddress(serverHostname, SERVER_PORT), 1000);

			ServerRequest serverR = ServerRequest.buildShowMyMessagesRequest(userID);
			oout = new ObjectOutputStream(socket.getOutputStream());
			oout.writeObject(serverR);

			log(socket);

		} catch (UnknownHostException e) {
			System.out.println("Rechnername unbekannt:\n" + e.getMessage());
			log("Showing messages failed:\n" + "Abteilung " + abt + " unknown or offline.\n");
		} catch (IOException e) {
			System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
			log("Showing messages failed:" + " I/O error while showing messages.\n" +  "Abteilung "+abt 
					+ " might be offline.");
		} finally {
			try {
				oout.close();
				socket.close();
			} catch (IOException e) {
				log("I/O error while showing messages.\n");
			}

		}
		if (str == null) {
			str = "";
		}
		return str;
	}

	public void publishMessage(int abt, int messageId, int userId) {
		setAbteilung(abt);
		Socket socket = new Socket();
		ObjectOutputStream oout = null;
		try {

			socket.connect(new InetSocketAddress(serverHostname, SERVER_PORT), 1000);

			ServerRequest serverRequest = ServerRequest.buildPublishRequest(messageId, userId);
			oout = new ObjectOutputStream(socket.getOutputStream());
			oout.writeObject(serverRequest);
			log(socket);

		} catch (UnknownHostException e) {
			System.out.println("Rechnername unbekannt:\n" + e.getMessage());
			log("Publishing failed.\n" + "Server " + serverHostname + " unknown or offline.\n");
		} catch (IOException e) {
			System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
			log("Publishing failed:" + " I/O error while showing messages.\n" + serverHostname
					+ " might be offline.");
		} finally {
			try {
				oout.close();
				socket.close();
			} catch (IOException e) {
				log("I/O error while publishing.\n");
			}

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

	private void log(String meldung) {
		logWin.addEntry(meldung);
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.startClient();

	}
}
