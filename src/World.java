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
 
import org.newdawn.slick.*;
import org.newdawn.geom.*;
import org.newdawn.fills.*;

import java.util.*;

public class World {

	/// Fields
	private Collection<StellarObject> gameObjects;
	private PlayerShip mainShip;
	
	/// Constructors
	public World() {
		this.gameObjects = new ArrayList<>();
		Collider c = new Collider(){
			};
		Vector2f origin = new Vector2f(0f,0f);
		Shape s = new Polygon( new float[]{
			pos.getX(), pos.getY() + 5f,
			pos.getX() + 5f, pos.getY() - 5f,
			pos.getX() - 5f, pos.getY() - 5f,
			});
		p = new Physics(s,c,pos, new GraphicalImage(s));
		this.mainShip = new PlayerShip(p, new Input(480)); // magic num needs to be fixed later
	}
	
	/// Methods
	/**
	 * Updates our game's state
	 * Throws a SlickException
	 */
	@Override
	public void update(GameContainer gc, int i) throws SlickException { 
		mainShip.update(gc, i);
		for (StellarObject so : gameObjects) {
			so.update(gc, i);
		}
	}
	
	/**
	 * Renders our game onto the screen
	 * Throws a SlickException
	 */
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException { 
		mainShip.render(gc, g);
		for (StellarObject so : gameObjects) {
			so.render(gc, g);
		}
	}
	
}