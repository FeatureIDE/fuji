

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import java.util.Iterator;

import java.lang.Thread;
import java.lang.InterruptedException;



/**
 * chat client
 */
abstract class Client$$Root	implements Runnable {
	
	/**
	 * 
	 * @param args 
	 * arg[0] host
	 * arg[1] port 
	 * arg[2] name the name of the Client (username)
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		if (args.length < 3)
			throw new RuntimeException("Syntax: ChatClient <host> <port> <userName>");
		
		Client client = new Client(args[0], Integer.parseInt(args[1]), args[2]);
	}
	
	protected static Client client_this;
	public static Client getClient(){
		return client_this;
	}
	
	protected ObjectInputStream inputStream;
	protected ObjectOutputStream outputStream;
	protected Thread thread;
	
	/**
	 * listener-list for the observer pattern
	 */
	private ArrayList listeners = new ArrayList();
	
	
	private String userName;
	public String getUserName() {
		return userName;
	}

	/**
	 * Create a Client Object 
	 * 
	 * @param host 
	 *          host of the server to connect
	 * @param port
	 *          port of the server to connect
	 * @param userName
	 *          name of this client (username)
	 */
	public Client$$Root(String host, int port, String userName) {
		init(host, port, userName);
	}
	
	
	public void init(String host, int port, String userNameL){
		client_this = ((Client) this);
		userName = userNameL;
		try {
			System.out.println("Connecting to " + host + " (port " + port + ")...");
			Socket s = new Socket(host, port);
			
			outputStream = new ObjectOutputStream((s.getOutputStream()));
			inputStream = new ObjectInputStream((s.getInputStream()));

			thread = new Thread(((Client) this));
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * main method. waits for incoming messages.
	 */
	public void run() {
		try {
			send(new Message(userName + " betritt den Raum"));
			while (true) {
				try {
					Object msg = inputStream.readObject();
					handleIncomingMessage(msg);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} finally {
			thread = null;
			try {
				outputStream.close();
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * decides what to do with incoming messages
	 * 
	 * @param msg
	 *            the message (Object) received from the sockets
	 */
	public void handleIncomingMessage(Object msg) {
		if (msg instanceof Message) {
			Message tMsg = (Message) msg;
			fireAddLine(tMsg);
		}
	}

	/**
	 * send a line with the specific color
	 * 
	 * @param line
	 *           message or pw to send
	 * @param textColor
	 *           color of the line if the message is a ColorMessage
	 */
	public void send(String line) {
		send(new Message(userName, line));
	}

	/**
	 * 
	 * send the specific TextMessage to the server
	 * 
	 * @param msg
	 *          Message to send
	 */
	public void send(Message msg) {
		try {
			outputStream.writeObject(msg);
			outputStream.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
			thread.stop();
		}
	}
	
	/**
	 * addListner method for the observer pattern
	 */
	public void addLineListener(ChatLineListener listener) {
		listeners.add(listener);
	}

	/**
	 * removeListner method for the observer pattern
	 */
	public void removeLineListener(ChatLineListener listner) {
		listeners.remove(listner);
	}

	/**
	 * fire Listner method for the observer pattern
	 * @param color 
	 */
	public void fireAddLine(Message msg){
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
			ChatLineListener listener = (ChatLineListener) iterator.next();
			listener.newChatLine(msg);
		}
	}

	/**
	 * stop the thread
	 */
	public void stop() {
		thread.stop();
	}
}



abstract class Client$$Log extends  Client$$Root {
	public Logger log = null;
	
	public void handleIncomingMessage(Object msg) {
		super.handleIncomingMessage(msg);
		
		if(msg instanceof Message){
			if(log == null){
				log = new Logger(getUserName());
			}
			if(log != null){
				log.writeLogLine(((Message) msg).getUserName_And_Content());
			}		
		}
	}
      // inherited constructors



	/**
	 * Create a Client Object 
	 * 
	 * @param host 
	 *          host of the server to connect
	 * @param port
	 *          port of the server to connect
	 * @param userName
	 *          name of this client (username)
	 */
	public Client$$Log ( String host, int port, String userName ) { super(host, port, userName); }
}



abstract class Client$$Authentification extends  Client$$Log  {
	boolean auth_accept = false;
	
	public void send(Message msg) {
		if(auth_accept == false){
			msg = new Auth_Message(msg.getUserName(), msg.getContent());
		}
		super.send(msg);	
	}
	
	public void addLineListener(ChatLineListener listener) {
		super.addLineListener(listener);
		
		if(auth_accept == false){
			Message tMsg = new Message("Geben Sie das PW ein:");
			listener.newChatLine(tMsg);
		}
	}
	
	public void handleIncomingMessage(Object msg){
		if(auth_accept == false){
			if(msg instanceof Auth_Message){
				Auth_Message authMsg = (Auth_Message) msg;
				
				if(authMsg.getContent().compareTo(Auth_Message.PW_ACCEPT) == 0){
					auth_accept = true;
					send(new Message(getUserName() + " betritt den Raum"));	
					super.handleIncomingMessage(new Message("PW akzeptiert!"));
				}else{
					super.handleIncomingMessage(new Message("PW war falsch, wiederholen Sie die Eingabe:"));
				}
			}
		}else{
			super.handleIncomingMessage(msg);
		}
	}
      // inherited constructors



	/**
	 * Create a Client Object 
	 * 
	 * @param host 
	 *          host of the server to connect
	 * @param port
	 *          port of the server to connect
	 * @param userName
	 *          name of this client (username)
	 */
	public Client$$Authentification ( String host, int port, String userName ) { super(host, port, userName); }
}



abstract class Client$$GUI extends  Client$$Authentification  {
	
	public static void main(String[] args) throws IOException {
		Client$$Authentification.main(args);
		
		Client client = Client.getClient();
		
		new Gui("Chat " + args[2], client);
	}
      // inherited constructors



	/**
	 * Create a Client Object 
	 * 
	 * @param host 
	 *          host of the server to connect
	 * @param port
	 *          port of the server to connect
	 * @param userName
	 *          name of this client (username)
	 */
	public Client$$GUI ( String host, int port, String userName ) { super(host, port, userName); }
}



public class Client extends  Client$$GUI  {
	
	public void send(Message msg) {
		msg.setColor(Gui.getTextColor());
		super.send(msg);
	}
      // inherited constructors



	/**
	 * Create a Client Object 
	 * 
	 * @param host 
	 *          host of the server to connect
	 * @param port
	 *          port of the server to connect
	 * @param userName
	 *          name of this client (username)
	 */
	public Client ( String host, int port, String userName ) { super(host, port, userName); }
}