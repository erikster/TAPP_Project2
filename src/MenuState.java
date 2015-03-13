import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.state.*;

public class MenuState extends BasicGameState {
	private Rectangle r1, r2;
	private boolean startGame, quit;
	private Image background;
	
	public MenuState() {
		r1 = new Rectangle(220, 160, 200, 70);
		r2 = new Rectangle(220, 260, 200, 70);
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		startGame = false;
		quit = false;
		gc.setMouseGrabbed(false);
	}
	
	@Override
	public void leave(GameContainer gc, StateBasedGame sbg) {
		gc.setMouseGrabbed(true);
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		background = new Image("media/MainMenu.png");
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
		g.drawImage(background, 0, 0);
		
		// draw buttons
		g.setColor(Color.blue);
		g.draw(r1);
		g.draw(r2);
		
		// draw labels of buttons
		g.setColor(Color.white);
		g.drawString("START", 295, r1.getY() + 27);
		g.drawString("QUIT", 300, r2.getY() + 27);
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