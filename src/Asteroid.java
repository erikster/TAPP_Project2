/**
 * Asteroid.java
 *
 * A class to represent individual Asteroids
 * 
 * Functionalities include:
 *    - graphical representation
 *    - self state update
 * 
 * Author:       Erik Steringer
 * Last Updated: 2014-Nov-11 by Erik Steringer
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Asteroid extends StellarObject {
	private Physics phys;
	
	/// Constructors
	/**
	 * Constructor
	 *
	 */
	public PlayerShip(Physics phys, Input input) {
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