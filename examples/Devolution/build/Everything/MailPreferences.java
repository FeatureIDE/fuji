package Everything;import java.awt.event.ActionEvent;



/**
 * @author Marcel Jaeschke
 * @since 1.6
 */
public class MailPreferences extends PreferencesEntry {
	/**
   * The serial number.
   */
  private static final long serialVersionUID = -3143066956838455737L;
  private Modul modul;
	/**
	 * @param modul
	 */
	public MailPreferences ( final Mail modul ) {
		super( modul.getName() );
		this.modul = modul;
		add( new AccountsEntry( modul ) );
	}
	/* (non-Javadoc)
	 * @see miscellaneous.PreferencesEntry#checkInput()
	 */
	public boolean checkInput () {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc)
	 * @see miscellaneous.PreferencesEntry#initContent()
	 */
	protected void initContent () {
		addSeperator();
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed ( ActionEvent e ) {
	// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see miscellaneous.MultilingualListener#changeLanguage()
	 */
	public void changeLanguage () {
	// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see PreferencesEntry#ok()
	 */
	protected void ok(){
		for (int i=0; i<((MailPreferences) this).getChildCount(); i++)
			((PreferencesEntry) ((MailPreferences) this).getChildAt(i)).ok();
		((MailView) modul.getView()).initSidepanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see PreferencesEntry#apply()
	 */
	protected void apply(){
		for (int i=0; i<((MailPreferences) this).getChildCount(); i++)
			((PreferencesEntry) ((MailPreferences) this).getChildAt(i)).apply();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see PreferencesEntry#cancel()
	 */
	protected void cancel(){
		for (int i=0; i<((MailPreferences) this).getChildCount(); i++)
			((PreferencesEntry) ((MailPreferences) this).getChildAt(i)).cancel();
		((MailView) modul.getView()).initSidepanel();
	}
}