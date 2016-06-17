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
     * @param benutzerName	
     * @param abtNr
     * @param administrator
     * @param userID
     */
    public Client(String benutzerName, int userID ,int abtNr)
    {
        this.benutzerName = benutzerName;
        this.userID = userID;
        this.abtNr = abtNr;
        
        
    }
    
    public String getbenutzername()
    {
        return benutzerName;
    }
    
    public int getabtNr()
    {
        return abtNr;
    }
    public int getUserID()
    {
        return userID;
    }
    public void  setBenutzerName(String benutzerName)
    {
    	this.benutzerName = benutzerName;
    }



    /**
     * Methode zum senden der Nachricht
     * Die Methode ist nur für das senden der Nachricht und das abfangen der 
     * damit verbundenen Fehlerfälle zuständig
    */
    public static boolean sendMessage(String name,int abteilung, int userID)
    {
       try
       {
    	// Erstellen einer Nachricht 
           Scanner ms = new Scanner(System.in);
           System.out.println("Geben sie ihre Nachricht ein.");
           String message = ms.nextLine();
           ms.close();
           
           // Eröffnen eines neuen Sockets um die Nachricht zu übermitteln
           Socket socket = new Socket (SERVER_HOSTNAME, SERVER_PORT);
           System.out.println ("Verbunden mit Server: " + socket.getRemoteSocketAddress());
           
           // 
           ServerRequest sr = ServerRequest.buildCreateRequest(ServerRequestType.CREATE, message, userID, abteilung);
           
           
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
   
    public void removemsg(int messageID)
    {
        //TODO
    }
    public static void modifyMessage(int messageID,int userID) throws IOException
    {
/*    	Socket socket = new Socket (SERVER_HOSTNAME, SERVER_PORT);
        System.out.println ("Verbunden mit Server: " + socket.getRemoteSocketAddress());
        
 //   	ServerRequest sr = ServerRequest.buildModifyRequest(ServerRequestType.MODIFY,messageID,  userID, message);
    	// Bauen eines Objektes 
        ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Sende Objekt...");
        oout.writeObject(sr);
        System.out.println("Objekt gesendet");
        oout.close();
    	socket.close();
 */   			 
    }			 
    public void oeffentlich(int messageID)
    {
    	//TODO
    }
    static public int hauptschleife()
    {
        while(true)
        {
           try
           {
        	   Scanner sc = new Scanner(System.in);
               
         	   System.out.println("Geben Sie ihren Namen ein: ");
               String name = sc.nextLine();
         	  
               System.out.println("Geben Sie Ihre Abteilungsnumer ein: ");
               int abteilung = sc.nextInt();

         	   	System.out.println("Geben Sie ihre userID ein :");
               int userID = sc.nextInt();

               boolean neueNachricht = false;
               neueNachricht = sendMessage( name, abteilung,userID);
               sc.close();
               /*               if(neueNachricht == true)
               {
            	   System.out.println("Nachricht wurde erfolgreich gesendet\n");
               }
               else
               {
            	   System.out.println("Nachricht konnte nicht gesendet werden");
               }*/
           }
           catch(Exception exception)
           {
        	   exception.printStackTrace();
             
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