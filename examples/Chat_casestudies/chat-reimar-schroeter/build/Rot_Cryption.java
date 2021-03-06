




public class Rot_Cryption extends Cryptography{

	public Rot_Cryption(Cryptography rek){
		this.rek = rek;
	}
	
	public Rot_Cryption(){
		
	}
	
	/**
	 * @see Cryptography
	 */
	public String decrypt(String msg) {
		if(rek == null){
			return encrypt_decrypt_Rot(msg, -12);
		}
		return encrypt_decrypt_Rot(rek.decrypt(msg), -12);
	}

	/**
	 * @see Cryptography
	 */
	public String encrypt(String msg) {
		if(rek ==  null){
			return encrypt_decrypt_Rot(msg, 12);
		}
		return rek.encrypt(encrypt_decrypt_Rot(msg, 12));
	}
	
	/**
	 * decrypt/encrypt the given text
	 * 
	 * @param text to decrypt/encrypt
	 * @param rot rotate to
	 * @return transformed text
	 */
	private String encrypt_decrypt_Rot(String text, int rot){
		int length = text.length();
		String returnS = "";
		for (int i = 0; i < length; i++) {
			int asc = (int) text.charAt(i);
			asc += rot;
			returnS += ((char) asc);
		}
		return returnS;
	}

}