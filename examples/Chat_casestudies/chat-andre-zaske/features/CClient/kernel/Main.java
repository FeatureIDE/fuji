package kernel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import engine.ChatClient.ChatClientListener;

import network.server.packets.DataPacket;
import network.server.packets.JoinMessage;
import network.server.packets.TextMessage;
import engine.ChatClient;
import engine.InputThread;
import engine.ChatClient.ChatClientEvent;
import engine.ChatClient.ChatClientStatus;

import cutil.RandomNameGen;

/**
 * TODO description
 */
public class Main implements ChatClientListener, engine.InputThread.InputThreadListener {
	private BufferedReader consoleInput;
	private ChatClient chatClient;
	private String chatName;

	private void startup() {
		// Welcome Message
				System.out.println("<a> Starting EPMD Client for Console ...");
	}

	private void init() {
		consoleInput = new BufferedReader(new InputStreamReader(System.in));
	}

	private void finish() {
		startCClient();
	}

	public void startCClient() {
		// Asking for Server address
		String serverAddress = getInputChecked("Server-Address",
				"Use L for localhost.", "[\\w\\d.]+");
		if (serverAddress.equals("L")) {
			serverAddress = "localhost";
		}

		// Asking for Server port
		String sPort = getInputChecked("Server-Port",
				"Use S for Standardport 8080.", "[\\dS]+");
		int serverPort = 8080;
		if (!sPort.equals("S")) {
			serverPort = Integer.valueOf(sPort);
		}

		// Asking for ChatName
		String randName = RandomNameGen.getRandomName();
		chatName = getInputChecked("Chat-Username", "Use D for " + randName,
				"[\\w\\d.]+");
		if (chatName.equals("D")) {
			chatName = randName;
		}

		// Showing Input Results
		System.out.println("<i> Connecting to -> " + serverAddress + ":"
				+ serverPort + " as " + chatName);

		chatClient = new ChatClient(serverAddress, serverPort, chatName);
		chatClient.addChatClientListener(this);
	}

	private void initLoginView() {
		// Asking for Passwort
		String strPasswort = getInput("Passwort", "Use A for auto(" + chatName
				+ ")");
		if (strPasswort.equals("A")) {
			strPasswort = chatName;
		}
		chatClient.login(strPasswort);
	}

	private void initChatView() {
		// Info Connection
		System.out.println("<i> - Connection established ...");
		System.out.println("<h> - Use Q to exit Client.");
		// Init Input Stream
		InputThread inputThread = new InputThread();
		inputThread.setInputThreadListener(this);
		Thread thread = new Thread(inputThread);
		thread.start();
	}

	private String getInput(String getWhat, String hint) {
		System.out.println("<?> Please enter " + getWhat + "!");
		// Showing Hint ?
		if (hint.length() > 1) {
			System.out.println("<h> " + hint);
		}
		System.out.print("=>");
		String strOut = "";
		try {
			strOut = consoleInput.readLine();
		} catch (IOException e) {
			System.out.println("<e> Error reading " + getWhat
					+ "\n <a> Shuting down...");
			System.exit(0);
		}
		System.out.println("");
		return strOut;
	}

	private String getInputChecked(String getWhat, String hint, String regex) {
		String strOut = getInput(getWhat, hint);
		while (!strOut.matches(regex)) {
			System.out.println("<w> Input is incorrect please try again.");
			strOut = getInput(getWhat, "");
		}
		return strOut;
	}

	@Override
	public void handleClientChatStatusChanged(ChatClientEvent clientEvent) {
		ChatClientStatus status = clientEvent.getChatClientStatus();
		if (status == ChatClientStatus.CONNECTIONESTABLISHED) {
			initLoginView();
		} else if (status == ChatClientStatus.LOGINSUCESSFUL) {
			initChatView();
		} else if (status == ChatClientStatus.CONNECTIONLOST) {
			System.out
					.println("<e> Connection to the server is broken.\n<a> Shuting down...");
			chatClient.stop();
			System.exit(0);

		} else if (status == ChatClientStatus.CONNECTIONNOTPOSSIBLE) {
			System.out.println("<e> Connection attempt fails.");
			System.out.println("<a> Try again ...");
			startCClient();
		}
	}

	@Override
	public void handlePacketIn(DataPacket packet) {
		String msg = "";
		String style = "";
		if (packet instanceof TextMessage) {
			TextMessage txtMsg = (TextMessage) packet;
			style = txtMsg.getStyleName();
			msg = txtMsg.getName() + ": " + txtMsg.getContent();
		} else if (packet instanceof JoinMessage) {
			JoinMessage joinMSG = (JoinMessage) packet;
			style = "blue";
			msg = joinMSG.getName() + " joins the Chat.";
		}
		// Showing ColorInfo
		msg = "" + style + "> " + msg;
		System.out.println(msg);

	}

	@Override
	public void newInput(String msg) {
		chatClient.sendTextMessage(msg);
	}

	@Override
	public void shuttingDown() {
		System.out.println("<a> Shuting down...");
		chatClient.stop();
		System.exit(0);
	}
}