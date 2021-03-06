
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;





public class ChatClientGUI extends JFrame implements ChatListener , MouseListener{
	
	private static final long serialVersionUID = 7312847584246971781L;
	private JMenuBar menuBar;
	protected JMenu mnChat;
	private JList lstContacts;
	private ChatClient chatClient;
	private List chatFrames;
	
	ChatClientStartDialog ccsd;
	
	private class ContactListModel extends DefaultListModel {
		private static final long serialVersionUID = 345851707387664779L;
		public void updateList(Object[] items) {
			((ContactListModel) this).clear();
			for (int i=0; i< items.length; i++)
				((ContactListModel) this).addElement(items[i]);
		}
	}

	public ChatClientGUI(ChatClient chatClient) {
		this.chatClient=chatClient;
		this.chatClient.addChatListener(this);
		
		setTitle("joChatClient");
		setFont(new Font("Gentium Basic", Font.PLAIN, 13));
		setSize(new Dimension(200, 600));
		
		init();
		
		chatFrames= new ArrayList();
		ccsd =new ChatClientStartDialog(this.chatClient);
		ccsd.setVisible(true);
	}
	
	protected void init() {
		menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.inactiveCaption);
		menuBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setJMenuBar(menuBar);
		
		JMenu mnPreferences = new JMenu("Preferences");
		menuBar.add(mnPreferences);
		
		mnChat = new JMenu("Chat");
		menuBar.add(mnChat);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		
		lstContacts= new JList(new ContactListModel());
		lstContacts.setBackground(SystemColor.desktop);
		lstContacts.setAutoscrolls(true);
		lstContacts.addMouseListener(((ChatClientGUI) this));
		getContentPane().add(lstContacts, BorderLayout.CENTER);
		
		JLabel lblFriendlist = new JLabel("Friendlist");
		lblFriendlist.setOpaque(true);
		lblFriendlist.setBackground(SystemColor.activeCaptionBorder);
		lblFriendlist.setHorizontalAlignment(SwingConstants.CENTER);
		lblFriendlist.setFont(new Font("Impact", Font.PLAIN, 14));
		getContentPane().add(lblFriendlist, BorderLayout.NORTH);
	}


	public JList getLstContacts() {
		return lstContacts;
	}


	
	public void newChatLine(MessageObject msgo) {
		System.out.println("received: "+msgo.getMessageType());
		if (msgo.getMessageType().equals(MessageType.AUTH_OK)) {
			JOptionPane.showMessageDialog(((ChatClientGUI) this), msgo.getMessage(), "Login successful.", JOptionPane.INFORMATION_MESSAGE);
			ccsd.setVisible(false);
			ccsd=null;
		} 
		else if (msgo.getMessageType().equals(MessageType.ERROR)) {
			JOptionPane.showMessageDialog(((ChatClientGUI) this), msgo.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} 
		else if (msgo.getMessageType().equals(MessageType.AUTH_ERROR)) {
			System.out.println("received auth error");
			JOptionPane.showMessageDialog(((ChatClientGUI) this), msgo.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			chatClient.setUsername("");
		}
		else if (msgo.getMessageType().equals(MessageType.TEXT)) {
			String[] recipients= msgo.getRecipients();
			if (recipients.length==1) {
				for (int i=0; i<chatFrames.size(); i++) {
					ChatFrame cf= (ChatFrame) chatFrames.get(i);
					if (cf.getRecipient().equals(msgo.getSender())) {
						cf.appendConversationWithColor(msgo.getMessage(), msgo.getColor());
						return;
					}
				}
				ChatFrame cf =new ChatFrame(msgo.getSender() ,chatClient);
				chatFrames.add(cf);
				cf.appendConversationWithColor(msgo.getMessage(), msgo.getColor());
				cf.setVisible(true);
			}
		}
		else if (msgo.getMessageType().equals(MessageType.USER_ON)) {
			((ContactListModel) lstContacts.getModel()).updateList(msgo.getRecipients());
		}
	}


	
	public void mouseClicked(MouseEvent e) {
		if (e.getSource()==lstContacts && e.getClickCount()==2 && e.getButton()==1) {
			String selectedFriend= lstContacts.getSelectedValue().toString();
			for (int i=0; i<chatFrames.size(); i++) {
				ChatFrame cf= (ChatFrame) chatFrames.get(i);
				if (cf.getRecipient().equals(selectedFriend)) {
					cf.requestFocus();
					cf.setVisible(true);
					return;
				}
			}
			ChatFrame cf =new ChatFrame(selectedFriend, chatClient);
			chatFrames.add(cf);
			cf.setVisible(true);
		}
	}


	
	public void mouseEntered(MouseEvent e) {
	}


	
	public void mouseExited(MouseEvent e) {
	}


	
	public void mousePressed(MouseEvent e) {
	}


	
	public void mouseReleased(MouseEvent e) {
	}
}