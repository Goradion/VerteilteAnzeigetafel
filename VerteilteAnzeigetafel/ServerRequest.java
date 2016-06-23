package VerteilteAnzeigetafel;

import java.io.Serializable;

public class ServerRequest implements Serializable {
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

	public ServerRequest(ServerRequestType type, int messageID, String message, int userID, int abteilungsID, boolean oeffentlich) {
		super();
		this.type = type;
		this.messageID = messageID;
		this.message = message;
		this.userID = userID;
		this.abteilungsID = abteilungsID;
		this.oeffentlich = oeffentlich;
	}

	@Override
	public String toString() {
		return "ServerRequest [type=" + type + ", messageID=" + messageID + ", message=" + message + ", abteilungsID="
				+ abteilungsID + "]";
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

	public static ServerRequest buildCreateRequest(String message, int userID, int abteilungsID) {
		return new ServerRequest(ServerRequestType.CREATE, 0, message, userID, abteilungsID, false);
	}

	public static ServerRequest buildDeleteRequest(int messageID, int userID) {
		return new ServerRequest(ServerRequestType.DELETE, messageID, null, userID, 0, false);
	}

	public static ServerRequest buildModifyRequest(int messageID, String newMessage, int userID) {
		return new ServerRequest(ServerRequestType.MODIFY, messageID, newMessage, userID, 0, false);
	}

	public static ServerRequest buildPublishRequest(int messageID, int userID) {
		return new ServerRequest(ServerRequestType.PUBLISH, messageID, null, userID, 0, true);
	}

	public static ServerRequest buildShowMyMessagesRequest(int userID) {
		return new ServerRequest(ServerRequestType.SHOW_MY_MESSAGES, 0, null, userID, 0, false);
	}

	public static ServerRequest buildRegisterRequest(int abteilungsID) {
		return new ServerRequest(ServerRequestType.REGISTER, 0, null, 0, abteilungsID, false);
	}
}
