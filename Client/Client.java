
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
    
    private String benutzername;
    private int abtNr;
    private String message;
    private int port=50000;
    private String ip;
    private static Socket socket;
    
    public Client(String benutzername,int abtNr)
    {
        this.benutzername = benutzername;
        this.abtNr = abtNr;
    }
    
    public String getbenutzername()
    {
        return benutzername;
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
    
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        int a; //tolle variable
        System.out.println("hi");
    }
}
