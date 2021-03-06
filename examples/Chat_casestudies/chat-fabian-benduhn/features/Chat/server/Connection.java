package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import common.TextMessage;

/**
 * class for an individual connection to a client. allows to send messages to
 * this client and handles incoming messages.
 */
public class Connection extends Thread {
	protected Socket socket;
	protected ObjectInputStream inputStream;
	protected ObjectOutputStream outputStream;
	private Server server;

	public Connection(Socket s, Server server) {
		this.socket = s;
		try {
			inputStream = new ObjectInputStream((s.getInputStream()));
			outputStream = new ObjectOutputStream((s.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.server = server;
	}
	private String getClientName(){
	return socket.getInetAddress().toString();
	}
	
	/**
	 * waits for incoming messages from the socket
	 */
	public void run() {
		System.out.println("hallo");
		String clientName=getClientName();
		try {
			server.broadcast(clientName + " has joined.",false);
			Object msg = null;
			while ((msg = inputStream.readObject()) != null) {
				handleIncomingMessage(clientName, msg);
			}
		} catch (SocketException e) {
		} catch (EOFException e) {
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			server.removeConnection(this);
			server.broadcast(clientName + " has left.",false);
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
	private void handleIncomingMessage(String name, Object msg) {
		System.out.println("TUTUT");
		if (msg instanceof TextMessage)
			server.broadcast(getName(name) + " - " + ((TextMessage) msg).getContent(),true);
	}
	private String getName(String name){
		return name;
	}
	/**
	 * sends a message to the client
	 * 
	 * @param line
	 *            text of the message
	 */
	public void send(String line, boolean encrypted) {
		send(new TextMessage(line,encrypted));
	}

	public void send(TextMessage msg) {
		try {
			synchronized (outputStream) {
				outputStream.writeObject(msg);
			}
			outputStream.flush();
		} catch (IOException ex) {
		}
	}
}
