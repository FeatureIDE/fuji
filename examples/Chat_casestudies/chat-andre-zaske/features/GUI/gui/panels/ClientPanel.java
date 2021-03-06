package gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;

import cutil.RandomNameGen;

import engine.ChatClient;
import engine.ChatClient.ChatClientEvent;
import engine.ChatClient.ChatClientStatus;
import engine.ChatClient.ChatClientListener;

import gui.Gui;
import gui.util.ChatStyles;
import gui.util.GuiTLH;
import gui.util.ImagePanel;

import network.server.packets.DataPacket;
import network.server.packets.JoinMessage;
import network.server.packets.TextMessage;
import network.server.packets.QuitMessage;

public class ClientPanel extends AbstractCloseTabPanel implements
		ChatClientListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4090642373194281416L;

	private ChatClient chatClient;
	// JContainers
	private JTextField txtAdress;
	private JSpinner spinnerPort;
	private JTextField txtChatName;
	private JPasswordField passwordField;
	private JPanel northPanel;
	private JPanel centerPanel;
	private JTextArea taSendText;
	private JTextPane tpShowText;
	private ChatStyles chatStyles;
	private JButton btGeneralHandler;

	public ClientPanel(Gui g) {
		super(new BorderLayout(), g);
		chatStyles = new ChatStyles();
		init();
		initView();
	}

	private void init() {
		setName("Client");
		setToolTipText("A Clientpanel");
		setIcon(GuiTLH.loadImageIcon("Client.png"));
	}

	private void initView() {
		removeAll();
		// Splash Preview
		northPanel = new JPanel();
		ImagePanel imgView = new ImagePanel(550, 300);
		imgView.setImage(GuiTLH.loadImage("epmd_client.png"));
		northPanel.add(imgView);

		// Layout
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		// Adress-Field
		txtAdress = new JTextField();
		txtAdress.setText("localhost");
		txtAdress.setColumns(20);
		panel.add(txtAdress);

		// RigidArea
		panel.add(Box.createRigidArea(new Dimension(20, 0)));

		// Port-Field
		spinnerPort = new JSpinner(new SpinnerNumberModel(8080, 1024, 49151, 1));
		panel.add(spinnerPort);

		// RigidArea
		panel.add(Box.createRigidArea(new Dimension(20, 0)));

		// ChatName-Field
		txtChatName = new JTextField();
		txtChatName.setText(RandomNameGen.getRandomName());
		txtChatName.setColumns(20);
		panel.add(txtChatName);

		// RigidArea
		panel.add(Box.createRigidArea(new Dimension(20, 0)));

		// Button StartServer
		btGeneralHandler = new JButton("Client starten");
		btGeneralHandler.addActionListener(this);
		btGeneralHandler.setActionCommand("btStartClient");
		panel.add(btGeneralHandler);

		// Border
		centerPanel = new JPanel();
		centerPanel.add(panel);

		// Finalize
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);

	}

	private void initLoginView() {
		// Clear CenterPanel
		centerPanel.removeAll();

		// Layout
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		// ChatName-Field
		txtChatName.setEnabled(false);
		panel.add(txtChatName);

		// RigidArea
		panel.add(Box.createRigidArea(new Dimension(20, 0)));

		// Passwort-Field
		passwordField = new JPasswordField(20);
		passwordField.setText(chatClient.getName());
		passwordField.setActionCommand("btLogin");
		passwordField.addActionListener(this);
		panel.add(passwordField);

		// RigidArea
		panel.add(Box.createRigidArea(new Dimension(20, 0)));

		// Button StartServer
		btGeneralHandler = new JButton("Login");
		btGeneralHandler.addActionListener(this);
		btGeneralHandler.setActionCommand("btLogin");
		panel.add(btGeneralHandler);

		// Border
		centerPanel.add(panel);

		validate();
		repaint();
	}

	private void initChatView() {
		// Clear CenterPanel
		centerPanel.removeAll();

		// TextPane and Scroll
		tpShowText = new JTextPane();
		tpShowText.setEditable(false);
		tpShowText.setBounds(0, 0, 550, 300);
		JScrollPane jScrollPane = new JScrollPane(tpShowText);
		jScrollPane.setPreferredSize(new Dimension(550, 300));
		// North add
		centerPanel.add(jScrollPane, BorderLayout.CENTER);

		// Load ClientPlugin Panels
		JPanel extensionPanel = new JPanel();
		modifyExtensionPanel(extensionPanel);
		centerPanel.add(extensionPanel, BorderLayout.EAST);

		// North finalize
		northPanel.removeAll();

		// South Panel
		JPanel southPanel = new JPanel(new BorderLayout());

		// TextArea SendMessage
		taSendText = new JTextArea();
		taSendText.setRows(5);
		taSendText.setMaximumSize(taSendText.getPreferredSize());
		taSendText.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				checkLength(arg0);
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				checkLength(arg0);
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				checkLength(arg0);
			}

			private void checkLength(DocumentEvent de) {
				if (de.getDocument().getLength() > 0) {
					btGeneralHandler.setEnabled(true);
				} else {
					btGeneralHandler.setEnabled(false);
				}
			}
		});
		JScrollPane textFieldScroll = new JScrollPane(taSendText);

		// Button SendMSF
		btGeneralHandler = new JButton("Senden");
		btGeneralHandler.addActionListener(this);
		btGeneralHandler.setEnabled(false);
		btGeneralHandler.setActionCommand("btSendMSG");

		// South finalize
		southPanel.add(textFieldScroll, BorderLayout.CENTER);
		southPanel.add(btGeneralHandler, BorderLayout.EAST);

		// Finalize
		add(southPanel, BorderLayout.SOUTH);
		validate();
		repaint();
	}

	private void modifyExtensionPanel(JPanel panel) {

	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEntering() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLeaving() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClose() {
		if (chatClient != null) {
			chatClient.removeChatClientListener(this);
			chatClient.stop();
		}
	}

	@Override
	public void onTabButtonClose() {
		getGui().removeTabPanel(this);
	}

	public void actionPerformed(ActionEvent e) {
		String acom = e.getActionCommand();
		if (acom.equals("btStartClient")) {
			JButton btStartServer = (JButton) e.getSource();
			int port = ((Number) spinnerPort.getValue()).intValue();
			btStartServer.setEnabled(false);
			spinnerPort.setEnabled(false);
			txtChatName.setEnabled(false);
			txtAdress.setEnabled(false);
			chatClient = new ChatClient(txtAdress.getText(), port,
					txtChatName.getText());
			chatClient.addChatClientListener(this);
		} else if (acom.equals("btSendMSG")) {
			chatClient.sendTextMessage(taSendText.getText());
			taSendText.setText("");
		} else if (acom.equals("btLogin")) {
			// Disable Button and Field
			passwordField.setEnabled(false);
			btGeneralHandler.setEnabled(false);
			chatClient.login(new String(passwordField.getPassword()));
		}
	}

	@Override
	public void handleClientChatStatusChanged(ChatClientEvent clientEvent) {
		ChatClientStatus status = clientEvent.getChatClientStatus();
		if (status == ChatClientStatus.CONNECTIONESTABLISHED) {
			initLoginView();
		} else if (status == ChatClientStatus.LOGINSUCESSFUL) {
			initChatView();
		} else if (status == ChatClientStatus.CONNECTIONLOST) {
			JOptionPane.showMessageDialog(null,
					"Die Verbindung zum Server wurde beendet.",
					"Verbindungsabbruch", JOptionPane.ERROR_MESSAGE);
			initView();
			validate();
			repaint();
		} else if (status == ChatClientStatus.CONNECTIONNOTPOSSIBLE) {
			JOptionPane
					.showMessageDialog(null,
							"Verbindung zum Server nicht m�glich.",
							"Verbindungsversuch gescheitert",
							JOptionPane.ERROR_MESSAGE);
			btGeneralHandler.setEnabled(true);
			spinnerPort.setEnabled(true);
			txtChatName.setEnabled(true);
			txtAdress.setEnabled(true);
		}
	}

	public void handlePacketIn(DataPacket packet) {
		Document doc = tpShowText.getDocument();
		Style style = chatStyles.getDefaultStyle();
		String msg = "";
		boolean handlePacket = true;
		if (packet instanceof TextMessage) {
			TextMessage txtMsg = (TextMessage) packet;
			msg = txtMsg.getName() + ": " + txtMsg.getContent();
		} else if (packet instanceof JoinMessage) {
			JoinMessage joinMSG = (JoinMessage) packet;
			msg = "-> " + joinMSG.getName() + " joins the Chat.";
		} else if (packet instanceof QuitMessage) {
			QuitMessage quitMSG = (QuitMessage) packet;
			msg = "-> " + quitMSG.getName() + " leaves the Chat.";
		} else {
			handlePacket = false;
		}
		if (handlePacket) {
			msg += "\n";
			try {
				doc.insertString(doc.getLength(), msg, style);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			tpShowText.setCaretPosition(tpShowText.getDocument().getLength());
		}
	}
}