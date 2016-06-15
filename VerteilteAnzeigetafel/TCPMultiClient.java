package VerteilteAnzeigetafel;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.*;
import java.sql.*;
import java.util.*;



public class TCPMultiClient {
 
	public static void main(String argv[]) throws Exception {
		
		String sentence;// die gesendete Nachricht
		String antwort;
		int idNumm;
 		
		BufferedReader inFromUser =	new BufferedReader(new InputStreamReader(System.in));// ist gleich bie fopen oder filereader liest die eingabe von client  
		BufferedReader eingabe =	new BufferedReader(new InputStreamReader(System.in));
	    System.out.println("username eingeben:");
	    antwort = eingabe.readLine();
		boolean istUser = loggin(antwort);
		if (istUser == true)
		{
			
			
	
			while (true) {
			    
				
				sentence = inFromUser.readLine();//speichert die eingabe in eine variable
				idNumm = idCreator ();
				speichernNachricht(idNumm,sentence);
				outToServer.writeBytes(antwort+":("+ idNumm +")" + sentence + '\n');// schick die varianble zu server
				if (sentence.equals("EXIT")) {
					break;
				}
				modifiedSentence = inFromServer.readLine();
				System.out.println("FROM SERVER: " + modifiedSentence);
				
 			
 			
 
			}
		
	
			clientSocket.close();
		}
		else{
			System.out.println("falshe user");
		}
			
		
	}
	
	public static void sendeMesseg (int idNumm, String antwort, String sentence) throws Exception {
		String modifiedSentence;// die angekommende Nachricht
		//String sentence;
		Socket clientSocket = new Socket("localhost", 10001 );
		//BufferedReader inFromUser =	new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());//write was schikt
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));//read was bekommt
			//sentence = inFromUser.readLine();
			idNumm = idCreator ();
			speichernNachricht(idNumm,sentence);
			outToServer.writeBytes(antwort+":("+ idNumm +")" + sentence + '\n');// schick die varianble zu server
			if (sentence.equals("EXIT")) {
				break;
			}
			modifiedSentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + modifiedSentence);
		}
		clientSocket.close();
		
	}
	public static int idCreator () throws IOException{
		String codeMsg;
		boolean exist = true;
		Random idGenerator = new Random();
		int id = (int)(idGenerator.nextDouble() * 10 + 1);
		
		while(true){
			
			codeMsg = String.valueOf(id);
			exist = suche ("messeg.txt",codeMsg);
			
			if (exist == false){
				
				return (id = Integer.parseInt(codeMsg));
			}
			else{
				
				id = (int)(idGenerator.nextDouble() * 10 + 1);
				
			}	
		}	
		//return 0;
	}
	
	
    public static void speichernNachricht(int idNachricht,String nachricht){
		try{
			File datei=new File("messeg.txt");
			FileWriter schreibenMsg =new FileWriter(datei,true);
			schreibenMsg.write(idNachricht + " " + nachricht + "\n");
			schreibenMsg.close();
		}
		catch(Exception e){
			System.out.println("man kann nicht die Datei speichern");
		}
	}

	public static boolean loggin (String name)throws IOException{
	return (suche ("user.txt", name));	
	}
	
	public static boolean suche (String dateiNamme , String sucheString) throws java.io.IOException{
		
		String userName;
		BufferedReader usersList = new BufferedReader (new FileReader(dateiNamme));
	    String listZeile ;//= usersList.readLine();
		//StringTokenizer st = new StringTokenizer (listZeile);
		int indexfound = 0;
		while ((listZeile = usersList.readLine()) != null){
			StringTokenizer st = new StringTokenizer (listZeile);
			while (st.hasMoreTokens())
			{
				
				userName = st.nextToken();
			   
				if ( userName.equals(sucheString)){
					indexfound ++;
				}
			
			}
			
					
		}
		 usersList.close();
			if (indexfound >= 1){
				return true;
		}
        else{
			return false;
		}		
	    
		
	    
			
	}

	
	
	
}