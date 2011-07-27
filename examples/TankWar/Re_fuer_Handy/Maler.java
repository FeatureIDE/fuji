
import java.util.Vector;
public class Maler {

	protected NameTextField textField;
	public String name = "";
		
	protected void note(Vector note) {
		if (menu == null) {
			menu = new Menu(this);
            menu.setKoordinateImage(GAME_WIDTH / 2, GAME_HEIGHT / 2);
            menu.setZeileAbstand(20);
            menu.setWaehlbar(false);
            menu.add(Sprach.HNOTE, 0);
			for (int i = 0; i < note.size(); i++) {
				menu.add(
						((Note) note.elementAt(i)).name + "----" + ((Note) note.elementAt(i)).note,
						0);
			}

		}
		menu.erscheinen(gTemp);
	}
	
	public void nameVergeben() {
        if (menu == null) {
            menu = new Menu(this);
            menu.setKoordinateImage(GAME_WIDTH / 2, GAME_HEIGHT / 2);
            menu.add(Sprach.Name, 0);
            
           
          	textField = new NameTextField(this);
			textField.setCenter(GAME_WIDTH / 2, GAME_HEIGHT *2/ 3);
			textField.setWidth(6);
			textField.setTextColor(0,255,0);
          	
        }
        menu.erscheinen(gTemp);
        textField.erscheinen(gTemp);
    }
	
	public void gameLose() {
		original();
		if (System.currentTimeMillis() - time > 2000) {
			gameManager.writeScore();
		}
	}
	
	protected void gameWin() {
		original();
		if (System.currentTimeMillis() - time > 2000) {
			gameManager.writeScore();
		}
	}
	
	public void menuBehandeln(String option) {
		original(option);
		if (option.equals(Sprach.MAIN_MENU)) {
			gameManager.writeScore();
		}
		if (option.equals(Sprach.START)) {
			this.setStatus(GameManager.NAME_VERGEBEN);
			this.gameManager.setStatus(GameManager.NAME_VERGEBEN);
			menu = null;
		}
		if (option.equals(Sprach.Name)) {
            this.setStatus(GameManager.TANK_WAEHLEN);
            this.gameManager.setStatus(GameManager.TANK_WAEHLEN);
            this.name = textField.getText();
            textField=null;
            menu = null;
        }
			
		}
	public void mainMenuerstellen() {
		original();
		menu.add(Sprach.NOTE, 9);
	}
	
	protected void keyRepeated(int keyCode) {
		
		if(textField!=null){
        	switch(keyCode){
        	case -1:
            case 50:
                textField.keyBehandeln(38);
                break;
            case -2:
            case 56:
                textField.keyBehandeln(40);
                break;
            case -3:
            case 52:
                textField.keyBehandeln(37);
                break;
            case -4:
            case 54:
                textField.keyBehandeln(39);
                break;
        	}
        	
        }
	}
	
	protected void keyPressedBehandeln(int keyCode) {
        original(keyCode);
        if(textField!=null){
        	textField.keyBehandeln(keyCode);
        }
    }
	
	
}