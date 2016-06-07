package VerteilteAnzeigetafel;

import java.io.*;
import java.net.Socket;

public class LocalThread extends Thread {
	private Socket client;
	public LocalThread(Socket client) {
		this.client = client;
	}
	public void run(){
		
		try {
			ObjectInputStream input = new ObjectInputStream(client.getInputStream());
			while (input.available() == 0);
			
	        ServerRequest request = (ServerRequest) input.readObject();
	        handleServerRequest(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handleServerRequest(ServerRequest request){
		switch (request.getType()) {
		case CREATE: TafelServer.createMessage();
			break;
		case DELETE: TafelServer.deleteMessage(request.getMessageID());
			break;
		case MODIFY: TafelServer.modifyMessage(request.getMessageID(), request.getMessage());
			break;
		case REGISTER: TafelServer.activateQueue(request.getAbteilungsID());
			break;
		default:
			break;
		}
	}
}
