package tafelServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.LinkedBlockingQueue;

import verteilteAnzeigetafel.Message;

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
		Message msg = null;
		try {
			while (true) {
				msg = null;
				socket = new Socket();
				msg = messageQueue.take();
				socket.connect(adress);
				ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
				ServerRequest request = ServerRequest.buildReceiveRequest(msg);
				oout.writeObject(request);				
			}
		} catch (InterruptedException e) {
			TafelServer.print("OutboxThread " + abteilungsID + " wurde unterbrochen!");
		} catch (IOException e) {
			TafelServer.print("OutboxThread " + abteilungsID+": "+adress.toString()+" nicht erreichbar! "+e.getMessage());
		} finally {
			if(msg != null){
				messageQueue.add(msg);
			}
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
