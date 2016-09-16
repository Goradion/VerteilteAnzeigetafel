package serverRequests;

import tafelServer.ServerRequestHandler;
import verteilteAnzeigetafel.TafelException;

public class DeletePublicRequest extends UserRequest {

	private static final long serialVersionUID = -8145101718329410770L;
	int messageID;
	/**
	 * Constructs a DeletePublicRequest.
	 * @param messageID
	 * @param userID
	 */
	public DeletePublicRequest(int messageID, int userID) {
		super(userID);
		this.messageID = messageID;
	}
	/**
	 * Returns the ID of the message to delete.
	 * @return messageID
	 */
	public int getMessageID() {
		return messageID;
	}
	@Override
	public String handleMe(ServerRequestHandler handler) throws TafelException {
		return handler.handle(this);		
	}
}
