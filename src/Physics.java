/**
 * Physics.java
 *
 * Handles all of the physics for an object:
 *    - logic and overarching interface
 *    - collision detection (see CollisionImage.java)
 *    - graphical representation (see GraphicalImage.java)
 *
 * //FIXME: after GraphicalImage is implemented, run ":%s/\/\/GIMG//g" and fix all relevant fixmes.
 *
 * Author: Wesley Gydé
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

//DEBUG imports
import org.newdawn.slick.Color;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;

public class Physics{

	//position
	private Vector2f position;
	private float theta;

	//motion
	private Vector2f velocity;
	private float dtheta;

	//cimg and gimg
	private CollisionImage cimg;
	//GIMGprivate GraphicalImage gimg;

	//------------------
	//--| 'structors |--
	//------------------
	
	/**
	 * Constructor
	 *
	 * @param centroid A base-location at which this will be created
	 */
	public Physics(
		Shape collision_mask,
		Collider collider,
		Vector2f position//GIMG,
		//GIMG//FIXME: gimg args
		){

		cimg = new CollisionImage( collision_mask, collider );
		//GIMGgimg = new GraphicalImage(); //FIXME: make a gimg with gimg args

		this.position = new Vector2f(position);
		theta = 0f;
		velocity = new Vector2f(0f, 0f);
		dtheta = 0f;

	}

	//---------------------
	//--| Physics Logic |--
	//---------------------

	/**
	 * Adds the input dx and dy to the stored velocity
	 *
	 * @param dx The change in velocity (x)
	 * @param dy The change in velocity (y)
	 */
	public void impartVelocity(float dx, float dy){
		velocity.x += dx;
		velocity.y += dy;
	}

	/**
	 * Adds the input vector to the stored velocity
	 *
	 * @param dv The change in velocity
	 */
	public void impartVelocity(Vector2f dv){
		impartVelocity(dv.getX(), dv.getY());
	}

	/**
	 * 
	 */
	public void impartForwardVelocity(float dx, float dy){
		impartVelocity( 
			(float)(dx*Math.cos(theta) - dy*Math.sin(theta)),
			(float)(dx*Math.sin(theta) + dy*Math.cos(theta))
			);
	}

	/**
	 * Adds the input dtheta to the stored rotation angle.
	 *
	 * @param dtheta The change in rotation; must be in the range 
	 */
	public void rotate(float dtheta){
		assert (dtheta >= 0) && (dtheta <= float2pi);

		this.dtheta = restrictAngle(this.dtheta + dtheta);
	}

	/** Returns theta as an angle in the range [0,2π]*/
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
	public void update(GameContainer gc, int time_passed_ms) throws SlickException {

		//update poisition ("centroid")
		position = position.add(velocity);
		theta = restrictAngle(theta + dtheta);

		//update gimg and cimg
		Transform motion_t = ( new Transform() )
			.concatenate(Transform.createRotateTransform( dtheta, position.getX(), position.getY() )
			.concatenate(Transform.createTranslateTransform( velocity.getX(), velocity.getY() )
			));
		cimg.transform(motion_t);
		//GIMGgimg.transform(t);

		//update motion (friction, etc.)
		dtheta = 0f;

	}

	/** Performs graphical updates; should be propegated from the base Slick2D game object */
	public void render(GameContainer gc, Graphics g) throws SlickException {

		//DEBUG - draw centroid
		g.draw(
			new Rectangle(
				position.x - 1f,
				position.y - 1f,
				2f,
				2f
			),
			new GradientFill(
				0.0f,   0.0f, Color.red,
				1.0f,   1.0f, Color.red
			));

		cimg.render(gc, g); //DEBUG - should be propegated from gimg, if it is called at all
		//GIMGgimg.render(gc, g);
		
	}

	//----------------------
	//--| Access Methods |--
	//----------------------

	public CollisionImage getCImg(){return cimg;}
	//GIMGpublic GraphicalImage getGImg(){return gimg;}

}
