package client;

public class Client 
{	
	public Boolean write(String aMsg)	
	{
		// �nderung des Benutzernamens beim Server anfragen
		if( aMsg.startsWith("MSG /username") ) aMsg = aMsg.replace("MSG /username", "USERNAME");
		return original(aMsg);
	}
}