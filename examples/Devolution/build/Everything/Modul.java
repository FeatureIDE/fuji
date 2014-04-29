package Everything;

/**
 * @author Marcel Jaeschke
 * @since 1.6
 */
public abstract class Modul implements MultilingualListener {
	/**
	 * The base which used/belongs the model.
	 */
	protected final Devolution base;
	/**
	 * The GUI-container of model.
	 */
	protected ModelView view;

	/**
	 * Default constructor.
	 * 
	 * @param base The base which the modul belong to.
	 */
	public Modul ( final Devolution base ) {
		this.base = base;
	}
	/**
	 * Called if the programm will be closed.
	 */
	public abstract void close ();
	/**
	 * Return the base of the modul.
	 * 
	 * @return The base of the modul.
	 */
	public Devolution getBase () {
		return ((Modul) this).base;
	}
	/**
	 * Return the name of the modul.
	 * 
	 * @return The name of the modul.
	 */
	public abstract String getName ();
	/**
	 * Return the GUI-container of modul.
	 * 
	 * @return The GUI-container of modul.
	 */
	public ModelView getView () {
		return ((Modul) this).view;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see miscellaneous.MultilingualListener#changeLanguage()
	 */
	public void changeLanguage () {
		((Modul) this).view.changeLanguage();
	}
}