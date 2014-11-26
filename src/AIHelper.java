/**
 * AIHelper.java
 *
 * Provides helper methods (including steering patterns) to simplify AI implementations elsewhere. Like Physis and Collider,
 * this class is intended to exist as a private subclass within an implementing superclass.
 *
 * Author: Wesley Gydé
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

public class AIHelper{
	
	private StellarObject subject;
	private float max_acceleration = .05f;
	private float max_rotation = .05f;
	private float max_velocity;

	//------------------
	//--| 'structors |--
	//------------------

	/** Links this with a StellarObject; this method must be called for most other methods in the class to work properly */
	public void setSubject(StellarObject subject){
		this.subject = subject;
		float decel_from_friction = (1-subject.getPhys().getFriction()); //percentage of acceleration lost to friction, per frame.
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
	 * @param dv Literally "Δv"; the desired change in velocity
	 */
	protected void accelerate(Vector2f dv){

		//* //Use the same physics as the player
		
		Vector2f target_velocity = getVelocityOf(subject).add(dv);
		float target_rot = (float)Math.toRadians(target_velocity.getTheta());
		rotate(
			restrictRotation(
				getRotationTowards(
					getPositionOf(subject).add(target_velocity)
				),
				max_rotation,
				.05f
			)
		);
		
		float tolerance = floatpi/3;
		if( Math.abs(getRotationOf(subject) - target_rot) < tolerance ){
			subject.getPhys().accelerateAligned(0, max_acceleration);
		}

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
	 * @param target The target to seek
	 * @return Vector2f The acceleration vector produced by this steering pattern
	 */
	protected Vector2f seek(StellarObject target){
		//approximate target position post-movement
		Vector2f velocity_desired = getVectorTo(target);
		Vector2f velocity_current = getVelocityOf(subject);
		return velocity_desired.sub(velocity_current);
	}

	/**
	 * Runs away from the input target
	 *
	 * @param target The target to flee from
	 * @return Vector2f The acceleration vector produced by this steering pattern
	 */
	protected Vector2f flee(StellarObject target){
		return seek(target).negate();
	}

	/**
	 * Pursue the specified target (attempt to predict its motion, and seek)
	 *
	 * @param target The target to pursue
	 * @return Vector2f The acceleration vector produced by this steering pattern
	 */
	protected Vector2f pursue(StellarObject target){
		float time = getVectorTo(target).length() / max_velocity;
		Vector2f velocity_desired =
			restrictVelocity(
				getPositionOf(target).add(
					getVelocityOf(target).scale(
						time
					)
				).sub(
					getPositionOf(
						subject
					)
				)
			);
		Vector2f velocity_current = getVelocityOf(subject);
		return velocity_desired.sub(velocity_current);
	}

	/**
	 * Wanders aimlessly
	 * Returns a random vector with the specified magnitude
	 *
	 * @param magnitude The magnitude of the change in velocity imparted by this
	 * @return Vector2f The movement vector produced by this steering pattern
	 */
	protected Vector2f wander(float magnitude){
		double theta = Math.random()*2*Math.PI;
		return new Vector2f(
			(float)( magnitude*Math.cos(theta) ),
			(float)( magnitude*Math.sin(theta) )
			);
	}

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
