/**
 * Launch.java
 *
 * A class to launch our game
 * 
 * Functionalities include:
 *    - game creation
 * 
 * Author:       Erik Steringer
 * Last Updated: 2014-Nov-9 by Erik Steringer
 */

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Launch {

	/**
	 * Main function to contain the game
	 */
	public static void main(String[] args) {
		try {
			AppGameContainer agc = new AppGameContainer(
				new FugitiveGame("Stellar Fugitive")
				);
			agc.setDisplayMode(640, 480, false);
			agc.start();
		} catch (SlickException ex) {
			ex.printStackTrace();
		}
	}
}