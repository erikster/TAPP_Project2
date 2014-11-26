/**
 *
 *
 *
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

public class AIHelper{
	
	//needed as a return value in several places
	protected class Movement{
		public Movement(Vector2f acceleration, float rotation){
			this.acceleration = acceleration;
			this.rotation = rotation;
		}

		public final Vector2f acceleration;
		public final float rotation;

		public Movement scale(float f){
			return new Movement(
				acceleration.scale(f),
				f < 1f ? rotation*f : rotation
				);
		}
	}

	private StellarObject subject;
	private float max_acceleration = .05f;
	private float max_rotation = .05f;
	private float max_velocity;

	//------------------
	//--| 'structors |--
	//------------------

	/**
	 * Constructor
	 *
	 * @param so a StellarObject to control
	 */
	AIHelper(StellarObject so){
		subject = so;

		float decel_from_friction = (1-so.getPhys().getFriction()); //percentage of acceleration lost to friction, per frame.
		max_velocity = (decel_from_friction==0) ? Float.MAX_VALUE : max_acceleration/decel_from_friction ;
	}
	
	//-----------------------------------
	//--| Data Collection and Metrics |--
	//-----------------------------------
	
	/** Returns the position of the input StellarObject. */
	private Vector2f getPositionOf(StellarObject so){
		return so.getPhys().getPosition();
	}

	/** Returns the rotation of the input StellarObject. */
	private float getRotationOf(StellarObject so){
		return restrictAngle(so.getPhys().getRotation());
	}

	/** Returns the velocity of the input StellarObject. */
	private Vector2f getVelocityOf(StellarObject so){
		return so.getPhys().getVelocity();
	}

	/** Returns a Collection of Shapes, representing visible objects with the potential to damage this */
	/*// UNIMPLEMENTED
	private Collection<Shape> getVisibleDamaging(){
		
	}
	//*/

	//---------------------------
	//--| Common Calculations |--
	//---------------------------
	
	/** Gets a vector to the input StellarObject's position (see getVectorTo(Vector2f)). */
	protected Vector2f getVectorTo(StellarObject so){return getVectorTo(getPositionOf(so));}

	/**
	 * Returns a displacement-vector between the current position and the input position
	 *
	 * @param target_pos the position to which a displacement-vector will be computed
	 * @return the aforementioned vector
	 */
	protected Vector2f getVectorTo(Vector2f target_pos){
		Vector2f current_pos = getPositionOf(subject);
		return target_pos.sub(current_pos);
	}

	/**
	 * Computes and returns a rotation-angle [-π,π], which will rotate this towards the specified position
	 *
	 * This method will not actually perform the rotation; see "Atomic Actions" if you want that.
	 *
	 * @param pos       the position to turn towards
	 * @return a rotation-angle, which can be used to rotate this towards the input position
	 */
	private float getRotationTowards(Vector2f target_pos){
		//compute current/target rotations
		float current_rot = getRotationOf(subject);
		float target_rot = restrictAngle((float)Math.toRadians(getVectorTo(target_pos).getTheta()));

		//compute angular difference in range [π,-π]
		float angle = target_rot - current_rot; //calculate the difference in angle
		angle = angle >  floatpi ? float2pi-angle : angle; //restrict to [π,-∞)
		angle = angle < -floatpi ? float2pi+angle : angle; //restrict to (∞,-π]

		return angle;
	}

	/**
	 * Restricts and returns rot.
	 *
	 * rot is restricted to the range [maxrot, -maxrot], and 0f is returned if it falls in the range [tolerance, -tolerance]
	 *
	 * @param rot       the rotation-angle to restrict
	 * @param maxrot    the maximum allowed rotation
	 * @param tolerance the allowable margin-of-error in rotation; zeroes out small angles to avert an excessive number of tiny rotations
	 * @return the angle, as restricted by maxrot and tolerance
	 */
	private float restrictRotation(float rot, float maxrot, float tolerance){
		//if angle is within tolerance, don't rotate
		if (Math.abs(rot) < tolerance){return 0f;}

		//cap rotation at maxrot
		rot = Math.max(rot, -maxrot);
		rot = Math.min(rot,  maxrot);

		return rot;
	}

	/**
	 * Restricts and returns the input velocity vector
	 *
	 * The output velocity will comply with the maximum-allowable velocity.
	 *
	 * @param v the velocity to restrict
	 * @return the velocity, restricted in the aforementioned ways
	 */
	private Vector2f restrictVelocity(Vector2f v){
		if (v.length() > max_velocity){
			return v.getNormal().scale(max_velocity);
		}
		return v;
	}

	/**
	 * Restricts and returns the input velocity vector
	 *
	 * The output velocity will comply with the maximum-allowable velocity.
	 *
	 * @param v the velocity to restrict
	 * @return the velocity, restricted in the aforementioned ways
	 */
	private Vector2f restrictAcceleration(Vector2f a){
		if (a.length() > max_acceleration){
			return a.getNormal().scale(max_acceleration);
		}
		return a;
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
	private static final float floatpi = (float)Math.PI * 2f;
	private static final float float2pi = 2*floatpi;

	//----------------------
	//--| Atomic Actions |--
	//----------------------

	/**
	 * Rotate the specified number of radians
	 *
	 * @param rads rotation, in radians
	 */
	protected void rotate(float rads){
		subject.getPhys().rotate(rads);
	}

	/**
	 * Accelerates according to the input velocity vector.
	 *
	 * vvv UNIMPLEMENTED vvv
	 * Note that the input vector will be restricted to conform with acceleration/rotation restrictions.
	 *
	 * @param dv Literally "Δv"; the desired change in velocity
	 */
	protected void accelerate(Vector2f dv){

		/* //Use the same physics as the player

		rotate(
			restrictRotation(
				getRotationTowards(
					getPositionOf(subject).add(getVelocityOf(subject).add(dv))
				),
				max_rotation,
				.001f
			)
		);
		subject.getPhys().accelerateAligned(0, max_acceleration);

		/*/ //Use the typical steering-pattern physics

		dv = restrictAcceleration(dv);
		subject.getPhys().accelerate(dv.x, dv.y);
		rotate((float)Math.toRadians(dv.getTheta()) - getRotationOf(subject));

		//*/
		
	}

	/** Fires a bullet (or executes an atomic shot, such as a shotgun blast / burst-fire / whatever */
	/*// UNIMPLEMENTED
	private void shoot(){
		
	}
	//*/

	//-------------------------
	//--| Steering Patterns |--
	//-------------------------

	/**
	 * Moves towards the specified target
	 *
	 * @return Vector2f The acceleration vector produced by this steering pattern
	 */
	protected Vector2f seek(StellarObject target){
		
		/* //DOES NOT WORK
		//predict position of target

		//	misc. objects I'll be needing
		Vector2f vs = getVelocityOf(subject);
		Vector2f vt = getVelocityOf(target);
		Vector2f ps = getPositionOf(subject);
		Vector2f pt = getPositionOf(target);
		float θ = -(float)Math.toRadians(vt.getTheta());

		//	english letters (concrete quantities; more gathered than calculated)
		float psx = (ps.x*(float)Math.cos(θ)) - (ps.y*(float)Math.sin(θ));
		float psy = (ps.x*(float)Math.sin(θ)) + (ps.y*(float)Math.cos(θ));
		float vsm = max_velocity;
		float vtm = vt.length();

		float psx2 = (float)Math.pow(psx, 2);
		float psy2 = (float)Math.pow(psy, 2);
		float vsm2 = (float)Math.pow(vsm, 2);
		float vtm2 = (float)Math.pow(vtm, 2);

		//	greek letters (gibberish spat out by wolfram)

		//	time to contact
		float t = ((float)Math.sqrt((vsm2*psx2) + (vsm2*psy2) - (vtm2*psy2)) - (vtm*psx))/(vsm2-vtm2);
		System.out.println("t=" + t);
		*/

		//calculate ideal acceleration
		Vector2f velocity_current = getVelocityOf(subject);
		Vector2f velocity_target  = restrictVelocity(getVectorTo(target));
		return velocity_target.sub(velocity_current);		
	}

	/**
	 * Runs away from the input target
	 *
	 * @return Vector2f The acceleration vector produced by this steering pattern
	 */
	/*protected Vector2f flee(StellarObject target){
		return seek(target).negate();
	}

	/**
	 * Wanders aimlessly
	 *
	 * @return Vector2f The movement vector produced by this steering pattern
	 */
	/*// UNIMPLEMENTED
	private Vector2f wander(float magnitude){
		
	}
	//*/

	/**
	 * Attempts to dodge nearby obstacles
	 *
	 * @return Vector2f The movement vector produced by this steering pattern
	 */
	/*// UNIMPLEMENTED
	private Vector2f obstacleAvoid(){
		
	}
	//*/
	
	//--------------
	//--| Update |--
	//--------------

	/** Performs framewise updates; should be propegated from the base Slick2D game object */
	public void update(GameContainer gc, int time_passed_ms) {
	}

}
