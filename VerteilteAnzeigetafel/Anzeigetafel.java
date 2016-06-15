package VerteilteAnzeigetafel;
//vsemenishch, Git funktioniert!

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class Anzeigetafel implements Serializable {

    private static String lastID;
    private final int abteilungsID;
    private int messageAnzahl;
    private int msgLaufNr;
    private final int koordinatorID;
    private HashMap<Integer, Message> Messages;
    private HashSet<Integer> userIDs;

    public Anzeigetafel() {
        /* welche Nummern sollen die einzelnen Anzeigetafeln bekommen?*/
        this.abteilungsID = 1; // hier muss man fuer jedes kompilierte Programm
        // eine neue Nummer festlegen
        this.koordinatorID = 1;
        this.messageAnzahl = 0;
        Anzeigetafel.lastID = null;
        this.Messages = new HashMap();

        this.userIDs = new HashSet<>();
        /* Füge 5 users ein, die zu dieser Anzeigetafel gehören*/
        for (int i = 1; i < 6; i++) {
            userIDs.add(i);
        }
    }

    public boolean isUser(int userID) {
        return userIDs.contains(userID);
    }

    public boolean isCoordinator(int userID) {
        return userID == koordinatorID;
    }

    public void modifyMessage(int messageID, String inhalt, int user) throws TafelException {

        if (Messages.containsKey(messageID)) {
            /* Autor oder Koordinator ?*/
            if (user == Messages.get(messageID).getUserID() || isCoordinator(user)) {
                Message msg = new Message(inhalt, Messages.get(messageID).getUserID(),
                        Messages.get(messageID).getAbtNr(), Messages.get(messageID).isOeffentlich());
                Messages.replace(messageID, msg);
            } else {
                throw new TafelException(user + " nicht berechtigt zum Modifizieren");
            }
        } else {
            throw new TafelException("Keine Message mit ID " + messageID + " gefunden!");
        }

    }

    public void publishMessage(int messageID, int user) throws TafelException {
        if (!Messages.containsKey(messageID)) {
            throw new TafelException("Keine Message mit ID " + messageID + " gefunden!");
        }
        if (isCoordinator(user)) {
            Messages.get(messageID).setOeffentlich();
        } else {
            throw new TafelException("Keine Berechtigung zum Publizieren!");
        }
    }

    public void deleteMessage(int messageID, int user) throws TafelException {
        /* Entferne die Message wenn vorhanden aus localMessages 
            und globalMessages */

        if (Messages.containsKey(messageID)) {
            /* Autor oder Koordinator ?*/
            if (user == Messages.get(messageID).getUserID() || isCoordinator(user)) {
                Messages.remove(messageID);
            } else {
                throw new TafelException(user + " nicht berechtigt zum Löschen");
            }
        } else {
            throw new TafelException("Keine Message mit ID " + messageID + " gefunden!");
        }

    }

    public int createMessage(Message msg, int user) throws TafelException {
        if (!userIDs.contains(user)) {
            throw new TafelException("Keine Berechtigung zum Erstellen!");
        }
        int msgID = Integer.parseInt(getNewMsgID(msg.getUserID()));
        Message nMsg = new Message(msg, msgID);
        Messages.put(nMsg.getMessageID(), nMsg);
        messageAnzahl++;
        msgLaufNr++;
        return msgID;
    }

    private String getNewMsgID(int userID) {
        Anzeigetafel.lastID = "" + abteilungsID + userID + msgLaufNr;
        return Anzeigetafel.lastID;
    }

    public int getAbteilungsID() {
        return abteilungsID;
    }

    public int getMessageAnzahl() {
        return messageAnzahl;
    }

    public int getKoordinatorID() {
        return koordinatorID;
    }

    public HashMap getMessages() {
        return Messages;
    }

//    public HashMap getGlobalMessages() {
//        return globalMessages;
//    }
    public HashSet getUserIDs() {
        return userIDs;
    }

    public static String getLastID() {
        return lastID;
    }

}
