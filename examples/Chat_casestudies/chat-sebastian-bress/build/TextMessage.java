


import java.io.Serializable;



/**
 * serializable message that can be send over the sockets between client and
 * server. 
 */
abstract class TextMessage$$Base implements Serializable {

	private static final long serialVersionUID = 1L; //-9161595018411902079L;
	
	private String messagetyp; //message, authenticate, authenticated
	private String content;
	private String color;
    private String password;
	private String reply_status; //ok,fail

	private String encryption_method; 
	
	public TextMessage$$Base(String content) {
		super();
		this.content = content;
	}
	
	public TextMessage$$Base(String messagetyp, String content,String color, String password,String reply_status) {
		super();
		
		this.messagetyp = messagetyp;
		this.content = encryptText( content );
		this.color = color;
		this.password = password;
		this.reply_status = reply_status;
		
	} 
	

	public String getContent() {
		return decryptText(content);
	}
	
	public String getMessagetyp() {
		return messagetyp;
	}
	
	public String getColor() {
		return color;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getReply_status() {
		return reply_status;
	}
	
	
	String encryptText(String text){
		//TODO
		
		return text;
	}
	
	String decryptText(String text){
		//TODO
		
		return text;
	}
	
}



public class TextMessage extends  TextMessage$$Base  {


	String encryptText(String text){
		//TODO
				//TODO
		String encrypted_text="";
		
		//Encryption Mode A (Reverse Order)

			
			for(int i=text.length()-1;i>=0;i--){
				
				encrypted_text += text.charAt(i);
				
			}
			//encryptionmode B
	
	    System.out.println("encrypt Messsage \""+text+"\" to \""+encrypted_text+"\"");
			
		return encrypted_text;
	}
	
	String decryptText(String text){
		
		String decrypted_text="";
		
		//Encryption Mode A (Reverse Order)		
		
            for(int i=text.length()-1;i>=0;i--){
				
				decrypted_text += text.charAt(i);
				
			}
			
		System.out.println("decrypt Messsage \""+text+"\" to \""+decrypted_text+"\"");
		
		return decrypted_text;
	}
      // inherited constructors

 
	
	public TextMessage ( String content ) { super(content); }
	
	public TextMessage ( String messagetyp, String content,String color, String password,String reply_status ) { super(messagetyp, content, color, password, reply_status); }
	
	
}