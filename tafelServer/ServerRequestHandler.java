package tafelServer;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;
import serverRequests.*;
import verteilteAnzeigetafel.Anzeigetafel;
import verteilteAnzeigetafel.Message;
import verteilteAnzeigetafel.TafelException;

public class ServerRequestHandler {
	private TafelServer tafelServer;
	private Anzeigetafel anzeigetafel;
	public ServerRequestHandler(TafelServer tafelServer, Anzeigetafel anzeigetafel) {
		super();
		this.tafelServer = tafelServer;
		this.anzeigetafel = anzeigetafel;
	}

	public String handle(ServerRequest serverRequest) throws TafelException, InterruptedException {
		return serverRequest.handleMe(this);
	}
	public String handle(CreateRequest createRequest) throws TafelException {
		int msgID = anzeigetafel.createMessage(createRequest.getMessage(), createRequest.getUserID(), createRequest.getAbteilungsID(), false);
		anzeigetafel.saveStateToFile();
		return "Nachricht erstellt:\n" + anzeigetafel.getMessages().get(msgID);
	}

	public String handle(DeleteRequest deleteRequest) throws TafelException {
		anzeigetafel.deleteMessage(deleteRequest.getMessageID(), deleteRequest.getUserID());
		anzeigetafel.saveStateToFile();
		return "Nachricht mit ID=" + deleteRequest.getMessageID() + " gelöscht!";
	}

	public String handle(ModifyRequest modifyRequest) throws TafelException {
		anzeigetafel.modifyMessage(modifyRequest.getMessageID(), modifyRequest.getNewMessage(), modifyRequest.getUserID());
		anzeigetafel.saveStateToFile();
		return "Nachricht mit ID=" + modifyRequest.getMessageID() + " geändert!";
	}

	public String handle(PublishRequest publishRequest) throws InterruptedException, TafelException {
		tafelServer.publishMessage(publishRequest.getMessageID(), publishRequest.getUserID());
		return "Nachricht mit ID=" + publishRequest.getMessageID() + " veröffentlicht!";
	}
	
	public String handle(ShowMyMessagesRequest showMyMessagesRequest) throws TafelException {
		int userID = showMyMessagesRequest.getUserID();
		LinkedList<Message> userMessages = anzeigetafel.getMessagesByUserID(userID);
		return "Messages of User "+userID+'\n'+userMessages.toString();
	}
	public String handle(ReceiveRequest receiveRequest) throws TafelException {
		anzeigetafel.receiveMessage(receiveRequest.getMessage());
		anzeigetafel.saveStateToFile();
		return "Nachricht von Abteilung "+receiveRequest.getMessage().getAbtNr()+" erhalten!";		
	}

	public String handle(RegisterRequest registerRequest) throws TafelException {
		if(registerRequest.getAbteilungsID()==tafelServer.getAbteilungsID()){
			throw new TafelException("Die eigene Abteilung wird nicht registriert");
		}
		
		HashMap<Integer, SocketAddress> tafelAdressen = tafelServer.getTafelAdressen();
		int abteilungsID = registerRequest.getAbteilungsID();
		SocketAddress address = registerRequest.getAddress();
		if(tafelAdressen .containsKey(abteilungsID)){
			if(!tafelAdressen.get(abteilungsID).equals(address)){
				tafelAdressen.replace(abteilungsID, address);
			}
						
		}else {
			tafelAdressen.put(abteilungsID, address);
			tafelServer.getQueueMap().put(abteilungsID, new LinkedBlockingDeque<Message>());
		}
		tafelServer.activateHeartbeat(abteilungsID);
		tafelServer.activateQueue(abteilungsID);
		tafelServer.saveTafelAdressenToFile();
		return "Tafel für Abteilung "+abteilungsID+" unter Adresse "+address+" registriert!";
		
	}

	



}
