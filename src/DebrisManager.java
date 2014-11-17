public class DebrisManager {
	private Collection<Asteroid> asteroids;
	private CollisionLayer cl;
	
	public DebrisManager(CollisionLayer cl) {
		this.cl = cl;
	}
	
	public void update(GameController gc, int delta) {
		// probably just gonna for-each update the contained objects for now
	}
	
	public void draw(GameController gc, Graphics g) {
		// probably just gonna for-each draw the contained objects for now
	}
}