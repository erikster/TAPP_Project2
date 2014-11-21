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
	private static int MAX_SHIPS = 4;
	private static int SPAWN_TIME = 1000; // 1s or 1000ms
	
	public AIManager(CollisionLayer cl, PlayerShip target) {
		this.cl = cl;
		this.ships = new ArrayList<>();
		this.interval = 0;
		this.target = target;
	}
	
	public void update(GameContainer gc, int delta) throws SlickException {
		interval += delta;
		if (interval >= SPAWN_TIME) {
			if (ships.size() < MAX_SHIPS) {
				addShip();
			}
			interval = 0;
		}
		for (AITest a : ships) {
			a.update(gc, delta);
		}
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (AITest a : ships) {
			a.render(gc, g);
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
		AITest a = AITest.makeAI(x + px, y + py, target);
		ships.add(a);
		a.getPhys().getCImg().addTo(cl);
	}
	
	public int count() { return ships.size(); }
}