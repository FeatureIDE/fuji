package client;

import output.*;

public class PluginManager {
	public PluginManager(Client client) {
		addLineListener(new ConsoleOutput());
	}
}