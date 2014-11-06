public class HitLayer{
	
	//------------------
	//--| 'structors |--
	//------------------

	/**
	 * Creates and returns a HitLayer
	 */
	public HitLayer(){
		
	}

	//------------------------------
	//--| Collision Notification |--
	//------------------------------

	/**
	 * Adds a Hitbox to this, including it in all future calls to notifyCollisions().
	 *
	 * @param hb the Hitbox to be added
	 */
	public void addHitbox(Hitbox hb){}

	/**
	 * Removes a Hitbox from this, excluding it from all future calls to notifyCollisions().
	 *
	 * @param hb the Hitbox to be removed
	 */
	public void removeHitbox(Hitbox hb){}

	/**
	 * For every colliding pair of Hitboxes, inform one of the pair that it is colliding with the other.
	 */
	public void notifyCollisions(){}

	//--------------------------
	//--| Test/Debug Methods |--
	//--------------------------

	/** Sandbox driver */
	public static void main(String[] args){
		
	}

}
