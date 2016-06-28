package serverRequests;

import java.io.Serializable;
import java.net.SocketAddress;

import tafelServer.ServerRequestHandler;
import verteilteAnzeigetafel.Message;
import verteilteAnzeigetafel.TafelException;

public abstract class ServerRequest implements Serializable {

	private static final long serialVersionUID = -2895374450189726605L;
	
	public abstract String handleMe(ServerRequestHandler handler) throws TafelException, InterruptedException;
	public boolean wantsAnswer(){
		return false;
	}
	public static ServerRequest buildCreateRequest(String message, int userID, int abteilungsID) {
		return new CreateRequest(message, userID, abteilungsID);
	}

	public static ServerRequest buildDeleteRequest(int messageID, int userID) {
		return new DeleteRequest(messageID, userID);
	}

	public static ServerRequest buildModifyRequest(int messageID, String newMessage, int userID) {
		return new ModifyRequest(messageID, newMessage, userID);
	}

	public static ServerRequest buildPublishRequest(int messageID, int userID) {
		return new PublishRequest(messageID, userID);
	}

	public static ServerRequest buildShowMyMessagesRequest(int userID) {
		return new ShowMyMessagesRequest(userID);
	}

	public static ServerRequest buildRegisterRequest(int abteilungsID, SocketAddress address) {
		return new RegisterRequest(abteilungsID, address);
	}
	
	public static ServerRequest buildReceiveRequest(Message msg){
		return new ReceiveRequest(msg);
	}
}
