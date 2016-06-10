package VerteilteAnzeigetafel;
//vsemenishch, Git funktioniert!

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class Anzeigetafel implements Serializable{

    private static String lastID;
    private final int abteilungsID;
    private int messageAnzahl;
    private int msgLaufNr;
    private final int koordinatorID;
    private HashMap<Integer,Message> localMessages;
    private HashMap<Integer,Message> globalMessages;
    private HashSet<Integer> userIDs;

    public Anzeigetafel() {
        /* welche Nummern sollen die einzelnen Anzeigetafeln bekommen?*/
        this.abteilungsID = 1; // hier muss man fuer jedes kompilierte Programm
        // eine neue Nummer festlegen
        this.koordinatorID = 1;
        this.messageAnzahl = 0;
        Anzeigetafel.lastID = null;
        this.localMessages = new HashMap();
        this.globalMessages = new HashMap();
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

    public void modifyMessage(int messageID, String inhalt) {

        if (localMessages.containsKey(messageID)){
            Message msg = (Message)localMessages.get(messageID);
            if(msg.getUserID()==koordinatorID /*|| msg.getUserID()==modifier*/){
                Message m = new Message(new Message(inhalt,
                        msg.getUserID(),msg.getAbtNr(),
                        msg.isOeffentlich()),msg.getMessageID());
                localMessages.replace(messageID, m);
            }else {
                // sollen Methoden Rückgabewerte für failures haben??
                // bzw. Berechtigungen prüfen ??
               // return 1;
            }
            
        }

    }

    public void deleteMessage(int messageID) {
        /* Entferne die Message wenn vorhanden aus localMessages 
            und globalMessages */
        if(localMessages.containsKey(messageID)){
            localMessages.remove(messageID);
            if (globalMessages.containsKey(messageID)){
                /* sollen öffentliche Nachrichten gleiche ID haben, wenn sie 
                * auch in lokalen Nachrichten sind? 
                */
                globalMessages.remove(messageID);
            }
            messageAnzahl--;
        }
        
    }

    public int createMessage(Message msg) {
        int msgID = Integer.parseInt(getNewMsgID(msg.getUserID()));
        Message nMsg = new Message(msg, msgID);
        localMessages.put(nMsg.getMessageID(), nMsg);
        if (nMsg.isOeffentlich()) {
            globalMessages.put(nMsg.getMessageID(), nMsg);
        }
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

    public HashMap getLocalMessages() {
        return localMessages;
    }

    public HashMap getGlobalMessages() {
        return globalMessages;
    }

    public HashSet getUserIDs() {
        return userIDs;
    }

    public static String getLastID() {
        return lastID;
    }

}
