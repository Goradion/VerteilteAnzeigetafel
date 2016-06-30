package verteilteAnzeigetafel;
//vsemenishch, Git funktioniert!

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class Anzeigetafel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4032175286694659532L;
	private String TAFELNAME;
	private String lastID;
	private final int abteilungsID;
	private int messageAnzahl;
	private int msgLaufNr;
	private final int koordinatorID;
	private LinkedHashMap<Integer, Message> messages;
	/* msgID, msg */
	private HashSet<Integer> userIDs;
	private HashMap<Integer, LinkedList<Integer>> userMsgs;

	/* userID, List<msgID> */

	public Anzeigetafel(int abtNr) {
		/* welche Nummern sollen die einzelnen Anzeigetafeln bekommen? */
		this.abteilungsID = abtNr; // hier muss man fuer jedes kompilierte
									// Programm
		// eine neue Nummer festlegen
		this.koordinatorID = 1;
                this.TAFELNAME="tafel"+abtNr;
		this.messageAnzahl = 0;
		lastID = null;
		this.messages = new LinkedHashMap<Integer, Message>();
		this.userMsgs = new HashMap<Integer, LinkedList<Integer>>();
		this.userIDs = new HashSet<Integer>();
		/* Füge 5 users ein, die zu dieser Anzeigetafel gehören */
		for (int i = 1; i < 6; i++) {
			userIDs.add(i);
		}
	}

	public synchronized boolean isUser(int userID) {
		return userIDs.contains(userID);
	}

	public synchronized boolean isCoordinator(int userID) {
		return userID == koordinatorID;
	}

	public synchronized void modifyMessage(int messageID, String inhalt, int user) throws TafelException {

		if (messages.containsKey(messageID)) {
			/* Autor oder Koordinator ? */
			if (user == messages.get(messageID).getUserID() || isCoordinator(user)) {
				Message msg = new Message(inhalt, messages.get(messageID).getUserID(),
						messages.get(messageID).getAbtNr(), messages.get(messageID).isOeffentlich(),
						messages.get(messageID).getMessageID());
				messages.replace(messageID, msg);
			} else {
				throw new TafelException(user + " nicht berechtigt zum Modifizieren");
			}
		} else {
			throw new TafelException("Keine Message mit ID " + messageID + " gefunden!");
		}
		/* Delete after debugging */
		System.out.println("######  Anzeigetafel ######");
		System.out.println(this.toString());
		System.out.println("###########################");

	}

	public synchronized void publishMessage(int messageID, int user) throws TafelException {
		if (!messages.containsKey(messageID)) {
			throw new TafelException("Keine Message mit ID " + messageID + " gefunden!");
		}
		if (isCoordinator(user)) {
			messages.get(messageID).setOeffentlich();
		} else {
			throw new TafelException("Keine Berechtigung zum Publizieren!");
		}

		/* Delete after debugging */
		System.out.println("######  Anzeigetafel ######");
		System.out.println(this.toString());
		System.out.println("###########################");

	}

	public synchronized void deleteMessage(int messageID, int user) throws TafelException {

		if (messages.containsKey(messageID)) {
			/* Autor oder Koordinator ? */
			if (user == messages.get(messageID).getUserID() || isCoordinator(user)) {
				messages.remove(messageID);
				userMsgs.get(user).remove(new Integer(messageID));
				// userMsgs.get(user).
				// userMsgs.get(user).remove(messageID);
			} else {
				throw new TafelException(user + " nicht berechtigt zum Löschen");
			}
		} else {
			throw new TafelException("Keine Message mit ID " + messageID + " gefunden!");
		}

		/* Delete after debugging */
		System.out.println("######  Anzeigetafel ######");
		System.out.println(this.toString());
		System.out.println("###########################");

	}

	public synchronized int createMessage(String inhalt, int user, int abtNr, boolean oeffentlich)
			throws TafelException {
		if (!userIDs.contains(user)) {
			throw new TafelException("Keine Berechtigung zum Erstellen!");
		}
		int msgID = Integer.parseInt(getNewMsgID(user));
		Message nMsg = new Message(inhalt, user, abtNr, oeffentlich, msgID);
		messages.put(nMsg.getMessageID(), nMsg);
		/* noch kein user da */
		if (!userMsgs.containsKey(user)) {
			userMsgs.put(user, new LinkedList<Integer>());
		}
		userMsgs.get(user).add(msgID);
		messageAnzahl++;
		msgLaufNr++;

		/* Delete after debugging */
		System.out.println("######  Anzeigetafel ######");
		System.out.println(this.toString());
		System.out.println("###########################");

		return msgID;
	}

	private String getNewMsgID(int userID) {
		lastID = "" + abteilungsID + userID + msgLaufNr;
		return lastID;
	}

	public synchronized int getAbteilungsID() {
		return abteilungsID;
	}

	public synchronized int getMessageAnzahl() {
		return messageAnzahl;
	}

	public synchronized int getKoordinatorID() {
		return koordinatorID;
	}

	public synchronized HashMap<Integer, Message> getMessages() {
		return messages;
	}

	// public HashMap getGlobalMessages() {
	// return globalMessages;
	// }
	public synchronized HashSet<Integer> getUserIDs() {
		return userIDs;
	}

	public synchronized String getLastID() {
		return lastID;
	}

	/**
	 * Ermöglicht das Speichern des aktuellen Zustandes der Anzeigetafel in eine
	 * Datei.
	 */
	public synchronized void saveStateToFile() {
		FileOutputStream fileoutput = null;
		ObjectOutputStream objoutput = null;
		try {
			fileoutput = new FileOutputStream("./" + TAFELNAME);
			objoutput = new ObjectOutputStream(fileoutput);
			objoutput.writeObject(this);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Anzeigetafel.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Anzeigetafel.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				objoutput.close();
			} catch (IOException ex) {
				Logger.getLogger(Anzeigetafel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * Ermöglicht das Laden des Zustandes aus einer Datei.
	 *
	 * @return
	 * @throws verteilteAnzeigetafel.TafelException
	 */
	public synchronized static Anzeigetafel loadStateFromFile(int abtNr) {
		Anzeigetafel at = null;
                
		FileInputStream fileinput = null;
		ObjectInputStream objinput = null;
		try {
			fileinput = new FileInputStream("./"+"tafel"+abtNr);
			objinput = new ObjectInputStream(fileinput);
			at = (Anzeigetafel) objinput.readObject();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Anzeigetafel.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException | ClassNotFoundException ex) {
			Logger.getLogger(Anzeigetafel.class.getName()).log(Level.SEVERE, null, ex);
		}
		return at;
	}

	// public LinkedList<Integer> getMessagesByUserID(int userID) throws
	// TafelException{
	// if(!userMsgs.containsKey(userID))
	// throw new TafelException("Kein User gefunden!");
	// return userMsgs.get(userID);
	// }
	public synchronized LinkedList<Message> getMessagesByUserID(int userID) throws TafelException {
		if (!userMsgs.containsKey(userID)) {
			throw new TafelException("Kein User gefunden!");
		}
		LinkedList<Integer> umsgIDs = userMsgs.get(userID);
		LinkedList<Message> uMsgs = new LinkedList<Message>();
		for (Integer element : umsgIDs) {
			uMsgs.add(messages.get(element));
		}
		return uMsgs;
	}

	@Override
	public synchronized String toString() {
		String str = "";
		for (HashMap.Entry<Integer, Message> entry : messages.entrySet()) {
			str = str + "User: " + entry.getValue().getUserID() + " / " + entry.getValue() + "\n\t\t"
					+ entry.getValue().getInhalt() + "\n";

		}
		return str;
	}

	public synchronized void receiveMessage(Message msg) throws TafelException {
		if (msg.getAbtNr() == abteilungsID) {
			throw new TafelException("msg.getAbtNr()==abteilungsID");
		}
		messages.put(msg.getMessageID(), msg);

	}

}
