package verteilteAnzeigetafel;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class Anzeigetafel extends Observable implements Serializable {

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
		this.abteilungsID = abtNr; 
		this.koordinatorID = 1;
                this.TAFELNAME="tafel"+abtNr;
		this.messageAnzahl = 0;
		lastID = null;
		this.messages = new LinkedHashMap<Integer, Message>();
		this.userMsgs = new HashMap<Integer, LinkedList<Integer>>();
		this.userIDs = new HashSet<Integer>();
		/* Fuege 5 users ein, die zu dieser Anzeigetafel gehoeren */
		for (int i = 1; i < 6; i++) {
			userIDs.add(i);
		}
		for (int i = 1; i < 6; i++){
			userMsgs.put(i, new LinkedList<Integer>());
		}
	}
        
	public synchronized boolean isUser(int userID) {
		return userIDs.contains(userID);
	}

	public synchronized boolean isCoordinator(int userID) {
		return userID == koordinatorID;
	}
        /**
         * Changes the content of a message if the enquirer is a valid user and
         * has the needed permissions.
         * @param messageID
         * @param inhalt
         * @param user
         * @throws TafelException 
         */
	public synchronized void modifyMessage(int messageID, String inhalt, int user) throws TafelException {

		if (messages.containsKey(messageID)) {
			/* Autor oder Koordinator ? */
			if (user == messages.get(messageID).getUserID() || isCoordinator(user)) {
				Message msg = new Message(inhalt, messages.get(messageID).getUserID(),
						messages.get(messageID).getAbtNr(), messages.get(messageID).isOeffentlich(),
						messages.get(messageID).getMessageID());
				messages.replace(messageID, msg);
			} else {
				throw new TafelException("User "+user + " nicht berechtigt zum Modifizieren");
			}
		} else {
			throw new TafelException("Keine Message mit ID " + messageID + " gefunden!");
		}
                updateState();
	}
        /**
         * Sets a message from "non-public" to "public" if it's a valid message
         * and the enquirer is the coordinator.
         * @param messageID
         * @param user
         * @throws TafelException 
         */
	public synchronized void publishMessage(int messageID, int user) throws TafelException {
		if (!messages.containsKey(messageID)) {
			throw new TafelException("Keine Message mit ID " + messageID + " gefunden!");
		}
		if (isCoordinator(user)) {
			messages.get(messageID).setOeffentlich();
		} else {
			throw new TafelException("Keine Berechtigung zum Publizieren!");
		}
                updateState();
	}
        /**
         * Deletes a message if it's a valid message and the enquirer has needed
         * permissions.
         * @param messageID
         * @param user
         * @throws TafelException 
         */
	public synchronized void deleteMessage(int messageID, int user) throws TafelException {

		if (messages.containsKey(messageID)) {
			if (user == messages.get(messageID).getUserID() || isCoordinator(user)) {
				messages.remove(messageID);
				userMsgs.get(user).remove(new Integer(messageID));
				// userMsgs.get(user).
				// userMsgs.get(user).remove(messageID);
			} else {
				throw new TafelException("User "+user + " nicht berechtigt zum Loeschen");
			}
		} else {
			throw new TafelException("Keine Message mit ID " + messageID + " gefunden!");
		}
                updateState();             
	}
        /**
         * Creates a new Message if the enquirer is a valid user and has needed
         * permissions.
         * @param inhalt
         * @param user
         * @param abtNr
         * @param oeffentlich
         * @return id of the created message
         * @throws TafelException 
         */
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
		System.out.println(userMsgs.toString());
		messageAnzahl++;
		msgLaufNr++;

                updateState();
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
         * Saves Anzeigetafel to a file.
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
	 * Loads an instance of Anzeigetafel from file if it's available.
	 * @return Anzeigetafel
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
			System.out.println("keine tafeldatei gefunden!");
//			Logger.getLogger(Anzeigetafel.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException | ClassNotFoundException ex) {
			Logger.getLogger(Anzeigetafel.class.getName()).log(Level.SEVERE, null, ex);
		}

		return at;
	}
        /**
         * Returns a list of messages to the provided user id.
         * @param userID
         * @return list of messages
         * @throws TafelException 
         */
	public synchronized LinkedList<Message> getMessagesByUserID(int userID) throws TafelException {
		if (!userMsgs.containsKey(userID)) {
			throw new TafelException("Kein User"+ userID +"gefunden! ");
		}
		if (isCoordinator(userID)){
			LinkedList<Message> msgs =getLocalMsgs();
			msgs.addAll(getGlobalMsgs());
			return msgs;
		}
		LinkedList<Integer> umsgIDs = userMsgs.get(userID);
		LinkedList<Message> uMsgs = new LinkedList<Message>();
		for (Integer element : umsgIDs) {
			uMsgs.add(messages.get(element));
		}
		return uMsgs;
	}
        /**
         * Returns the state of Anzeigetafel in a string-form.
         * @return 
         */
	@Override
	public synchronized String toString() {
		String str = "";
		for (HashMap.Entry<Integer, Message> entry : messages.entrySet()) {
			str = str + "User: " + entry.getValue().getUserID() + " / " + entry.getValue() + "\n\t\t"
					+ entry.getValue().getInhalt() + "\n";

		}
		return str;
	}
        /**
         * Receives a message and puts it into the messages-list.
         * @param msg
         * @throws TafelException 
         */
	public synchronized void receiveMessage(Message msg) throws TafelException {
		if (msg.getAbtNr() == abteilungsID) {
			throw new TafelException("msg.getAbtNr()==abteilungsID");
		}
		messages.put(msg.getMessageID(), msg);
		userMsgs.get(msg.getUserID()).add(msg.getMessageID()); 
        updateState();
	}
        
        /**
         * Returns a list of local messages.
         * @return 
         */
        public synchronized LinkedList<Message> getLocalMsgs(){
           LinkedList<Message> pm = new LinkedList<Message>();
           for(HashMap.Entry<Integer, Message> entry : messages.entrySet()){
               if(!entry.getValue().isOeffentlich()){
                   pm.add(entry.getValue());
               }
           }
           return pm;
        }
        
        /**
         * Returns a list of global (published) messages.
         * @return 
         */
        public synchronized LinkedList<Message> getGlobalMsgs(){
           LinkedList<Message> lm = new LinkedList<Message>();
           for(HashMap.Entry<Integer, Message> entry : messages.entrySet()){
               if(entry.getValue().isOeffentlich()){
                   lm.add(entry.getValue());
               }
           }
           return lm;
        }
        
        public Message getMessageByID(int messageID){
        	return messages.get(messageID);
        }
        public int getUserByMessage(int messageID) throws TafelException{
        	int user = 0;
        	for (Integer userID: userMsgs.keySet()){
        		for (Integer message: userMsgs.get(userID)){
        			if(message == messageID){
        				user = userID;
        				break;
        			}
        		}
        	}
        	if(user == 0)
        		throw new TafelException("Kein User zu Message "+messageID+" gefunden!");
        	return user;
        }
        
        public void updateState(){
            setChanged();
            notifyObservers();
        }
}
