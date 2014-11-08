/**
 * CollisionImage.java
 *
 * Handles collision-detection for a StellarObject
 *
 * Author: Wesley Gydé
 *
 */

import java.util.HashSet;
import org.newdawn.slick.Color;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.SlickException;

public class CollisionImage{
	
	private static final ShapeFill DEFAULT_FILL = new GradientFill(
		   0.0f,   0.0f, Color.cyan,
		   1.0f,   1.0f, Color.cyan
		);
	private ShapeFill fill;

	private Container<Shape> shape_container;
	private Collider col;
	private HashSet<CollisionLayer> layers;
	
	//------------------
	//--| 'structors |--
	//------------------

	/**
	 * Constructor
	 *
	 * @param shape an initial shape, which is used as a collision-mask (arbitrarily-shaped hitbox).
	 * @param col   a Collider, which is activated during collisions
	 */
	public CollisionImage(Shape shape, Collider col){
		super();
		shape_container = new Container<Shape>(shape);
		this.col = col;
		this.layers = new HashSet<CollisionLayer>();
		fill = DEFAULT_FILL;
	}

	/** Prepares this for destruction; unsubscribes from all push-event systems. */
	public void destroy(){
		for (CollisionLayer cl : layers){
			cl.remove(shape_container);
		}
	}

	//---------------------------------
	//--| CollisionLayer Add/Remove |--
	//---------------------------------

	/**
	 * Passes collision information to cl, causing this to be notified when it collides
	 * with other members of cl.
	 *
	 * FIXME: exception for duplicate layer? let the layer take care of that?
	 *
	 * @param cl The CollisionLayer which this willadd itself to
	 */
	public void addTo( CollisionLayer cl ){
		cl.add(shape_container, col);
		layers.add(cl);
	}

	/**
	 * Removes collision information from cl, preventing future notifications about
	 * collisions with other members of cl.
	 *
	 * FIXME: exception for not-currently-added layer? let the layer take care of that?
	 *
	 * @param cl The CollisionLayer which this willadd itself to
	 */
	public void removeFrom( CollisionLayer cl ){
		cl.remove(shape_container);
		layers.remove(cl);
	}

	//--------------------------
	//--| Updates / Graphics |--
	//--------------------------

	/** Updates the location/orientation of this via the input Transform. */
	public void transform(Transform t){
		shape_container.val = shape_container.val.transform(t);
	}

	/** Performs graphical updates; should be propegated from the base Slick2D game object */
	public void render(GameContainer gc, Graphics g) throws SlickException{
		g.draw( shape_container.val, fill ); //For some reason, using a fill is faster than g.setColor().
	}

	public void setFill(ShapeFill f){
		fill = f;
	}

}
