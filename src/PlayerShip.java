/**
 * PlayerShip.java
 *
 * A ship, controlled by the player (via the arrow keys)
 *
 * Author: Wesley Gydé
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class PlayerShip extends StellarObject{
	
	private static final float THRUSTER_ACCELERATION = .05f; //thruster acceleration in px/frame^2
	private static final float ROTATION = .05f; //rotational displacement in rad/frame
	protected int hp = 1;

	//------------------
	//--| 'structors |--
	//------------------

	private PlayerShip(Physics phys){
		super(phys);
	}

	public static PlayerShip makeShip(float x, float y){
		//make centroid
		Vector2f centroid = new Vector2f(x, y);

		//make cimg
		_PlayerCollider pc = new _PlayerCollider();
		Polygon pol = new Polygon(new float[]{
			x   , y+7f,
			x+5f, y-5f,
			x-5f, y-5f
			});
		CollisionImage cimg = new CollisionImage(pol, pc);

		GraphicalImage gimg = new GraphicalImage(pol);

		//make the PlayerShip
		PlayerShip ps = new PlayerShip(new _PlayerShipPhysics(centroid, cimg, gimg));

		//close circular references
		pc.ps = ps;

		return ps;
	}

	//---------------
	//--| Updates |--
	//---------------

	/** Performs framewise updates; should be propegated from the base Slick2D game object */
	@Override
	public void update(GameContainer gc, int time_passed_ms){
		if (hp <= 0) {
			// got a different idea for now
			// destroy(); 
		}

		Input in = gc.getInput();
		if (in.isKeyDown(Input.KEY_LEFT)){
			getPhys().rotate(-ROTATION);
		}
		if (in.isKeyDown(Input.KEY_RIGHT)){
			getPhys().rotate(ROTATION);
		}
		if (in.isKeyDown(Input.KEY_UP)){
			getPhys().accelerateAligned(0f, THRUSTER_ACCELERATION);
		}
		super.update(gc, time_passed_ms);
	}
	
	public int getHP() { return hp; }

	//---------------
	//--| Physics |--
	//---------------

	private static class _PlayerShipPhysics extends Physics{
		public _PlayerShipPhysics(
		Vector2f centroid,
		CollisionImage cimg,
		GraphicalImage gimg
		){
			super(centroid, 0f, cimg, gimg);
		}
	}
	
	private static class _PlayerCollider extends Collider{
		
		public PlayerShip ps;

		@Override
		public void collide(Collider c) {
			c.inflictDamage(1);
		}

		@Override
		public void inflictDamage(int dam){
			ps.hp -= dam;
		}

	}

}
