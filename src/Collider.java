/**
 * Collider.java
 *
 * Provides a common medium for communication between colliding objects.
 * Subclasses will typically be used as a wrapper for another object, and are expected to
 * provide the following information:
 *     1) During a collision, what should the contained object do to other objects?
 *     2) During a collision, what are other objects allowed to do to the contained object?
 *
 * Author: Wesley Gydé
 */

/**
 * ## USAGE ##
 *
 * What is this?
 * 	Colliders are mildly quirky in their intended usage. This comment
 * 	is intended to act as a list of conventions to ensure neat and consistent
 * 	use of Colliders.
 *
 * What is a Collider?
 * 	A Collider is an abstract class which replaces a "Collidable" interface. It provides a common interface for
 * 	passing information between objects during a collision. A Collider -contains- the class for which it is handling
 * 	collisions, and the contained class is referred to as "collidable" (in quotes; think of it as a fake Collidable interface,
 * 	thus deserving air-quotes around it).
 *
 * Making Subclasses
 * 	- "One-to-one": In most cases, a Collider subclass will be tailor-made to contain a specific "collidable" class.
 * 		- This type of Collider should be a private inner class for the corresponding "collidable"
 *		- Naming convention: The Collider subclass corresponding to "MyClass" is called "_MyClassCollider"
 * 	- "Many-to-one": Classes with variable collision behavior can have multiple Colliders.
 *		- These colliders should be contained within a submodule of the module containing their corresponding "collidable"
 *		- Naming convention: same as the one-to-one/tailor-made, but with an extra word (ideally only one extra word) as a description. For example, "_MyClassColliderSpiky" and "_MyClassColliderSmooth".
 *	- "One-to-many": The ONLY time when a Collider should correspond to multiple classes is when those classes are all children of a common class/interface
 * 		- This type of Collider should be defined in the common class/interface, following all of the "one-to-one" rules/conventions.
 * 		- Why not allow this in more cases? Having one collider per interface makes it easy to update the Collider whenever the interface changes.
 *
 * Modifying the Collision Interface
 * 	- Methods can be added here as needed, but...
 * 	- DO NOT change existing methods
 * 		- you will break a LOT of code if you try to do this
 * 		- keep in mind that people will not be able to modify the things that you add. Confer with a teammate before finalizing changes.
 * 	- Try not to add more than is necessary
 * 		- why?
 * 			- fewer methods means fewer method-implementations per subclass and less time searching for a desired collision-action in the collision interface.
 * 			- generalized methods (see the elemental damage example below) can be extended in functionality without changing the abstract Collider class.
 * 			- extraneous/duplicate/unnecessary methods are not easy to delete (requires modification to every subclass which implemented them).
 * 		- use existing methods if at all possible
 * 		- favor a few general-purpose methods over many specific ones
 *	 		- BAD:  public void inflictFireDamage(int amount); [...] public void inflictWaterDamage(int amount); [...] public void inflictEarthDamage(int amount)
 *	 		- GOOD: public void inflictElementalDamage(int amount, Element el);
 * 	- When possible, methods should act upon the callee
 * 		- why?
 * 			- separates "me acting on you" from "you acting on me". The former can be found in the collide() method, the latter can be found in the collision interface.
 * 			- each collision-action in the collision interface is isolated to a single, clearly-named, easy-to-use method.
 * 			- replaces unnecessary conditionals with optional method calls
 *		- intuitive example:
 * 		- BAD:  this.damageMyself(  c.getDamageAmount() ); //called in collider1.collide(collider2)
 * 		- GOOD: c.inflictDamage( this.getDamageAmount() ); //called in collider2.collide(collider1)
 *		- unintuitive example:
 *		- BAD:  if (c.isSolid()){ //called in collider1.collide(collider2); c.isSolid() returns a boolean, indicating whether or not c is solid.
 * 		- GOOD: c.informSolid();  //called in collider2.collide(collider1); c.informSolid() tells c that it is colliding with a solid object.
 *
 * Direct any/all questions to the author, Wesley Gydé.
 */

public abstract class Collider{

	/**
	* Perform a collision on the input Collider
	*
	* @param c The Collider on which to perform a collision
	*/
	public void collide(Collider c){}

	//---------------------------
	//--| Collision Interface |--
	//---------------------------
	//methods by which to interact with another Collidable during a collision

	//EXAMPLES:
	//public void inflictDamage(int dam);
	//public void impartForce(double force_newtons);
	//
	//public static final int SOLID_TO_PLAYER = 0b1;
	//public static final int SOLID_TO_BULLET = 0b10;
	//public static final int SOLID_TO_ENEMY  = 0b100;
	//public void informSolidity(int solidity);

}
