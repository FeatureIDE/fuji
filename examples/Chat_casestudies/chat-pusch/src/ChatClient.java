
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.*;



/**
 * ChatClient for joChat.
 * @author Jonas Pusch
 */
abstract class ChatClient$$messaging {
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private static ReceiverThread receiverThread;
	
	protected String username="";

	private static final String HOST= "jobook";
	private static final int PORT= 1120;
	/**
	 * listener-list for the observer pattern
	 */
	private List chatListeners;
	
	
	
	public ChatClient$$messaging() {
		chatListeners = new ArrayList();
		try {			
			Socket s= new Socket(HOST,PORT);
			in= new ObjectInputStream(s.getInputStream());
			out= new ObjectOutputStream(s.getOutputStream());
			System.out.println("connected to:" + s);
			receiverThread= new ReceiverThread();
			receiverThread.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructs a ChatClient and lays a GUI over it.
	 * @param args
	 */
	public static void main(String[] args) {
		ChatClient cc= new ChatClient();
	}
	
	/**
	 * class for receiving incoming <br>
	 * messages from the server.
	 * @author Jonas Pusch
	 */
	private class ReceiverThread extends Thread {
		private boolean stop=false;

		public void stopReceiverThread() {
			stop=true;
		}
		
		public void run() {
			try {
				while (!stop) {
					try {
						MessageObject msgo = (MessageObject) in.readObject();
						handleIncomingMessageObject(msgo);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				stopReceiverThread();
			} finally {
				try {
					receiverThread=null;
					in.close();
					out.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
      // inherited constructors


	
	
	
	public ReceiverThread (  ) { super(); }
	}

	/**
	 * decides what to do with incoming messages
	 * 
	 * @param msg
	 *            the message (Object) received from the sockets
	 */
	protected void handleIncomingMessageObject(MessageObject msgo) {
			fireMessageReceived(msgo);
	}
	
	/** 
	 * sends a MessageObject to the server.
	 * @param msg
	 */
	public void sendMessage(MessageObject msg) {
		try {
			out.writeObject(msg);
			out.flush();
			out.reset();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * addListner method for the observer pattern
	 */
	public void addChatListener(ChatListener listener) {
		chatListeners.add(listener);
	}

	/**
	 * removeListner method for the observer pattern
	 */
	public void removeChatListener(ChatListener listner) {
		chatListeners.remove(listner);
	}

	/**
	 * fire Listner method for the observer pattern
	 */
	public void fireMessageReceived(MessageObject msgo) {
		for (int i=0; i< chatListeners.size(); i++) {
			ChatListener listener= (ChatListener) chatListeners.get(i);
			listener.newChatLine(msgo);
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		((ChatClient) this).username = username;
	}
	
	
}


abstract class ChatClient$$gui extends  ChatClient$$messaging  {

	/**
	 * Constructs a ChatClient and lays a GUI over it.
	 * @param args
	 */
	public static void main(String[] args) {
		ChatClient cc= new ChatClient();
		ChatClientGUI ccg= new ChatClientGUI(cc);
		ccg.setVisible(true);
	}
      // inherited constructors


	
	
	
	public ChatClient$$gui (  ) { super(); }
}


abstract class ChatClient$$rot13 extends  ChatClient$$gui  {

	protected void handleIncomingMessageObject(MessageObject msgo) {
		ROT13Encryption.decrypt(msgo);
		super.handleIncomingMessageObject(msgo);
	}
	
	public void sendMessage(MessageObject msg) {
		ROT13Encryption.encrypt(msg);
		super.sendMessage(msg);
	}
      // inherited constructors


	
	
	
	public ChatClient$$rot13 (  ) { super(); }
}

abstract class ChatClient$$invert extends  ChatClient$$rot13  {
	

	protected void handleIncomingMessageObject(MessageObject msgo) {
		InvertEncryption.decrypt(msgo);
		super.handleIncomingMessageObject(msgo);
	}
	
	public void sendMessage(MessageObject msg) {
		InvertEncryption.encrypt(msg);
		super.sendMessage(msg);
	}
      // inherited constructors


	
	
	
	public ChatClient$$invert (  ) { super(); }
}



abstract class ChatClient$$history extends  ChatClient$$invert  {
	
	private PrintWriter historyOut;
	
	
	
	public ChatClient$$history() { super(); 

		try {
			Random random= new Random();
			historyOut= new PrintWriter(new FileOutputStream(random.nextLong()+"_clientHistory.txt"), true);
		} catch (IOException e) {
			e.printStackTrace();
		} }

	
	protected void handleIncomingMessageObject(MessageObject msgo) {
		historyOut.println("in: "+msgo);
		super.handleIncomingMessageObject(msgo);
	}
	
	public void sendMessage(MessageObject msgo) {
		historyOut.println("out: "+msgo);
		super.sendMessage(msgo);
	}
      // inherited constructors


}

 


public class ChatClient extends  ChatClient$$history  {
	
	protected void handleIncomingMessageObject(MessageObject msgo) {
		if (msgo.getMessageType().equals(MessageType.TEXT)) {
			playSound();
		}
		super.handleIncomingMessageObject(msgo);
	}
	
	public void sendMessage(MessageObject msg) {
		if (msg.getMessageType().equals(MessageType.TEXT)) {
			playSound();
		}
		super.sendMessage(msg);
	}
	
	private void playSound() {
		try {
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("msg.wav"));
					BufferedInputStream bufferedInputStream = new BufferedInputStream(audioInputStream);
			        AudioFormat af = audioInputStream.getFormat();
			        int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
			        byte[] audio = new byte[size];
			        DataLine.Info info = new DataLine.Info(Clip.class, af, size);
		            bufferedInputStream.read(audio, 0, size);
		            Clip clip = (Clip) AudioSystem.getLine(info);
		            clip.open(af, audio, 0, size);
		            clip.start();
				} catch (Exception e) {
					e.printStackTrace();	
				}
	}
      // inherited constructors


	
	
	
	public ChatClient (  ) { super(); }
}