import java.util.*;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.SlickException;

public class AIManager {
	private Collection<AITest> ships;
	private CollisionLayer cl;
	
	public AIManager(CollisionLayer cl) {
		this.cl = cl;
		this.ships = new ArrayList<>();
	}
	
	public void update(GameContainer gc, int delta) throws SlickException {
		for (AITest a : ships) {
			a.update(gc, delta);
		}
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (AITest a : ships) {
			a.render(gc, g);
		}
	}
	
	public void addShip(AITest a) {
		ships.add(a);
		a.getPhys().getCImg().addTo(cl);
	}
}