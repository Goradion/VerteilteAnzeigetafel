package serverRequests;

import tafelServer.ServerRequestHandler;
import verteilteAnzeigetafel.Message;
import verteilteAnzeigetafel.TafelException;

public class ReceiveRequest extends ServerRequest {

	private static final long serialVersionUID = -3124875056829274290L;
	private Message msg;
	/**
	 * Constructs a ReceiveRequest.
	 * @param msg
	 */
	public ReceiveRequest(Message msg) {
		this.msg = msg;
	}
	/**
	 * Returns the full message to receive. (not only the text content)
	 * @return
	 */
	public Message getMessage(){
		return msg;
	}
	@Override
	public String handleMe(ServerRequestHandler handler) throws TafelException {
		return handler.handle(this);		
	}
}
