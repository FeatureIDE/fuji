
import java.io.IOException;
public class Missile {
	protected void init(TankManager tankManager, int x_Koordinate, int y_Koordinate, int width,
			int height, int missileRichtung, int missileType, boolean feindlich,int tankId) {
			original(tankManager,x_Koordinate,y_Koordinate,width,
			height,missileRichtung,missileType,feindlich,tankId);
			if(!feindlich){
			try {
				SoundPlayer.getInstance().playFireSound();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			}
			}
			
}