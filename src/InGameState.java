import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class InGameState extends BasicGameState {
	private World gameWorld;
	
	public InGameState() {
		gameWorld = new World();
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
		gameWorld.update(gc, i);
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		gameWorld.render(gc, g);
	}
	
	public int getID() {
		return 1;
	}
}