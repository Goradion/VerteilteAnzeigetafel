package VerteilteAnzeigetafel;
//vsemenishch, Git funktioniert!

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;

public class Anzeigetafel implements Serializable {
    private static final String TAFELNAME = "tafel";
    private String lastID;
    private final int abteilungsID;
    private int messageAnzahl;
    private int msgLaufNr;
    private final int koordinatorID;
    private LinkedHashMap<Integer, Message> messages;
    private HashSet<Integer> userIDs;
    private HashMap<Integer, LinkedList<Integer>> userMsgs;

    public Anzeigetafel() {
        /* welche Nummern sollen die einzelnen Anzeigetafeln bekommen?*/
        this.abteilungsID = 1; // hier muss man fuer jedes kompilierte Programm
        // eine neue Nummer festlegen
        this.koordinatorID = 1;
        this.messageAnzahl = 0;
        lastID = null;
        this.messages = new LinkedHashMap<Integer,Message>();
        this.userMsgs = new HashMap<Integer,LinkedList<Integer>>();
        this.userIDs = new HashSet<Integer>();
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

        if (messages.containsKey(messageID)) {
            /* Autor oder Koordinator ?*/
            if (user == messages.get(messageID).getUserID() || isCoordinator(user)) {
                Message msg = new Message(inhalt, messages.get(messageID).getUserID(),
                        messages.get(messageID).getAbtNr(), messages.get(messageID).isOeffentlich());
                messages.replace(messageID, msg);
            } else {
                throw new TafelException(user + " nicht berechtigt zum Modifizieren");
            }
        } else {
            throw new TafelException("Keine Message mit ID " + messageID + " gefunden!");
        }

    }

    public void publishMessage(int messageID, int user) throws TafelException {
        if (!messages.containsKey(messageID)) {
            throw new TafelException("Keine Message mit ID " + messageID + " gefunden!");
        }
        if (isCoordinator(user)) {
            messages.get(messageID).setOeffentlich();
        } else {
            throw new TafelException("Keine Berechtigung zum Publizieren!");
        }
    }

    public void deleteMessage(int messageID, int user) throws TafelException {
        
        if (messages.containsKey(messageID)) {
            /* Autor oder Koordinator ?*/
            if (user == messages.get(messageID).getUserID() || isCoordinator(user)) {
                messages.remove(messageID);
                userMsgs.get(user).remove(messageID);
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
        messages.put(nMsg.getMessageID(), nMsg);
        /* noch kein user da*/
        if(!userMsgs.containsKey(user)){
            userMsgs.put(user, new LinkedList<Integer>());
        }
        userMsgs.get(user).add(msgID);
        messageAnzahl++;
        msgLaufNr++;
        return msgID;
    }

    private String getNewMsgID(int userID) {
        lastID = "" + abteilungsID + userID + msgLaufNr;
        return lastID;
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
        return messages;
    }

//    public HashMap getGlobalMessages() {
//        return globalMessages;
//    }
    public HashSet getUserIDs() {
        return userIDs;
    }

    public String getLastID() {
        return lastID;
    }
    /**
     * Ermöglicht das Speichern des aktuellen Zustandes der Anzeigetafel
     * in eine Datei.
     */
    public void saveStateToFile() {
        FileOutputStream fileoutput = null;
        ObjectOutputStream objoutput = null;
        try {
            fileoutput = new FileOutputStream("./"+TAFELNAME);
            objoutput = new ObjectOutputStream(fileoutput);
            objoutput.writeObject(this);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Anzeigetafel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Anzeigetafel.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                objoutput.close();
            } catch (IOException ex) {
                Logger.getLogger(Anzeigetafel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * Ermöglicht das Laden des Zustandes aus einer Datei.
     * @return 
     * @throws VerteilteAnzeigetafel.TafelException
     */
    public static Anzeigetafel loadStateFromFile() throws TafelException{
        if(!Files.exists(FileSystems.getDefault().getPath("./", TAFELNAME))){
           throw new TafelException("Could not load state from file.");
        }
        Anzeigetafel at = null;
        FileInputStream fileinput = null;
        ObjectInputStream objinput = null;
        try {
            fileinput = new FileInputStream("./"+TAFELNAME);
            objinput = new ObjectInputStream(fileinput);
            at = (Anzeigetafel)objinput.readObject();            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Anzeigetafel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Anzeigetafel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return at;
    }
    
    public LinkedList<Integer> getMessagesByUserID(int userID) throws TafelException{
        if(!userMsgs.containsKey(userID))
            throw new TafelException("Kein User gefunden!");
        return userMsgs.get(userID);
    }
    
   
    @Override
    public String toString(){
        String str = "";
        for(HashMap.Entry<Integer,Message> entry : messages.entrySet()){
            str = str+"User : "+entry.getKey()+" / "+entry.getValue()+"\n";
            
        }
        return str;
    }
    
}
