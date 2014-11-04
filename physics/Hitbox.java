public class Hitbox{

	//------------------
	//--| 'structors |--
	//------------------

	/**
	 * Constructs a Hitbox at the specified location, where p1 and p2
	 * are opposite corner-points.
	 *
	 * @param p1 One corner point of the Hitbox
	 * @param p2 The corner point opposite p1
	 * @param c  The Collidable to notify when this collides with another Collidable
	 */
	public Hitbox(Point p1, Point p2, Collider c){
		
	}
	
	//-----------------------------
	//--| Collision Propegation |--
	//-----------------------------
	//Used to respond to collisions post-detection

	/**
	 * Informs this that it is colliding with every element in hbs
	 *
	 * @param hbs A list of hitboxes, which are colliding with this
	 */
	public void collide(Collection<Hitbox> hbs){
		
	}

	/**
	 * Informs this that it is colliding with the input Hitbox
	 *
	 * @param hb a Hitbox with which this is colliding
	 */
	public void collide(Hitbox hb){
		
	}

	/**
	 * Collides the input collider with the collider stored by this
	 *
	 * @param c The collider with which to collide the stored Collider
	 */
	protected void collide(Collider c){
		
	}

	//----------------------
	//--| Access Methods |--
	//----------------------

	//lower-bounds
	public int getLowerX(){}
	public int getLowerY(){}

	//upper-bounds
	public int getUpperX(){}
	public int getUpperY(){}

	//--------------------------
	//--| Test/Debug Methods |--
	//--------------------------

	/** Sandbox driver */
	public static void main(String[] args){
		
	}

}
