/**
 * CollisionLayer.java
 *
 * Checks a group of Shapes for intercollisions, and activates corresponding Colliders when a collision is detected.
 *
 * Author: Wesley Gydé
 */

import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import org.newdawn.slick.geom.Shape;

public class CollisionLayer{
	
	private class ShapeCollider{
		
		private Container<Shape> s;
		private Collider c;

		/**
		 * Constructor
		 * 
		 * @param s the Shape to be used in precision-collision-detection
		 * @param c the Collider to be activated if/when a collision is detected
		 */
		public ShapeCollider(Container<Shape> s, Collider c){
			this.s = s;
			this.c = c;
		}

		/**
		 * Performs precision collision-detection on the input ShapeColliderBound, and
		 * activates the Colliders for this and the input ShapeColliderBound if a collision
		 * is detected
		 * 
		 * @param scb The ShapeColliderBound to perform precision collision-detection with
		 */
		public void collide( ShapeCollider sc ){

			//are the Shapes belonging to this and scb colliding?
			if (   getShape().intersects( sc.getShape() )
				|| getShape().contains( sc.getShape() )
				|| sc.getShape().contains( getShape() )
				){

				//inform the colliders of a collision
				c.collide( sc.getCollider() );
				sc.getCollider().collide( c );

			}

		}

		public boolean sameContainer(Container<Shape> s){return this.s == s;}
		public Shape getShape(){ return s.val; }
		public Collider getCollider(){ return c; }

	}

	//One bound (upper/lower x/y) of a Shape's bounding box
	private class ShapeColliderBound implements Comparable<ShapeColliderBound>{
		
		//which_bound values
		public static final int LOWER_X = 0;
		public static final int LOWER_Y = 1;
		public static final int UPPER_X = 2;
		public static final int UPPER_Y = 3;
		
		private ShapeCollider sc;
		private int which_bound;

		/**
		 * Constructor
		 * 
		 * @param s           a shape, used for precision-collision-detection
		 * @param c           a collider to activate if a collision is detected
		 * @param which_bound which type of bound is this? (upper/lower, x/y)
		 */
		public ShapeColliderBound(ShapeCollider sc, int which_bound){
			assert
				   (which_bound == LOWER_X)
				|| (which_bound == LOWER_Y)
				|| (which_bound == UPPER_X)
				|| (which_bound == UPPER_Y);
			
			this.sc = sc;
			this.which_bound = which_bound;
		}

		
		/** Returns the bound stored by this (the bound of shape corresponding to which_bound) */
		public float getBound(){
			switch (which_bound){
				case LOWER_X:
					return sc.getShape().getMinX();
				case LOWER_Y:
					return sc.getShape().getMinY();
				case UPPER_X:
					return sc.getShape().getMaxX();
				case UPPER_Y:
					return sc.getShape().getMaxY();
			}

			//FIXME: invlaid which_bound value

			return -1;
		}
	
		public int getWhichBound(){ return which_bound; }
		public ShapeCollider getShapeCollider(){ return sc; }

		@Override
		public int compareTo(ShapeColliderBound scb){
			float f = getBound() - scb.getBound();
			int i = 0;
			if (f > 0){
				i =  1;
			}else if (f < 0){
				i = -1;
			}
			return i;
		}
			
	}

	private LinkedList<ShapeColliderBound> bounds_x;
	private LinkedList<ShapeColliderBound> bounds_y;

	//------------------
	//--| 'structors |--
	//------------------

	/**
	 * Creates and returns a HitLayer
	 */
	public CollisionLayer(){

		bounds_x = new LinkedList<ShapeColliderBound>();
		bounds_y = new LinkedList<ShapeColliderBound>();

	}

	//------------------------------
	//--| Collision Notification |--
	//------------------------------

	/**
	 * Adds s to this CollisionLayer, and activates c when s collides with other Shapes in this CollisionLayer.
	 * 
	 * FIXME: Throws an exception if s is already in this CollisionLayer.
	 *
	 * @param s the Shape to add
	 * @param c the Collider to associate with s
	 */
	public void add(Container<Shape> s, Collider c){
		//FIXME: exception for adding duplicate Shape

		ShapeCollider sc = new ShapeCollider(s, c);
		bounds_x.add(new ShapeColliderBound( sc, ShapeColliderBound.LOWER_X ));
		bounds_y.add(new ShapeColliderBound( sc, ShapeColliderBound.LOWER_Y ));
		bounds_x.add(new ShapeColliderBound( sc, ShapeColliderBound.UPPER_X ));
		bounds_y.add(new ShapeColliderBound( sc, ShapeColliderBound.UPPER_Y ));

	}

	/**
	 * Removes s from this CollisionLayer.
	 *
	 * FIXME: Throws an exception if s is not in this CollisionLayer.
	 *
	 * @param s the Shape to remove
	 */
	public void remove(Container<Shape> s){
		//FIXME: exception for removing nonexistant Shape

		Iterator<ShapeColliderBound> itx = bounds_x.iterator();
		Iterator<ShapeColliderBound> ity = bounds_y.iterator();
		while (itx.hasNext() && ity.hasNext()){
			ShapeColliderBound scbx = itx.next();
			ShapeColliderBound scby = ity.next();
			if ( scbx.getShapeCollider().sameContainer(s) ){ itx.remove(); }
			if ( scby.getShapeCollider().sameContainer(s) ){ ity.remove(); }
		}

		//same number of bounds in each list
		assert (!itx.hasNext()) && (!ity.hasNext());		

	}

	/**
	 * Activate the corresponding Colliders for each pair of colliding Shapes.
	 * Collisions only happen once; "A collides B" and "B collides A" will NOT
	 * be detected as separate collisions.
	 */
	@SuppressWarnings("unchecked")
	public void notifyCollisions(){
		Collections.sort(bounds_x);
		Collections.sort(bounds_y);

		HashSet<ShapeCollider> active_sc = new HashSet<ShapeCollider>();
		HashMap<ShapeCollider, HashSet<ShapeCollider>> collisions_x = new HashMap<ShapeCollider, HashSet<ShapeCollider>>();

		for ( ShapeColliderBound xscb : bounds_x ){

			ShapeCollider xsc = xscb.getShapeCollider();
			if ( active_sc.contains( xsc ) ){
				active_sc.remove( xsc );
			} else {
				collisions_x.put( xsc, (HashSet<ShapeCollider>)active_sc.clone() );
				active_sc.add( xsc );
			}

		}
		
		//every ShapeCollider should have an even number of bounds (specifically, 2 x bounds)
		assert active_sc.isEmpty(); 

		for ( ShapeColliderBound yscb : bounds_y ){

			ShapeCollider ysc = yscb.getShapeCollider();
			if ( active_sc.contains(ysc) ){
				active_sc.remove( ysc );
			} else {
				for( ShapeCollider asc : active_sc ){

					if (   collisions_x.get(asc).contains(ysc)
						|| collisions_x.get(ysc).contains(asc)
						){
						asc.collide(ysc);
					}
				}
				active_sc.add( ysc );
			}

		}

		//every ShapeCollider should have an even number of bounds (specifically, 2 y bounds)
		assert active_sc.isEmpty();
		

	}

	//--------------------------
	//--| Test/Debug Methods |--
	//--------------------------

	/** Sandbox driver */
	public static void main(String[] args){


	}

}
