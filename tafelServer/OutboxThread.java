package tafelServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.LinkedBlockingDeque;

import serverRequests.ServerRequest;

public class OutboxThread extends Thread {
	int abteilungsID;
	SocketAddress adress;
	LinkedBlockingDeque<ServerRequest> messageQueue;
	TafelServer tafelServer;

	/**
	 * Constructs a new OutboxThread
	 * 
	 * @param abteilungsID
	 *            Who I deliver to.
	 * @param adress
	 *            Where to deliver.
	 * @param messageQueue
	 *            Here are the messages to deliver.
	 * @param tafelServer
	 *            The tafelServer where status messages are sent.
	 */
	public OutboxThread(int abteilungsID, SocketAddress adress, LinkedBlockingDeque<ServerRequest> messageQueue,
			TafelServer tafelServer) {
		super();
		this.abteilungsID = abteilungsID;
		this.adress = adress;
		this.messageQueue = messageQueue;
		this.tafelServer = tafelServer;
	}

	/**
	 * Attempts to deliver the messages from the messageQueue.
	 */
	public void run() {
		tafelServer.print(getMyName() + " läuft!\n" + messageQueue.toString());
		Socket socket = null;
		ServerRequest request = null;
		try {
			while (true) {
				if (socket == null || socket.isClosed()) {
					socket = new Socket();
					socket.connect(adress);
				}

				request = messageQueue.take();
				tafelServer.saveQueueMapToFile();
				ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
				oout.writeObject(request);
				// oout.flush();
				request = null;
			}
		} catch (InterruptedException e) {
			tafelServer.print(getMyName() + " wurde unterbrochen!");
			ObjectOutputStream oout;
			try {
				oout = new ObjectOutputStream(socket.getOutputStream());
				oout.writeObject(null);
			} catch (IOException e1) {
				tafelServer.print(getMyName() + " konnte sich nicht verabschieden!");
			}

		} catch (IOException e) {
			tafelServer.print(getMyName() + ": " + adress.toString() + " nicht erreichbar! " + e.getMessage());
		} finally {
			if (request != null) {
				try {
					messageQueue.putFirst(request);
					tafelServer.saveQueueMapToFile();
				} catch (InterruptedException e) {
					tafelServer.print(
							"Request " + request + " ging auf dem Weg zu Abteilung " + abteilungsID + " verloren");
				}
			}
			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
					tafelServer.print(getMyName() + ": Socket geschlossen!");
				}
			} catch (IOException e) {
				tafelServer.print(getMyName() + ": Fehler beim Schließen des Sockets" + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	/**
	 * Return a name to distinguish which OutboxThread is talking.
	 * 
	 * @return a name
	 */
	private String getMyName() {
		return "OutboxThread " + abteilungsID;
	}
}
