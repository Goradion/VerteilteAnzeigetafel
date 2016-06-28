package serverRequests;

import tafelServer.ServerRequestHandler;
import verteilteAnzeigetafel.TafelException;

public class ModifyRequest extends UserRequest {

	private static final long serialVersionUID = -6733826872984962144L;
	private int messageID;
	private String newMessage;
	public ModifyRequest(int messageID, String newMessage, int userID) {
		super(userID);
		this.messageID = messageID;
		this.newMessage = newMessage;
	}
	
	public int getMessageID() {
		return messageID;
	}
	
	public String getNewMessage(){
		return newMessage;
	}
	
	@Override
	public String handleMe(ServerRequestHandler handler) throws TafelException {
		return handler.handle(this);		
	}
}
