package VerteilteAnzeigetafel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.LinkedBlockingQueue;

public class OutboxThread extends Thread {
	int abteilungsID;
	SocketAddress adress;
	LinkedBlockingQueue<Message> messageQueue;

	public OutboxThread(int abteilungsID, SocketAddress adress, LinkedBlockingQueue<Message> messageQueue) {
		super();
		this.abteilungsID = abteilungsID;
		this.adress = adress;
		this.messageQueue = messageQueue;
	}

	public void run() {
		Socket socket = null; 
		try {
			while (true) {
				socket = new Socket();
				socket.connect(adress);
				ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
				Message msg = messageQueue.take();
				//TODO rework ServerRequest
				ServerRequest request = ServerRequest.buildCreateRequest(msg.getInhalt(), msg.getUserID(),
						msg.getUserID());
				oout.writeObject(request);
				
			}
		} catch (InterruptedException e) {
			TafelServer.print("OutboxThread für Abteilung " + abteilungsID + " wurde unterbrochen!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				System.err.println(
						"Socket in OutboxThread der Abteilung " + abteilungsID + " konnte nicht geschlossen werden!");
				e.printStackTrace();
			}
		}

	}
}
