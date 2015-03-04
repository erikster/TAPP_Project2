import java.io.*;
import java.util.*;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.state.*;

public class HighScoresState extends BasicGameState {
	private List<Long> highScores;
	private boolean gotFile;
	private File scoreFile;
	private long newScore;
	
	public HighScoresState() {
		Scanner sc = null;
		try {
			scoreFile = new File("SFhighscores.txt");
			if (!scoreFile.exists()) {
				PrintWriter pw = new PrintWriter(new FileOutputStream("SFhighscores.txt", false));
				pw.print("0");
				for (int i = 0; i < 9; i++)
					pw.print(" 0");
				pw.close();
			}
			sc = new Scanner(scoreFile).useDelimiter("\\s");
			gotFile = true;
		} catch (IOException ioex) {
			ioex.printStackTrace();
			gotFile = false;
		}
		highScores = new ArrayList<Long>(10);
		if (gotFile) {
			highScores.add(sc.nextLong());
			for (int i = 0; i < 9; i++) {
				long l = sc.nextLong();
				highScores.add(l);
			}
			sc.close();
		} else {
			for (int i = 0; i < 10; i++) {
				highScores.add(0L);
			}
		}
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
			sbg.enterState(0);
		}
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawString("High Scores! Press SPACE to go to the menu.", 10, 10);
		int y = 50;
		for (int i = 0; i < 10; i++) {
			g.drawString((i + 1) + ": " + highScores.get(i), 10, y);
			y += 20;
		}
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		highScores.add(newScore);
		Collections.sort(highScores, Collections.reverseOrder());
		highScores.remove(10); // remove 11th item
	}
	
	@Override
	public void leave(GameContainer gc, StateBasedGame sbg) {
		// write data
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream("SFhighscores.txt", false));
			pw.print(highScores.get(0));
			for (int i = 1; i < 10; i++) {
				pw.print(" " + highScores.get(i));
			}
			pw.close();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}
	
	@Override
	public int getID() {
		return 2;
	}
	
	public void setScore(long score) {
		newScore = score;
	}
}