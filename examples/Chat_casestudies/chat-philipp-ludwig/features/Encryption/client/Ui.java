package client;

public class Ui 
{
	public void onConnected()
	{
		original();
		addText( "Um verschl�sselte Nachrichten zu senden, schreibe: /xor <Text> oder /rot13 <Text>\n" );
	}
}