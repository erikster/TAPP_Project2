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
import org.newdawn.slick.geom.*;
import org.newdawn.slick.fills.*;

import java.util.*;

public class World {

	/// Fields
	private PlayerShip mainShip;
	private CollisionLayer cl;
	private DebrisManager debris;
	private AIManager aiships;
	
	private static boolean DEBUG_DISP = false;
	
	/// Constructors
	public World() {
		this.cl       = new CollisionLayer();
		this.debris   = new DebrisManager(cl);
		this.aiships  = new AIManager(cl);
		this.mainShip = PlayerShip.makeShip(0f, 0f);
		
		mainShip.getPhys().getCImg().addTo(cl);	
	}
	
	/// Methods
	/**
	 * Updates our game's state
	 * Throws a SlickException
	 */
	public void update(GameContainer gc, int i) throws SlickException {
		mainShip.update(gc, i);
		debris.update(gc, i, mainShip.getPhys().getPosition(), mainShip.getPhys().getVelAngle());
		aiships.update(gc, i);
		cl.notifyCollisions();
		
		if (gc.getInput().isKeyPressed(Input.KEY_D)) {
			DEBUG_DISP = !DEBUG_DISP;
		}
		
		if (mainShip.getHP() < 0 || gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			gc.exit();
		}
	}
	
	/**
	 * Renders our game onto the screen
	 * Throws a SlickException
	 */
	public void render(GameContainer gc, Graphics g) throws SlickException { 
		float midx = mainShip.getPhys().getGImg().getMidX();
		float midy = mainShip.getPhys().getGImg().getMidY();
		g.translate(-midx + 320f, -midy + 240f);
		
		debris.render(gc, g);
		aiships.render(gc, g);
		
		mainShip.render(gc, g);
		
		g.translate(midx - 320f, midy - 240f); // undo the translation
		
		g.drawString("Use the arrow keys to move.", 10f, 10f);
		g.drawString("HP: " + mainShip.getHP(), 10, 440);
		
		if (DEBUG_DISP) { // display debugging information
			g.drawString("X: " + midx + ", Y: " + midy, 10, 420);
			g.drawString("Asteroids: " + debris.count(), 10, 460);
			g.drawString("FPS: " + gc.getFPS(), 570, 460);
		}
	}
}