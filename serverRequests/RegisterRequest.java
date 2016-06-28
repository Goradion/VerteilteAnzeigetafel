package serverRequests;

import java.net.SocketAddress;

import tafelServer.ServerRequestHandler;
import verteilteAnzeigetafel.TafelException;

public class RegisterRequest extends ServerRequest {

	private static final long serialVersionUID = 6407081221719838758L;
	private int abteilungsID;
	private SocketAddress address;
	
	public RegisterRequest(int abteilungsID, SocketAddress address) {
		this.abteilungsID = abteilungsID;
		this.address = address;
	}
	
	public int getAbteilungsID(){
		return abteilungsID;
	}
	
	public SocketAddress getAddress(){
		return address;
	}
	@Override
	public String handleMe(ServerRequestHandler handler) throws TafelException {
		return handler.handle(this);		
	}
}
