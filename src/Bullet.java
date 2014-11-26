import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.*;
import org.newdawn.slick.fills.*;

public class Bullet extends StellarObject {
	private int life_ms; // gonna default 3s lifetime

	private Bullet(Physics phys) {
		this(phys, 3000);
	}
	
	private Bullet(Physics phys, int life_ms) {
		super(phys);
		HP = 1;
		this.life_ms = life_ms;
	}
	
	
	@Override
	public void update(GameContainer gc, int delta_ms) {
		life_ms -= delta_ms;
		if (life_ms <= 0)
			HP -= 1;
		super.update(gc, delta_ms);
	}
	
	public static Bullet makeBullet(float x, float y, float angle, float speed) {
		Vector2f centroid = new Vector2f(x, y);
		
		_BulletCollider bc = new _BulletCollider();
		Circle cir = new Circle(x, y, 5f);
		
		CollisionImage cimg = new CollisionImage(cir, bc);
		GraphicalImage gimg = new GraphicalImage(cir, new GradientFill(0.0f, 0.0f, Color.green, 1.0f, 1.0f, Color.green));
		
		Bullet b = new Bullet(new _BulletPhysics(centroid, cimg, gimg));
		float dx = (float) Math.cos(angle);
		float dy = (float) Math.sin(angle);
		b.getPhys().accelerate(speed * dx, speed * dy);
		
		bc.bst = b;
		
		return b;
	}
	
	private static class _BulletPhysics extends Physics {
		public _BulletPhysics(
		Vector2f centroid,
		CollisionImage cimg,
		GraphicalImage gimg
		) {
			super(centroid, 0f, cimg, gimg);
		}
	}
	
	private static class _BulletCollider extends Collider{
	
		public Bullet bst;
		
		@Override
		public void collide(Collider c) { 
			c.inflictDamage(1); // inflicts damage on object it collides with
		}
		
		@Override
		public void inflictDamage(int dam) {
			bst.HP -= dam;
		}
	}
}