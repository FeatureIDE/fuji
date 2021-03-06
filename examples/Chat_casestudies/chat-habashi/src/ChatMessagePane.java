
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;



public class ChatMessagePane extends JPanel {
	private static final long serialVersionUID = 1L;

	public ChatMessagePane() {
		setLocation(0, 0);
		setSize(795, 420);
		setLayout(new BorderLayout());
		drawComponents();
	}

	private void drawComponents() {
		addChatTA();
	}

	private JTextArea ta;

	private void addChatTA() {
		ta = new JTextArea();
		ta.setLineWrap(true);
		ta.setEditable(false);
		JScrollPane sa = new JScrollPane(ta);
		sa.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		//ta.append("normaler Chat");
		add(sa);
	}

	public void addLineToTA(String line) {
		ta.append(line);
		ta.setCaretPosition(ta.getText().length());
		repaint();
	}

}