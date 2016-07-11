package serverRequests;

import tafelServer.ServerRequestHandler;
import verteilteAnzeigetafel.TafelException;

public class ShowMyMessagesRequest extends UserRequest {
	
	private static final long serialVersionUID = 7916617565034793074L;
	/**
	 * Constructs a ShowMyMessagesRequest.
	 * @param userID
	 */
	public ShowMyMessagesRequest(int userID) {
		super(userID);
	}
	@Override
	public String handleMe(ServerRequestHandler handler) throws TafelException {
		return handler.handle(this);		
	}

}
