package tafelServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class HeartbeatThread extends Thread {
	private static final int sleepTime = 5000;
	int abteilungsID;
	SocketAddress adress;
	public HeartbeatThread(int abteilungsID, SocketAddress adress) {
		super();
		this.abteilungsID = abteilungsID;
		this.adress = adress;
	}

	public void run() {
		Socket socket = new Socket();
		ServerRequest heartbeat = ServerRequest.buildHeartbeatRequest();
		try {
			while (true) {
				try{
					if(!socket.isConnected()){
						socket.connect(adress);
					}
					ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
					oout.writeObject(heartbeat);
					
				} catch (IOException e) {
					TafelServer.print("HeartbeatThread " + abteilungsID+": "+adress.toString()+" nicht erreichbar! "+e.getMessage());
				}
				Thread.sleep(sleepTime);
			}
		}catch(	InterruptedException e)	{
			TafelServer.print("HeartbeatThread " + abteilungsID + " wurde unterbrochen!");
		} finally {
			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				System.err.println(
						"Socket in HeartbeatThread der Abteilung " + abteilungsID + " konnte nicht geschlossen werden!");
				e.printStackTrace();
			}
		}
	}
}
