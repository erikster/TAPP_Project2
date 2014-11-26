import java.util.*;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.SlickException;

public class AIManager {
	private Collection<AITest> ships;
	private CollisionLayer cl;
	private int interval;
	private PlayerShip target;
	
	private static Random rand = new Random();
	private static final int MAX_SHIPS = 4;
	private static final int SPAWN_TIME = 1000; // 1s or 1000ms
	private static final float MAX_DIST_SQ  = 600f * 600f; // max distance before AIships get wrecked
	
	public AIManager(CollisionLayer cl, PlayerShip target) {
		this.cl = cl;
		this.ships = new ArrayList<>();
		this.interval = 0;
		this.target = target;
	}
	
	public void update(GameContainer gc, int delta, Vector2f pos) {
		// spawning behavior
		interval += delta;
		if (interval >= SPAWN_TIME) {
			if (ships.size() < MAX_SHIPS) {
				addShip();
			}
			interval = 0;
		}
		// destruction checks
		ArrayList<AITest> toDestroy = new ArrayList<>();
		for (AITest s : ships) {
			s.update(gc, delta);
			Vector2f apos = s.getPhys().getPosition();
			if (s.getHP() < 1 || pos.distanceSquared(apos) > MAX_DIST_SQ)
				toDestroy.add(s);
		}
		// actually destroy ships
		for (AITest s : toDestroy) {
			ships.remove(s);
			s.destroy();
		}
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (AITest s : ships) {
			s.render(gc, g);
		}
	}
	
	private void addShip() {
		float x = 0f, y = 0f;
		switch (rand.nextInt(4)) {
			case 0:
				x = 360f; y = 260f; // SE corner
				break;
			case 1:
				x = -360f; y = 260f; // SW corner
				break;
			case 2:
				x = 360f; y = -260f; // NE corner
				break;
			case 3:
				x = -360f; y = -260f;
				break;
		}
		float px = target.getPhys().getGImg().getMidX();
		float py = target.getPhys().getGImg().getMidY();
		AITest s = AITest.makeAI(x + px, y + py, target);
		ships.add(s);
		s.getPhys().getCImg().addTo(cl);
	}
	
	public int count() { return ships.size(); }
}