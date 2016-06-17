/**
 *Projekt Anzeigetafel
 *
 * @author: Michael Moser
 * @author: Andrea Caruana
 * @author: Diego Rodriguez Castellanos
 * @author: Viktor Semenitsch
 * @author: Simon Bastian
 * @author: Alexander Mueller 
 * Datei: Client Client zur Kommunikation zwischen
 * Benutzer und Anzeigetafel
 */
package Client;

import java.net.*;
import java.util.Scanner;

import VerteilteAnzeigetafel.ServerRequest;
import VerteilteAnzeigetafel.ServerRequestType;

import java.io.*;

public class Client implements Serializable {

    private static final long serialVersionUID = -1466790708777017802L;
    public static final int ENDE = 0;
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

    private boolean administrator = false;
    private boolean removeMsg = false;
    private boolean modifyMsg = false;
    private boolean newMessage = false;

    /**
     * Konstruktor zum Erstellung des Benutzers
     *
     * @param benutzerName
     * @param abtNr
     * @param administrator
     * @param userID
     */
    public Client(String benutzerName, int userID, int abtNr, boolean administrator) {
        this.benutzerName = benutzerName;
        this.userID = userID;
        this.abtNr = abtNr;
        this.administrator = administrator;

    }

    public String getbenutzername() {
        return benutzerName;
    }

    public int getabtNr() {
        return abtNr;
    }

    public int getUserID() {
        return userID;
    }

    public void setBenutzerName(String benutzerName) {
        this.benutzerName = benutzerName;
    }

    /*
     * Methode zum senden der Nachricht Die Methode ist nur fuer das senden der
     * Nachricht und das abfangen der damit verbundenen Fehlerfuelle zustaendig
     */
    public static boolean sendeMessage(String name, int abteilung, int userID) {
        try {
            // Erstellen einer Nachricht 
            Scanner ms = new Scanner(System.in);
            System.out.println("Geben sie ihre Nachricht ein.");
            String message = ms.nextLine();

            // Eroeffnen eines neuen Sockets um die Nachricht zu uebermitteln
            Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            System.out.println("Verbunden mit Server: " + socket.getRemoteSocketAddress());

            // Senden der Nachricht ueber einen Stream
            ServerRequest sr = ServerRequest.buildCreateRequest(ServerRequestType.CREATE, message, userID, abteilung);

            // Bauen eines Objektes 
            ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sende Objekt...");
            oout.writeObject(sr);
            System.out.println("Objekt gesendet");
            oout.close();
            ms.close();
            socket.close();
        } catch (UnknownHostException e) {
            // Wenn Rechnername nicht bekannt ist.
            System.out.println("Rechnername unbekannt:\n" + e.getMessage());
        } catch (IOException e) {
            // Wenn die Kommunikation fehlschlaegt
            System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
        }

        return true;

    }

