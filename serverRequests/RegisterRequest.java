package serverRequests;

import java.net.SocketAddress;

import tafelServer.ServerRequestHandler;
import verteilteAnzeigetafel.TafelException;

public class RegisterRequest extends ServerRequest {

	private static final long serialVersionUID = 6407081221719838758L;
	private int abteilungsID;
	private SocketAddress address;
	/**
	 * Constructs a RegisterRequest.
	 * @param abteilungsID
	 * @param address
	 */
	public RegisterRequest(int abteilungsID, SocketAddress address) {
		this.abteilungsID = abteilungsID;
		this.address = address;
	}
	/**
	 * Returns the abteilungsID of the TafelServer to register.
	 * @return abteilungsID
	 */
	public int getAbteilungsID(){
		return abteilungsID;
	}
	/**
	 * Retunrs the SocketAddress of the TafelServer to register.
	 * @return address
	 */
	public SocketAddress getAddress(){
		return address;
	}
	@Override
	public String handleMe(ServerRequestHandler handler) throws TafelException {
		return handler.handle(this);		
	}
}
