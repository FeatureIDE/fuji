package Everything;import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class InstantMessenger extends Modul implements ActionListener {
    protected static final String NAME = "Instant Messenger";
    protected final Devolution base;

    /**
     * Default constructor.
     * 
     * @param base The base which the modul belong to.
     */
    public InstantMessenger ( Devolution base ) {
        super( base );
        this.base = base;
        this.view = new IMView( this, this.base );
    }
    
    public JPanel getContent () {
        return new JPanel();
    }
    
    public JPanel getToolbar () {
        return new JPanel();
    }
    
    public Component getSettingTab () {
        return new JPanel();
    }
    
    public JMenu getMenu () {
        JMenu menu = new JMenu( InstantMessenger.NAME );
        menu.setMnemonic( KeyEvent.VK_E );
        return menu;
    }
    
    public String getName () {
        return InstantMessenger.NAME;
    }
    
    public Setting getSetting () {
        return base.getSetting();
    }
    
    public void actionPerformed ( ActionEvent e ) {}
	/* (non-Javadoc)
	 * @see de.rephstone.rezar.base.Modul#close()
	 */
	public void close () {
		// TODO Auto-generated method stub
	}
}