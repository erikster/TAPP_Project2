/**
 * StellarGame.java
 *
 * A class to contain the actual game we are making
 * 
 * Functionalities include:
 *    - game creation
 *    - game state updates
 *    - graphical representation
 * 
 * Author:       Erik Steringer
 * Last Updated: 2014-Nov-9 by Erik Steringer
 */

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.fills.*;
import org.newdawn.slick.state.*;

public class StellarGame extends StateBasedGame {
	/// fields
	private MenuState mainMenu;
	private InGameState ingame;
	private HighScoresState hiscores;
	
	/// constructors
	/**
	 * Constructor, takes a string for the game's name
	 * "initializes" gameWorld so init can load all the stuff we need
	 */
	public StellarGame(String gamename) {
		super(gamename);
	}
	
	@Override
	public void initStatesList(GameContainer gc) {
		this.hiscores = new HighScoresState();
		this.mainMenu = new MenuState();
		this.ingame = new InGameState(hiscores);
		addState(mainMenu);
		addState(ingame);
		addState(hiscores);
		enterState(mainMenu.getID());
	}
}