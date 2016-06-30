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
package client;

import java.net.*;
import java.util.LinkedList;
import java.util.Scanner;

import serverRequests.ServerRequest;
import verteilteAnzeigetafel.Message;

import java.io.*;


public class Client implements Serializable {

    private static final long serialVersionUID = -1466790708777017802L;
    public static final int ENDE = 0;
    public static final int SERVER_PORT = 10001;
    public static final String SERVER_HOSTNAME = "localhost";

    private String benutzerName;
    private int userID;
    private int abtNr;//Abteilungsnummer

//    static private int port = 50000; //festgelegter Port(frei)
//    private String ip;

    /**
     * Konstruktor zum Erstellung des Benutzers
     *
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


    public String getbenutzername() {
        return benutzerName;
    }

    public int getabtNr() {
        return abtNr;
    }

    public int getUserID() {
        return userID;
    }

    public void  setBenutzerName(String benutzerName)
    {
    	this.benutzerName = benutzerName;
    }

    /*
     * Methode zum senden der Nachricht Die Methode ist nur fuer das senden der
     * Nachricht und das abfangen der damit verbundenen Fehlerfuelle zustaendig
     */
    public static void sendeMessage(String name, int abteilung, int userID) {
        try {
            // Erstellen einer Nachricht 
            //Scanner ms = new Scanner(System.in);
        	BufferedReader eingabe = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Geben sie ihre Nachricht ein.");
            String message = eingabe.readLine();
            

            
            // Eroeffnen eines neuen Sockets um die Nachricht zu uebermitteln
            Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            System.out.println("Verbunden mit Server: " + socket.getRemoteSocketAddress());
            boolean oeffentlich =true;
            // Senden der Nachricht ueber einen Stream
            ServerRequest sr = ServerRequest.buildCreateRequest(message, userID, abteilung);

            // Bauen eines Objektes 
            ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
           
            oout.writeObject(sr);
            oout.writeObject(null);
            oout.close();
            //ms.close();
            socket.close();
        } catch (UnknownHostException e) {
            // Wenn Rechnername nicht bekannt ist.
            System.out.println("Rechnername unbekannt:\n" + e.getMessage());
        } catch (IOException e) {
            // Wenn die Kommunikation fehlschlaegt
            System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());

        }

        

    }

    public static void removemsg(String benutzerName, int abteilungNr, int userIdClient){
        try {
        	
        	BufferedReader messegID = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Geben Sie messegId ein: ");
            String msgId = messegID.readLine();
            int nachrichtId = Integer.parseInt(msgId);

            Socket socketServer = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            System.out.println("Verbunden mit Server: " + socketServer.getRemoteSocketAddress());
            ServerRequest serverR = ServerRequest.buildDeleteRequest(nachrichtId, userIdClient);
            ObjectOutputStream oout = new ObjectOutputStream(socketServer.getOutputStream());
        
            oout.writeObject(serverR);
            
            oout.close();
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
        	BufferedReader newMesseg = new BufferedReader(new InputStreamReader(System.in));
        	BufferedReader messegID = new BufferedReader(new InputStreamReader(System.in));
        	System.out.println("Geben Sie messegId ein,die sie aendern wollen: ");
            String stringId = messegID.readLine();
            int nachrichtId = Integer.parseInt(stringId);
            
            
            Socket socketServer = new Socket (SERVER_HOSTNAME, SERVER_PORT);
            System.out.println ("Verbunden mit Server: " + socketServer.getRemoteSocketAddress());
            System.out.println("Geben Sie die geaenderte nachrich: ");
            String neueNachricht = newMesseg.readLine(); 
            ServerRequest serverR = ServerRequest.buildModifyRequest(nachrichtId,neueNachricht,userID);
            ObjectOutputStream oout = new ObjectOutputStream(socketServer.getOutputStream());
        
            oout.writeObject(serverR);
            System.out.println("id" + nachrichtId + "nachricht:" + neueNachricht + "user"+userID);
            
            oout.close();
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
    
    
    static public void showMsg(int userId) {
    	
    	try{
    		
    		 Socket socketServer = new Socket (SERVER_HOSTNAME, SERVER_PORT);
    		 System.out.println ("Verbunden mit Server: " + socketServer.getRemoteSocketAddress());
    		 ServerRequest serverR = ServerRequest.buildShowMyMessagesRequest(userId);
             ObjectOutputStream oout = new ObjectOutputStream(socketServer.getOutputStream());
             System.out.println("Sende Objekt...");
             oout.writeObject(serverR);
             System.out.println("Objekt gesendet");

             
 		
    			// Empfangen der Nachricht 
             //TODO antwort ist jetzt ein String
 			 ObjectInputStream input = new ObjectInputStream(socketServer.getInputStream());
			 System.out.println(input.readObject());
             /*           for(Message m : userMessages){
                            System.out.println(m.toString());
                        }*/
                        oout.close();
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
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    }
    static public void publicMsg ( int userId ){
    	try{
        	BufferedReader messegID = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Geben Sie messegId ein: ");
            String msgId = messegID.readLine();
            int messegId = Integer.parseInt(msgId);
    		Socket socketServer = new Socket (SERVER_HOSTNAME, SERVER_PORT);
   		    System.out.println ("Verbunden mit Server: " + socketServer.getRemoteSocketAddress());
   		    ServerRequest serverR = ServerRequest.buildPublishRequest(messegId, userId);
            ObjectOutputStream oout = new ObjectOutputStream(socketServer.getOutputStream());
          
            oout.writeObject(serverR);
           
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
    static public void hauptschleife() {
        int option;

        try {
            Scanner sc = new Scanner(System.in);

            System.out.println("Geben Sie ihren Namen ein: ");
            String name = sc.nextLine();
            //              System.out.println("\n");

            System.out.println("Geben Sie Ihre Abteilungsnummer ein: ");
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
        int auswahl ;
        BufferedReader eingabe = new BufferedReader(new InputStreamReader(System.in));
    	System.out.println("*****anzeigetafel*******\n");
        System.out.println("(1) Neue Nachricht \n"
        		+ "(2)Nachricht ENTFERNEN \n "
        		+ "(3) Nachricht aendern \n "
        		+ "(4)Nachrichten ansehen\n "
        		+ "(5)Nachricht publizieren\n "
        		+ "(6) Nachricht Ver�ffentlichen\n"
        		+ " (7) ende\n");
        String wahl = eingabe.readLine();
        auswahl = Integer.parseInt(wahl);
        return auswahl;
    }

    
    public static boolean menuTafel(int option,String name,int abteilung, int userID)throws IOException{
		
    	switch (option){
    	case 1:
    		sendeMessage(name,abteilung, userID);
    		return true;
     	case 2:
     		showMsg(userID);
    		removemsg(name,abteilung, userID);
    		return true;
    	case 3: 
    		showMsg(userID);
    		changeMessage(userID);
    		return true;
    	case 4:
    		showMsg(userID);
    		return true;
    	case 5:
    		showMsg(userID);
    		publicMsg (userID );
    		return true;
    	case 6:
			showMsg(userID);
			publicMsg(userID);
			return true;
    	case 7:
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
