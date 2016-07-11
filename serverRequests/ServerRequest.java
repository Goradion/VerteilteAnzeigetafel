package serverRequests;

import java.io.Serializable;
import java.net.SocketAddress;

import tafelServer.ServerRequestHandler;
import verteilteAnzeigetafel.Message;
import verteilteAnzeigetafel.TafelException;

public abstract class ServerRequest implements Serializable {

	private static final long serialVersionUID = -2895374450189726605L;
	/**
	 * Instructs the ServerRequestHandler to handle this ServerRequest
	 * @param handler
	 * @return The answer to the ServerRequest
	 * @throws TafelException
	 * @throws InterruptedException
	 */
	
	public abstract String handleMe(ServerRequestHandler handler) throws TafelException, InterruptedException;
	/**
	 * Returns whether the ServerRequest wants an answer to be read by a user.
	 * @return false default, otherwise as specified in subclass
	 */
	public boolean wantsAnswer(){
		return false;
	}
	/**
	 * creates a CreateRequest with the given parameters
	 * @param message
	 * @param userID
	 * @param abteilungsID
	 * @return a CreateRequest 
	 */
	public static ServerRequest buildCreateRequest(String message, int userID, int abteilungsID) {
		return new CreateRequest(message, userID, abteilungsID);
	}
	/**
	 * creates a DeleteRequest with the given parameters
	 * @param messageID
	 * @param userID
	 * @return a DeleteRequest
	 */
	public static ServerRequest buildDeleteRequest(int messageID, int userID) {
		return new DeleteRequest(messageID, userID);
	}
	/**
	 * creates a ModifyRequest with the given parameters
	 * @param messageID
	 * @param newMessage
	 * @param userID
	 * @return a ModifyRequest
	 */
	public static ServerRequest buildModifyRequest(int messageID, String newMessage, int userID) {
		return new ModifyRequest(messageID, newMessage, userID);
	}
	/**
	 * creates a PublishRequest with the given parameters
	 * @param messageID
	 * @param userID
	 * @return a PublishRequest
	 */
	public static ServerRequest buildPublishRequest(int messageID, int userID) {
		return new PublishRequest(messageID, userID);
	}
	/**
	 * creates a ShowMyMessagesRequest with the given parameters
	 * @param userID
	 * @return a ShowMyMessagesRequest
	 */
	public static ServerRequest buildShowMyMessagesRequest(int userID) {
		return new ShowMyMessagesRequest(userID);
	}
	/**
	 * creates a RegisterRequest with the given parameters
	 * @param abteilungsID
	 * @param address
	 * @return a RegisterRequest
	 */
	public static ServerRequest buildRegisterRequest(int abteilungsID, SocketAddress address) {
		return new RegisterRequest(abteilungsID, address);
	}
	/**
	 * creates a ReceiveRequest with the given parameters
	 * @param msg
	 * @return a ReceiveRequest
	 */
	public static ServerRequest buildReceiveRequest(Message msg){
		return new ReceiveRequest(msg);
	}
}
