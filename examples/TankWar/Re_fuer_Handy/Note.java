
public class Note{
	public String name;
	public int note;

	public Note(String name, int note) {
		if (name.length() >= 7) {
			this.name = name.substring(0, 6);
		} else {
			int leng = name.length();
			for (int i = 0; i < 6 - leng; i++) {
				name += " ";
			}
			this.name = name;
		}
		this.note = note;
	}
}