import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Input;
import org.newdawn.slick.Color;
import org.newdawn.slick.fills.GradientFill;
import java.util.Random;

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
		phys.getCImg().setColor(Color.red);
	}

	public static AITest makeAI(float x, float y, StellarObject target){
		//make centroid
		Vector2f centroid = new Vector2f(x, y);
		//make cimg
		_AITestCollider ac = new _AITestCollider();
		Polygon pol = new Polygon(new float[]{
			x+7f, y   ,
			x-5f, y+5f,
			x-5f, y-5f	});
		CollisionImage cimg = new CollisionImage(pol, ac);
		GraphicalImage gimg = new GraphicalImage(pol, new GradientFill(0.0f, 0.0f, Color.red, 1.0f, 1.0f, Color.red));
		//make the AITest
		AITest ai = new AITest(new _AITestPhysics(centroid, cimg, gimg), target);

		//close circular references
		ac.ai = ai;

		return ai;
	}

	//---------------
	//--| Updates |--
	//---------------

	/** gets the vector in the direction of the target */
	private Vector2f getVectorToTarget(){
		Vector2f target_pos = target.getPhys().getPosition();
		Vector2f current_pos = getPhys().getPosition();
		// using x & y explicitly is ignoring the idiotproofing built into Vector2f
		return target_pos.sub(current_pos);
	}

	Vector2f seek(){
		/* AI velocity and speed */
		Vector2f ai_vel = getPhys().getVelocity();
		float ai_speed	= ai_vel.length(); // ai_speed >= 0

		/* target's velocity and speed */
		Vector2f target_vel = target.getPhys().getVelocity();
		float target_speed = target_vel.length();

		/* vector to target */
		Vector2f delta_pos = getVectorToTarget();	// vector difference of positions
		float dist_to_target = delta_pos.length();	// dist to target

		/* lead target by player speed * approximate time arrival time to target */
		delta_pos.add(target_vel.scale(dist_to_target/(ai_speed+1))); // the +X is to avoid div!0


		/* coefficient for calculating length of target vector */

		float speed_cap = Math.max(target_speed+1, 3)  ; // speed cap calculation

		target_vel = delta_pos.normalise().scale(speed_cap);

		return target_vel;

	}

	Vector2f wander(float magnitude){
		Random rand = new Random();
		float rx = (rand.nextFloat()*2-1);
		float ry = (rand.nextFloat()*2-1);
		return new Vector2f(rx,ry).normalise().scale(magnitude*rand.nextFloat());
	}

	/** Performs framewise updates; should be propegated from the base Slick2D game object */
	@Override
	public void update(GameContainer gc, int time_passed_ms){

		/* AI velocity and speed */
		Vector2f ai_vel = getPhys().getVelocity();
		float ai_speed	= ai_vel.length(); // ai_speed

		/* we can set target velocity to whatever we want
		 * in this section and AI will adjust it's velocity
		 * to match it 
		 */
	
		Vector2f target_vel = seek();
		target_vel.add(wander(5.8f));

		/* after this point we calculate the difference between our target velocity and the 
		   current velocity, and attempt to accelerate in the direction of the difference. */

		target_vel.sub(ai_vel);

		float target_theta = restrictAngle( (float)Math.atan2(target_vel.y, target_vel.x) );
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

		// if angles are different enough turn
		if (rot_mag > 0.05f){
			getPhys().rotate(ROTATION*rot_sign);
		}

		// if target velocity is different enough to warrant thrusting thrust.
		if ((target_vel.length() > 0.05f) && (rot_mag < 0.8f*(float)Math.PI ) ){
			getPhys().accelerateAligned(0f, THRUSTER_ACCELERATION);
		}
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
		
		private static final float FRICTION_DEFAULT = .995f;

		public _AITestPhysics(
			Vector2f centroid,
			CollisionImage cimg,
			GraphicalImage gimg
			){
			super(centroid, 0f, cimg, gimg);

			setFriction(FRICTION_DEFAULT);
		}
	}
	
	private static class _AITestCollider extends Collider{
		public AITest ai;
	}

}
