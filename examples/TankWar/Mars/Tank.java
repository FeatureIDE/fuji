
public class Tank {
	protected boolean mars = false;
	protected long marsTimer;
	
	protected void toolKontroller(){
		original();
		if (mars && System.currentTimeMillis() - marsTimer > 10000) {
			mars = false;
		}
		if (mars) {
			tankManager.maler.setColor(255, 255, 255);
			tankManager.maler.drawRoundRect(x_Koordinate - 3, y_Koordinate - 3, objWidth + 6,
					objHeight + 6, tankManager.koernigkeit, tankManager.koernigkeit);
		}
	}

	protected void toolBehandeln(int toolType) {
		original(toolType);
		switch (toolType) {
		case 375:// 255,255,255
			marsTimer = System.currentTimeMillis();
			this.mars = true;
		break;
		}
	}
	
	public void beschaedigen(int besch, int beschId) {
		if(mars){
			this.energie = this.energie + besch;
		}
		original(besch,beschId);
	}

}