/**
 * PlayerShip.java
 *
 * A class to represent the main player
 * 
 * Functionalities include:
 *    - graphical representation
 *    - self state update
 *    - taking and using input
 * 
 * Author:       Erik Steringer
 * Last Updated: 2014-Nov-9 by Erik Steringer
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class PlayerShip extends StellarObject {
	
	/// Fields
	private Input input;
	private Physics phys;
	
	/// Constructors
	/**
	 * Constructor
	 *
	 */
	public PlayerShip(Phys phys, Input input) {
		this.phys = phys;
		this.input = input;
	}
	
	/// Methods
	/**
	 * Updates its own state
	 * throws a SlickException
	 */
	@Override
	public void update(GameContainer gc, int time_passed_ms) throws SlickException {
	
	}
	
	/**
	 * Draws itself
	 * throws a SlickException
	 */ 
	@Override
	public void render(GameContainer gc, Graphics g) {
	
	}
}