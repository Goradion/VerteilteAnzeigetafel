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
		Socket socket = new Socket();
		
		Message msg = null;
		try {
			socket.connect(adress);
			ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
			while (true) {
				msg = null;
				msg = messageQueue.take();
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
					ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
					oout.writeObject(null);		
					socket.close();
				}
			} catch (IOException e) {
				System.err.println(
						"Fehler beim Schließen der Verbindung zu Abteilung "+ abteilungsID+"! "+e.getMessage());
				e.printStackTrace();
			}
		}

	}
}
