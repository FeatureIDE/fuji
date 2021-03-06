
import java.awt.Color;
import java.awt.Event;
import java.io.Serializable;



public class TextMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	protected final int key = 13;
	
	private Color color;
	protected String text;
	
	public TextMessage(String text){
		this.text = text;
		this.setColor(Color.BLACK);
	}
	
	public TextMessage(String text, Color color){
		this.text = text;
		this.setColor(color);
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String text){
		((TextMessage) this).text = text;
	}

	public void setColor(Color color) {
		((TextMessage) this).color = color;
	}

	public Color getColor() {
		return color;
	}

	public String textColor(){
		if(color.equals(Color.BLACK))
			return "Black";
		else if(color.equals(Color.RED))
			return "Red";
		else if(color.equals(Color.GREEN))
			return "GREEN";
		else if(color.equals(Color.YELLOW))
			return "YELLOW";
		else if(color.equals(Color.BLUE))
			return "BLUE";
		else if(color.equals(Color.ORANGE))
			return "ORANGE";
		else
			return color.toString();
	}
	
	protected String invert(String text){
		StringBuilder res = new StringBuilder();
		for(int i = text.length()-1; i >= 0; i--){
			res.append(text.charAt(i));
		}
		return res.toString();
	}
}