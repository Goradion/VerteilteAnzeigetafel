package VerteilteAnzeigetafel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class OutboxThread extends Thread {
	int abteilungsID;
	Socket socket;
	LinkedBlockingQueue<Message> messageQueue;

	public OutboxThread(int abteilungsID, Socket socket, LinkedBlockingQueue<Message> messageQueue) {
		super();
		this.abteilungsID = abteilungsID;
		this.socket = socket;
		this.messageQueue = messageQueue;
	}

	public void run() {

		try {
			while (true) {
				ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
				Message msg = messageQueue.take();
				ServerRequest request = ServerRequest.buildCreateRequest(msg.getInhalt(), msg.getUserID(), 
						msg.getUserID(), msg.isOeffentlich());
				oout.writeObject(request);
			}
		} catch (InterruptedException e) {
			TafelServer.print("OutboxThread für Abteilung "+abteilungsID+" wurfe unterbrochen!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
