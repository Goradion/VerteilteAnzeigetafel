/**
 *Projekt Anzeigetafel
 * @author  Michael Moser, Andrea Caruana, Diego Rodriguez Castellanos,
 * Alex Müller,Viktor Semenitsch, Simon Bastian??
 *Datei: Client
 * Client zur Kommunikation zwischen Benutzer und Anzeigetafel
 */
package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

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
   
    private boolean administrator;
    private boolean removeMsg;
    private boolean modifyMsg;
    private boolean newMessage;
    /**
     * Erstellung eines neuen Sockets;
     */
    private static Socket socket;
    

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

    public static int sendeMessage()
    {
        
        
       return TRUE;
    }
    public static int neueNachricht(int port,String adresse)
    {
        
        socket = new Socket(int port, String adresse);
        int senden;
        senden =sendeMessage();
        if(senden==1)
        {
            System.out.printf("Nachricht Erfolgreich versenden");
        }
        else
        {
           System.out.printf("Nachricht konnte nicht gesendet werden");   
        }
    }
    static public int hauptschleife()
    {
        while(true)
        {
           try
           {
               String host ="localhost";
               InetAddress adresse= InetAddress.getByName(host);
               int neueNachricht = -1;
               neueNachricht=neueNachricht(port,adresse);
               /*          
               switch()
              {
                   case 1:
              }*/
              
           }
           catch(Exception exception)
           {
              System.out.printf("Client Fehler");
           }
           finally
           {
               try
               {
                   socket.close();
               }
               catch(Exception e)
               {
                   System.out.printf("Fehler beim Schließen des Sockets");
               }
           }
            break;    
        }
        return ENDE;
    }
    
    
    
    public static void main(String[] args) {
       
        int ende ;
        ende = hauptschleife();
        System.out.println("Client geschlossen");
    }
}
