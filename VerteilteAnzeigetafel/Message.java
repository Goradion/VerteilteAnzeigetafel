/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VerteilteAnzeigetafel;

import java.io.Serializable; // importiert Libarys
import java.sql.Time;

/**
 * 
 * @author am
 */
public class Message implements Serializable {
    private int messageID;
    private int userID; // 
    private int abtNr; // von welcher Abteilung kommt die Nachricht
    private String inhalt;
    private boolean oeffentlich;
    private Time time;
    
    /**
     Konstruiert ein Message-Objekt mit Hilfe eines anderen Msg-Objekts
     * @param msg 
     * @param messageID
     */
    
    public Message(Message msg, int msgID){
        this.messageID = msgID;
        this.inhalt = msg.getInhalt();
        this.userID = msg.getUserID();
        this.abtNr = msg.getAbtNr();
        this.time = msg.getTime();
        this.oeffentlich = msg.isOeffentlich();
    }
    /* Dieser Konstruktor wird vom Client benutzt um eine Nachricht, die noch
        ohne messageID ist zu erstellen
    */
    public Message(String inhalt, int userID, int abtNr, boolean oeffentlich ){
        this.inhalt = inhalt;
        this.userID = userID;
        this.abtNr = abtNr;
        this.oeffentlich = oeffentlich;
        time = new Time(System.currentTimeMillis());
        this.messageID = Integer.parseInt(""+userID+""+abtNr+""+time.getTime()%1000);
    }
    
    
    
    public String toString(){
        return ""+userID+"  "+abtNr+"   "+"\""+inhalt+"\"";
    }

    public int getAbtNr() {
        return abtNr;
    }
    
    public int getMessageID() {
        return messageID;
    }

    public int getUserID() {
        return userID;
    }

    public String getInhalt() {
        return inhalt;
    }

    public boolean isOeffentlich() {
        return oeffentlich;
    }

    public Time getTime() {
        return time;
    }
    
   
}
