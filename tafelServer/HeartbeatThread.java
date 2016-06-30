package tafelServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import serverRequests.ServerRequest;

public class HeartbeatThread extends Thread {
	private static final int sleepTime = 5000;
	private int abteilungsID;
	private SocketAddress adress;
	private TafelServer tafelServer;
	public HeartbeatThread(int abteilungsID, SocketAddress adress, TafelServer tafelServer) {
		super();
		this.abteilungsID = abteilungsID;
		this.adress = adress;
		this.tafelServer = tafelServer;
	}

	public void run() {
		Socket socket = null;
		ServerRequest heartbeat;
		try {
			while (true) {
				try{
					if (socket == null || socket.isClosed()) {
						socket = new Socket();
						socket.connect(adress);
					}
					InetAddress myAddress = socket.getLocalAddress(); 
					heartbeat = ServerRequest.buildRegisterRequest(tafelServer.getAbteilungsID(), new InetSocketAddress(myAddress, TafelServer.SERVER_PORT));
					ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
					oout.writeObject(heartbeat);
				} catch (IOException e) {
					tafelServer.print("HeartbeatThread " + abteilungsID+": "+adress.toString()+" nicht erreichbar! "+e.getMessage());
					try {
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				Thread.sleep(sleepTime);
			}
		}catch(	InterruptedException e)	{
			tafelServer.print("HeartbeatThread " + abteilungsID + " wurde unterbrochen!");
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
