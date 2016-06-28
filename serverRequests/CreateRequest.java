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
	
	CreateRequest(String message, int userID, int abteilungsID) {
		super(userID);
		this.message = message;		
		this.abteilungsID = abteilungsID;
	}
	
	public String getMessage() {
		return message;
	}
	public int getUserID() {
		return userID;
	}
	public int getAbteilungsID() {
		return abteilungsID;
	}

	@Override
	public String handleMe(ServerRequestHandler handler) throws TafelException {
		return handler.handle(this);		
	}

}
