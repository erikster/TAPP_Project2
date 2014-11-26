import java.util.*;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics.*;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.fills.*;
import org.newdawn.slick.*;

public class BulletManager {
	private Collection<Bullet> bullets;
	private CollisionLayer cl;
	
	protected final int MAX_BULLETS = 4; // max amount of bullets at a time
	
	public BulletManager(CollisionLayer cl) {
		this.cl = cl;
		this.bullets = new ArrayList<>();
	}
	
	public void update(GameContainer gc, int delta) {
		List<Bullet> toDestroy = new LinkedList<>();
		for (Bullet b : bullets) {
			b.update(gc, delta);
			if (b.getHP() < 1) {
				toDestroy.add(b);
			}
		}
		for (Bullet b : toDestroy) {
			bullets.remove(b);
			b.destroy();
		}
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (Bullet b : bullets) {
			b.render(gc, g);
		}
	}
	
	public void requestBullet(float x, float y, float angle, float speed) {
		if (bullets.size() <= MAX_BULLETS) {
			Bullet result = Bullet.makeBullet(x, y, angle, speed);
			result.getPhys().getCImg().addTo(cl);
			bullets.add(result);
		}
	}
	
}