package serverRequests;

import tafelServer.ServerRequestHandler;
import verteilteAnzeigetafel.TafelException;

public class CreateRequest extends UserRequest {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4774781940211234344L;
	private String message;
	private int abteilungsID;
	/**
	 * Constructs a CreateRequest.
	 * @param message
	 * @param userID
	 * @param abteilungsID
	 */
	CreateRequest(String message, int userID, int abteilungsID) {
		super(userID);
		this.message = message;		
		this.abteilungsID = abteilungsID;
	}
	/**
	 * Returns the text content of the message to create.
	 * @return message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * Returns the userID of the message to create.
	 * @return userID
	 */
	public int getUserID() {
		return userID;
	}
	/**
	 * Returns the abteilungsID of the message.
	 * @return abteilungsID
	 */
	public int getAbteilungsID() {
		return abteilungsID;
	}

	@Override
	public String handleMe(ServerRequestHandler handler) throws TafelException {
		return handler.handle(this);		
	}

}
