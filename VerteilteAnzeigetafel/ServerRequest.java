package VerteilteAnzeigetafel;

public class ServerRequest {
	private ServerRequestType type;
	private int messageID;
	private String message;
	private int abteilungsID; 
	public ServerRequest(ServerRequestType type, int messageID, String message,
			int abteilungsID) {
		super();
		this.type = type;
		this.messageID = messageID;
		this.message = message;
		this.abteilungsID = abteilungsID;
	}



	
	@Override
	public String toString() {
		return "ServerRequest [type=" + type + ", messageID=" + messageID
				+ ", message=" + message + ", abteilungsID=" + abteilungsID
				+ "]";
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

	
}
