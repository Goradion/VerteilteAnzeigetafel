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

	
}
