import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class InGameState extends BasicGameState {
	private World gameWorld;
	
	public InGameState() {
		gameWorld = null;
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		gameWorld = new World();
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
		gameWorld.update(gc, i);
		if (gameWorld.shipHP() < 1) {
			gameWorld.getFinalScore();
			sbg.enterState(2); // go to high-scores state
		}
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		gameWorld.render(gc, g);
	}
	
	@Override
	public int getID() {
		return 1;
	}
}