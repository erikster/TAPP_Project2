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

public class Asteroid extends StellarObject {
	
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
		Circle cir = new Circle(x, y, 20f);
		
		CollisionImage cimg = new CollisionImage(cir, ac);
		GraphicalImage gimg = new GraphicalImage(cir);

		//make the Asteroid
		Asteroid as = new Asteroid(new _AsteroidPhysics(centroid, cimg, gimg));

		//close circular references
		ac.ast = as;

		return as;
	}
	
	/// Methods
	
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
		public void collide(Collider c) { }

	}
}