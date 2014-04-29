package Everything;

/**
 * Thrown exception for the FileSaver.
 * 
 * @author Marcel Jaeschke
 */
public class SaveException extends Exception {
	/**
	 * Prüfnummer zur Serialisierung.
	 */
	private static final long serialVersionUID = 7198777245697769151L;
	/**
	 * The message which decribed the error in natural speech.
	 */
	private String message = "There was an unexpected and undescribed.";

	/**
	 * Throwable exception without a discribed message.
	 */
	public SaveException () {
		super();
	}
	/**
	 * Throwable exception which print the message which decribed the error in
	 * natural speech.
	 * 
	 * @param message The message which decribed the error in natural speech.
	 */
	public SaveException ( String message ) {
		this();
		System.err.println( "Error during the Saving: " + message );
	}
	/**
	 * Returns the message which decribed the error in natural speech.
	 * 
	 * @return The message which decribed the error in natural speech.
	 */
	public String getMessage () {
		return "Error in the setting: " + ((SaveException) this).message;
	}
}