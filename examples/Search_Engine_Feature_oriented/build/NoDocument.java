

import java.awt.Font;
import java.awt.Point;

import javax.swing.JPanel;



/**
 * Panel zur Anzeige eines leeren Suchergebnisses. 
 * 
 * Diese Klasse erzeugt ein JPanel, auf dem in fetter Schrift steht, dass kein Dokument gefunden wurde.
 * 
 * @author Mr. Pink
 */
public class NoDocument extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Konstruktor.
	 * 
	 * Der Kontruktor erzeugt eine Instanz dieser Klasse, die auf dem parent geadded wird. Die Position legt die Position
	 * fest, an der dieses Objekt auf seinem parent positioniert wird.
	 * 
	 * @param parent     Das JPanel auf dem diese Objekt positioniert werden soll.
	 * @param position   Die Position dieses Objektes auf seinem obergeordneten JPanel.
	 */
	public NoDocument(JPanel parents, Point position) {
		int offset = 3;
		Point current = new Point(offset,offset);
		
		new Point();
		
		this.setLayout(null);
		int fontSize = 15;
		Font fontFat = new Font("", Font.LAYOUT_LEFT_TO_RIGHT | Font.BOLD, fontSize);
		
		Point ende = null;
		
		// dokID, Name, Location
		HitComponentLabel dokText = new HitComponentLabel();
		ende = dokText.setText(this,current, "No matching documents were found.", fontFat);
		
		parents.add(this).setBounds(position.x, position.y, ende.x, fontSize+5);
	}
}