package Base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;




/**
 * simple chat client
 */
abstract class Client$$Base implements Runnable {
	

	

	
		
	public static void main(String args[]) throws IOException {
		if (args.length != 2)
			throw new RuntimeException("Syntax: ChatClient <host> <port>");
		Client client=new Client (args[0], Integer.parseInt(args[1]));
		//new Gui("Chat " + args[0] + ":" + args[1], client);
	}	
	
	
	protected ObjectInputStream inputStream;

	protected ObjectOutputStream outputStream;

	protected Thread thread;

	

	public Client$$Base(String host, int port) {
		try {
			System.out.println("Connecting to " + host + " (port " + port
					+ ")...");
			Socket s = new Socket(host, port);
			this.outputStream = new ObjectOutputStream((s.getOutputStream()));
			this.inputStream = new ObjectInputStream((s.getInputStream()));
			
			thread = new Thread(this);
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
			while (true) {
				try {
					Object msg = inputStream.readObject();
					handleIncomingMessage(msg);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (IOException ex) {
			ex.printStackTrace();
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
	protected void handleIncomingMessage(Object msg) {
		if (msg instanceof TextMessage) {
			fireAddLine(((TextMessage) msg).getOrigin());
			fireAddLine(((TextMessage) msg).getContent() + "\n");
		}
	}

	public void send(String line) {
		send(new TextMessage(line));
	}

	public void send(TextMessage msg) {
		try {
			outputStream.writeObject(msg);
			outputStream.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
			thread.stop();
		}
	}

	/**
	 * listener-list for the observer pattern
	 */
	private ArrayList listeners = new ArrayList();

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
	 */
	public void fireAddLine(String line) {
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
			ChatLineListener listener = (ChatLineListener) iterator.next();
			listener.newChatLine(line);
		}
	}

	public void stop() {
		thread.stop();
	}

}



abstract class Client$$Console extends  Client$$Base  {
	
	protected Gui gui;
	
	public static void main(String args[]) throws IOException {
		if (args.length != 2)
			throw new RuntimeException("Syntax: ChatClient <host> <port>");
		Client client=new Client (args[0], Integer.parseInt(args[1]));
		
		new Gui("Chat " + args[0] + ":" + args[1], client);
	}
      // inherited constructors



	

	public Client$$Console ( String host, int port ) { super(host, port); }	
}



abstract class Client$$Logging extends  Client$$Console  {
	protected void handleIncomingMessage(Object msg){
		super.handleIncomingMessage(msg);
		Logging logger=new Logging();
		logger.log(msg);
	}
      // inherited constructors



	

	public Client$$Logging ( String host, int port ) { super(host, port); }
}



abstract class Client$$Flip extends  Client$$Logging  {
	public void send(TextMessage msg){
		String text=msg.getContent();
		msg.setContent(text.substring(1, 2) + text.substring(0, 1) + text.substring(2,text.length()));
		msg.cryp=true;
		super.send(msg);
	}
	
	protected void handleIncomingMessage(Object msg) {
		if (msg instanceof TextMessage) {
			if (((TextMessage)msg).cryp){
				String text=((TextMessage)msg).getContent();
				((TextMessage) msg).setContent(text.substring(1, 2) + text.substring(0, 1) + text.substring(2,text.length()));
			}
			super.handleIncomingMessage(msg);
		}
	}
      // inherited constructors



	

	public Client$$Flip ( String host, int port ) { super(host, port); }
	
}



public class Client extends  Client$$Flip  {
	
	
	public void handleIncomingMessage(Object msg){
		if(msg instanceof TextMessage)
			fireAddLine(((TextMessage) msg).getColor());
		super.handleIncomingMessage(msg);
		
	}
      // inherited constructors



	

	public Client ( String host, int port ) { super(host, port); }
	
	
	
}