package Base;import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultMutableTreeNode;




/**
 * @author Marcel Jaeschke
 * @since 1.6
 */
public abstract class PreferencesEntry extends DefaultMutableTreeNode implements ActionListener, MultilingualListener {
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = -5584779935239670473L;
	private final String name;
	private final JPanel panel;
	private boolean constructed = false;
	/**
	 * A flag which will only set by the PreferencesPanel itself.
	 */
	private boolean inputCorrect = false;
	/**
	 * The constraints which is used by the layout-manager.
	 */
	private final GridBagConstraints constraints = new GridBagConstraints( GridBagConstraints.RELATIVE, 0, 2, 1, 1D, 0D, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 1, 1 );

	/**
	 * The default constructor.
	 * 
	 * @param name The name of the preferences entry.
	 */
	public PreferencesEntry ( final String name ) {
		super();
		this.name = name;
		this.panel = new JPanel( new GridBagLayout() );
		this.panel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString () {
		return ((PreferencesEntry) this).name;
	}
	/**
	 * The panel which conatins the preferens-interface.
	 * 
	 * @return The panel which conatins the preferens-interface.
	 */
	public JPanel getPanel () {
		return ((PreferencesEntry) this).constructed ? ((PreferencesEntry) this).panel : construct();
	}
	private JPanel construct () {
		initContent();
		changeLanguage();
		((PreferencesEntry) this).constraints.weighty = 1D;
		((PreferencesEntry) this).panel.add( Box.createVerticalGlue(), ((PreferencesEntry) this).constraints );
		((PreferencesEntry) this).constructed = true;
		return ((PreferencesEntry) this).panel;
	}
	/**
	 * Add a headline to the form.
	 * 
	 * @param headlineLabel The headline label.
	 */
	protected void addHeadline ( final JLabel headlineLabel ) {
		((PreferencesEntry) this).constraints.gridwidth = 2;
		((PreferencesEntry) this).constraints.insets.left = 2;
		((PreferencesEntry) this).panel.add( headlineLabel, ((PreferencesEntry) this).constraints );
		((PreferencesEntry) this).constraints.gridwidth = 1;
		((PreferencesEntry) this).constraints.gridy++;
		((PreferencesEntry) this).constraints.insets.left = 12;
	}
	/**
	 * Add a seperator to the form.
	 */
	protected void addSeperator () {
		((PreferencesEntry) this).constraints.gridwidth = 2;
		((PreferencesEntry) this).constraints.insets.left = 2;
		((PreferencesEntry) this).panel.add( new JSeparator(), ((PreferencesEntry) this).constraints );
		((PreferencesEntry) this).constraints.gridwidth = 1;
		((PreferencesEntry) this).constraints.gridy++;
		((PreferencesEntry) this).constraints.insets.left = 12;
	}
	/**
	 * Add a form element and set a link between both components.
	 * 
	 * @param label The label which discriped the component
	 * @param component
	 */
	protected void addElement ( final JLabel label, final JComponent component ) {
		label.setLabelFor( component );
		((PreferencesEntry) this).panel.add( label, ((PreferencesEntry) this).constraints );
		((PreferencesEntry) this).panel.add( component, ((PreferencesEntry) this).constraints );
		((PreferencesEntry) this).constraints.gridy++;
	}
	
	protected void addElements ( final JLabel label, final JComponent[] components ) {
		label.setLabelFor( components [0] );
		((PreferencesEntry) this).panel.add( label, ((PreferencesEntry) this).constraints );
		for (int i = 0; i < components.length; i++) {
			((PreferencesEntry) this).panel.add( components[i], ((PreferencesEntry) this).constraints );
		}
		((PreferencesEntry) this).constraints.gridy++;
	}

	protected void addElement ( final JComponent component ) {
		((PreferencesEntry) this).panel.add( component, ((PreferencesEntry) this).constraints );
		((PreferencesEntry) this).constraints.gridy++;
	}

	/**
	 * Set the test of an label in a headline style.
	 * 
	 * @param text The text of the label.
	 * @param label The label which will be labeled.
	 */
	protected static void setHeadlineText ( final String text, final JLabel label ) {
		label.setText( String.format( "<html><body><b>%s</b></body></html>", text ) );
	}
	/**
	 * Set the test of an label in a form style.
	 * 
	 * @param text The text of the label.
	 * @param label The label which will be labeled.
	 */
	protected static void setLabelText ( final String text, final JLabel label ) {
		label.setText( String.format( "%s:", text ) );
	}
	/**
	 * Create the content of the panel. Please use only the own methodes to add
	 * components, it will lokks better.
	 */
	protected abstract void initContent ();
	/**
	 * Returns the answer of the last check.
	 * 
	 * @return TRUE if the form is complete correctly.
	 */
	public boolean isInputCorrect () {
		return ((PreferencesEntry) this).inputCorrect;
	}
	/**
	 * Check if the form is complete correctly.
	 * 
	 * @return TRUE if the form is complete correctly.
	 */
	public abstract boolean checkInput ();
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.DefaultMutableTreeNode#getUserObject()
	 */
	public Object getUserObject () {
		return ((PreferencesEntry) this).panel;
	}

	/**
	 * when the PreferencesDialog Cancel button was pressed this method will be executed
	 */
	protected abstract void cancel ();

	/**
	 * when the PreferencesDialog Apply button was pressed this method will be executed
	 */
	protected abstract void apply ();

	/**
	 * when the PreferencesDialog Ok button was pressed this method will be executed
	 */
	protected abstract void ok ();
}