/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VerteilteAnzeigetafel;

import java.net.*;
import java.util.LinkedList;
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
			Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
			ServerRequest sr = new ServerRequest(ServerRequestType.CREATE, 0, "hi", 0, 1);
			// ServerRequest sr = new
			// ServerRequest(ServerRequestType.DELETE,31792,"asdf",3,1);
			ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
			// sr =
			// ServerRequest.buildShowMyMessagesRequest(ServerRequestType.SHOW_MY_MESSAGES,
			// 1);
			oout.writeObject(sr);
			byte[] b = new byte [128];
			InputStream stream = socket.getInputStream();
			while (stream.available() == 0);
			stream.read (b);
			System.out.println(new String(b));
			// ObjectInputStream input = new
			// ObjectInputStream(socket.getInputStream());
			// LinkedList<Message> userMessages =(LinkedList<Message>)
			// input.readObject();
			// System.out.println(userMessages.toString());
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
