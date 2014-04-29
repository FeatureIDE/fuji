package Everything;import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;



/**
 * static class that holds the images for the frontend
 * 
 * @author Christian Spillker
 */
public class ProgramImages {
	// the path to the icons
	//private static final String gfxPath = '/' + ProgramImages.class.getPackage().getName().replace( '.', '/' ) + '/';
	private static final String gfxPath = "/resources/gfx/";
	public static final ImageIcon ABOUT;
	public static final ImageIcon CANCEL;
	public static final ImageIcon EXIT;
	public static final ImageIcon ICON;
	public static final ImageIcon MANUEL;
	public static final ImageIcon LOGO;
	public static final ImageIcon PREFERENCES;
	public static final ImageIcon SAVE_AS;
	public static final ImageIcon CLOSE;
	static {
		ABOUT = ProgramImages.getImageIcon( ProgramImages.gfxPath + "icons/about.png" ); //$NON-NLS-1$
		CANCEL = ProgramImages.getImageIcon( ProgramImages.gfxPath + "icons/cancel.png" ); //$NON-NLS-1$
		EXIT = ProgramImages.getImageIcon( ProgramImages.gfxPath + "icons/exit.png" ); //$NON-NLS-1$
		ICON = ProgramImages.getImageIcon( ProgramImages.gfxPath + "icon.png" ); //$NON-NLS-1$
		MANUEL = ProgramImages.getImageIcon( ProgramImages.gfxPath + "icons/help.png" ); //$NON-NLS-1$
		LOGO = ProgramImages.getImageIcon( ProgramImages.gfxPath + "logo.png" ); //$NON-NLS-1$
		PREFERENCES = ProgramImages.getImageIcon( ProgramImages.gfxPath + "icons/preferences.png" ); //$NON-NLS-1$
		SAVE_AS = ProgramImages.getImageIcon( ProgramImages.gfxPath + "icons/save.png" ); //$NON-NLS-1$
		CLOSE = ProgramImages.getImageIcon( ProgramImages.gfxPath + "icons/close.png" ); //$NON-NLS-1$
	}

	/**
	 * Returns the imageicon of the given path
	 * 
	 * @param imagePath The path for the image in the gfx-directory.
	 * @return the imageicon of the given path or null if the image didn't exists.
	 */
	private static ImageIcon getImageIcon ( String imagePath ) {
		try {
			return new ImageIcon ( ImageIO.read( ProgramImages.class.getResource( imagePath ) ) );
		} catch ( IOException e ) {
			System.err.println( String.format( "Coudn't find the image addressed by '%s'", imagePath ) );
			return null;
		}
	}
}