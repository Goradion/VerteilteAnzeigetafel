package VerteilteAnzeigetafel;
//vsemenishch, Git funktioniert!
import java.util.AbstractSet;
import java.util.ArrayList;

public class Anzeigetafel {
    private int abteilungsID;
    private int messageAnzahl;
    private int koordinatorID;
    private ArrayList localMessages;
    private ArrayList globalMessages;
    private AbstractSet userIDs;
    
    public boolean isUser(){
        return false;
    }
    
    public boolean isCoordinator(){
        return false;
    }
    
    public void modifyMessage(int messageID, String inhalt){
        
    }
    
    public void deleteMessage(int messageID){
        
    }
    
    public void createMessage(){
        
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

    public AbstractSet getUserIDs() {
        return userIDs;
    }
    
}
