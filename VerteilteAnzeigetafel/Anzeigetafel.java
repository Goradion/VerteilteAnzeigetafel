package VerteilteAnzeigetafel;
//vsemenishch, Git funktioniert!

import java.util.ArrayList;
import java.util.TreeSet;

public class Anzeigetafel {
    private static String lastID;
    private int abteilungsID;
    private int messageAnzahl;
    private int koordinatorID;
    private ArrayList localMessages;
    private ArrayList globalMessages;
    private TreeSet userIDs;
    
    public Anzeigetafel(){
        /* welche Nummern sollen die einzelnen Anzeigetafeln bekommen?*/
        this.abteilungsID=1; // hier muss man fuer jedes kompilierte Programm
                                // eine neue Nummer festlegen
        this.koordinatorID=1;
        this.messageAnzahl=0;
        Anzeigetafel.lastID = null;
        this.localMessages = new ArrayList<>();
        this.globalMessages = new ArrayList<>();
        this.userIDs = new TreeSet<>();
        /* Füge 5 users ein die zu dieser Anzeigetafel gehören*/
        for(int i = 1; i == 5;i++){
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
        for (int i=0;i<localMessages.size();i++){
            Message msg = (Message)localMessages.get(i);
            if(msg.getMessageID()==messageID){
                //TODO code zum Modifizieren
            }
        }
    }

    public void deleteMessage(int messageID) {
        /* Entferne die Message wenn vorhanden aus localMessages 
            und globalMessages */
        for (int i=0;i<localMessages.size();i++){
            Message msg = (Message)localMessages.get(i);
            if(msg.getMessageID()==messageID){
                localMessages.remove(i);
            }
        }
        for (int i=0;i<globalMessages.size();i++){
            Message msg = (Message)globalMessages.get(i);
            if(msg.getMessageID()==messageID){
                globalMessages.remove(i);
            }
        }
    }
    
    public int createMessage(Message msg) {
        int msgID = Integer.parseInt(getNewMsgID(msg.getUserID()));
        Message nMsg = new Message(msg,msgID);
        localMessages.add(nMsg);
        if(nMsg.isOeffentlich()){
            globalMessages.add(nMsg);
        }
        return msgID;
    }
    
    private String getNewMsgID(int userID){
        Anzeigetafel.lastID = ""+userID+abteilungsID+System.currentTimeMillis()%1000;
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

    public ArrayList getLocalMessages() {
        return localMessages;
    }

    public ArrayList getGlobalMessages() {
        return globalMessages;
    }

    public TreeSet getUserIDs() {
        return userIDs;
    }

}
