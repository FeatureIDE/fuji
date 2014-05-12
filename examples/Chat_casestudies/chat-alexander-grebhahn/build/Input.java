

import java.util.Scanner;



class Input implements Runnable{

	Client chatClient;
	
	public Input(Client chatClient){
		this.chatClient = chatClient;
	}
	
	public void run() {
		while(true){
			System.out.print("Eingabe:");
			Scanner sc = new Scanner(System.in);
			String outPut = sc.next();
			chatClient.send(outPut);
		}
	}
	
	
	

}