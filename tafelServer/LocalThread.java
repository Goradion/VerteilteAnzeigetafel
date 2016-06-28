package tafelServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;

import verteilteAnzeigetafel.Message;
import verteilteAnzeigetafel.TafelException;

public class LocalThread extends Thread {
	private Socket client;

	public LocalThread(Socket client) {
		this.client = client;
	}

	public void run() {

		try {

			ObjectInputStream input = new ObjectInputStream(client.getInputStream());
			ServerRequest request = (ServerRequest) input.readObject();
			handleServerRequest(request);
			// input.close();
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleServerRequest(ServerRequest request) throws IOException {
		OutputStream output = client.getOutputStream();
		String antwort = "";
		try {

			switch (request.getType()) {
			case CREATE:
				int msgID = TafelServer.createMessage(request.getMessage(), request.getUserID(),
						request.getAbteilungsID(), request.isOeffentlich());
				antwort = "Nachricht erstellt! msgID=" + msgID;
				break;
			case DELETE:
				TafelServer.deleteMessage(request.getMessageID(), request.getUserID());
				antwort = "Nachricht gelöscht!";
				break;
			case MODIFY:
				TafelServer.modifyMessage(request.getMessageID(), request.getMessage(), request.getUserID());
				antwort = "Nachricht geändert!";
				break;
			case SHOW_MY_MESSAGES:
				showMessagesToClient(request.getUserID());
				break;
			case PUBLISH:
				TafelServer.publishMessage(request.getMessageID(), request.getUserID());
				antwort = "Nachricht veröffentlicht!";
				break;
			case REGISTER:
				TafelServer.registerTafel(request.getAbteilungsID(), new InetSocketAddress(client.getInetAddress(), TafelServer.SERVER_PORT));
				antwort = "Welcome!";
				break;
			case RECEIVE: TafelServer.receiveMessage(new Message(request.getMessage(), request.getUserID(), 
					request.getAbteilungsID(), request.isOeffentlich(), request.getMessageID()));
				antwort = "Nachricht erhalten!";
				break;
			default:
				antwort = "Unrecognized ServerRequest!";
				break;
			}
		} catch (TafelException te) {
			te.printStackTrace();
			antwort = te.getMessage();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		output.write(antwort.getBytes());
	}
	
	private void showMessagesToClient(int userID) throws TafelException {
		LinkedList<Message> userMessages = TafelServer.getMessagesByUserID(userID);
		// TafelServer.print(userMessages.toString());
		try {
			ObjectOutputStream oout = new ObjectOutputStream(client.getOutputStream());
			oout.writeObject(userMessages);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
