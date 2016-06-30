package tafelServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.LinkedBlockingDeque;

import serverRequests.ServerRequest;
import verteilteAnzeigetafel.Message;

public class OutboxThread extends Thread {
	int abteilungsID;
	SocketAddress adress;
	LinkedBlockingDeque<Message> messageQueue;
	TafelServer tafelServer;

	public OutboxThread(int abteilungsID, SocketAddress adress, LinkedBlockingDeque<Message> messageQueue,
			TafelServer tafelServer) {
		super();
		this.abteilungsID = abteilungsID;
		this.adress = adress;
		this.messageQueue = messageQueue;
		this.tafelServer = tafelServer;
	}

	public void run() {
		tafelServer.print("OutboxThread " + abteilungsID + " läuft!");
		Socket socket = new Socket();
		Message msg = null;
		try {
			socket.connect(adress);
			ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
			while (true) {
				if (socket == null || socket.isClosed()) {
					socket = new Socket();
					socket.connect(adress);
				}

				msg = messageQueue.take();
				ServerRequest request = ServerRequest.buildReceiveRequest(msg);
				oout.writeObject(request);
				msg = null;
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
			tafelServer.print(getMyName() + " : " + adress.toString() + " nicht erreichbar! " + e.getMessage());
		} finally {
			if (msg != null) {
				try {
					messageQueue.putFirst(msg);
				} catch (InterruptedException e) {
					tafelServer.print("Nachricht ID=" + msg.getMessageID() + " ging auf dem Weg zu Abteilung "
							+ abteilungsID + " verloren");
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

	private String getMyName() {
		return "OutboxThread " + abteilungsID;
	}
}
