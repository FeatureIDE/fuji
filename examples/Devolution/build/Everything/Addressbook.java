package Everything;/* Java */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
/* JDOM */
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.JDOMParseException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;



/**
 * @author Marcel Jaeschke
 * @since 1.6
 */
public class Addressbook extends Modul implements XMLizeable {
	/**
	 * Das temporäre XML-Dokument im Hauptspeicher.
	 */
	private Document xml;
	/**
	 * Der Pfad zu den Einstellungs-Dateien.
	 */
	private final File file = new File( "./addressbook.xml" );
	/**
	 * Default constructor.
	 * 
	 * @param base The base which the modul belong to.
	 */
	public Addressbook ( Devolution base ) {
		super( base );
		//
		try {
			this.xml = new SAXBuilder().build( this.file );
		} catch ( Exception exception ) {
			this.xml = new Document( new Element( "addressbook" ) );
			System.err.println( "Create a new addressbook!" );
		}
		//
		this.view = new AddressbookView( this );
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see Base.Modul#getName()
	 */
  	public String getName () {
		return "Addressbook";
	}
	/**
	 * @see Base.miscellaneous.XMLizeable#toXML()
	 */
	public Element toXML () {
		return ( (Document) ((Addressbook) this).xml.clone() ).detachRootElement();
	}
	public boolean remove ( Buddy buddy ) {
		return buddy.getElement().getParent().removeContent( buddy.getElement() );
	}
	public Buddy add () {
		try {
			final Element addressbook = (Element) XPath.selectSingleNode( ((Addressbook) this).xml, "/addressbook" );
			final Element buddy = new Element( "buddy" );
			addressbook.addContent( buddy );
			return new Buddy( buddy );
		} catch ( JDOMException exception ) {
			System.err.println( "Coudn't add a new Buddy." );
		}
		return null;
	}
	public List getBuddy ( String key, String info ) {
		final List buddylist = new LinkedList();
		try {
			final List list = XPath.selectNodes( ((Addressbook) this).xml, String.format( "/addressbook/buddy[@%s='%s']", key, info ) );			
			final Iterator iterator = list.iterator();
			while ( iterator.hasNext() ) {
				final Element element = (Element) iterator.next();
				//add here the buddyies
				buddylist.add( new Buddy( element ) );
			}
		} catch ( Exception exception ) {
			exception.printStackTrace();
		}
		return buddylist;
	}
	public List getAllBuddy ( ) {
		final List buddylist = new LinkedList();
		try {
			final List list = XPath.selectNodes( ((Addressbook) this).xml, "/addressbook/buddy" );			
			final Iterator iterator = list.iterator();
			while ( iterator.hasNext() ) {
				buddylist.add( new Buddy( (Element) iterator.next() ) );
			}
		} catch ( Exception exception ) {
			exception.printStackTrace();
		}
		return buddylist;
	}
	private void save () throws SaveException {
		FileSaver.save( ((Addressbook) this).xml, ((Addressbook) this).file, true );
	}
	/* (non-Javadoc)
	 * @see de.rephstone.rezar.base.Modul#close()
	 */
	public void close () {
		try {
	  		save();
   		} catch ( SaveException exception ) {
    		System.err.println( "Coudn't save the addressbook." );
    	}
	}
}