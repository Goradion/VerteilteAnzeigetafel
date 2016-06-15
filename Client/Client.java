/**
 *Projekt Anzeigetafel
 * @author:  Michael Moser
 * @author: Andrea Caruana 
 * @author: Diego Rodriguez Castellanos
 * @author:Viktor Semenitsch
 * @author:Simon Bastian
 * @author:Alex Müller
 *Datei: Client
 * Client zur Kommunikation zwischen Benutzer und Anzeigetafel
 */
package Client;

import java.net.*;
import java.io.*;

public class Client {
    
    public static final int ENDE =0;
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    public static final int SERVER_PORT = 10001;
    public static final String SERVER_HOSTNAME = "localhost";
    
    private String benutzerName;
    private int userID;
    private int abtNr;//Abteilungsnummer
    private String message;
    private int messageID;
//    static private int port = 50000; //festgelegter Port(frei)
    private String ip;
   
    private boolean administrator;
    private boolean removeMsg;
    private boolean modifyMsg;
    private boolean newMessage;
   
    /**
     * Erstellung eines neuen Sockets;
     */
   // private static Socket socket;
    

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
 /*   
    public int getport()
    {
        return port;
    }
    
    public String getip()
    {
        return ip;
    }
*/
    /**
     * Methode zum senden der Nachricht
     * Die Methode ist nur für das senden der Nachricht und das abfangen der 
     * damit verbundenen Fehlerfälle zuständig
    */
    public static int sendeMessage()
    {
       try
       {
           boolean nachricht;
           Socket socket = new Socket (SERVER_HOSTNAME, SERVER_PORT);
           System.out.println ("Verbunden mit Server: " + socket.getRemoteSocketAddress());
           nachricht = neueNachricht();
           if(nachricht == true)
           {
        	   System.out.printf("Nachricht wurde erfolgreich gesendet");
           }
           else
           {
        	   //TODO // andere Funktion benutzen um nochmal versuchen zu versenden
           }
     //      socket.getOutputStream().write ();
           socket.close(); 
       } 
       catch (UnknownHostException e)
       {
       // Wenn Rechnername nicht bekannt ist.
    	   System.out.println ("Rechnername unbekannt:\n" +  e.getMessage());
       }
       catch (IOException e)
       {
         // Wenn die Kommunikation fehlschlägt
    	   System.out.println ("Fehler während der Kommunikation:\n" + e.getMessage());
       }
  
       return TRUE;
    }
    /**
     *Methode zu Erstellung einer neuen Nachricht 
     * Diese Methode wird nur für das Erstellen der Nachricht verwendet
     * 
    */
    public static boolean neueNachricht()
    {
       int senden= -1;
       if(senden == 1)
       {
    	   return true;
       }
       else
       {
    	   return false;
       }
    	   
    }
    static public int hauptschleife()
    {
        while(true)
        {
           try
           {
               
               boolean neueNachricht = false;
               neueNachricht = neueNachricht();
               if(neueNachricht == true)
               {
            	   System.out.println("Nachricht wurde erfolgreich gesendet\n");
               }
               else
               {
            	   System.out.println("Nachricht konnte nicht gesendet werden");
               }
           }
           catch(Exception exception)
           {
              System.out.printf("Client Fehler");
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