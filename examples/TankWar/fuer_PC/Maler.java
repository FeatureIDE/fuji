
public class Maler {
	protected Map images = new HashMap();
	public void drawImage(String imageFileName, int x, int y) {
		int xw = GAME_WIDTH / 60;
		int yh = GAME_HEIGHT / 60;
		Image temp = (Image) images.get(imageFileName);
		if (temp != null) {
			gTemp.drawImage(temp, x, y, null);
		} else {
			System.out.println(imageFileName + "----------" + "null");
			try {
				// ---------------------------
				if (imageFileName.equals("gh")) {
					loadimage("gh", "gh.png", 7 * xw, 7 * yh);
				}
				// --------------------------
				if (imageFileName.equals("31.png")) {
					loadimage("31.png", "brickWall.png", 2 * xw, 2 * yh);
				}
				if (imageFileName.equals("32.png")) {
					loadimage("32.png", "grassWall.png", 2 * xw, 2 * yh);
				}
				if (imageFileName.equals("33.png")) {
					loadimage("33.png", "metalWall.png", 2 * xw, 2 * yh);
				}
				if (imageFileName.equals("34.png")) {
					loadimage("34.png", "waterWall.png", 2 * xw, 2 * yh);
				}
				// -----------------------------------------
				if (imageFileName.equals("explode1.png")) {
					loadimage("explode1.png", "explode1.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode2.png")) {
					loadimage("explode2.png", "explode2.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode3.png")) {
					loadimage("explode3.png", "explode3.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode4.png")) {
					loadimage("explode4.png", "explode4.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode5.png")) {
					loadimage("explode5.png", "explode5.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode6.png")) {
					loadimage("explode6.png", "explode6.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode7.png")) {
					loadimage("explode7.png", "explode7.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode8.png")) {
					loadimage("explode8.png", "explode8.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode9.png")) {
					loadimage("explode9.png", "explode9.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode10.png")) {
					loadimage("explode10.png", "explode10.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode11.png")) {
					loadimage("explode11.png", "explode11.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode12.png")) {
					loadimage("explode12.png", "explode12.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode13.png")) {
					loadimage("explode13.png", "explode13.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode14.png")) {
					loadimage("explode14.png", "explode14.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode15.png")) {
					loadimage("explode15.png", "explode15.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode16.png")) {
					loadimage("explode16.png", "explode16.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode17.png")) {
					loadimage("explode17.png", "explode17.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode18.png")) {
					loadimage("explode18.png", "explode18.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode19.png")) {
					loadimage("explode19.png", "explode19.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode20.png")) {
					loadimage("explode20.png", "explode20.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode21.png")) {
					loadimage("explode21.png", "explode21.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode22.png")) {
					loadimage("explode22.png", "explode22.png", 6 * xw, 6 * yh);
				}
				if (imageFileName.equals("explode23.png")) {
					loadimage("explode23.png", "explode23.png", 6 * xw, 6 * yh);
				}
				// -------------------------
				if (imageFileName.equals("missileU.png")) {
					loadimage("missileU.png", "missileU.png", xw, 2 * yh);
				}
				if (imageFileName.equals("missileD.png")) {
					loadimage("missileD.png", "missileD.png", xw, 2 * yh);
				}
				if (imageFileName.equals("missileL.png")) {
					loadimage("missileL.png", "missileL.png", 2 * xw, yh);
				}
				if (imageFileName.equals("missileR.png")) {
					loadimage("missileR.png", "missileR.png", 2 * xw, yh);
				}
				// --------------------------------Tools
				if (imageFileName.equals("370")) {
					loadimage("370", "acc.png", 2 * xw, 2 * yh);
				}
				if (imageFileName.equals("371")) {
					loadimage("371", "allstop.png", 2 * xw, 2 * yh);
				}
				if (imageFileName.equals("372")) {
					loadimage("372", "macc.png", 2 * xw, 2 * yh);
				}
				if (imageFileName.equals("373")) {
					loadimage("373", "blood.png", 2 * xw, 2 * yh);
				}
				if (imageFileName.equals("374")) {
					loadimage("374", "bomb.png", 2 * xw, 2 * yh);
				}
				if (imageFileName.equals("375")) {
					loadimage("375", "mars.png", 2 * xw, 2 * yh);
				}
				// -----------------------------------
				if (imageFileName.equals("01U")) {
					loadimage("01U", "playerTank01U.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("01D")) {
					loadimage("01D", "playerTank01D.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("01L")) {
					loadimage("01L", "playerTank01L.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("01R")) {
					loadimage("01R", "playerTank01R.png", 3 * xw, 3 * yh);
				}
				
				if (imageFileName.equals("02U")) {
					loadimage("02U", "playerTank02U.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("02D")) {
					loadimage("02D", "playerTank02D.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("02L")) {
					loadimage("02L", "playerTank02L.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("02R")) {
					loadimage("02R", "playerTank02R.png", 3 * xw, 3 * yh);
				}

				if (imageFileName.equals("03U")) {
					loadimage("03U", "playerTank03U.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("03D")) {
					loadimage("03D", "playerTank03D.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("03L")) {
					loadimage("03L", "playerTank03L.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("03R")) {
					loadimage("03R", "playerTank03R.png", 3 * xw, 3 * yh);
				}
				// ----------------------------------
				if (imageFileName.equals("11U")) {
					loadimage("11U", "enemyTank11U.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("11D")) {
					loadimage("11D", "enemyTank11D.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("11L")) {
					loadimage("11L", "enemyTank11L.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("11R")) {
					loadimage("11R", "enemyTank11R.png", 3 * xw, 3 * yh);
				}

				if (imageFileName.equals("12U")) {
					loadimage("12U", "enemyTank12U.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("12D")) {
					loadimage("12D", "enemyTank12D.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("12L")) {
					loadimage("12L", "enemyTank12L.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("12R")) {
					loadimage("12R", "enemyTank12R.png", 3 * xw, 3 * yh);
				}

				if (imageFileName.equals("13U")) {
					loadimage("13U", "enemyTank13U.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("13D")) {
					loadimage("13D", "enemyTank13D.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("13L")) {
					loadimage("13L", "enemyTank13L.png", 3 * xw, 3 * yh);
				}
				if (imageFileName.equals("13R")) {
					loadimage("13R", "enemyTank13R.png", 3 * xw, 3 * yh);
				}
				// -------------------------------21,24,25
				if (imageFileName.equals("21U")) {
					loadimage("21U", "enemyBoss21U.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("21D")) {
					loadimage("21D", "enemyBoss21D.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("21L")) {
					loadimage("21L", "enemyBoss21L.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("21R")) {
					loadimage("21R", "enemyBoss21R.png", 4 * xw, 4 * yh);
				}

				if (imageFileName.equals("24U")) {
					loadimage("24U", "enemyBoss24U.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("24D")) {
					loadimage("24D", "enemyBoss24D.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("24L")) {
					loadimage("24L", "enemyBoss24L.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("24R")) {
					loadimage("24R", "enemyBoss24R.png", 4 * xw, 4 * yh);
				}

				if (imageFileName.equals("25U")) {
					loadimage("25U", "enemyBoss25U.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("25D")) {
					loadimage("25D", "enemyBoss25D.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("25L")) {
					loadimage("25L", "enemyBoss25L.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("25R")) {
					loadimage("25R", "enemyBoss25R.png", 4 * xw, 4 * yh);
				}

				if (imageFileName.equals("22U")) {
					loadimage("22U", "enemyBoss22U.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("22D")) {
					loadimage("22D", "enemyBoss22D.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("22L")) {
					loadimage("22L", "enemyBoss22L.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("22R")) {
					loadimage("22R", "enemyBoss22R.png", 4 * xw, 4 * yh);
				}

				if (imageFileName.equals("26U")) {
					loadimage("26U", "enemyBoss26U.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("26D")) {
					loadimage("26D", "enemyBoss26D.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("26L")) {
					loadimage("26L", "enemyBoss26L.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("26R")) {
					loadimage("26R", "enemyBoss26R.png", 4 * xw, 4 * yh);
				}

				if (imageFileName.equals("27U")) {
					loadimage("27U", "enemyBoss27U.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("27D")) {
					loadimage("27D", "enemyBoss27D.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("27L")) {
					loadimage("27L", "enemyBoss27L.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("27R")) {
					loadimage("27R", "enemyBoss27R.png", 4 * xw, 4 * yh);
				}

				if (imageFileName.equals("23U")) {
					loadimage("23U", "enemyBoss23U.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("23D")) {
					loadimage("23D", "enemyBoss23D.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("23L")) {
					loadimage("23L", "enemyBoss23L.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("23R")) {
					loadimage("23R", "enemyBoss23R.png", 4 * xw, 4 * yh);
				}

				if (imageFileName.equals("28U")) {
					loadimage("28U", "enemyBoss28U.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("28D")) {
					loadimage("28D", "enemyBoss28D.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("28L")) {
					loadimage("28L", "enemyBoss28L.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("28R")) {
					loadimage("28R", "enemyBoss28R.png", 4 * xw, 4 * yh);
				}

				if (imageFileName.equals("29U")) {
					loadimage("29U", "enemyBoss29U.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("29D")) {
					loadimage("29D", "enemyBoss29D.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("29L")) {
					loadimage("29L", "enemyBoss29L.png", 4 * xw, 4 * yh);
				}
				if (imageFileName.equals("29R")) {
					loadimage("29R", "enemyBoss29R.png", 4 * xw, 4 * yh);
				}
				// -----------------------------------
				if (imageFileName.equals("base")) {
					loadimage("base", "base.png", 2 * xw, 2 * yh);
				}
				// -----------------------------------

			} catch (Exception exception) {
				System.out.println(exception);
			}
			temp = (Image) images.get(imageFileName);
			if (temp != null) {
				gTemp.drawImage(temp, x, y, null);
			}
		}

	}

	public void loadimage(String id, String imagename, int a, int b) {
		Image image = tk.getImage(Tank.class.getClassLoader().getResource(
				imagename));
		images.put(id, image.getScaledInstance(a, b, Image.SCALE_FAST));
	}
	
}