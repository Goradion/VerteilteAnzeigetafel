/**
 *Projekt Anzeigetafel
 *
 * @author: Michael Moser
 * @author: Andrea Caruana
 * @author: Diego Rodriguez Castellanos
 * @author: Viktor Semenitsch
 * @author: Simon Bastian
 * @author: Alexander Mueller Datei: Client Client zur Kommunikation zwischen
 * Benutzer und Anzeigetafel
 */
package client;

import java.net.*;
import java.util.Scanner;

import serverRequests.*;
import tafelServer.*;
import java.io.*;

public class Client implements Serializable {

    private final long serialVersionUID = -1466790708777017802L;
    private final int ENDE = 0;
    private final int SERVER_PORT = 10001;
    private String SERVER_HOSTNAME = "localhost";
    private final String ABTEILUNG_1 = "192.168.1.77";
    private final String ABTEILUNG_2 = "192.168.1.78";
    private final String ABTEILUNG_3 = "192.168.1.79";
    private ClientWindow mainWindow;
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
    public Client() {
        this.userID = 0;
        this.abtNr = 0;
        this.mainWindow = new ClientWindow("Client", this);
        mainWindow.setResizable(false);
    }

    public void startClient() {
        mainWindow.run();
    }

    private void setAbteilung(int abt) {
        switch (abt) {
            case 1:
                SERVER_HOSTNAME = ABTEILUNG_1;
                break;
            case 2:
                SERVER_HOSTNAME = ABTEILUNG_2;
                break;
            case 3:
                SERVER_HOSTNAME = ABTEILUNG_3;
                break;
            default:
                break;
        }
    }

    public int getabtNr() {
        return abtNr;
    }

