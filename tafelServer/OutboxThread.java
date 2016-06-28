package tafelServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.LinkedBlockingQueue;

import serverRequests.ServerRequest;
import verteilteAnzeigetafel.Message;

public class OutboxThread extends Thread {
	int abteilungsID;
	SocketAddress adress;
	LinkedBlockingQueue<Message> messageQueue;
	TafelServer tafelServer;
	public OutboxThread(int abteilungsID, SocketAddress adress, LinkedBlockingQueue<Message> messageQueue, TafelServer tafelServer) {
		super();
		this.abteilungsID = abteilungsID;
		this.adress = adress;
		this.messageQueue = messageQueue;
		this.tafelServer =tafelServer;
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
			tafelServer.print("OutboxThread " + abteilungsID + " wurde unterbrochen!");
		} catch (IOException e) {
			tafelServer.print("OutboxThread " + abteilungsID+": "+adress.toString()+" nicht erreichbar! "+e.getMessage());
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
