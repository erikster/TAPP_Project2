/**
 * World.java
 *
 * A class to contain the objects of the game's world. Used to track objects 
 * and ease the transition between game and menus.
 * 
 * Functionalities include:
 *    - graphical representation
 *    - game state containment
 * 
 * Author:       Erik Steringer
 * Last Updated: 2014-Nov-9 by Erik Steringer
 */
 
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class World {

	/// Fields
	private Collection<StellarObject> gameObjects;
	
	/// Constructors
	public World() {
		// TODO: implement constructor
	}
	
	/// Methods
	/**
	 * Updates our game's state
	 * Throws a SlickException
	 */
	@Override
	public void update(GameContainer gc, int i) throws SlickException { }
	
	/**
	 * Renders our game onto the screen
	 * Throws a SlickException
	 */
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException { }
}