package Base;import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



/**
 * @author Marcel Jaeschke
 * @since 1.6
 */
public class AboutDialog {
	private final JPanel credits = new JPanel( new GridLayout( 0, 2 ) );

	/**
	 * Create an about dialog for this program.
	 * 
	 * @param program The program which use the Gui.
	 * @param owner The fram who was calling this dialog.
	 */
	public AboutDialog ( final SettableProgram program, final JFrame owner ) {
		final JPanel panel = new JPanel( new BorderLayout() );
		final JPanel main = new JPanel( new GridLayout( 0, 1 ) );
		final JLinkLabel linkLabel = new JLinkLabel( "wwwiti.cs.uni-magdeburg.de", JLabel.CENTER ); //$NON-NLS-1$
		panel.add( new JLabel( ProgramImages.LOGO ), BorderLayout.NORTH );
		main.add( new JLabel( String.format( "<html><body><font size=\"+1\"><b>%s %s</b></font></body></html>", program.getProgramName(), program.getProgramVersion() ), JLabel.CENTER ) ); //$NON-NLS-1$
		main.add( new JLabel( String.format( "<html><body><b>%s</b></body></html>", program.getSubtitle() ), JLabel.CENTER ) ); //$NON-NLS-1$
		main.add( Box.createHorizontalGlue() );
		main.add( new JLabel( "Faculty of Computer Science / Lecture \"EPMD\" WS 07/08", JLabel.CENTER ) );
		try {
			linkLabel.setUri( new URI( "http", "wwwiti.cs.uni-magdeburg.de", "/iti_db/lehre/epmd/", null ) ); //$NON-NLS-1$ //$NON-NLS-2$
		} catch ( URISyntaxException e ) {
			e.printStackTrace();
		}
		main.add( linkLabel );
		main.add( new JLabel( "Copyright © 2008 Marcel Jaeschke, Hinrich Harms & Stephan Kauschka", JLabel.CENTER ) ); //$NON-NLS-1$
		main.add( Box.createHorizontalGlue() ); 
		panel.add( main, BorderLayout.CENTER );
		addCredit( "Base & Addressbook", "Marcel Jaeschke", "marcel.jaeschke@st.ovgu.de" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addCredit( "Messenger", "Hinrich Harms", "hinrich.harms@st.ovgu.de" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addCredit( "Mail-Client", "Stephan Kauschka", "stephan.kauschka@st.ovgu.de" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		panel.add( this.credits, BorderLayout.SOUTH );
		JOptionPane.showMessageDialog( owner, panel, String.format( "About %s", program.getProgramName() ), JOptionPane.PLAIN_MESSAGE, null );
	}
	private void addRow ( final String name, final String value ) {
		final JLabel nameLabel = new JLabel( name + ":  ", JLabel.RIGHT ); //$NON-NLS-1$
		((AboutDialog) this).credits.add( nameLabel );
		((AboutDialog) this).credits.add( new JLabel( value ) );
	}
	private void addCredit ( final String name, final String value, final String link ) {
		final JLinkLabel linkLabel = new JLinkLabel( link );
		addRow( name, value );
		((AboutDialog) this).credits.add( new JLabel() );
		try {
			linkLabel.setUri( new URI( "mailto", link, null ) ); //$NON-NLS-1$
		} catch ( URISyntaxException e ) {
			System.err.println( "Coudn't use the link uri." );
		}
		((AboutDialog) this).credits.add( linkLabel );
	}
}