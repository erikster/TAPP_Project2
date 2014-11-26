import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.state.*;

public class MenuState extends BasicGameState {
	private Rectangle r1, r2;
	private boolean startGame, quit;
	
	public MenuState() {
		r1 = new Rectangle(220, 250, 200, 70);
		r2 = new Rectangle(220, 350, 200, 70);
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		startGame = false;
		quit = false;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
		if (startGame) {
			sbg.enterState(1); // enter game state
		} else if (quit) {
			gc.exit();
		}
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// draw title - can replace with image later
		g.setColor(Color.white);
		g.drawString("WELCOME TO STELLAR FUGITIVE!", 200, 100);
		g.drawString("PRESS THE START BUTTON TO PLAY!", 190, 120);
		
		// draw buttons
		g.setColor(Color.blue);
		g.draw(r1);
		g.draw(r2);
		
		// draw labels of buttons
		g.setColor(Color.white);
		g.drawString("START", 295, 280);
		g.drawString("QUIT", 300, 380);
	}
	
	@Override
	public void mouseClicked(int mouseNum, int x, int y, int clickCount) {
		if (mouseNum == Input.MOUSE_LEFT_BUTTON) {
			if (r1.contains(x, y)) {
				// mark to start game on next update
				startGame = true;
			} else if (r2.contains(x, y)) {
				// mark to leave program on next update
				quit = true;
			}
		}
	}
	
	@Override
	public int getID() {
		return 0;
	}
}