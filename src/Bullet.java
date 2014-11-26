import org.newdawn.slick.*;

public class Bullet extends StellarObject {
	private int timeToLive;

	public Bullet(float x, float y, float angle) {
		
	}
	
	@Override
	public void update(GameContainer gc, int delta_ms) {
		phys.update(gc, delta_ms);
	}
}