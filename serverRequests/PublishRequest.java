package serverRequests;

import tafelServer.ServerRequestHandler;
import verteilteAnzeigetafel.TafelException;

public class PublishRequest extends UserRequest {

	private static final long serialVersionUID = 3494885460990163756L;
	private int messageID;
	
	public PublishRequest(int messageID, int userID) {
		super(userID);
		this.messageID = messageID;
	}
	
	public int getMessageID() {
		return messageID;
	}
	@Override
	public String handleMe(ServerRequestHandler handler) throws InterruptedException, TafelException {
		return handler.handle(this);		
	}
}
