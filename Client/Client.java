
package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
/**
 *
 * @author micha
 */
public class Client {
    
    public static final int ENDE =0;
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    
    private String benutzerName;
    private int userID;
    private int abtNr;//Abteilungsnummer
    private String message;
    private int messageID;
    static private int port = 50000; //festgelegter Port(frei)
    private String ip;
    private static Socket socket;
    private boolean administrator;
    private boolean removeMsg;
    private boolean modifyMsg;
    private boolean newMessage;
  
    /**
     * Konstruktor zum Erstellung des Benutzers
     * @param user
     * @param abtNr
     * @param administrator
     * @param message 
     */
    public Client(String user,int abtNr,boolean administrator,String message)
    {
        this.benutzerName = benutzerName;
        this.abtNr = abtNr;
        this.administrator=administrator;
        this.message =message;
    }
    
    public String getbenutzername()
    {
        return benutzerName;
    }
    
    public int getabtNr()
    {
        return abtNr;
    }
    public String getmessage()
    {
        return message;
    }
    
    public int getport()
    {
        return port;
    }
    
    public String getip()
    {
        return ip;
    }

    public boolean sendeMessage()
    {
        
    }
    static public int hauptschleife()
    {
        while(true)
        {
            
           /* int auswahl=0;
            switch(auswahl)
            {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                
            }*/
            break;    
        }
        return ENDE;
    }
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        int ende ;
        ende = hauptschleife();
        System.out.println("Client geschlossen");
    }
}
