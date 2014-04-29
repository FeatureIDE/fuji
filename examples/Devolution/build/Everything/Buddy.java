package Everything;import java.util.HashMap;
import java.util.Iterator;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;



/**
 * @author Marcel Jaeschke
 * @since 1.6
 */
public class Buddy extends Object implements XMLizeable {
	public static final String TITLE = "title";
	public static final String NAME = "name";
	public static final String LASTNAME = "lastname";
	public static final String NICK = "nick";
	public static final String BIRTHDAY = "birthday";
	public static final String MAIL_PRIVATE = "mail_private";
	public static final String MAIL_BUSINESS = "mail_buisness";
	public static final String MAIL_NICK = "mail_nick";
	public static final String JABBER = "jabber";
	private final Element element;
	private final HashMap info = new HashMap();

	public Buddy ( Element element ) {
		super();
		this.element = element;	
	}
	public void setInfo ( final String key, final String info ) {
		((Buddy) this).info.put( key, info );
	}
	public void save () {
		Iterator iterator = ((Buddy) this).info.keySet().iterator();
		while ( iterator.hasNext() ) {
			String key = (String) iterator.next();
			((Buddy) this).element.setAttribute( key, getInfo( key ) );
		}
	}
	public String getInfo ( final String key ) {
		if ( !((Buddy) this).info.containsKey( key ) ) {
			try {
				final Attribute attribute = (Attribute) XPath.selectSingleNode( ((Buddy) this).element, String.format( "@%s", key ) );
				setInfo( key, attribute.getValue() );
			} catch ( final Exception exception ) {
				System.err.println( String.format( "Buddy has no the '%s' tag", key ) );
				setInfo( key, "" );
			}
		}
		return (String) ((Buddy) this).info.get( key );
	}
	public Element getElement() {
		return ((Buddy) this).element;
	}
	public boolean equals ( final Buddy buddy ) {
		try {
			final Iterator iterator = ((Buddy) this).info.values().iterator();
			while ( iterator.hasNext() ) {
				final String key = (String) iterator.next();
				if ( !getInfo( key ).equals( buddy.getInfo( key ) ) )
					return false;
			}
		} catch ( NullPointerException exception ) {
			return false;
		}
		return true;
	}
	public String toString () {
		return String.format( "%s %s %s", getInfo( Buddy.TITLE ), getInfo( Buddy.NAME ), getInfo( Buddy.LASTNAME ) );
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.rephstone.rezar.miscellaneous.XMLizeable#toXML()
	 */
	public Element toXML () {
		return ((Buddy) this).element;
	}
}