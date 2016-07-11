package serverRequests;

import tafelServer.ServerRequestHandler;
import verteilteAnzeigetafel.TafelException;

public class DeleteRequest extends UserRequest {

	private static final long serialVersionUID = -2934517731267628088L;
	int messageID;
	/**
	 * Constructs a DeleteRequest.
	 * @param messageID
	 * @param userID
	 */
	public DeleteRequest(int messageID, int userID) {
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
