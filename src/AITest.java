/**
 * AITest.java
 *
 * A simple AI
 *
 * Author: Wesley Gydé
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Input;
import org.newdawn.slick.Color;
import org.newdawn.slick.fills.*;

public class AITest extends StellarObject{
	
	private static final float THRUSTER_ACCELERATION = .05f; //thruster acceleration in px/frame^2
	private static final float ROTATION = .05f; //rotational displacement in rad/frame
	private StellarObject seek_target;
	private StellarObject flee_target;

	//------------------
	//--| 'structors |--
	//------------------

	private AITest(Physics phys, StellarObject seek_target, StellarObject flee_target){
		super(phys);
		this.seek_target = seek_target;
		this.flee_target = flee_target;

		phys.getCImg().setColor(Color.green);
	}

	public static AITest makeAI(float x, float y, StellarObject seek_target, StellarObject flee_target){
		//make centroid
		Vector2f centroid = new Vector2f(x, y);

		//make cimg
		_AITestCollider ac = new _AITestCollider();
		Polygon pol = new Polygon(new float[]{
			x+5f, y   ,
			x-5f, y+5f,
			x-5f, y-5f
			});
		CollisionImage cimg = new CollisionImage(pol, ac);
		GraphicalImage gimg = new GraphicalImage(pol, new GradientFill(0.0f, 0.0f, Color.red, 1.0f, 1.0f, Color.red));
		//make the AITest
		AITest ai = new AITest(new _AITestPhysics(centroid, cimg, gimg), seek_target, flee_target);

		//close circular references
		ac.ai = ai;

		return ai;
	}

	//---------------
	//--| Updates |--
	//---------------

	/** Performs framewise updates; should be propegated from the base Slick2D game object */
	@Override
	public void update(GameContainer gc, int time_passed_ms){
		
		AIHelper ai = new AIHelper(this){
			@Override
			public void update(GameContainer gc, int time_passed_ms) {

				Vector2f steer = new Vector2f();

				float distance = getVectorTo(flee_target).length();
				float flee_scale = 80f/Math.max(0.0001f, getVectorTo(flee_target).length() - 20f); //weaker as distance increases
				flee_scale = flee_scale < .1 ? 0 : flee_scale;

				float seek_scale = 1f - flee_scale;
				seek_scale = seek_scale <  0 ? 0 : seek_scale;
				
				steer = steer.add( flee(flee_target).scale(flee_scale) );
				steer = steer.add( seek(seek_target).scale(seek_scale) );

				accelerate(steer);

			}
		};
		ai.update(gc, time_passed_ms);

		super.update(gc, time_passed_ms);
	}

	/** Returns theta as an angle in the range [0,2π] */
	private float restrictAngle(float theta){
		while (theta > float2pi) {
			theta -= float2pi;
		}
		while (theta < 0) {
			theta += float2pi;
		}
		return theta;
	}
	private static final float float2pi = (float)Math.PI * 2f;

	//---------------
	//--| Aspects |--
	//---------------

	private static class _AITestPhysics extends Physics{
		
		private static final float FRICTION = .98f;

		public _AITestPhysics(
			Vector2f centroid,
			CollisionImage cimg,
			GraphicalImage gimg
			){
			super(centroid, 0f, cimg, gimg);


			setFriction(FRICTION);
		}
	}
	
	private static class _AITestCollider extends Collider{
		public AITest ai;
		
		@Override
		public void collide(Collider c) { 
			c.inflictDamage(1); // inflicts damage on object it collides with
		}
		
		@Override
		public void inflictDamage(int dam){
			ai.HP -= dam;
		}
	}

}
