/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AC.Client;
import java.io.Serializable;

/**
 *
 * @author andrea
 */
public class SMessage implements Serializable {

    String benutzerName;
    int userID;
    int abtNr;//Abteilungsnummer
    String message;

    boolean administrator;
    boolean removeMsg;
    boolean modifyMsg;
    boolean newMessage;
    int msgid;
    String datum;
   // int anzahl_Objekte;
    
    
    public String getUser() {
        return this.benutzerName;
    }

    public String getDatum() {
        return this.datum;
    }

    public void setDatum(String s) {
        this.datum = s;
    }
    
 /*   public int getAnzahlObjekte() {
        return this.anzahl_Objekte;
    }

    public void setAnzahlObjekte(int a) {
        this.anzahl_Objekte = a;
    } */

    public int ermittleLastID(SMessage ID[], int anzahl) {
        int i = 0;
        int tmp;
        tmp = ID[0].getMsgID();

        for (i = 0; i < anzahl; i++) {
           
            if (ID[i].getMsgID() > tmp) {
                tmp = ID[i].getMsgID();
            //    System.out.println(tmp);
            }
        }

        return tmp;
    }

    public boolean getAdmin() {
        return this.administrator;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean getMod() {
        return this.modifyMsg;
    }

    public boolean getNewMessage() {
        return this.newMessage;
    }

    public boolean getRemoveMsg() {
        return this.removeMsg;
    }

    public int getMsgID() {
        return this.msgid;
    }

    public void setMsgID(int i) {
        this.msgid = i + 1;
    }

    public void setUser(String s) {
        this.benutzerName = s;
    }

    public void setAdmin(String s) {
        this.administrator = Boolean.parseBoolean(s);
    }

    public void setMessage(String txt) {
        this.message = txt;
    }

    public void setMessageIDRead(String txt) {
        this.msgid = Integer.parseInt(txt);
    }

    public void setModi(String txt) {
        this.modifyMsg = Boolean.parseBoolean(txt);
    }

    public SMessage(String user, boolean administrator, String message, boolean modifyMsg, boolean newMessage, int msgid) { //Sollte normalerweise SMessage heißen
        //Sendet eine neue Nachricht
        this.benutzerName = user;
        this.administrator = administrator;
        this.message = message;
        this.modifyMsg = modifyMsg;
        this.newMessage = newMessage;
        this.msgid = msgid;

        // System.out.println(message);
    }

    public SMessage(String user, boolean administrator, boolean removeMsg, int msgid) {
        // Welches NachrichtenObjekt soll gelöscht werden
        this.benutzerName = user;
        this.administrator = administrator;
        this.removeMsg = removeMsg;
        this.msgid = msgid;
    }

}
