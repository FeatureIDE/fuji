package Authentication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.io.*;
import java.text.*;
import java.util.*;




/**
 * class for an individual connection to a client. allows to send messages to
 * this client and handles incoming messages.
 */
abstract class Connection$$server extends Thread {
	protected Socket socket;

	protected ObjectInputStream inputStream;

	protected ObjectOutputStream outputStream;

	protected Server server;
	protected boolean connectionOpen = true;

	public Connection$$server(Socket s, Server server) {
		this.socket = s;
		try {
			inputStream = new ObjectInputStream((s.getInputStream()));
			outputStream = new ObjectOutputStream((s.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.server = server;
	}

	/**
	 * waits for incoming messages from the socket
	 */
	public void run() {
		System.out.println("Thread is now running");
		String clientName = socket.getInetAddress().toString();
		try {
			TextMessage joinNotification = new TextMessage(clientName + " has joined.");
			server.broadcast(joinNotification);
			while (connectionOpen) {

				try {
					Object msg = inputStream.readObject();
					handleIncomingMessage(clientName, msg);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			server.removeConnection(((Connection) this));
			TextMessage quitNotification =  new TextMessage(clientName + " has left.");
			server.broadcast(quitNotification);
			try {
				socket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * decides what to do with incoming messages
	 * 
	 * @param name
	 *            name of the client
	 * @param msg
	 *            received message
	 */
	protected void handleIncomingMessage(String name, Object msg) {
		if (msg instanceof TextMessage){
			((TextMessage)msg).setContent(name + " - " + ((TextMessage) msg).getContent());
			server.broadcast((TextMessage)msg);
		}
	}

	/**
	 * sends a message to the client
	 * 
	 * @param line
	 *            text of the message
	 */
	public void send(TextMessage msg) {
		try {
			synchronized (outputStream) {
				outputStream.writeObject(msg);
			}
			outputStream.flush();
		} catch (IOException ex) {
			connectionOpen = false;
		}

	}

}



abstract class Connection$$Authentication extends  Connection$$server  {
	public void run() {
		String clientName = socket.getInetAddress().toString();
		try {
			while (connectionOpen) {
				try {
					Object msg = inputStream.readObject();
					handleIncomingMessage(clientName, msg);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			server.removeConnection(((Connection) this));
			TextMessage quitNotification =  new TextMessage(clientName + " has left.");
			server.broadcast(quitNotification);
			try {
				socket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	protected void handleIncomingMessage(String name, Object msg) {
		if (msg instanceof LoginRequest){
			String uName = ((LoginRequest)msg).Usrname;
			String pwd = ((LoginRequest)msg).Pwd;
			if (server.userData.containsKey(uName)){
				if (((String)server.userData.get(uName)).equals(pwd)){
					try{
						LoginReply reply = new LoginReply();
						reply.status = true;
						outputStream.writeObject( reply );
					} catch( IOException w ){w.toString();}
				}else{
					try{
						LoginReply reply = new LoginReply();
						reply.status = false;
						outputStream.writeObject( reply );
					} catch( IOException w ){w.toString();}
				}	
			}else{
				try{
					LoginReply reply = new LoginReply();
					reply.status = false;
					outputStream.writeObject( reply );
				} catch( IOException w ){w.toString();}
			} 
		}
		super.handleIncomingMessage(name,msg);
			
	}
      // inherited constructors



	public Connection$$Authentication ( Socket s, Server server ) { super(s, server); } 
}



public class Connection extends  Connection$$Authentication  {
	protected void handleIncomingMessage(String name,Object msg) {
		super.handleIncomingMessage(name,msg);
		if (msg instanceof TextMessage) {
			archive(name,(TextMessage)msg);
		}
	}
	public synchronized void archive(String name,TextMessage usrMessage){
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter("serverLog.log",true));
			TimeZone tz = TimeZone.getTimeZone("EST"); 
         	Date now = new Date();
         	DateFormat df = new SimpleDateFormat ("yyyy.mm.dd hh:mm:ss ");
         	df.setTimeZone(tz);
         	String currentTime = df.format(now);

			bw.write(currentTime + " " + name + " - " + usrMessage.getContent() + "\n");
			bw.newLine();
			bw.flush();
			bw.close();
		} catch(Exception e){}
	}
      // inherited constructors



	public Connection ( Socket s, Server server ) { super(s, server); }
}