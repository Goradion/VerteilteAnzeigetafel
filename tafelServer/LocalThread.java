package tafelServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;

import serverRequests.ServerRequest;
import verteilteAnzeigetafel.Message;
import verteilteAnzeigetafel.TafelException;

public class LocalThread extends Thread {
	private Socket client;
	private TafelServer tafelServer;
	private ServerRequestHandler handler;
	public LocalThread(Socket client, TafelServer tafelServer) {
		this.client = client;
		this.tafelServer = tafelServer;
		this.handler = new ServerRequestHandler(tafelServer, tafelServer.getAnzeigetafel());
	}

	public void run() {

		try {

			ObjectInputStream input = new ObjectInputStream(client.getInputStream());
			Object o = null;
			ServerRequest request;
			while((o = input.readObject()) != null){
				if (o instanceof ServerRequest){
					request = (ServerRequest) o;
					handleServerRequest(request);
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
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void handleServerRequest(ServerRequest request) throws IOException {
		ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
		String antwort = "";
		try {

//			switch (request.getType()) {
//			case CREATE:
//				int msgID = TafelServer.createMessage(request.getMessage(), request.getUserID(),
//						request.getAbteilungsID(), request.isOeffentlich());
//				antwort = "Nachricht erstellt! msgID=" + msgID;
//				break;
//			case DELETE:
//				TafelServer.deleteMessage(request.getMessageID(), request.getUserID());
//				antwort = "Nachricht gelöscht!";
//				break;
//			case MODIFY:
//				TafelServer.modifyMessage(request.getMessageID(), request.getMessage(), request.getUserID());
//				antwort = "Nachricht geändert!";
//				break;
//			case SHOW_MY_MESSAGES:
//				showMessagesToClient(request.getUserID());
//				break;
//			case PUBLISH:
//				TafelServer.publishMessage(request.getMessageID(), request.getUserID());
//				antwort = "Nachricht veröffentlicht!";
//				break;
//			case REGISTER:
//				TafelServer.registerTafel(request.getAbteilungsID(), new InetSocketAddress(client.getInetAddress(), TafelServer.SERVER_PORT));
//				antwort = "Welcome!";
//				break;
//			case RECEIVE: TafelServer.receiveMessage(new Message(request.getMessage(), request.getUserID(), 
//					request.getAbteilungsID(), request.isOeffentlich(), request.getMessageID()));
//				antwort = "Nachricht erhalten!";
//				break;
//			default:
//				antwort = "Unrecognized ServerRequest!";
//				break;
//			}
			antwort = handler.handle(request);
		} catch (TafelException te) {
			te.printStackTrace();
			antwort = te.getMessage();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tafelServer.print(antwort);
		output.writeObject(antwort);
	}
}
