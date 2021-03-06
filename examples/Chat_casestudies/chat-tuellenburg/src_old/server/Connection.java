package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import common.AuthMessage;
import common.EncryptedMessage;
import common.Encrypter;
import common.Message;
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
	private Encrypter enc;

	public Connection(Socket s, Server server, Encrypter enc) {
		this.socket = s;
		this.enc = enc;
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
		String clientName = socket.getInetAddress().toString();
		try {
			server.broadcast(clientName + " has joined.");
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
			server.broadcast(clientName + " has left.");
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
		if (msg instanceof TextMessage) {
			String line = name + " - " + ((TextMessage) msg).getContent();
			server.broadcast(line);
			server.addToHistory(line);
		}
		//what to do with Authentication Messages
		else if (msg instanceof AuthMessage) {
			server.checkLogin(Connection.this, ((AuthMessage) msg).getPassword());
		}
		//what to do with Encrypted Messages
		else if (msg instanceof EncryptedMessage) {
			String decrypted = enc.decrypt((EncryptedMessage) msg);
			server.addToHistory(name + " - " + decrypted);
			server.broadcast(decrypted);
		}
	}

	/**
	 * sends a message to the client
	 * 
	 * @param line
	 *            text of the message
	 */
	public void send(String line) {
		Message msg = new TextMessage(line);
		if (enc != null) {
			EncryptedMessage encMsg = enc.encrypt(msg);
			msg = encMsg;
		}
		send(msg);
	}
	
	//modified 15.05: TextMessage to Message msg
	public void send(Message msg) {
		try {
			synchronized (outputStream) {
				outputStream.writeObject(msg);
			}
			outputStream.flush();
		} catch (IOException ex) {
		}
	}
}
