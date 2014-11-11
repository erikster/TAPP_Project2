/**
 * StellarObject.java
 *
 * A base class for a generic StellarObject.
 * Functionalities include
 *    - collision detection (see CollisionImage.java)
 *    - graphical representation (see GraphicalImage.java)
 *
 * Author: Wesley Gydé
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public abstract class StellarObject{

	private Physics phys;

	//------------------
	//--| 'structors |--
	//------------------
	
	/**
	 * Constructor
	 *
	 * @param phys A physics object for this
	 */
	public StellarObject( Phys phys ){

		this.phys = phys;

	}

	//---------------
	//--| Updates |--
	//---------------

	/** Performs framewise updates; should be propegated from the base Slick2D game object */
	public void update(GameContainer gc, int time_passed_ms) throws SlickException {
		
		phys.update(gc, time_passed_ms);

	}

	/** Performs graphical updates; should be propegated from the base Slick2D game object */
	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		phys.update(gc, g);
		
	}

}
