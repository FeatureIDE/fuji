



/*
 * 
 * bei der VerschlÃ¼sselung handelt es sich nicht wirklich um eine POT13 VerschlÃ¼sselung, 
 * weil auch Zahlen zugelassen sind
 * 
 */
public class POT13 extends Decryption{
	
	private int offset = 13;
	
	public String decrypt(String text) {
		String decypted = "";
		for(int i = 0; i < text.length();i++){
			char charAtI = text.charAt(i);
			charAtI = (char)((int)charAtI+offset);
			decypted += charAtI;
		}
		return decypted;
	}

	public String enctypt(String text) {
		String encypted = "";
		for(int i = 0; i < text.length();i++){
			char charAtI = text.charAt(i);
			charAtI = (char)((int)charAtI-offset);
			encypted += charAtI;
		}
		return encypted;
	}

}