package VerteilteAnzeigetafel;

import java.io.Serializable;

public class ServerRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 974934460395601110L;
	private ServerRequestType type;
	private int messageID;
	private String message;
	private int userID;
	private int abteilungsID;
	private boolean oeffentlich;
	


	public ServerRequest(ServerRequestType type, int messageID, String message, int userID,
			int abteilungsID) {
		super();
		this.type = type;
		this.messageID = messageID;
		this.message = message;
		this.userID = userID;
		this.abteilungsID = abteilungsID;
	}
	

	
	@Override
	public String toString() {
		return "ServerRequest [type=" + type + ", messageID=" + messageID
				+ ", message=" + message + ", abteilungsID=" + abteilungsID
				+ "]";
	}


	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	public int getAbteilungsID() {
		return abteilungsID;
	}

	public ServerRequestType getType() {
		return type;
	}

	public int getMessageID() {
		return messageID;
	}


	public String getMessage() {
		return message;
	}

	public int getUserID() {
		return userID;
	}

	public boolean isOeffentlich() {
		return oeffentlich;
	}
	public static  ServerRequest buildCreateRequest(ServerRequestType type, int messageID, String message, 
			int userID, int abteilungsID ) {
		if (type == ServerRequestType.CREATE){
			return new ServerRequest(type, 0, message, userID, abteilungsID);
		} else {
			return null;
		}	
	}
	public static ServerRequest buildDeleteRequest(ServerRequestType type, int messageID, int userID){
		if (type == ServerRequestType.DELETE){
			return new ServerRequest(type, messageID, null, userID, 0);
		} else {
			return null;
		}
	}
	
	public static ServerRequest buildModifyRequest(ServerRequestType type, int messageID, String newMessage,
			int userID){
		if (type == ServerRequestType.MODIFY){
			return new ServerRequest(type, messageID, newMessage, userID, 0);
		} else {
			return null;
		}
	}
	
	public static ServerRequest buildPublishRequest(ServerRequestType type, int messageID, int userID){
		if (type == ServerRequestType.PUBLISH){
			return new ServerRequest(type, messageID, null, userID, 0);
		} else {
			return null;
		}
	}
	
	public static ServerRequest buildShowMyMessagesRequest(ServerRequestType type, int userID){
		if (type == ServerRequestType.SHOW_MY_MESSAGES){
			return new ServerRequest(type, 0, null, userID, 0);
		} else {
			return null;
		}
	}
	
	public static ServerRequest buildRegisterRequest(ServerRequestType type,int abteilungsID){
		if (type == ServerRequestType.REGISTER){
			return new ServerRequest(type, 0, null, 0, abteilungsID);
		} else {
			return null;
		}
	}
}
