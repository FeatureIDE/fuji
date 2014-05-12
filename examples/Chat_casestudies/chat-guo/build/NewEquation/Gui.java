
package NewEquation;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Color;
import java.awt.Panel;
import java.awt.TextField;

import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.BadLocationException;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.text.StyleConstants;



/**
 * simple AWT gui for the chat client
 */
abstract class Gui$$Gui extends Frame implements ChatLineListener {

	private static final long serialVersionUID = 1L;

	protected JTextPane outputTextbox;
	
	protected DefaultStyledDocument doc;
	protected StyleContext sc;
	protected Panel panel;

	protected TextField inputField;

	protected Client chatClient;

	
	public Gui$$Gui(Client chatClient) {
		
		System.out.println("starting gui...");
		this.setLocation(400, 300);
		this.setSize(500, 300);
		
		initAtoms();
		initLayout();
		
		// register listener so that we are informed whenever a new chat message
		// is received (observer pattern)
		chatClient.addLineListener(this);
		setVisible(true);
		inputField.requestFocus();
		this.chatClient = chatClient;
	}
	
	public void initAtoms(){
	
		sc = new StyleContext();
		doc = new DefaultStyledDocument(sc);
		outputTextbox = new JTextPane(doc);
		outputTextbox.setBackground(new Color(240, 240, 240));
		outputTextbox.setEditable(false);
		inputField = new TextField();
		panel = new Panel();
		panel.setLayout(new BorderLayout());
	}
	
	public void initLayout(){
		
		setLayout(new BorderLayout());
		panel.add("Center", inputField);

		add("Center", new JScrollPane(outputTextbox));
		add("South", panel);
		
	}

	/**
	 * this method gets called every time a new message is received (observer
	 * pattern)
	 */
	public void newChatLine(TextMessage msg) {		
		
		try {
			doc.insertString(doc.getLength(), (msg.getHead()
					+ msg.getContent() + "\n"), attributEinstellen(msg));
		} catch (BadLocationException e) {
			
			e.printStackTrace();
		}
		
	}

	/**
	 * handles AWT events (enter in textfield and closing window)
	 */
	public boolean handleEvent(Event e) {
		if ((e.target == inputField) && (e.id == Event.ACTION_EVENT)) {
			//chatClient.send(new TextMessage((String) e.arg));
			chatClient.send(nachrichtErstellung((String) e.arg));
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
	
	public TextMessage nachrichtErstellung(String text){
	
		return new TextMessage(text);
	}
	
	public SimpleAttributeSet attributEinstellen(TextMessage msg){
		
		SimpleAttributeSet aset = new SimpleAttributeSet();
		return aset;
	}
}



public class Gui extends  Gui$$Gui  {

	public JComboBox cbColor;
	
	public void initAtoms(){
	
		super.initAtoms();
		cbColor = new JComboBox();
		cbColor.addItem("red");
		cbColor.addItem("black");
		cbColor.addItem("blue");
		cbColor.addItem("green");
		cbColor.addItem("orange");
		
	}
	
	public void initLayout(){
	
		super.initLayout();
		panel.add("West", cbColor);
		
	}
	public Color getColor(){
		Color sColor;
		switch (cbColor.getSelectedIndex()) {
		case 0:
			sColor = Color.red;
			break;
		case 1:
			sColor = Color.black;
			break;
		case 2:
			sColor = Color.blue;
			break;
		case 3:
			sColor = Color.green;
			break;
		case 4:
			sColor = Color.orange;
			break;
		default:
			sColor = Color.black;
		}
		return sColor;
	}
	
	
	public TextMessage nachrichtErstellung(String text){
	
		return new TextMessage(text,getColor());
	}
	
	public SimpleAttributeSet attributEinstellen(TextMessage msg){
		
		SimpleAttributeSet aset = new SimpleAttributeSet();
		StyleConstants.setForeground(aset, msg.getFarbe());
		return aset;
	}
      // inherited constructors



	
	public Gui ( Client chatClient ) { super(chatClient); }
	
}