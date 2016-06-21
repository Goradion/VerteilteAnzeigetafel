package VerteilteAnzeigetafel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class OutboxThread {
	int abteilungsID;
	Socket socket;
	LinkedBlockingQueue<Message> messageQueue;

	public OutboxThread(int abteilungsID, Socket socket, LinkedBlockingQueue<Message> messageQueue) {
		super();
		this.abteilungsID = abteilungsID;
		this.socket = socket;
		this.messageQueue = messageQueue;
	}

	void run() {

		try {
			while (true) {
				ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
				Message message = messageQueue.take();
				ServerRequest request;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
