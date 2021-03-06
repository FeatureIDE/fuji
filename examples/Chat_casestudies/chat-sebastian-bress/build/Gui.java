



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;



//import common.TextMessage;

/**
 * simple AWT gui for the chat client
 */
abstract class Gui$$GUI extends Frame implements ChatLineListener {

	private static final long serialVersionUID = 1L;

	protected TextArea outputTextbox;

	protected TextField inputField;

	private Client chatClient;

	/**
	 * creates layout
	 * 
	 * @param title
	 *            title of the window
	 * @param chatClient
	 *            chatClient that is used for sending and receiving messages
	 */
	public Gui$$GUI(String title, Client chatClient) {
		super(title);
		System.out.println("starting gui...");
		setLayout(new BorderLayout());
		outputTextbox = new TextArea();
		add("Center", outputTextbox);
		outputTextbox.setEditable(false);
		inputField = new TextField();
		add("South", inputField);

		// register listener so that we are informed whenever a new chat message
		// is received (observer pattern)

		chatClient.addLineListener(this);

		pack();
		setVisible(true);
		inputField.requestFocus();

		this.chatClient = chatClient;
	}

	/**
	 * this method gets called every time a new message is received (observer
	 * pattern)
	 */
	public void newChatLine(String line) {
		outputTextbox.setForeground(new Color(0,0,0));
		outputTextbox.append(line);
	}

	/**
	 * handles AWT events (enter in textfield and closing window)
	 */
	public boolean handleEvent(Event e) {
		if ((e.target == inputField) && (e.id == Event.ACTION_EVENT)) {
			//chatClient.send((String) e.arg);

			chatClient.send(new TextMessage("send",(String) e.arg,"","",""));
			inputField.setText("");
			return true;
		} else if ((e.target == ((Gui) this)) && (e.id == Event.WINDOW_DESTROY)) {
			if (chatClient != null)
				chatClient.stop();
			setVisible(false);
			System.exit(0);
			return true;
		}
		return super.handleEvent(e);
	}
}



public class Gui extends  Gui$$GUI  {

	/**
	 * this method gets called every time a new message is received (observer
	 * pattern)
	 */
	public void newChatLine(String line) {
		
		super.newChatLine("Black: "+line);
		

	}
      // inherited constructors



	/**
	 * creates layout
	 * 
	 * @param title
	 *            title of the window
	 * @param chatClient
	 *            chatClient that is used for sending and receiving messages
	 */
	public Gui ( String title, Client chatClient ) { super(title, chatClient); }


}