
package NewEquation;



public class Konsole implements ChatLineListener{

	Konsole(Client client){
		client.addLineListener(this);
	}
	
	public void newChatLine(TextMessage msg) {
		
		System.out.println(msg.getHead() + msg.getContent());
		

	}
	
}