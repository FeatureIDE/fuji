package client;

import java.io.IOException;

import common.TextMessage;

/**
 * TODO description
 */
public class Client {
	protected void handleIncomingMessage(Object msg) throws IOException {
		original(msg);
		if (msg instanceof TextMessage)	
			System.out.println(((TextMessage) msg).getContent());
	}
}