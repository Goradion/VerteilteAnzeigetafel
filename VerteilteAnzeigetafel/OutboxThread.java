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
				Message msg = messageQueue.take();
				socket.connect(adress);
				ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
				
				//TODO rework ServerRequest
				ServerRequest request = ServerRequest.buildReceiveRequest(msg);
				oout.writeObject(request);
				
			}
		} catch (InterruptedException e) {
			TafelServer.print("OutboxThread " + abteilungsID + " wurde unterbrochen!");
		} catch (IOException e) {
			TafelServer.print("OutboxThread " + abteilungsID+": "+adress.toString()+" nicht erreichbar! "+e.getMessage());
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
