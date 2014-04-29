package server;

public class Client 
{
	protected Boolean handleMessage( String aMsg )
	{		
		// �berpr�fen, ob eine private Nachricht vorliegt
		// Format: PRIVMSG Empf�nger Nachricht
		if( aMsg.startsWith("PRIVMSG") )			
		{
			aMsg = aMsg.replace("PRIVMSG ", "");
			
			// Empf�ngername
			String recv = aMsg.substring(0, aMsg.indexOf(" ") );
			aMsg = aMsg.substring(recv.length());
			
			// Absender
			String sender = mUserName;
			if( sender.isEmpty() ) sender = mSocket.getInetAddress().toString();
			
			// �berpr�fen, ob der Empf�nger bekannt ist.
			if( mServer.isUserNameAvailable(recv) )
			{
				write( "SERVERMSG Unbekannter Empf�nger: " + recv + "\r\n" );				
			}
			else if( !mServer.sendPrivateMessage( recv, sender, aMsg ) )
			{
				write( "SERVERMSG Senden der Nachricht fehlgeschlagen!\r\n" );
			}				
			return true;
		}
		// Falls keine private Nachricht vorliegt: Mit der �blichen Behandlung verfahren.
		else return original(aMsg);		
	}	
}