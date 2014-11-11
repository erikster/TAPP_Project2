/**
 * SlickSandbox.java
 *
 * Used as a quick reference for Slick functionality, and to experiment with
 * the library in a controlled environment.
 *
 * Code is based on an example given by Slick2D, found here:
 *     http://slick.ninjacave.com/wiki/index.php?title=Hello_World
 *
 * Author/Modifier: Wesley Gydé
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.HashSet;
import org.newdawn.slick.Color;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Vector2f;

public class SlickSandbox extends BasicGame
{

	//Fill colors for shapes
	private ShapeFill fillBlue = new GradientFill(
			   0.0f,   0.0f, Color.blue,
			   1.0f,   1.0f, Color.blue
			);

	private ShapeFill fillRed = new GradientFill(
			   0.0f,   0.0f, Color.red,
			   1.0f,   1.0f, Color.red
			);

	private ShapeFill fillGreen = new GradientFill(
			   0.0f,   0.0f, Color.green,
			   1.0f,   1.0f, Color.green
			);

	//Test objects
	Physics p;
	HashSet<Character> keys_pressed = new HashSet<Character>();

	ArrayList<CollisionImage> cimgs = new ArrayList<CollisionImage>();

	CollisionLayer cl = new CollisionLayer();
	private class B{public boolean b;}
	public void addShape(Shape shape){
		final B b1 = new B();
		CollisionImage cimg = new CollisionImage(
			shape,
			new Collider(){
				B bb = b1;
				@Override
				public void collide(Collider c){bb.b = true;}
			}
			){
				B bb = b1;
				@Override
				public void render(GameContainer gc, Graphics g) throws SlickException{
					super.setFill( bb.b?fillRed:fillBlue );
					super.render(gc, g);
					bb.b = false;
				}
			};
		cimg.addTo(cl);
		cimgs.add(cimg);

	}

	boolean firstkey = false;

	//END FIELDS


	public SlickSandbox(String gamename){
		super(gamename);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		Collider c = new Collider(){
			};
		Vector2f pos = new Vector2f(100f,100f);
		Shape s = new Polygon( new float[]{
			pos.getX(), pos.getY() + 5f,
			pos.getX() + 5f, pos.getY() - 5f,
			pos.getX() - 5f, pos.getY() - 5f,
			});
		p = new Physics(s,c,pos);
		p.getCImg().addTo(cl);
		addShape(new Rectangle(200f,200f,40f,40f));
		addShape(new Rectangle(100f,200f,50f,150f));
		addShape(new Ellipse(250,375,50,70));
		addShape(new Polygon(new float[]{
			400,200,
			430,210,
			460,230,
			490,280,
			490,330,
			430,340,
			400,350,
            400,300,
			430,330,
			480,300
			}));
	}

	@Override
	public void update(GameContainer gc, int time_passed_ms) throws SlickException {
		if (keys_pressed.contains('w')){
			p.impartForwardVelocity(0f,.05f);
		}
		if (keys_pressed.contains('a')){
			p.rotate(-.05f);
		}
		if (keys_pressed.contains('d')){
			p.rotate(.05f);
		}

		p.update(gc, time_passed_ms);

		cl.notifyCollisions();
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		p.render(gc, g);
		for (CollisionImage cimg : cimgs){
			cimg.render(gc, g);
		}
		if (!firstkey){
			g.drawString("Move w/ WASD\nShapes light up on collision\n- Press any key -", 200, 100);
		}
	}

	@Override
	public void keyPressed(int key, char c){
		firstkey = true;
		keys_pressed.add(c);
	}

	@Override
	public void keyReleased(int key, char c){
		keys_pressed.remove(c);
		
	}

	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new SlickSandbox("PHYSICS DEMO 1.0"));
			appgc.setDisplayMode(640, 480, false);
            appgc.setTargetFrameRate(60);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(SlickSandbox.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
