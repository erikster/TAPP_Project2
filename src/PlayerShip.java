/**
 * PlayerShip.java
 *
 * A ship, controlled by the player (via the arrow keys)
 *
 * Authors: Wesley Gydé, Erik Steringer
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Image;
import org.newdawn.slick.particles.*;
import org.newdawn.slick.SlickException;
import java.io.*;

public class PlayerShip extends StellarObject{
	
	private static final float THRUSTER_ACCELERATION = .05f; //thruster acceleration in px/frame^2
	private static final float ROTATION = .05f; //rotational displacement in rad/frame
	private CollisionLayer cl;
	private BulletManager bm;
	private ParticleSystem thrusters;
	private ConfigurableEmitter emitter;

	//------------------
	//--| 'structors |--
	//------------------

	private PlayerShip(Physics phys, CollisionLayer cl) {
		super(phys);
		this.cl = cl;
		HP = 1;
		bm = new BulletManager(cl);
		try {
			Image particle = new Image("media/particle_flame.png", false);
			thrusters = new ParticleSystem(particle, 1500);
			thrusters.setRemoveCompletedEmitters(false);
		} catch (SlickException ex) {
			ex.printStackTrace();
			System.err.println("Couldn't load particle image, exiting.");
			System.exit(0);
		}
		try {
			File particleData = new File("media/shipflame.xml");
			emitter = ParticleIO.loadEmitter(particleData);
			emitter.setPosition(getPhys().getPosition().x, getPhys().getPosition().y);
			thrusters.addEmitter(emitter);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("Couldn't load particle file, exiting.");
			System.exit(0);
		}
		thrusters.setBlendingMode(ParticleSystem.BLEND_ADDITIVE);
	}

	public static PlayerShip makeShip(float x, float y, CollisionLayer cl) {
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
		PlayerShip ps = new PlayerShip(new _PlayerShipPhysics(centroid, cimg, gimg), cl);

		//close circular references
		pc.ps = ps;

		return ps;
	}

	//---------------
	//--| Updates |--
	//---------------

	/** Performs framewise updates; should be propegated from the base Slick2D game object */
	@Override
	public void update(GameContainer gc, int time_passed_ms) {

		if (HP <= 0){
			destroy();
		}
		
		//keyboard input
		Input in = gc.getInput();

		if (in.isKeyDown(Input.KEY_LEFT)) {
			getPhys().rotate(-ROTATION);
		}

		if (in.isKeyDown(Input.KEY_RIGHT)) {
			getPhys().rotate(ROTATION);
		}

		if (in.isKeyDown(Input.KEY_UP)) {
			getPhys().accelerateAligned(0f, THRUSTER_ACCELERATION);
			thrusters.getEmitter(0).setEnabled(true);
		} else {
			thrusters.getEmitter(0).setEnabled(false);
		}

		if (in.isKeyDown(Input.KEY_DOWN)){
			getPhys().setFriction(_PlayerShipPhysics.FRICTION_BRAKING);
		} else {
			getPhys().setFriction(_PlayerShipPhysics.FRICTION_DEFAULT);
		}
		
		if (in.isKeyPressed(Input.KEY_SPACE)) {
			fire();
		}
		
		bm.update(gc, time_passed_ms);
		
		emitter.setPosition(-getPhys().getPosition().x, -getPhys().getPosition().y);
		emitter.angularOffset.setValue(getPhys().getVelAngle());
		emitter.speed.setMax(getPhys().getSpeed());
		emitter.speed.setMin(getPhys().getSpeed());
		thrusters.update(time_passed_ms);
		
		super.update(gc, time_passed_ms);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		thrusters.render();
		super.render(gc, g);
		bm.render(gc, g);
	}
	
	private void fire() {
		float x     = getPhys().getPosition().x;
		float y     = getPhys().getPosition().y;
		float angle = getPhys().getRotation();
		x += (float) 10 * Math.cos(angle);
		y += (float) 10 * Math.sin(angle);
		float speed = getPhys().getSpeed();
		
		bm.requestBullet(x, y, angle, speed + 10f);
	}

	//---------------
	//--| Physics |--
	//---------------

	private static class _PlayerShipPhysics extends Physics{
		private static final float FRICTION_DEFAULT = .995f;
		private static final float FRICTION_BRAKING = .950f;
		
		public _PlayerShipPhysics(Vector2f centroid, CollisionImage cimg, GraphicalImage gimg ) {
			super(centroid, (float) Math.PI / 2, cimg, gimg);

			setFriction(FRICTION_DEFAULT);
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
			ps.HP -= dam;
		}

	}

}
