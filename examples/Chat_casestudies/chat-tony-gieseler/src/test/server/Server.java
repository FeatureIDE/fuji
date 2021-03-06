package server; 

import java.io.IOException; 
import java.net.ServerSocket; 
import java.net.Socket; 
import java.util.HashSet; 
import java.util.Iterator; 

import server.Connection; 
import common.Change01; 


/**
 * TODO description
 */
public   class  Server {
	
	public static void main(String args[]) throws IOException {
		if(args.length != 1)
			throw new RuntimeException("Syntax: ChatServer <port>");
		new Server(Integer.parseInt(args[0]));
	}

	

	/**
	 * awaits incoming connections and creates Connection objects accordingly.
	 * 
	 * @param port
	 *            port to listen on
	 */
	public Server(int port) throws IOException {
		start(port);
	}

	
	
	 private void  start__wrappee__Authentifizierung  (int port) throws IOException {
		ServerSocket server = new ServerSocket(port);
		while (true) {
			System.out.println("Waiting for Connections...");
			Socket client = server.accept();
			System.out.println("Accepted from " + client.getInetAddress());
			Connection c = connectTo(client);
			
			String passC = "";
			byte[]buf = new byte[20];
			int len = c.inputStream.read(buf);
				
			for(int i = 0; i < len; i++)
				passC += (char)buf[i];
			
			if(pass.equals(passC))
				c.start();
		}
	}

	
	private void start(int port) throws IOException {
		Change01 v = new Change01();
		pass = v.crypt(pass);
		
		start__wrappee__Authentifizierung(port);
	}

	

	/**
	 * creates a new connection for a socket
	 * 
	 * @param socket
	 *            socket
	 * @return the Connection object that handles all further communication with
	 *         this socket
	 */
	public Connection connectTo(Socket socket) {
		Connection connection = new Connection(socket, this);
		connections.add(connection);
		return connection;
	}

	

	/**
	 * list of all known connections
	 */
	protected HashSet connections = new HashSet();

	

	/**
	 * send a message to all known connections
	 * 
	 * @param text
	 *            content of the message
	 */
	public void broadcast(String text) {
		synchronized (connections) {
			for (Iterator iterator = connections.iterator(); iterator.hasNext();) {
				Connection connection = (Connection) iterator.next();
				connection.send(text);
			}
		}
	}

	

	/**
	 * remove a connection so that broadcasts are no longer sent there.
	 * 
	 * @param connection
	 *            connection to remove
	 */
	public void removeConnection(Connection connection) {
		connections.remove(connection);
	}

	
	private String pass = "Passwort";


}
