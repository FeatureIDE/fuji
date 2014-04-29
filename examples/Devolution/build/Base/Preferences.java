package Base;import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;



/**
 * @author Marcel Jaeschke
 * @since 1.6
 */
public class Preferences extends PreferencesEntry {
	/**
	 * The serial number.
	 */
	private static final long serialVersionUID = -8581141559103897169L;
	// GUI
	private final JLabel lbOption = new JLabel();
	private final JLabel lbElement = new JLabel();
	
	/**
	 * The default constructor.
	 */
	public Preferences () {
		super( "Devolution" );
  	}
	/* (non-Javadoc)
	 * @see miscellaneous.PreferencesPanel#initContent()
	 */
 	 protected void initContent () {
		addHeadline( ((Preferences) this).lbOption );
		String[] languages = { "Deutsch", "English" };
		final JComboBox cnLanguages = new JComboBox( languages );
		cnLanguages.setToolTipText( "Select the language in which the program appears." );
		addElement( ((Preferences) this).lbElement , cnLanguages );
		addSeperator();
	}
	/* (non-Javadoc)
	 * @see miscellaneous.PreferencesPanel#checkInput()
	 */
	public boolean checkInput () {
		// TODO Auto-generated method stub
		return true;
	}
	/* (non-Javadoc)
	 * @see miscellaneous.MultilingualListener#changeLanguage()
	 */
	public void changeLanguage () {
		PreferencesEntry.setHeadlineText( "Option", ((Preferences) this).lbOption );
		PreferencesEntry.setLabelText( "Language", ((Preferences) this).lbElement );
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed ( ActionEvent e ) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see PreferencesEntry#ok()
	 */
	protected void ok(){}

	/*
	 * (non-Javadoc)
	 * 
	 * @see PreferencesEntry#apply()
	 */
	protected void apply(){}

	/*
	 * (non-Javadoc)
	 * 
	 * @see PreferencesEntry#cancel()
	 */
	protected void cancel(){}
}