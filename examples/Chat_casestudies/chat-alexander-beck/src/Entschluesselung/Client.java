/**
 * simple chat client
 */
public class Client implements Runnable {

	public void send(String line) {
		original(Verschl�sselung(line));
	}

	private String Verschl�sselung(String text){
		String verschl�sselterText="";
		//--------1. Verschl�sselung-----------
		for(int i=text.length()-1;i>=0;i--){
			verschl�sselterText += text.charAt(i);
		}
		//--------1. Verschl�sselung Ende------

		//--------2. Verschl�sselung-----------
		String[] verschl�sselterTextTemp = verschl�sselterText.split(" ");
		verschl�sselterText="";
		for(int i=verschl�sselterTextTemp.length-1;i>=0;i--){
			verschl�sselterText += verschl�sselterTextTemp[i]+(i>0?" ":"");
			
		}
		//--------2. Verschl�sselung Ende------

		return verschl�sselterText;
	}

}
