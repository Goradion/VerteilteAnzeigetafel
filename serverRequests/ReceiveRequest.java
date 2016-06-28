package serverRequests;

import tafelServer.ServerRequestHandler;
import verteilteAnzeigetafel.Message;
import verteilteAnzeigetafel.TafelException;

public class ReceiveRequest extends ServerRequest {

	private static final long serialVersionUID = -3124875056829274290L;
	private Message msg;
	
	public ReceiveRequest(Message msg) {
		this.msg = msg;
	}
	
	public Message getMessage(){
		return msg;
	}
	@Override
	public String handleMe(ServerRequestHandler handler) throws TafelException {
		return handler.handle(this);		
	}
}
