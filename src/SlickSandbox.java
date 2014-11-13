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
	PlayerShip p;
	AITest ai;

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
					super.setColor( bb.b?Color.red:Color.blue );
					super.render(gc, g);
					bb.b = false;
				}
			};
		cimg.addTo(cl);
		cimgs.add(cimg);

	}

	boolean killbox_activated = false;
	CollisionImage killbox = new CollisionImage(
		new Rectangle(50,50,15,15),
		new Collider(){
			@Override
			public void collide(Collider c){c.inflictDamage(1); killbox_activated = true;}
			}
		);

	boolean firstkey = false;

	//END FIELDS


	public SlickSandbox(String gamename){
		super(gamename);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		p = PlayerShip.makeShip(100f, 100f);
		p.getPhys().getCImg().addTo(cl);
		ai = AITest.makeAI(200f, 200f, p);
		ai.getPhys().getCImg().addTo(cl);

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
		killbox.setColor(Color.magenta);
		killbox.addTo(cl);
	}

	@Override
	public void update(GameContainer gc, int time_passed_ms) throws SlickException {
		p.update(gc, time_passed_ms);
		ai.update(gc, time_passed_ms);

		cl.notifyCollisions();
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		ai.render(gc, g);
		p.render(gc, g);
		for (CollisionImage cimg : cimgs){
			cimg.render(gc, g);
		}

		if (killbox_activated){
			killbox.setColor(Color.darkGray);
		}
		killbox.render(gc, g);

		if (!firstkey){
			g.drawString("Move with the arrow keys\nShapes light up on collision\nPurple box kills you (unfinished)\n- Press any key -", 200, 100);
		}
	}

	@Override
	public void keyPressed(int key, char c){
		firstkey = true;
	}

	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new SlickSandbox("PHYSICS DEMO 1.0"));
			appgc.setDisplayMode(640, 480, false);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(SlickSandbox.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
