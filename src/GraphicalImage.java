/**
 * GraphicsImage.java
 *
 * A class to contain the objects and methods needed to display individual 
 * objects on a screen.
 * 
 * Functionalities include:
 *    - graphical representation
 * 
 * Author:       Erik Steringer
 * Last Updated: 2014-Nov-9 by Erik Steringer
 */
 
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import org.lwjgl.opengl.GL11;

public class GraphicalImage {
	/// fields
	private Shape shapeView;
	private ShapeFill fillup;
	// private Image imageView; // unimplemented until further version
	
	/// constructors
	public GraphicalImage(Shape shape) {
		this.shapeView = shape;
		this.fillup = new GradientFill(0.0f, 0.0f, Color.blue, 1.0f, 1.0f, Color.blue); // default patriotic shapefill
	}
	
	public GraphicalImage(Shape shape, ShapeFill fill) {
		this.shapeView = shape;
		this.fillup = fill;
	}
	
	/// methods
	/**
	 * Draws the containing StellarObject
	 * 
	 */
	public void render(GameContainer gc, Graphics g) {
		g.fill(shapeView, fillup);
	}
	
	/**
	 * Updates by translating the contained shape
	 *
	 */
	public void transform(Transform t) {
		shapeView = shapeView.transform(t);
	}
	
	public float getMidX() {
		return shapeView.getCenterX();
	}
	
	public float getMidY() {
		return shapeView.getCenterY();
	}
}