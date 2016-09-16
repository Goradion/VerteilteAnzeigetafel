package serverRequests;

import tafelServer.ServerRequestHandler;
import verteilteAnzeigetafel.TafelException;

public class ModifyPublicRequest extends UserRequest {

	private static final long serialVersionUID = 5828113576693444289L;
	private int messageID;
	private String newMessage;
	/**
	 * Constructs a ModifyPublicRequest.
	 * @param messageID
	 * @param newMessage
	 * @param userID
	 */
	public ModifyPublicRequest(int messageID, String newMessage) {
		super(1);
		this.messageID = messageID;
		this.newMessage = newMessage;
	}
	/**
	 * Returns the ID of the message to modify.
	 * @return messageID
	 */
	public int getMessageID() {
		return messageID;
	}
	/**
	 * Returns the new text content of the message.
	 * @return newMessage
	 */
	public String getNewMessage(){
		return newMessage;
	}
	
	@Override
	public String handleMe(ServerRequestHandler handler) throws TafelException {
		return handler.handle(this);		
	}
}