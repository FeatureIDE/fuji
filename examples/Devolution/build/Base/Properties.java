package Base;import java.util.MissingResourceException;
import java.util.ResourceBundle;



/**
 * Properties Class - Used for non-multilingual strings.
 * 
 * @author Marcel Jaeschke
 */
public class Properties {
	// private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( Resources.class.getPackage().getName() + ".lang.const" ); //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( "resources.lang.const" ); //$NON-NLS-1$

	private Properties () {}
	/**
	 * Return a non-multlingual string which was requested by the key.
	 * @param key The key to the non-multlingual string.
	 * @return The non-multlingual string.
	 */
	public static String getString ( String key ) {
		try {
			return Properties.RESOURCE_BUNDLE.getString( key );
		} catch ( MissingResourceException e ) {
			return "!" + key; //$NON-NLS-1$
		}
	}
}