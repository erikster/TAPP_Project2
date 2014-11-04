public class LinkedPoint extends Point{
	
	public static final LinkedPoint STATIC_ANCHOR; //always at x=0, y=0, rot=0

	//------------------
	//--| 'structors |--
	//------------------
	
	/**
	 * Creates a LinkedPoint, which maintains coordinates relative to the input AnchorPoint
	 *
	 * @param x               the x-coordinate relative to ap
	 * @param y               the y-coordinate relative to ap
	 * @param ap              the AnchorPoint to which this will be anchored
	 * @param ignore_rotation (optional) if true, does not change î and ĵ according to the rotation of ap
	 */
	public LinkedPoint(double x, double y, AnchorPoint ap){

	}
	public LinkedPoint(double x, double y, AnchorPoint ap, boolean ignore_rotation){

	}

	//----------------------
	//--| Access Methods |--
	//----------------------

	//absolute (non-relative) position
	@Override
	public double getX(){}
	@Override
	public double getY(){}

	//position relative to anchor
	public double getRelX(){}
	public double getRelY(){}

	//setters
	public double setRelX(double x){}
	public double setRelY(double y){}

}
