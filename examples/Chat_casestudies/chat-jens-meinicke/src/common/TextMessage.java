package common;
import java.io.Serializable;



/**
 * serializable message that can be send over the sockets between client and
 * server. 
 */
public class TextMessage implements Serializable {

	private static final long serialVersionUID = -9161595018411902079L;
	
	protected String content;

	public TextMessage(String content) {
		super();
		setContent(content);
	}

	protected void setContent(String content) {
		((TextMessage) this).content = content;
	}

	public String getContent() {
		return content;
	}
}