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
	private Collection<StellarObject> gameObjects;
	private PlayerShip mainShip;
	private CollisionLayer cl = new CollisionLayer();
	private ArrayList<CollisionImage> cimgs = new ArrayList<>();
	
	private class B{ public boolean b; }
	
	public void addShape(Shape shape){
		final B b1 = new B();
		CollisionImage cimg = new CollisionImage(
			shape,
			new Collider(){
				B bb = b1;
				@Override
				public void collide(Collider c){bb.b = true;}
			}
			){
				B bb = b1;
				@Override
				public void render(GameContainer gc, Graphics g) throws SlickException{
					super.setColor( bb.b?Color.red:Color.blue );
					super.render(gc, g);
					bb.b = false;
				}
			};
		cimg.addTo(cl);
		cimgs.add(cimg);

	}
	
	/// Constructors
	public World() {
		this.gameObjects = new ArrayList<>();
		
		this.mainShip = PlayerShip.makeShip(0f, 0f);
		mainShip.getPhys().getCImg().addTo(cl);
		
		Asteroid a1 = Asteroid.makeAsteroid(-100f, 100f);
		a1.getPhys().getCImg().addTo(cl);
		gameObjects.add(a1);
	}
	
	/// Methods
	/**
	 * Updates our game's state
	 * Throws a SlickException
	 */
	public void update(GameContainer gc, int i) throws SlickException { 
		mainShip.update(gc, i);
		for (StellarObject so : gameObjects) {
			so.update(gc, i);
		}
		cl.notifyCollisions();
	}
	
	/**
	 * Renders our game onto the screen
	 * Throws a SlickException
	 */
	public void render(GameContainer gc, Graphics g) throws SlickException { 
		float midx = mainShip.getPhys().getGImg().getMidX();
		float midy = mainShip.getPhys().getGImg().getMidY();
		g.translate(-midx + 320f, -midy + 240f);
		
		for (StellarObject so : gameObjects) {
			so.render(gc, g);
		}
			
		for (CollisionImage cimg : cimgs){
			cimg.render(gc, g);
		}
		mainShip.render(gc, g);
		
		g.drawString("HP: " + mainShip.getHP(), midx - 40f, midy - 30f);

		g.translate(midx - 320f, midy - 240f);
	}
	
}