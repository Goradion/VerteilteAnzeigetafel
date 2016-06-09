
import java.io.ObjectInputStream;
import java.net.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStream;

class pserver {

    public static void schreibeAbteilungDatei(SMessage NachrichtinDatei[],  int i) throws IOException {

        FileWriter fw = new FileWriter("ausgabe.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);

        //bw.newLine();
        bw.append(NachrichtinDatei[i].getUser() + "\n" + NachrichtinDatei[i].getAdmin() + "\n" + NachrichtinDatei[i].getMsgID() + "\n" + NachrichtinDatei[i].getMessage() + "\n");
        bw.close();

    }

    public static int leseLetzteNachrichten(SMessage NachrichtenObject[]) throws IOException {

        int z = 0;
        int i = 1;
        String name = "NULL";
        boolean admin = false;
        String message = "NULL";
        boolean modi = false;
        boolean newm = false;
        int msgid = -1;
        int anzahl_Objekte = 0;

        int a = 0;
        FileReader fr = new FileReader("ausgabe.txt");
        BufferedReader br = new BufferedReader(fr);

        String zeile = br.readLine();

        while (zeile != null) {
            switch (i) {
                case 0:
                    //INIT - DO NOT DELETE THIS CASE 0 !
                    break;
                case 1:
                    NachrichtenObject[a] = new SMessage(name, admin, message, modi, newm, msgid);
                    NachrichtenObject[a].setUser(zeile);
                    System.out.println("Verfasser: " + NachrichtenObject[a].getUser());
                    break;
                case 2:
                    NachrichtenObject[a].setAdmin(zeile);
                    break;
                case 3:
                    NachrichtenObject[a].setMessageIDRead(zeile);
                    System.out.println("Message ID: " + NachrichtenObject[a].getMsgID());
                    break;
                case 4:
                    NachrichtenObject[a].setMessage(zeile);
                    anzahl_Objekte++;
                    System.out.println("Message: " + NachrichtenObject[a].getMessage() + "\n");

                    break;
                default:
                    System.out.println("Fehler in der Datei!");
                    break;
            }

            if (i == 4) {
                a++;
                i = 0;
            }

            zeile = br.readLine();
            i++;

        }

        br.close();

        return anzahl_Objekte;
    }
    
    public static void schreibeNeueDatei(int anzahl, SMessage[] MsgObj) throws IOException
    {
        int i = 0;
        FileWriter fw = new FileWriter("ausgabe.txt");
        BufferedWriter bw = new BufferedWriter(fw);

        while(i<anzahl)
        {
            bw.write(MsgObj[i].getUser() + "\n" + MsgObj[i].getAdmin() + "\n" + MsgObj[i].getMsgID() + "\n" + MsgObj[i].getMessage() + "\n");
            i++;
        }
        bw.close();
    }

    public static void aendere_Nachricht(int m_id, String txt, SMessage[] MsgObj, int anzahl) throws IOException {
        int i = 0;

        while (i < anzahl) {
            if (MsgObj[i].getMsgID() == m_id) {
                MsgObj[i].setMessage(txt);
                 schreibeNeueDatei(anzahl,MsgObj); 
                 System.out.print("Die Nachricht mit der M_ID: " + MsgObj[i].getMsgID() + " wurde geändert! \n" + "-> " + MsgObj[i].getMessage());
            }
            i++;
        }
        
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        String name = "TEST";
        boolean rem = false;
        boolean modi = false;
        boolean newm = false;
        int msgid = 0;
        boolean admin = false;
        boolean wyd = false;
        String txt ="";
        int i = 0;
        int z = 0;
        int m_id = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String uhrzeit = sdf.format(new Date());
        SMessage[] NachrichtenObject = new SMessage[100];

        System.out.println("*** Letzte Nachrichten: ***");
        z = leseLetzteNachrichten(NachrichtenObject); // z = Anzahl eingelesener Objekte

        System.out.println("______________________________________________________" + "\n");
        System.out.println("*** Ab hier folgen die neuen Anzeigen: *** " + "\n");
        ServerSocket anschluss = new ServerSocket(6000);  // Server lauscht am Port 6000 um eine Verbindung aufzubauen, wenn eine Anfrage an diesem Port reinkommt!
        int co = 0;
        
        while (true) {

            Socket lauschen = anschluss.accept();  // Mit der blockierenden Methode accept() der ServerSocket-Klasse nehmen wir genau eine wartende Verbindung an
      
            try {

             /*     if (lauschen.isConnected() == true) {
                   
                    BufferedReader in_d = new BufferedReader(new InputStreamReader(lauschen.getInputStream()));
                  txt = in_d.readLine();
                  wyd = Boolean.parseBoolean(txt);
                    
                    if (wyd == true)
                    {
                         ObjectOutputStream Out = new ObjectOutputStream(lauschen.getOutputStream());
                         Out.writeObject(NachrichtenObject[co]);
                         co++;
                            
                    }
                    
                    
                    }*/
                  
                if (lauschen.isConnected() == true) {
                    if (z != 0) {
                        m_id = NachrichtenObject[0].ermittleLastID(NachrichtenObject, z);

                    }
                    
                    InputStream input = lauschen.getInputStream();
                    ObjectInputStream in = new ObjectInputStream(input);
                    NachrichtenObject[z] = (SMessage) in.readObject();
                    if (NachrichtenObject[z].getMod() == true) {
                        m_id = NachrichtenObject[z].getMsgID();
                        txt = NachrichtenObject[z].getMessage();
                        aendere_Nachricht(m_id, txt, NachrichtenObject, z);
                          // schreibeAbteilungDatei(NachrichtenObject, z);
                    } else  {
                        NachrichtenObject[z].setMsgID(m_id);
                        schreibeAbteilungDatei(NachrichtenObject, z);
                        System.out.println(uhrzeit + " " + NachrichtenObject[z].getUser() + " sagt:\n" + NachrichtenObject[z].getMessage());
                        z++;
                    }
                 

                    in.close();

                }
            } catch (IOException ex) {
                // ex.printStackTrace();
            }

        }

    }

}
