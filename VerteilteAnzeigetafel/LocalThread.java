package VerteilteAnzeigetafel;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class LocalThread extends Thread {
	private Socket client;
	public LocalThread(Socket client) {
		this.client = client;
	}
	public void run(){
		
		try {
			//TafelServer.print("hi");
			ObjectInputStream input = new ObjectInputStream(client.getInputStream());
			//while (input.available() == 0);
			//TafelServer.print("lese");
	        ServerRequest request = (ServerRequest) input.readObject();
	        handleServerRequest(request);
	        //TafelServer.print(request.toString());
	        TafelServer.printMessages();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handleServerRequest(ServerRequest request){
		try{
			switch (request.getType()) {
			case CREATE: TafelServer.createMessage(request.getMessage(), request.getUserID(), 
					request.getAbteilungsID(), request.isOeffentlich());
				break;
			case DELETE: TafelServer.deleteMessage(request.getMessageID(), request.getUserID());
				break;
			case MODIFY: TafelServer.modifyMessage(request.getMessageID(), request.getMessage(), request.getUserID());
				break;
			case SHOW_MY_MESSAGES: showMessagesToClient(request.getUserID());
				break;
			case PUBLISH: //TODO
				break;
			case REGISTER: TafelServer.activateQueue(request.getAbteilungsID());
				break;
			default:
				break;
			}
		} catch (TafelException te) {
			te.printStackTrace();
		}
	}
	private void showMessagesToClient(int userID)  {
		LinkedList<Message> userMessages= TafelServer.getMessagesByUserID(userID);
		try {
			ObjectOutputStream oout = new ObjectOutputStream(client.getOutputStream());
			oout.writeObject(userMessages);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
