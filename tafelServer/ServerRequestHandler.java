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

	/**
	 * Construcs a new ServerRequestHandler
	 * 
	 * @param tafelServer
	 * @param anzeigetafel
	 */
	public ServerRequestHandler(TafelServer tafelServer, Anzeigetafel anzeigetafel) {
		super();
		this.tafelServer = tafelServer;
		this.anzeigetafel = anzeigetafel;
	}

	/**
	 * Instructs a ServerRequest to indentify itself.
	 * 
	 * @param serverRequest
	 * @return an answer
	 * @throws TafelException
	 *             if the anzeigetafel rejects the request.
	 * @throws InterruptedException
	 *             if the handling was interrupted
	 */
	public String handle(ServerRequest serverRequest) throws TafelException, InterruptedException {
		return serverRequest.handleMe(this);
	}

	/**
	 * Handles a request to create a new message.
	 * 
	 * @param createRequest
	 * @return an answer
	 * @throws TafelException
	 *             if the anzeigetafel rejects the request.
	 */
	public String handle(CreateRequest createRequest) throws TafelException {
		int msgID = anzeigetafel.createMessage(createRequest.getMessage(), createRequest.getUserID(),
				createRequest.getAbteilungsID(), false);
		anzeigetafel.saveStateToFile();
		return "Nachricht mit ID=" + msgID + " erstellt!";
	}

	/**
	 * Handles a request to delete a message.
	 * 
	 * @param deleteRequest
	 * @return an answer
	 * @throws TafelException
	 *             if the anzeigetafel rejects the request.
	 */
	public String handle(DeleteRequest deleteRequest) throws TafelException {
		// // if (anzeigetafel.getMessagesByUserID(deleteRequest.getUserID()))
		// LinkedList<Message> messagesByUserID =
		// anzeigetafel.getMessagesByUserID(deleteRequest.getUserID());
		// int deleteID = deleteRequest.getMessageID();
		// for (Message m : messagesByUserID) {
		// if (m.getMessageID() == deleteID && m.isOeffentlich()) {
		// tafelServer.deletePublicMessage(deleteID);
		// break;
		// }
		// }
		String antwort = "Nachricht mit ID=" + deleteRequest.getMessageID() + " gelöscht!";
		Message message = anzeigetafel.getMessageByID(deleteRequest.getMessageID());
		if (message == null) {
			return "Nachricht mit ID =" + deleteRequest.getMessageID() + " nicht gefunden";
		}
		if (message.getAbtNr() != anzeigetafel.getAbteilungsID()){
			return "Nachricht geh�rt nicht zur Abteilung!";
		}
		anzeigetafel.deleteMessage(deleteRequest.getMessageID(), deleteRequest.getUserID());
		if ((message.isOeffentlich() && message.getAbtNr() == anzeigetafel.getAbteilungsID()))
			tafelServer.deletePublicMessage(deleteRequest.getMessageID());

		anzeigetafel.saveStateToFile();
		return antwort;
	}

	/**
	 * Handles a request to delete a public message.
	 * 
	 * @param deletePublicRequest
	 * @return an answer
	 * @throws TafelException
	 *             if the anzeigetafel rejects the request.
	 */
	public String handle(DeletePublicRequest deletePublicRequest) throws TafelException {
		anzeigetafel.deleteMessage(deletePublicRequest.getMessageID(), deletePublicRequest.getUserID());
		anzeigetafel.saveStateToFile();
		return "Nachricht mit ID=" + deletePublicRequest.getMessageID() + " gelöscht!";
	}

	/**
	 * Handles a request to modify a message. Initiates the
	 * handle(ModifyPublicRequest) method in case the message is public.
	 * 
	 * @param modifyRequest
	 * @return a answer
	 * @throws TafelException
	 *             if the anzeigetafel rejects the request.
	 */
	public String handle(ModifyRequest modifyRequest) throws TafelException {
		// LinkedList<Message> messagesByUserID =
		// anzeigetafel.getMessagesByUserID(anzeigetafel.getUserByMessage(modifyRequest.getMessageID()));
		// int modID = modifyRequest.getMessageID();
		// for (Message m : messagesByUserID) {
		// if (m.getMessageID() == modID && m.isOeffentlich()) {
		// tafelServer.modifyPublicMessage(modID,
		// modifyRequest.getNewMessage());
		// break;
		// }
		// }
		String antwort = "Nachricht mit ID=" + modifyRequest.getMessageID() + " geändert!";
		Message message = anzeigetafel.getMessageByID(modifyRequest.getMessageID());
		if (message == null) {
			return "Nachricth mit ID= " + modifyRequest.getMessageID() + " nicht gefunden!";
		}
		if (message.getAbtNr() != anzeigetafel.getAbteilungsID()){
			return "Nachricht geh�rt nicht zur Abteilung!";
		}
		anzeigetafel.modifyMessage(modifyRequest.getMessageID(), modifyRequest.getNewMessage(),
				modifyRequest.getUserID());
		if ((message.isOeffentlich() && message.getAbtNr() == anzeigetafel.getAbteilungsID())) {
			tafelServer.modifyPublicMessage(modifyRequest.getMessageID(), modifyRequest.getNewMessage());
		}

		anzeigetafel.saveStateToFile();
		return antwort;
	}

	/**
	 * Handles a request to modify a public messages, usually initiated by the
	 * handle(ModifyRequest) method.
	 * 
	 * @param modifyPublicRequest
	 * @return an answer
	 * @throws TafelException
	 *             if anzeigetafel rejects the request
	 */
	public String handle(ModifyPublicRequest modifyPublicRequest) throws TafelException {
		anzeigetafel.modifyMessage(modifyPublicRequest.getMessageID(), modifyPublicRequest.getNewMessage(),
				modifyPublicRequest.getUserID());
		anzeigetafel.saveStateToFile();
		return "Nachricht mit ID=" + modifyPublicRequest.getMessageID() + " geändert!";
	}

	/**
	 * Handles a request to publish a message.
	 * 
	 * @param publishRequest
	 * @return an aswer
	 * @throws InterruptedException
	 *             if the handling was interrupted.
	 * @throws TafelException
	 *             if the anzeigetafel rejects the request.
	 */
	public String handle(PublishRequest publishRequest) throws InterruptedException, TafelException {
		tafelServer.publishMessage(publishRequest.getMessageID(), publishRequest.getUserID());
		return "Nachricht mit ID=" + publishRequest.getMessageID() + " veröffentlicht!";
	}

	/**
	 * Handles a request to show a user his or her messages.
	 * 
	 * @param showMyMessagesRequest
	 * @return the messages as a String
	 * @throws TafelException
	 */
	public String handle(ShowMyMessagesRequest showMyMessagesRequest) throws TafelException {
		int userID = showMyMessagesRequest.getUserID();
		LinkedList<Message> userMessages = anzeigetafel.getMessagesByUserID(userID);
		return "Messages of User " + userID + '\n' + userMessages.toString();
	}

	/**
	 * Hanldes a Request to receive a message from another TafelServer.
	 * 
	 * @param receiveRequest
	 * @return an answer
	 * @throws TafelException
	 *             if the anzeigetafel rejects the request.
	 */
	public String handle(ReceiveRequest receiveRequest) throws TafelException {
		anzeigetafel.receiveMessage(receiveRequest.getMessage());
		anzeigetafel.saveStateToFile();
		return "Nachricht von Abteilung " + receiveRequest.getMessage().getAbtNr() + " erhalten!";
	}

	/**
	 * Handles a request to register a TafelServer.
	 * 
	 * @param registerRequest
	 * @return an answer
	 * @throws TafelException
	 *             if the TafelServer rejects the request.
	 */
	public String handle(RegisterRequest registerRequest) throws TafelException {
		if (registerRequest.getAbteilungsID() == tafelServer.getAbteilungsID()) {
			throw new TafelException("Die eigene Abteilung wird nicht registriert!");
		}

		HashMap<Integer, SocketAddress> tafelAdressen = tafelServer.getTafelAdressen();
		int abteilungsID = registerRequest.getAbteilungsID();
		SocketAddress address = registerRequest.getAddress();
		if (tafelAdressen.containsKey(abteilungsID)) {
			if (!tafelAdressen.get(abteilungsID).equals(address)) {
				tafelAdressen.replace(abteilungsID, address);
			}

		} else {
			tafelAdressen.put(abteilungsID, address);
			tafelServer.getQueueMap().put(abteilungsID, new LinkedBlockingDeque<ServerRequest>());
		}
		tafelServer.activateHeartbeat(abteilungsID);
		tafelServer.activateQueue(abteilungsID);
		tafelServer.saveTafelAdressenToFile();
		return "Tafel für Abteilung " + abteilungsID + " unter Adresse " + address + " registriert!";

	}

}
