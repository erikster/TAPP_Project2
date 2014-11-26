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

public class AITest extends StellarObject{
	
	private static final float THRUSTER_ACCELERATION = .05f; //thruster acceleration in px/frame^2
	private static final float ROTATION = .05f; //rotational displacement in rad/frame
	private AIHelper ai;

	//------------------
	//--| 'structors |--
	//------------------

	private AITest(Physics phys, AIHelper ai){
		super(phys);
		this.ai = ai;

		phys.getCImg().setColor(Color.green);
	}

	public static AITest makeAI(float x, float y, StellarObject seek_target){
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
		GraphicalImage gimg = new GraphicalImage(pol);

		//make AIHelper
		_AITestAIHelper aih = new _AITestAIHelper(seek_target);

		//make the AITest
		AITest ai = new AITest(new _AITestPhysics(centroid, cimg, gimg), aih);

		//close circular references
		ac.ai = ai;
		aih.setSubject(ai);

		return ai;
	}

	//---------------
	//--| Updates |--
	//---------------

	/** Performs framewise updates; should be propegated from the base Slick2D game object */
	@Override
	public void update(GameContainer gc, int time_passed_ms){
		
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
		
		private static final float FRICTION = .995f;

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
	}

	private static class _AITestAIHelper extends AIHelper{
		private StellarObject seek_target;

		public _AITestAIHelper(StellarObject seek_target){
			this.seek_target = seek_target;
		}

		@Override
		public void update(GameContainer gc, int time_passed_ms) {

			Vector2f steer = new Vector2f();
			accelerate(pursue(seek_target));

		}
	}

}
