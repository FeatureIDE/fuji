public class Connection extends Thread {
	
	
	
	private void handleIncomingMessage(String name, Object msg) {
		 if(msg instanceof TextMessage){
			 String vers = ((TextMessage) msg).getContent();
			 System.out.println("Enc:"+vers);
			 String text = Entschl�sselung(vers);
			 System.out.println("Dec:"+text);
			 original(name, new TextMessage(text)); 
		 }
	}
	
	
	private String Entschl�sselung(String text){
		String entschl�sselterText="";

		//Bei der Art der Verschl�sselung spielt die Reihenfolge der Aufl�sung keine Rolle. 
		//Korrekt w�re aber eigentlich erst 2. dann 1.
		
		//--------1. Entschl�sselung-----------
		for(int i=text.length()-1;i>=0;i--){
			entschl�sselterText += text.charAt(i);
		}
		//--------1. Entschl�sselung Ende------
		
		//--------2. Entschl�sselung-----------
		String[] entschl�sselterTextTemp = entschl�sselterText.split(" ");
		entschl�sselterText="";
		for(int i=entschl�sselterTextTemp.length-1;i>=0;i--){
			entschl�sselterText += entschl�sselterTextTemp[i]+(i>0?" ":"");
			
		}
		//--------2. Entschl�sselung Ende------
		return entschl�sselterText;
	}
}
