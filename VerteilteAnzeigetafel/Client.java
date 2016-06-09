/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VerteilteAnzeigetafel;

import java.net.*;
import java.io.*;
/**
 *
 * @author noone
 */

public class Client {
	public static final int SERVER_PORT = 10001;
	public static final String SERVER_HOSTNAME = "localhost";
	 public static void main(String[] args) {
		 try {
			Socket socket = new Socket (SERVER_HOSTNAME, SERVER_PORT);
			//ServerRequest sr = new ServerRequest(ServerRequestType.CREATE, 0, "hi", 3, 1);
			ServerRequest sr = new ServerRequest(ServerRequestType.DELETE,31792,"asdf",3,1);
			ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("Sende Objekt...");
			oout.writeObject(sr);
			System.out.println("Objekt gesendet");
			oout.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
}
