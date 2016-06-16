/**
 *Projekt Anzeigetafel
 * @author:  Michael Moser
 * @author: Andrea Caruana 
 * @author: Diego Rodriguez Castellanos
 * @author:Viktor Semenitsch
 * @author:Simon Bastian
 * @author:Alex Müller
 * Datei: Client
 * Client zur Kommunikation zwischen Benutzer und Anzeigetafel
 */
package Client;

import java.net.*;
import java.util.Scanner;

import VerteilteAnzeigetafel.ServerRequest;
import VerteilteAnzeigetafel.ServerRequestType;

import java.io.*;

public class Client implements Serializable{
    
    
	private static final long serialVersionUID = -1466790708777017802L;
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
    private String datum;
//    static private int port = 50000; //festgelegter Port(frei)
//    private String ip;
   
    private boolean administrator =false;
    private boolean removeMsg =false;
    private boolean modifyMsg = false;
    private boolean newMessage =false;
   
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
    public void  setBenutzerName(String benutzerName)
    {
    	this.benutzerName = benutzerName;
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
    public static boolean sendeMessage()
    {
       try
       {
           
           // Eröffnen eines neuen Sockets um die Nachricht zu übermitteln
           Socket socket = new Socket (SERVER_HOSTNAME, SERVER_PORT);
           System.out.println ("Verbunden mit Server: " + socket.getRemoteSocketAddress());
           
           // Erstellen einer Nachricht 
           Scanner sc = new Scanner(System.in);
           System.out.println("Geben sie ihre Nachricht ein.\n");
           String message = sc.next();
           
           // Senden der Nachricht über einen Stream
           ServerRequest sr = new ServerRequest(ServerRequestType.CREATE, 0, message , 3, 1);
           
           // Bauen eines Objektes 
           ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
           System.out.println("Sende Objekt...");
           oout.writeObject(sr);
           System.out.println("Objekt gesendet");
           oout.close();
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
  
       return true;
    }
    
    /**
     *Methode zu Erstellung einer neuen Nachricht 
     * Diese Methode wird nur für das Erstellen der Nachricht verwendet
     * 
    */
    
/*    public static boolean neueNachricht()
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
    	   
    }*/
    public void removemsg(String benutzerName, boolean administrator, boolean removeMsg, int messageID) {
        // Welches NachrichtenObjekt soll gelÃ¶scht werden
        this.benutzerName = benutzerName;
        this.administrator = administrator;
        this.removeMsg = removeMsg;
        this.messageID= messageID;
    }
    public void changeMessage(int messageID)
    {
    	//TODO	
    }
    static public int hauptschleife()
    {
        while(true)
        {
           try
           {
               
               boolean neueNachricht = false;
               neueNachricht = sendeMessage();
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
       
        hauptschleife();
        System.out.println("Client geschlossen");
    }
}