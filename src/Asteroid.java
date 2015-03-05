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
import org.newdawn.slick.geom.*;
import org.newdawn.slick.*;
import org.newdawn.slick.fills.*;

public class Asteroid extends StellarObject {
	public Image img;
	
	/// Constructors
	/**
	 * Constructor
	 *
	 */
	public Asteroid(Physics phys) {
		super(phys);
	}
	
	public static Asteroid makeAsteroid(float x, float y) {
		//make centroid
		Vector2f centroid = new Vector2f(x, y);

		//make cimg
		_AsteroidCollider ac = new _AsteroidCollider();
		Circle cir = new Circle(x, y, 16f);
		
		CollisionImage cimg = new CollisionImage(cir, ac);
		GraphicalImage gimg = new GraphicalImage(cir, new GradientFill(0.0f, 0.0f, Color.darkGray, 1.0f, 1.0f, Color.darkGray));
		
		//make the Asteroid
		Asteroid as = new Asteroid(new _AsteroidPhysics(centroid, cimg, gimg));

		//close circular references
		ac.ast = as;
		
		// assign image
		as.img = DebrisManager.asteroidImg;

		return as;
	}
	
	/// Methods
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.drawImage(img, getPhys().getPosition().x - 20, getPhys().getPosition().y - 20);
		// super.render(gc, g);
	}
	
	private static class _AsteroidPhysics extends Physics {
		public _AsteroidPhysics(
		Vector2f centroid,
		CollisionImage cimg,
		GraphicalImage gimg
		){
			super(centroid, 0f, cimg, gimg);
		}
	}
	
	private static class _AsteroidCollider extends Collider{
	
		public Asteroid ast;
		
		@Override
		public void collide(Collider c) { 
			c.inflictDamage(1); // inflicts damage on object it collides with
		}
		
		@Override
		public void inflictDamage(int dam) {
			ast.HP -= dam;
		}
	}
}