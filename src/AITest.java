import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Input;
import org.newdawn.slick.Color;

public class AITest extends StellarObject{
	
	private static final float THRUSTER_ACCELERATION = .05f; //thruster acceleration in px/frame^2
	private static final float ROTATION = .05f; //rotational displacement in rad/frame
	private StellarObject target;

	//------------------
	//--| 'structors |--
	//------------------

	private AITest(Physics phys, StellarObject target){
		super(phys);
		this.target = target;
		phys.getCImg().setColor(Color.green);
	}

	public static AITest makeAI(float x, float y, StellarObject target){
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
		//make the AITest
		AITest ai = new AITest(new _AITestPhysics(centroid, cimg, gimg), target);

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
		Vector2f target_pos = target.getPhys().getPosition();
		Vector2f current_pos = getPhys().getPosition();
		float dx = target_pos.x - current_pos.x;
		float dy = target_pos.y - current_pos.y;
		float target_theta = restrictAngle( (float)Math.atan2(dy, dx) );
		float current_theta = getPhys().getRotation();
		
		float neg_rot = 0f;
		float pos_rot = 0f;
		if (current_theta > target_theta){
			neg_rot = current_theta - target_theta;
			pos_rot = float2pi - current_theta + target_theta;
		}else{
			neg_rot = float2pi - target_theta + current_theta;
			pos_rot = target_theta - current_theta;
		}

		//float rot_mag = Math.min(ROTATION, Math.min(neg_rot, pos_rot));
		float rot_mag = Math.min(neg_rot, pos_rot);
		float rot_sign = neg_rot < pos_rot ? -1f : 1f;

		//getPhys().rotate(rot_mag * rot_sign);
		getPhys().rotate(restrictAngle(target_theta - current_theta));
		getPhys().accelerateAligned(0f, THRUSTER_ACCELERATION);
		
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
	//--| Physics |--
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
	}

}
