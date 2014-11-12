/**
 * Physics.java
 *
 * Handles all of the physics for an object:
 *    - logic and overarching interface
 *    - collision detection (see CollisionImage.java)
 *    - graphical representation (see GraphicalImage.java)
 *
 */
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

//DEBUG imports
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.fills.GradientFill;

public abstract class Physics{

	//position
	private Vector2f centroid_pos;
	private float centroid_rot;

	//motion
	private Vector2f velocity;
	private float drot;

	//cimg and gimg
	private CollisionImage cimg;
	private GraphicalImage gimg;

	//------------------
	//--| 'structors |--
	//------------------

	/**
	 * Constructor
	 * 
	 * @param centroid A point representing the object's centroid; used as a point of reference in construction, a center of mass in "Physics Logic", and likewise in other places.
	 * @param centroid_rot (optional) Specifies an initial rotation for the centroid.
	 * @param cimg A collision image, which detects and handles collisions with other Physics instances.
	 * @param gimg A graphical image, responsible for drawing this Physics instance on the screen.
	 */
	public Physics(
		Vector2f centroid,
		CollisionImage cimg,
		GraphicalImage gimg
		){
		this(centroid, 0f, cimg, gimg);
	}
	public Physics(
		Vector2f centroid,
		float centroid_rot,
		CollisionImage cimg,
		GraphicalImage gimg
		){
		this.cimg = cimg;
		this.gimg = gimg;
		centroid_pos = centroid;
		this.centroid_rot = centroid_rot;

		velocity = new Vector2f(0f, 0f);
		drot = 0f;
	}

	/** Destroys this, removing it from any/all push-notification systems */
	public void destroy(){
		cimg.destroy();
	}

	//---------------------
	//--| Physics Logic |--
	//---------------------
	
	/**
	 * Rotates this by dtheta degrees. Rotation is done once, not per-frame.
	 * dtheta is a change in rotational position, not rotational velocity.
	 * 
	 * @param drot The change in rotational position (radians)
	 */
	public void rotate(float drot){
		assert (drot >= 0) && (drot <= float2pi);

		this.drot = restrictAngle(this.drot + drot);
	}

	/**
	 * Accelerates this - that is, changes its velocity by the input amount.
	 *
	 * @param dvx The change in velocity (x)
	 * @param dvy The change in velocity (y)
	 */
	public void accelerate(float dvx, float dvy){
		velocity.x += dvx;
		velocity.y += dvy;
	}
	

	/**
	 * Imparts the input velocity, rotated such that ĵ aligns with the rotation of the centroid
	 *
	 * @param dvs The change in velocity (sideways/strafing - 90degrees counter-colockwise from centroid's rotation)
	 * @param dvf The change in velocity (forwards - aligned with centroid's rotation)
	 */
	public void accelerateAligned(float dvs, float dvf){
		accelerate( 
			(float)(dvs*Math.cos(centroid_rot) - dvf*Math.sin(centroid_rot)),
			(float)(dvs*Math.sin(centroid_rot) + dvf*Math.cos(centroid_rot))
			);
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
	//--| Updates |--
	//---------------

	/** Performs framewise updates; should be propegated from the base Slick2D game object */
	public void update(GameContainer gc, int time_passed_ms){
		//update poisition (centroid)
		centroid_pos = centroid_pos.add(velocity);
		centroid_rot = restrictAngle(centroid_rot + drot);

		//update gimg and cimg
		Transform motion_t = ( new Transform() )
			.concatenate(Transform.createRotateTransform( drot, centroid_pos.getX(), centroid_pos.getY() )
			.concatenate(Transform.createTranslateTransform( velocity.getX(), velocity.getY() )
			));
		cimg.transform(motion_t);
		gimg.transform(motion_t);

		//update motion (friction, etc.)
		drot = 0f;
	}

	/** Performs graphical updates; should be propegated from the base Slick2D game object */
	public void render(GameContainer gc, Graphics g) throws SlickException{
		//DEBUG - draw centroid
		g.draw(
			new Rectangle(
				centroid_pos.x - 1f,
				centroid_pos.y - 1f,
				2f,
				2f
			),
			new GradientFill(
				0.0f,   0.0f, Color.red,
				1.0f,   1.0f, Color.red
			));

		cimg.render(gc, g); //DEBUG - should be propegated from gimg, if it is called at all
		gimg.render(gc, g);
	}

	//----------------------
	//--| Access Methods |--
	//----------------------
	
	/** Returns cimg, as it was passed to the constructor */
	public CollisionImage getCImg(){return cimg;}

	/** Returns gimg, as it was passed to the constructor */
	public GraphicalImage getGImg(){return gimg;}

}
