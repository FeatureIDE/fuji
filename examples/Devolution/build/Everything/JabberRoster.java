package Everything;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;
import java.util.*;
import javax.swing.tree.*;



abstract class JabberRoster$$Jabber implements RosterListener {
	protected JabberConnection jabCon;
	protected final DefaultMutableTreeNode jabberRosterRoot = new DefaultMutableTreeNode("Jabber");
	protected ArrayList listeners = new ArrayList(); // Listeners that get notified if something changes

	public JabberRoster$$Jabber(JabberConnection jabCon) {
		this.jabCon = jabCon;
		rebuildRoster();
	}
	
	// Rebuilds the roster
	public void rebuildRoster() {
		XMPPConnection connection = ((JabberRoster) this).jabCon.connection; 
		DefaultMutableTreeNode root = ((JabberRoster) this).jabberRosterRoot;
		root.removeAllChildren();
		
		// If offline:
		if (connection == null) {
			root.add(new DefaultMutableTreeNode("Disconnected"));
			notifyListeners();
			return;
		}
		
		// Group for offline contacts:
		DefaultMutableTreeNode offlineGroup = new DefaultMutableTreeNode("Offline");
		
		// Add groups
		Iterator groupIter = connection.getRoster().getGroups().iterator(); // <RosterGroup>
		while (groupIter.hasNext()) {
    		RosterGroup group = (RosterGroup)groupIter.next();
    		
    		DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(group.getName());
    		root.add(groupNode);
    		
    		// Add contacts filed in groups
    		Iterator entryIter = group.getEntries().iterator();
			while (entryIter.hasNext()) {
	    		RosterEntry entry = (RosterEntry)entryIter.next();
	    		Presence curpres = connection.getRoster().getPresence(entry.getUser());
	    		if ( curpres != null && curpres.getType() != Presence.Type.unavailable ) { // user is online
	    			groupNode.add(new DefaultMutableTreeNode(new IMBuddy(entry.getUser(), fetchNickname(entry))));
	    		} else {
	    			offlineGroup.add(new DefaultMutableTreeNode(new IMBuddy(entry.getUser(), fetchNickname(entry))));
	    		}
	    	}
    	}
    	
    	// Add contact that don't belong to any groups
		Iterator entryIter = connection.getRoster().getUnfiledEntries().iterator();
		while (entryIter.hasNext()) {
    		RosterEntry entry = (RosterEntry)entryIter.next();
    		Presence curpres = connection.getRoster().getPresence(entry.getUser());
    		if ( curpres != null && curpres.getType() != Presence.Type.unavailable ) { // user is online
    			root.add(new DefaultMutableTreeNode(new IMBuddy(entry.getUser(), fetchNickname(entry))));
    		} else {
    			offlineGroup.add(new DefaultMutableTreeNode(new IMBuddy(entry.getUser(), fetchNickname(entry))));
    		}
    	}
    	
    	root.add(offlineGroup);
    	
    	notifyListeners();
	}
	
	public String fetchNickname(RosterEntry entry) {
		return entry.getName();
	}
	
	public void expandAll() {
	}
	
	public DefaultMutableTreeNode getJabberRosterRoot() {
 		return ((JabberRoster) this).jabberRosterRoot;
 	}
 	
 	///////////// Implement interface that will be notified by Smack ///////////	
	public void	entriesAdded(Collection addresses) {
		rebuildRoster();
	}
	
 	public void	entriesDeleted(Collection addresses) {
 		rebuildRoster();
 	}
 	
 	public void	entriesUpdated(Collection addresses) {
 		rebuildRoster();
 	}
 	
 	public void presenceChanged(Presence presence) {
 		rebuildRoster();
 	}
 	
 	/////////// Mangement for listeneres that listen *me* /////////////
	public void addLineListener(JabberRosterListener listener) {
		listeners.add(listener);
	}

	public void removeLineListener(JabberRosterListener listner) {
		listeners.remove(listner);
	}

	private void notifyListeners() {
		for (Iterator iter = listeners.iterator(); iter.hasNext();) {
			JabberRosterListener listener = (JabberRosterListener)iter.next();
			listener.rosterChanged();
		}
	}
}



public class JabberRoster extends  JabberRoster$$Jabber  {
	public String fetchNickname(RosterEntry entry) {
		Addressbook ab = (Addressbook)((JabberRoster) this).jabCon.getBase().getModul("Addressbook");
		List list = ab.getBuddy( Buddy.JABBER, entry.getUser() );
		if( list != null && !list.isEmpty()) {
			Buddy abBuddy = (Buddy)list.get(0);
			String abNickname = abBuddy.getInfo(Buddy.NICK);
			return abNickname;
		} else {
			return super.fetchNickname(entry);
		}
	}
      // inherited constructors

 // Listeners that get notified if something changes

	public JabberRoster ( JabberConnection jabCon ) { super(jabCon); }
}