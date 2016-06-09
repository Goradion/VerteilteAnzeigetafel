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


import java.net.*;
import java.io.*;

public class Client2 {
    
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
    static private int port = 50000; //festgelegter Port(frei)
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
     * @return 
     */
    public  Client2(String user,int abtNr,boolean administrator,String message)
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
           String nachricht;
           Socket socket = new Socket (SERVER_HOSTNAME, SERVER_PORT);
           System.out.println ("Verbunden mit Server: " + socket.getRemoteSocketAddress());
           nachricht = neueNachricht();
     //      socket.getOutputStream().write ();
            System.out.printf("Nachricht wurde erfolgreich gesendet");
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
        finally
        {
           try
               {
                  // socket.close();
               }
               catch(Exception e)
               {
                   System.out.printf("Fehler beim Schließen des Sockets");
               }
       }
        
       return TRUE;
    }
    /**
     *Methode zu Erstellung einer neuen Nachricht 
     * Diese Methode wird nur für das Erstellen der Nachricht verwendet
     * 
    */
    public static String neueNachricht()
    {
       String nachricht ="";
       
       return nachricht;
    }
    static public int hauptschleife()
    {
        while(true)
        {
           try
           {
               
               int neueNachricht = -1;
              // neueNachricht = neueNachricht();
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