    public void setAbtNr(int abtNr) {
        this.abtNr = abtNr;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    /*
     * Methode zum senden der Nachricht Die Methode ist nur fuer das senden der
     * Nachricht und das abfangen der damit verbundenen Fehlerfuelle zustaendig
     */
    public void sendeMessage(int abteilung, int userID) {
        try {
            // Erstellen einer Nachricht 
            //Scanner ms = new Scanner(System.in);
            BufferedReader eingabe = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Geben sie ihre Nachricht ein.");
            String message = eingabe.readLine();

            // Eroeffnen eines neuen Sockets um die Nachricht zu uebermitteln
            System.out.println("Verbinde mit Server");
            Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            System.out.println("Verbunden mit Server: " + socket.getRemoteSocketAddress());
            boolean oeffentlich = true;
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

    public void sendeMessageWithGui(int abt, String message, int userID) {
        setAbteilung(abt);
        try {
            // Eroeffnen eines neuen Sockets um die Nachricht zu uebermitteln
            Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            // Senden der Nachricht ueber einen Stream
            ServerRequest sr = ServerRequest.buildCreateRequest(message, userID, abt);

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

    public void removemsg(int abteilungNr, int userIdClient) {
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

    public void removeMessageWithGui(int abt, int userID, int msgID) {
        setAbteilung(abt);
        try {
            Socket socketServer = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            ServerRequest serverR = ServerRequest.buildDeleteRequest(msgID, userID);
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

    public void changeMessage(int userID) {
        try {
            BufferedReader newMesseg = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader messegID = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Geben Sie messegId ein,die sie aendern wollen: ");
            String stringId = messegID.readLine();
            int nachrichtId = Integer.parseInt(stringId);

            Socket socketServer = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            System.out.println("Verbunden mit Server: " + socketServer.getRemoteSocketAddress());
            System.out.println("Geben Sie die geaenderte nachrich: ");
            String neueNachricht = newMesseg.readLine();
            ServerRequest serverR = ServerRequest.buildModifyRequest(nachrichtId, neueNachricht, userID);
            ObjectOutputStream oout = new ObjectOutputStream(socketServer.getOutputStream());

            oout.writeObject(serverR);
            System.out.println("id" + nachrichtId + "nachricht:" + neueNachricht + "user" + userID);

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

    public String changeMessageWithGui(int abt, int userID, int msgID, String neueNachricht) {
        setAbteilung(abt);
        String rueckmeldung = "Aenderung erfolgreich abgeschickt";

        try {
            Socket socketServer = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            ServerRequest serverR = ServerRequest.buildModifyRequest(msgID, neueNachricht, userID);
            ObjectOutputStream oout = new ObjectOutputStream(socketServer.getOutputStream());
            
            oout.writeObject(serverR);
            
            oout.close();
            socketServer.close();

        } catch (UnknownHostException e) {
            // Wenn Rechnername nicht bekannt ist.
            System.out.println("Rechnername unbekannt:\n" + e.getMessage());
            rueckmeldung = "Rechnername unbekannt:\n" + e.getMessage();
        } catch (IOException e) {
            // Wenn die Kommunikation fehlschlaegt
            System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
            rueckmeldung = "Fehler waehrend der Kommunikation:\n" + e.getMessage();
        }
        return rueckmeldung;
    }

    public String showMsg(int userId) {
        String str = null;
        try {

            Socket socketServer = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            System.out.println("Verbunden mit Server: " + socketServer.getRemoteSocketAddress());
            ServerRequest serverR = ServerRequest.buildShowMyMessagesRequest(userId);
            ObjectOutputStream oout = new ObjectOutputStream(socketServer.getOutputStream());
            System.out.println("Sende Objekt...");
            oout.writeObject(serverR);
            System.out.println("Objekt gesendet");

            // Empfangen der Nachricht 
            //TODO antwort ist jetzt ein String
            ObjectInputStream input = new ObjectInputStream(socketServer.getInputStream());
            str = input.readObject().toString();
            System.out.println(str);

            /*           for(Message m : userMessages){
                            System.out.println(m.toString());
                        }*/
            oout.close();
            socketServer.close();

        } catch (UnknownHostException e) {
            // Wenn Rechnername nicht bekannt ist.
            str = "Rechnername unbekannt:\n";
            System.out.println("Rechnername unbekannt:\n" + e.getMessage());
        } catch (IOException e) {
            // Wenn die Kommunikation fehlschlaegt
            str = "Fehler waehrend der Kommunikation:\n";
            System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (str == null) {
            str = "";
        }
        return str;
    }

    public String showMessagesWithGui(int abt, int userID) {
        setAbteilung(abt);
        String str = "abrakatabra";
        try {

            Socket socketServer = new Socket(SERVER_HOSTNAME, SERVER_PORT);           
            ServerRequest serverR = ServerRequest.buildShowMyMessagesRequest(userID);
            ObjectOutputStream oout = new ObjectOutputStream(socketServer.getOutputStream());          
            oout.writeObject(serverR);
           
            // Empfangen der Nachricht 
            //TODO antwort ist jetzt ein String
            ObjectInputStream input = new ObjectInputStream(socketServer.getInputStream());
            str = input.readObject().toString();
            System.out.println(str);

            /*           for(Message m : userMessages){
                            System.out.println(m.toString());
                        }*/
            oout.close();
            socketServer.close();

        } catch (UnknownHostException e) {
            // Wenn Rechnername nicht bekannt ist.
            str = "Rechnername unbekannt:\n";
            System.out.println("Rechnername unbekannt:\n" + e.getMessage());
        } catch (IOException e) {
            // Wenn die Kommunikation fehlschlaegt
            str = "Fehler waehrend der Kommunikation:\n";
            System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (str == null) {
            str = "";
        }
        return str;
    }

    public void publishMessage(int messageId, int userId){
    	
    		try {
				Socket serverSocket = new Socket(SERVER_HOSTNAME,SERVER_PORT);
				ServerRequest serverRequest = ServerRequest.buildPublishRequest(messageId, userId);
				ObjectOutputStream oout = new ObjectOutputStream(serverSocket.getOutputStream());
				oout.writeObject(serverRequest);
				serverSocket.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	
    }
    
    public void publicMsg(int userId) {
        try {
            BufferedReader messegID = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Geben Sie messegId ein: ");
            String msgId = messegID.readLine();
            int messegId = Integer.parseInt(msgId);
            Socket socketServer = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            System.out.println("Verbunden mit Server: " + socketServer.getRemoteSocketAddress());
            ServerRequest serverR = ServerRequest.buildPublishRequest(messegId, userId);
            ObjectOutputStream oout = new ObjectOutputStream(socketServer.getOutputStream());

            oout.writeObject(serverR);

            socketServer.close();
        } catch (UnknownHostException e) {
            // Wenn Rechnername nicht bekannt ist.
            System.out.println("Rechnername unbekannt:\n" + e.getMessage());
        } catch (IOException e) {
            // Wenn die Kommunikation fehlschlaegt
            System.out.println("Fehler waehrend der Kommunikation:\n" + e.getMessage());
        }

    }

    public void hauptschleife() {
        int option;

        try {
            Scanner sc = new Scanner(System.in);

//            System.out.println("Geben Sie ihren Namen ein: ");
//            String name = sc.nextLine();
//            //              System.out.println("\n");
            System.out.println("Geben Sie Ihre Abteilungsnummer ein: ");
            int abteilung = sc.nextInt();
            //              System.out.println("\n");
            switch (abteilung) {
                case 1:
                    SERVER_HOSTNAME = ABTEILUNG_1;
                    break;
                case 2:
                    SERVER_HOSTNAME = ABTEILUNG_2;
                    break;
                case 3:
                    SERVER_HOSTNAME = ABTEILUNG_3;
                    break;
                default:
                    break;
            }
            System.out.println("Geben Sie ihre userID ein :");
            int userID = sc.nextInt();
            boolean inMenu = true;
            while (inMenu == true) {
                option = auswahl();
                inMenu = menuTafel(option, abteilung, userID);

            }
            sc.close();
        } catch (Exception exception) {
            exception.printStackTrace();

        }

    }

    public int auswahl() throws IOException {
        int auswahl;
        BufferedReader eingabe = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("*****anzeigetafel*******\n");
        System.out.println("(1) Neue Nachricht\n"
                + "(2) Nachricht ENTFERNEN\n"
                + "(3) Nachricht aendern\n"
                + "(4) Nachrichten ansehen\n"
                + "(5) Nachricht publizieren\n"
                + "(6) Ende\n");
        String wahl = eingabe.readLine();
        auswahl = Integer.parseInt(wahl);
        return auswahl;
    }

    public boolean menuTafel(int option, int abteilung, int userID) throws IOException {

        switch (option) {
            case 1:
                sendeMessage(abteilung, userID);
                return true;
            case 2:
                showMsg(userID);
                removemsg(abteilung, userID);
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
                publicMsg(userID);
                return true;
            case 6:
                System.out.println("EXIT! \n");
                return false;
            default:
                System.out.println("falsche eingabe! \n");
                return true;
        }

    }

    public static void main(String[] args) {
        Client client = new Client();
        client.startClient();
        client.hauptschleife();
    }
}
