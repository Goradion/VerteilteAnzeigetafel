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

	/**
	 * Construcs a new HeartbeatThread
	 * 
	 * @param abteilungsID
	 * @param adress
	 * @param tafelServer
	 */
	public HeartbeatThread(int abteilungsID, SocketAddress adress, TafelServer tafelServer) {
		super();
		this.abteilungsID = abteilungsID;
		this.adress = adress;
		this.tafelServer = tafelServer;
	}

	/**
	 * The HeartbeatThread tries to contact its assigned TafelServer via the
	 * given adress in intervals given in \var sleepTime;
	 */
	public void run() {
		Socket socket = null;
		ServerRequest heartbeat;
		try {
			while (true) {
				try {
					if (socket == null || socket.isClosed()) {
						socket = new Socket();
						socket.connect(adress);
					}
					InetAddress myAddress = socket.getLocalAddress();
					heartbeat = ServerRequest.buildRegisterRequest(tafelServer.getAbteilungsID(),
							new InetSocketAddress(myAddress, TafelServer.SERVER_PORT));
					ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
					oout.writeObject(heartbeat);
				} catch (IOException e) {
					tafelServer.print("HeartbeatThread " + abteilungsID + ": " + adress.toString()
							+ " nicht erreichbar! " + e.getMessage());
					try {
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				Thread.sleep(sleepTime);
			}
		} catch (InterruptedException e) {
			tafelServer.print("HeartbeatThread " + abteilungsID + " wurde unterbrochen!");
		} finally {
			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				tafelServer.print("Socket in HeartbeatThread " + abteilungsID + " konnte nicht geschlossen werden!");
				tafelServer.printStackTrace(e);
			}
		}
	}

}
