/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tafelServer;

import java.net.*;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import serverRequests.ServerRequest;
import verteilteAnzeigetafel.Anzeigetafel;
import verteilteAnzeigetafel.Message;
import verteilteAnzeigetafel.TafelException;

import java.io.*;

/**
 *
 * @author noone
 */

public class Client {
	public static final int SERVER_PORT = 10001;
	public static final String SERVER_HOSTNAME = "localhost";

	public static void main(String[] args) {
		LinkedBlockingQueue<Message> q = new LinkedBlockingQueue<Message>();
		TafelServer ts = new TafelServer();
//		q.add(new Message("qq", 7, 4, true, 4711));
//		q.add(new Message("qq", 7, 7, true, 4712));
//		new OutboxThread(1, new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), q, ts).start();;
		new HeartbeatThread(2, new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), ts).start();
		
//		try {
//			Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
//			//ServerRequest sr = ServerRequest.buildShowMyMessagesRequest(1);
//			ServerRequest sr = ServerRequest.buildCreateRequest("test", 1, 1);
//			ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
//			// sr =
//			// ServerRequest.buildShowMyMessagesRequest(ServerRequestType.SHOW_MY_MESSAGES,
//			// 1);
//			//oout.writeObject(sr);
//			//Thread.sleep(5000);
//			// byte[] b = new byte [128];
//			// InputStream stream = socket.getInputStream();
//			// while (stream.available() == 0);
//			// stream.read (b);
//			// System.out.println(new String(b));
////			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
////			LinkedList<Message> userMessages = (LinkedList<Message>) input.readObject();
////			System.out.println(userMessages.toString());
//			//oout.writeObject(null);
//			socket.close();
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		 catch (ClassNotFoundException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
	}
}
