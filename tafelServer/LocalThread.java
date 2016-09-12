package tafelServer;

import java.io.*;
import java.net.Socket;
import serverRequests.ServerRequest;
import verteilteAnzeigetafel.TafelException;

public class LocalThread extends Thread {
	private Socket client;
	private TafelServer tafelServer;
	private ServerRequestHandler handler;
	/**
	 * Constructs a new LocalThread.
	 * @param client the Socket where requests come from
	 * @param tafelServer the Server on which to execute requests.
	 */
	public LocalThread(Socket client, TafelServer tafelServer) {
		this.client = client;
		this.tafelServer = tafelServer;
		this.handler = new ServerRequestHandler(tafelServer, tafelServer.getAnzeigetafel());
	}
	/**
	 * Reads ServerRequests from the client Socket and invokes the handleServerRequest method.
	 */
	public void run() {

		try {

			ObjectInputStream input = new ObjectInputStream(client.getInputStream());
			Object o = null;
			ServerRequest request;
			while((o = input.readObject()) != null){
				if (o instanceof ServerRequest){
					request = (ServerRequest) o;
					handleServerRequest(request);
					input = new ObjectInputStream(client.getInputStream());
				} else {
					break;
				}
			}
			// input.close();
			
		} catch (EOFException eofe){
			tafelServer.print("Client "+client.getRemoteSocketAddress()+" hang up!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
			finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * Handles a ServerRequest via the handler.
	 * @param request
	 * @throws IOException if something went wrong the output stream.
	 */
	private void handleServerRequest(ServerRequest request) throws IOException {
		ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
		String antwort = "";
		try {
			antwort = handler.handle(request);
		} catch (TafelException te) {
			antwort = te.getMessage();
		} catch (InterruptedException e) {
			tafelServer.printStackTrace(e);
		}
		tafelServer.print(antwort);
		output.writeObject(antwort);
	}
}