    public static void removemsg(String benutzerName, int abteilungNr, int userIdClient){
        try {
            Scanner messegID = new Scanner(System.in);
            System.out.println("Geben Sie messegId ein: ");
            int nachrichtId = messegID.nextInt();

            Socket socketServer = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            System.out.println("Verbunden mit Server: " + socketServer.getRemoteSocketAddress());
            ServerRequest serverR = ServerRequest.buildDeleteRequest(ServerRequestType.DELETE, nachrichtId, userIdClient);
            ObjectOutputStream oout = new ObjectOutputStream(socketServer.getOutputStream());
            System.out.println("Sende Objekt...");
            oout.writeObject(serverR);
            System.out.println("Objekt gesendet");
            oout.close();
            messegID.close();
            socketServer.close();

        } catch (UnknownHostException e) {
            // Wenn Rechnername nicht bekannt ist.
            System.out.println("Rechnername unbekannt:\n" + e.getMessage());
        } catch (IOException e) {
            // Wenn die Kommunikation fehlschlaegt
            System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
        }
    }
    
    
    public static void changeMessage(int userID)
    {
        try
        {
        	Scanner newMesseg = new Scanner(System.in);
        	Scanner messegID = new Scanner(System.in);
            System.out.println("Geben Sie messegId ein,die sie aendern wollen: ");
            int nachrichtId = messegID.nextInt();
            
            Socket socketServer = new Socket (SERVER_HOSTNAME, SERVER_PORT);
            System.out.println ("Verbunden mit Server: " + socketServer.getRemoteSocketAddress());
            System.out.println("Geben Sie die geaenderte nachrich: ");
            String neueNachricht = newMesseg.nextLine(); 
            ServerRequest serverR = ServerRequest.buildModifyRequest(ServerRequestType.MODIFY, nachrichtId,neueNachricht,userID);
            ObjectOutputStream oout = new ObjectOutputStream(socketServer.getOutputStream());
            System.out.println("Sende Objekt...");
            oout.writeObject(serverR);
            System.out.println("Objekt gesendet");
            
            oout.close();
            messegID.close();
            newMesseg.close();
            socketServer.close();
            
         }
        catch (UnknownHostException e)
        {
        // Wenn Rechnername nicht bekannt ist.
     	   System.out.println ("Rechnername unbekannt:\n" +  e.getMessage());
        }
        catch (IOException e)
        {
          // Wenn die Kommunikation fehlschlaegt
     	   System.out.println ("Fehler waehrend der Kommunikation:\n" + e.getMessage());
        }
    }
    
    
    static public void meineNachricht(int userId){
    	try{
    		 Socket socketServer = new Socket (SERVER_HOSTNAME, SERVER_PORT);
    		 System.out.println ("Verbunden mit Server: " + socketServer.getRemoteSocketAddress());
    		 ServerRequest serverR = ServerRequest.buildShowMyMessagesRequest(ServerRequestType.SHOW_MY_MESSAGES, userId);
             ObjectOutputStream oout = new ObjectOutputStream(socketServer.getOutputStream());
             System.out.println("Sende Objekt...");
             oout.writeObject(serverR);
             System.out.println("Objekt gesendet");
             oout.close();
    	}
        catch (UnknownHostException e)
        {
        // Wenn Rechnername nicht bekannt ist.
     	   System.out.println ("Rechnername unbekannt:\n" +  e.getMessage());
        }
        catch (IOException e)
        {
          // Wenn die Kommunikation fehlschlaegt
     	   System.out.println ("Fehler waehrend der Kommunikation:\n" + e.getMessage());
        }
    	
    	
    }

    static public void hauptschleife() {
        int option;

        try {
            Scanner sc = new Scanner(System.in);

            System.out.println("Geben Sie ihren Namen ein: ");
            String name = sc.nextLine();
            //              System.out.println("\n");

            System.out.println("Geben Sie Ihre Abteilungsnumer ein: ");
            int abteilung = sc.nextInt();
            //              System.out.println("\n");

            System.out.println("Geben Sie ihre userID ein :");
            int userID = sc.nextInt();
            boolean inMenu = true;
            while (inMenu == true) {
                option = auswahl();
                inMenu = menuTafel(option, name, abteilung, userID);

            }
            sc.close();
        } catch (Exception exception) {
            exception.printStackTrace();

        }

    }

    public static int auswahl() throws IOException {
        int genommen;
        BufferedReader eingabe = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("*****anzeigetafel*******\n");
        System.out.println("(1) Neuer Nachricht \n(2)Nachricht ENTFERNEN \n (3) aendern \n (4) senden\n (5) ende\n");
        String wahl = eingabe.readLine();
        genommen = Integer.parseInt(wahl);
        return genommen;
    }

    public static boolean menuTafel(int option,String name,int abteilung, int userID)throws IOException{
		
    	switch (option){
    	case 1:
    		sendeMessage(name,abteilung, userID);
    		return true;
     	case 2:
    		removemsg(name,abteilung, userID);
    		return true;
    	case 3: 
    		changeMessage(userID);
    		return true;
    	case 4:
    		meineNachricht(userID);
    		return true;
    	case 5:
    		System.out.println("EXIT! \n");
    		return false;
    	default:
    		System.out.println("falsche eingabe! \n");
    		return true;
    	
    	}
    	
    }  

    public static void main(String[] args) {
        hauptschleife();

    }
}
